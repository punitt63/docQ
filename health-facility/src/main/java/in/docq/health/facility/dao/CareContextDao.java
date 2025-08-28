package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.CareContext;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    private final String getCareContextByRequestIdQuery;
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
                "request_id = EXCLUDED.request_id, " +
                "is_linked = EXCLUDED.is_linked, " +
                "is_patient_notified = EXCLUDED.is_patient_notified";
        this.getCareContextQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE appointment_id = ?";
        this.getCareContextByRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE patient_id = ? and request_id = ?";
    }

    public CompletionStage<Void> upsert(CareContext careContext) {
        return postgresDAO.update(dbMetricsGroupName, "upsert", upsertCareContextQuery,
                        careContext.getAppointmentID(),
                        careContext.getHealthFacilityId(),
                        careContext.getPatientId(),
                        careContext.getRequestId(),
                        careContext.isLinked(),
                        careContext.isPatientNotified())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<CareContext>> get(String appointmentID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .requestId(rs.getString(Column.REQUEST_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .build(), appointmentID);
    }

    public CompletionStage<Optional<CareContext>> getByRequestId(String patientId, String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getCareContextByRequestIdQuery, (rs, rowNum) -> CareContext.builder()
                .appointmentID(rs.getString(Column.APPOINTMENT_ID.getColumnName()))
                .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                .requestId(rs.getString(Column.REQUEST_ID.getColumnName()))
                .isLinked(rs.getBoolean(Column.IS_LINKED.getColumnName()))
                .isPatientNotified(rs.getBoolean(Column.IS_PATIENT_NOTIFIED.getColumnName()))
                .build(), patientId, requestId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    public enum Column {
        APPOINTMENT_ID("appointment_id", false),
        HEALTH_FACILITY_ID("health_facility_id", false),
        PATIENT_ID("patient_id", false),
        REQUEST_ID("request_id", true),
        IS_LINKED("is_linked", true),
        IS_PATIENT_NOTIFIED("is_patient_notified", true);

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