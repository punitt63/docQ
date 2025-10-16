package in.docq.health.facility.dao;

import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.Doctor;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class DoctorDao {
    private final String dbMetricsGroupName = "doctor";
    private final String table = "doctor";
    private final String insertDoctorQuery;
    private final String getDoctorQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public DoctorDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertDoctorQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (?, ?, ?) on conflict do nothing";
        this.getDoctorQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and doctor_id = ?";
    }

    public CompletionStage<Void> insert(String healthFacilityID, String doctorID, String facilityManagerID) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertDoctorQuery, healthFacilityID, doctorID, facilityManagerID)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<Doctor>> get(String healthFacilityID, String doctorID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getDoctorQuery, (rs, rowNum) ->
                        Doctor.builder()
                                .healthFacilityID(rs.getString("health_facility_id"))
                                .id(rs.getString("doctor_id"))
                                .facilityManagerID(rs.getString("facility_manager_id"))
                                .build(), healthFacilityID, doctorID);
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        DOCTOR_ID("doctor_id"),
        FACILITY_MANAGER_ID("facility_manager_id");

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