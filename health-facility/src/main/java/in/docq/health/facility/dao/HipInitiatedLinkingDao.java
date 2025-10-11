package in.docq.health.facility.dao;

import in.docq.health.facility.model.HipInitiatedLinking;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class HipInitiatedLinkingDao {
    private static final Logger logger = LoggerFactory.getLogger(HipInitiatedLinkingDao.class);
    private final String dbMetricsGroupName = "hip_initiated_linking";
    private final String table = "hip_initiated_linking";
    private final String upsertQuery;
    private final String getByAppointmentIdQuery;
    private final String getByLinkRequestIdQuery;
    private final String getByNotifyRequestIdQuery;

    private final PostgresDAO postgresDAO;

    @Autowired
    public HipInitiatedLinkingDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.upsertQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")" +
                " ON CONFLICT (appointment_id) DO UPDATE SET " +
                "health_facility_id = EXCLUDED.health_facility_id, " +
                "patient_id = EXCLUDED.patient_id, " +
                "link_request_id = EXCLUDED.link_request_id, " +
                "is_patient_notified = EXCLUDED.is_patient_notified, " +
                "notify_request_id = EXCLUDED.notify_request_id";
        this.getByAppointmentIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE appointment_id = ?";
        this.getByLinkRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE link_request_id = ?";
        this.getByNotifyRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE notify_request_id = ?";
    }

    public CompletionStage<Void> upsert(HipInitiatedLinking hipLinking) {
        return postgresDAO.update(dbMetricsGroupName, "upsert", upsertQuery,
                        hipLinking.getAppointmentId(),
                        hipLinking.getHealthFacilityId(),
                        hipLinking.getPatientId(),
                        hipLinking.getLinkRequestId(),
                        hipLinking.isPatientNotified(),
                        hipLinking.getNotifyRequestId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<HipInitiatedLinking>> getByAppointmentId(String appointmentId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByAppointmentId",
                getByAppointmentIdQuery, this::mapRow, appointmentId);
    }

    public CompletionStage<Optional<HipInitiatedLinking>> getByLinkRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByLinkRequestId",
                getByLinkRequestIdQuery, this::mapRow, requestId);
    }

    public CompletionStage<Optional<HipInitiatedLinking>> getByNotifyRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByNotifyRequestId",
                getByNotifyRequestIdQuery, this::mapRow, requestId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    private HipInitiatedLinking mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return HipInitiatedLinking.builder()
                .appointmentId(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .linkRequestId(rs.getString(Column.LINK_REQUEST_ID.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .notifyRequestId(rs.getString(Column.NOTIFY_REQUEST_ID.getColumnName()))
                .build();
    }

    public enum Column {
        APPOINTMENT_ID("appointment_id"),
        HEALTH_FACILITY_ID("health_facility_id"),
        PATIENT_ID("patient_id"),
        LINK_REQUEST_ID("link_request_id"),
        IS_PATIENT_NOTIFIED("is_patient_notified"),
        NOTIFY_REQUEST_ID("notify_request_id");

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

        public static String allColumnValuesSeparatedByComma() {
            return Arrays.stream(values())
                    .map(ignore -> "?")
                    .collect(Collectors.joining(","));
        }
    }
}