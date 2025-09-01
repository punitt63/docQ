package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.CareContext;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class CareContextDao {
    private final static Logger logger = LoggerFactory.getLogger(CareContextDao.class);
    private final String dbMetricsGroupName = "care_context";
    private final String table = "care_context";
    private final String upsertCareContextQuery;
    private final String getCareContextQuery;
    private final String getCareContextByLinkRequestIdQuery;
    private final String getCareContextByNotifyRequestIdQuery;
    private final String getUnlinkedByPatientAndFacilityQuery;

    private final Gson gson = new Gson();
    private final PostgresDAO postgresDAO;

    @Autowired
    public CareContextDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.upsertCareContextQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")" +
                " ON CONFLICT (appointment_id) DO UPDATE SET " +
                "health_facility_id = EXCLUDED.health_facility_id, " +
                "patient_id = EXCLUDED.patient_id, " +
                "link_request_id = EXCLUDED.link_request_id, " +
                "is_linked = EXCLUDED.is_linked, " +
                "is_patient_notified = EXCLUDED.is_patient_notified, " +
                "notify_request_id = EXCLUDED.notify_request_id";
        this.getCareContextQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE appointment_id = ?";
        this.getCareContextByLinkRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE link_request_id = ?";
        this.getCareContextByNotifyRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE notify_request_id = ?";
        this.getUnlinkedByPatientAndFacilityQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE patient_id = ? AND health_facility_id = ? AND is_linked = false";
    }

    public CompletionStage<Void> upsert(CareContext careContext) {
        return postgresDAO.update(dbMetricsGroupName, "upsert", upsertCareContextQuery,
                        careContext.getAppointmentID(),
                        careContext.getHealthFacilityId(),
                        careContext.getPatientId(),
                        careContext.getLinkRequestId(),
                        careContext.isLinked(),
                        careContext.isPatientNotified(),
                        careContext.getNotifyRequestId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<CareContext>> get(String appointmentID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .linkRequestId(rs.getString(Column.LINK_REQUEST_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .notifyRequestId(rs.getString(Column.NOTIFY_REQUEST_ID.getColumnName()))
                .build(), appointmentID);
    }

    public CompletionStage<Optional<CareContext>> getByLinkRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextByLinkRequestIdQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .linkRequestId(rs.getString(Column.LINK_REQUEST_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .notifyRequestId(rs.getString(Column.NOTIFY_REQUEST_ID.getColumnName()))
                .build(), requestId);
    }

    public CompletionStage<Optional<CareContext>> getByNotifyRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextByNotifyRequestIdQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .linkRequestId(rs.getString(Column.LINK_REQUEST_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .notifyRequestId(rs.getString(Column.NOTIFY_REQUEST_ID.getColumnName()))
                .build(), requestId);
    }

    public CompletionStage<List<CareContext>> getUnlinkedByPatientAndFacility(String patientId, String healthFacilityId) {
        return postgresDAO.query(dbMetricsGroupName, "getUnlinkedByPatientAndFacility",
                getUnlinkedByPatientAndFacilityQuery, (rs, rowNum) -> CareContext.builder()
                        .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                        .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                        .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                        .linkRequestId(rs.getString(Column.LINK_REQUEST_ID.getColumnName()))
                        .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                        .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                        .notifyRequestId(rs.getString(Column.NOTIFY_REQUEST_ID.getColumnName()))
                        .build(), patientId, healthFacilityId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    public enum Column {
        APPOINTMENT_ID("appointment_id", false),
        HEALTH_FACILITY_ID("health_facility_id", false),
        PATIENT_ID("patient_id", false),
        LINK_REQUEST_ID("link_request_id", true),
        IS_LINKED("is_linked", true),
        IS_PATIENT_NOTIFIED("is_patient_notified", true),
        NOTIFY_REQUEST_ID("notify_request_id", true);

        @Getter
        private final String columnName;
        @Getter
        private final boolean isModifiable;

        Column(String columnName, boolean isModifiable) {
            this.columnName = columnName;
            this.isModifiable = isModifiable;
        }

        public static String allColumNamesSeparatedByComma() {
            return Arrays.stream(values())
                    .map(Column::getColumnName)
                    .collect(Collectors.joining(","));
        }

        public static String allColumnValuesSeparatedByComma() {
            return Arrays.stream(values())
                    .filter(column -> !column.getColumnName().equals("created_at") && !column.getColumnName().equals("updated_at"))
                    .map(ignore -> "?")
                    .collect(Collectors.joining(","));
        }
    }
}