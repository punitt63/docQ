package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.CareContext;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
    private final String getLinkedByPatientAndFacilityQuery;

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
                "is_linked = EXCLUDED.is_linked";
        this.getCareContextQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE appointment_id = ?";
        this.getCareContextByLinkRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE link_request_id = ?";
        this.getCareContextByNotifyRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE notify_request_id = ?";
        this.getUnlinkedByPatientAndFacilityQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE patient_id = ? AND health_facility_id = ? AND is_linked = false";
        this.getLinkedByPatientAndFacilityQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE patient_id = ? AND health_facility_id = ? AND is_linked = true AND appointment_start_time >= ? AND appointment_start_time <= ?";
    }

    public CompletionStage<Void> upsert(CareContext careContext) {
        return postgresDAO.update(dbMetricsGroupName, "upsert", upsertCareContextQuery,
                        careContext.getAppointmentID(),
                        careContext.getHealthFacilityId(),
                        careContext.getPatientId(),
                        careContext.isLinked())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<CareContext>> get(String appointmentID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .appointmentStartTime(rs.getTimestamp(Column.APPOINTMENT_START_TIME.getColumnName()).getTime())
                .build(), appointmentID);
    }

    public CompletionStage<Optional<CareContext>> getByLinkRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextByLinkRequestIdQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .appointmentStartTime(rs.getTimestamp(Column.APPOINTMENT_START_TIME.getColumnName()).getTime())
                .build(), requestId);
    }

    public CompletionStage<Optional<CareContext>> getByNotifyRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextByNotifyRequestIdQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .appointmentStartTime(rs.getTimestamp(Column.APPOINTMENT_START_TIME.getColumnName()).getTime())
                .build(), requestId);
    }

    public CompletionStage<List<CareContext>> getUnlinkedByPatientAndFacility(String patientId, String healthFacilityId) {
        return postgresDAO.query(dbMetricsGroupName, "getUnlinkedByPatientAndFacility",
                getUnlinkedByPatientAndFacilityQuery, (rs, rowNum) -> CareContext.builder()
                        .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                        .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                        .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                        .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                        .appointmentStartTime(rs.getTimestamp(Column.APPOINTMENT_START_TIME.getColumnName()).getTime())
                        .build(), patientId, healthFacilityId);
    }

    public CompletionStage<List<CareContext>> getLinkedByPatientAndFacility(String patientId, String healthFacilityId, Long fromAppointmentTime, Long toAppointmentTime) {
        return postgresDAO.query(dbMetricsGroupName, "getLinkedByPatientAndFacility",
                getLinkedByPatientAndFacilityQuery, (rs, rowNum) -> CareContext.builder()
                        .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                        .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                        .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                        .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                        .appointmentStartTime(rs.getTimestamp(Column.APPOINTMENT_START_TIME.getColumnName()).getTime())
                        .build(), patientId, healthFacilityId, new Timestamp(fromAppointmentTime), new Timestamp(toAppointmentTime));
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    public enum Column {
        APPOINTMENT_ID("appointment_id", false),
        HEALTH_FACILITY_ID("health_facility_id", false),
        PATIENT_ID("patient_id", false),
        IS_LINKED("is_linked", true),
        APPOINTMENT_START_TIME("appointment_start_time", false);

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