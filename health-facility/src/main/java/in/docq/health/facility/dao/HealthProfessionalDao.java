package in.docq.health.facility.dao;

import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class HealthProfessionalDao {
    private final String dbMetricsGroupName = "healthProfessional";
    private final String table = "health_professional";
    private final String insertHealthProfessionalQuery;
    private final String getHealthProfessionalQuery;
    private final String listByStateDistrictQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public HealthProfessionalDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertHealthProfessionalQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on conflict do nothing";
        this.getHealthProfessionalQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and health_professional_id = ?";
        this.listByStateDistrictQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE state_code = ? AND district_code = ?";
    }

    public CompletionStage<Void> insert(String healthFacilityID,
                                        String healthFacilityName,
                                        String healthProfessionalID,
                                        String healthProfessionalName,
                                        HealthProfessionalType type,
                                        int stateCode,
                                        int districtCode,
                                        String speciality,
                                        String address,
                                        String pincode,
                                        Double latitude,
                                        Double longitude) {
        // Order of values must match Column.allColumNamesSeparatedByComma()
        // Columns order: health_facility_id, health_facility_name, state_code, district_code, address, pincode, latitude, longitude, health_professional_id, health_professional_name, type, speciality
        return postgresDAO.update(dbMetricsGroupName, "insert", insertHealthProfessionalQuery,
                healthFacilityID, healthFacilityName, stateCode, districtCode, address, pincode, latitude, longitude, healthProfessionalID, healthProfessionalName, type.name(), speciality)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<HealthProfessional> get(String healthFacilityID, String healthProfessionalID) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getHealthProfessionalQuery, (rs, rowNum) ->
                HealthProfessional.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .healthFacilityName(rs.getString("health_facility_name"))
                        .id(rs.getString("health_professional_id"))
                        .healthProfessionalName(rs.getString("health_professional_name"))
                        .type(HealthProfessionalType.valueOf(rs.getString("type")))
                        .stateCode(rs.getInt("state_code"))
                        .districtCode(rs.getInt("district_code"))
                        .speciality(rs.getString("speciality"))
                        .address(rs.getString("address"))
                        .pincode(rs.getString("pincode"))
                        .latitude((Double) rs.getObject("latitude"))
                        .longitude((Double) rs.getObject("longitude"))
                .build(),healthFacilityID, healthProfessionalID)
                .exceptionally((throwable) -> {
                    throwable = throwable.getCause();
                    if (throwable instanceof EmptyResultDataAccessException) {
                        throw new CompletionException(new HealthProfessionalNotFound(healthFacilityID, healthProfessionalID));
                    }
                    throw new CompletionException(throwable);
                });
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<java.util.List<HealthProfessional>> listByStateAndDistrict(int stateCode, int districtCode, java.util.Optional<String> specialityOpt) {
        final String baseQuery = listByStateDistrictQuery;
        final boolean filterBySpeciality = specialityOpt.isPresent() && !specialityOpt.get().isBlank();
        final String finalQuery = filterBySpeciality ? baseQuery + " AND speciality = ?" : baseQuery;

        if (filterBySpeciality) {
            return postgresDAO.query(dbMetricsGroupName, "listByStateDistrictSpeciality", finalQuery, (rs, rowNum) ->
                    HealthProfessional.builder()
                            .healthFacilityID(rs.getString("health_facility_id"))
                            .healthFacilityName(rs.getString("health_facility_name"))
                            .stateCode(rs.getInt("state_code"))
                            .districtCode(rs.getInt("district_code"))
                            .address(rs.getString("address"))
                            .pincode(rs.getString("pincode"))
                            .latitude((Double) rs.getObject("latitude"))
                            .longitude((Double) rs.getObject("longitude"))
                            .id(rs.getString("health_professional_id"))
                            .healthProfessionalName(rs.getString("health_professional_name"))
                            .type(HealthProfessionalType.valueOf(rs.getString("type")))
                            .speciality(rs.getString("speciality"))
                            .build(), stateCode, districtCode, specialityOpt.get());
        }

        return postgresDAO.query(dbMetricsGroupName, "listByStateDistrict", finalQuery, (rs, rowNum) ->
                HealthProfessional.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .healthFacilityName(rs.getString("health_facility_name"))
                        .stateCode(rs.getInt("state_code"))
                        .districtCode(rs.getInt("district_code"))
                        .address(rs.getString("address"))
                        .pincode(rs.getString("pincode"))
                        .latitude((Double) rs.getObject("latitude"))
                        .longitude((Double) rs.getObject("longitude"))
                        .id(rs.getString("health_professional_id"))
                        .healthProfessionalName(rs.getString("health_professional_name"))
                        .type(HealthProfessionalType.valueOf(rs.getString("type")))
                        .speciality(rs.getString("speciality"))
                        .build(), stateCode, districtCode);
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        HEALTH_FACILITY_NAME("health_facility_name"),
        STATE_CODE("state_code"),
        DISTRICT_CODE("district_code"),
        ADDRESS("address"),
        PINCODE("pincode"),
        LATITUDE("latitude"),
        LONGITUDE("longitude"),
        HEALTH_PROFESSIONAL_ID("health_professional_id"),
        HEALTH_PROFESSIONAL_NAME("health_professional_name"),
        TYPE("type"),
        SPECIALITY("speciality");

        @Getter
        private final String columnName;

        Column(String columnName) {
            this.columnName = columnName;
        }

        public static String allColumNamesSeparatedByComma() {
            return Arrays.stream(values())
                    .map(Column::getColumnName)
                    .collect(Collectors.joining(","));
        }
    }


}
