package in.docq.health.facility.dao;

import in.docq.health.facility.model.Doctor;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static in.docq.health.facility.model.HealthProfessionalType.DOCTOR;

@Component
public class DoctorDao {
    private final String dbMetricsGroupName = "doctor";
    private final String table = "doctor";
    private final String insertDoctorQuery;
    private final String getDoctorQuery;
    private final String listByStateDistrictQuery;
    private final String listByHealthFacilityQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public DoctorDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertDoctorQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";
        this.getDoctorQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? AND id = ?";
        this.listByStateDistrictQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE facility_state_code = ? AND facility_district_code = ?";
        this.listByHealthFacilityQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ?";
    }

    public CompletionStage<Void> insert(Doctor doctor) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertDoctorQuery,
                        doctor.getHealthFacilityID(),
                        doctor.getId(),
                        doctor.getFacilityManagerID(),
                        doctor.getFacilityName(),
                        doctor.getName(),
                        doctor.getFacilityStateCode(),
                        doctor.getFacilityDistrictCode(),
                        doctor.getFacilityAddress(),
                        doctor.getFacilityPincode(),
                        doctor.getFacilityLatitude(),
                        doctor.getFacilityLongitude(),
                        doctor.getSpeciality())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<Doctor>> get(String healthFacilityID, String doctorID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getDoctorQuery, (rs, rowNum) ->
                Doctor.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .id(rs.getString("id"))
                        .facilityManagerID(rs.getString("facility_manager_id"))
                        .facilityName(rs.getString("facility_name"))
                        .name(rs.getString("name"))
                        .facilityStateCode(rs.getInt("facility_state_code"))
                        .facilityDistrictCode(rs.getInt("facility_district_code"))
                        .facilityAddress(rs.getString("facility_address"))
                        .facilityPincode(rs.getString("facility_pincode"))
                        .facilityLatitude(rs.getDouble("facility_latitude"))
                        .facilityLongitude(rs.getDouble("facility_longitude"))
                        .speciality(rs.getString("speciality"))
                        .build(), healthFacilityID, doctorID);
    }

    public CompletionStage<List<Doctor>> listByStateAndDistrict(int stateCode, int districtCode) {
        return postgresDAO.query(dbMetricsGroupName, "listByStateDistrict", listByStateDistrictQuery, (rs, rowNum) ->
                Doctor.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .facilityName(rs.getString("facility_name"))
                        .facilityStateCode(rs.getInt("facility_state_code"))
                        .facilityDistrictCode(rs.getInt("facility_district_code"))
                        .facilityAddress(rs.getString("facility_address"))
                        .facilityPincode(rs.getString("facility_pincode"))
                        .facilityLatitude((Double) rs.getObject("facility_latitude"))
                        .facilityLongitude((Double) rs.getObject("facility_longitude"))
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .speciality(rs.getString("speciality"))
                        .build(), stateCode, districtCode);
    }

    public CompletionStage<List<Doctor>> listByHealthFacility(String healthFacilityID, String facilityManagerID) {
        final String query = facilityManagerID != null ?
                listByHealthFacilityQuery + " AND facility_manager_id = ?" :
                listByHealthFacilityQuery;
        List<Object> args = new ArrayList<>();
        args.add(healthFacilityID);
        if(facilityManagerID != null) {
            args.add(facilityManagerID);
        }
        return postgresDAO.query(dbMetricsGroupName, "listByHealthFacility", query, (rs, rowNum) ->
                Doctor.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .facilityName(rs.getString("facility_name"))
                        .facilityStateCode(rs.getInt("facility_state_code"))
                        .facilityDistrictCode(rs.getInt("facility_district_code"))
                        .facilityAddress(rs.getString("facility_address"))
                        .facilityPincode(rs.getString("facility_pincode"))
                        .facilityLatitude(rs.getDouble("facility_latitude"))
                        .facilityLongitude(rs.getDouble("facility_longitude"))
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .speciality(rs.getString("speciality"))
                        .build(), args.toArray());
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        DOCTOR_ID("id"),
        FACILITY_MANAGER_ID("facility_manager_id"),
        FACILITY_NAME("facility_name"),
        NAME("name"),
        STATE_CODE("facility_state_code"),
        DISTRICT_CODE("facility_district_code"),
        ADDRESS("facility_address"),
        PINCODE("facility_pincode"),
        LATITUDE("facility_latitude"),
        LONGITUDE("facility_longitude"),
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