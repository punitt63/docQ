package in.docq.health.facility.dao;

import in.docq.health.facility.model.HIPLinkingToken;
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
public class HIPLinkingTokenDao {
    private final static Logger logger = LoggerFactory.getLogger(HIPLinkingTokenDao.class);
    private final String dbMetricsGroupName = "hip_linking_token";
    private final String table = "hip_linking_token";
    private final String insertTokenQuery;
    private final String updateTokenQuery;
    private final String getTokenQuery;
    private final String getByRequestIdQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public HIPLinkingTokenDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;

        this.insertTokenQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")" +
                " ON CONFLICT (" + Column.HEALTH_FACILITY_ID.getColumnName() + ", " + Column.PATIENT_ID.getColumnName() + ")" +
                " DO UPDATE SET " + Column.LAST_TOKEN_REQUEST_APPOINTMENT_ID.getColumnName() + " = EXCLUDED." + Column.LAST_TOKEN_REQUEST_APPOINTMENT_ID.getColumnName() + "," +
                Column.LAST_TOKEN_REQUEST_ID.getColumnName() + " = EXCLUDED." + Column.LAST_TOKEN_REQUEST_ID.getColumnName() + "," +
                Column.LAST_TOKEN.getColumnName() + " = EXCLUDED." + Column.LAST_TOKEN.getColumnName();

        this.updateTokenQuery = "UPDATE " + table + " SET " + Column.LAST_TOKEN.getColumnName() + " = ?" +
                " WHERE " + Column.PATIENT_ID.getColumnName() + " = ?" +
                " AND " + Column.LAST_TOKEN_REQUEST_ID.getColumnName() + " = ?";

        this.getTokenQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.HEALTH_FACILITY_ID.getColumnName() + " = ?" +
                " AND " + Column.PATIENT_ID.getColumnName() + " = ?";

        this.getByRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.PATIENT_ID.getColumnName() + " = ?" +
                " AND " + Column.LAST_TOKEN_REQUEST_ID.getColumnName() + " = ?";
    }

    public CompletionStage<Void> upsert(HIPLinkingToken token) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertTokenQuery,
                        token.getHealthFacilityId(),
                        token.getPatientId(),
                        token.getLastTokenRequestAppointmentId(),
                        token.getLastTokenRequestId(),
                        token.getLastToken())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> update(String patientId, String lastTokenRequestId, String newToken) {
        return postgresDAO.update(dbMetricsGroupName, "update", updateTokenQuery,
                        newToken,
                        patientId,
                        lastTokenRequestId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<HIPLinkingToken>> get(String healthFacilityId, String patientId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getTokenQuery,
                (rs, rowNum) -> HIPLinkingToken.builder()
                        .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                        .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                        .lastTokenRequestAppointmentId(rs.getString(Column.LAST_TOKEN_REQUEST_APPOINTMENT_ID.getColumnName()))
                        .lastTokenRequestId(rs.getString(Column.LAST_TOKEN_REQUEST_ID.getColumnName()))
                        .lastToken(rs.getString(Column.LAST_TOKEN.getColumnName()))
                        .build(),
                healthFacilityId, patientId);
    }

    public CompletionStage<HIPLinkingToken> getByRequestId(String patientId, String lastTokenRequestId) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getTokenQuery,
                (rs, rowNum) -> HIPLinkingToken.builder()
                        .healthFacilityId(rs.getString(Column.HEALTH_FACILITY_ID.getColumnName()))
                        .patientId(rs.getString(Column.PATIENT_ID.getColumnName()))
                        .lastTokenRequestAppointmentId(rs.getString(Column.LAST_TOKEN_REQUEST_APPOINTMENT_ID.getColumnName()))
                        .lastTokenRequestId(rs.getString(Column.LAST_TOKEN_REQUEST_ID.getColumnName()))
                        .lastToken(rs.getString(Column.LAST_TOKEN.getColumnName()))
                        .build(),
                patientId, lastTokenRequestId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        PATIENT_ID("patient_id"),
        LAST_TOKEN_REQUEST_APPOINTMENT_ID("last_token_request_appointment_id"),
        LAST_TOKEN_REQUEST_ID("last_token_request_id"),
        LAST_TOKEN("last_token");

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