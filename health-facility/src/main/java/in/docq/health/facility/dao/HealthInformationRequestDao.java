package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.model.HealthInformationRequest;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class HealthInformationRequestDao {
    private final String dbMetricsGroupName = "health_information_request";
    private final String table = "health_information_request";
    private final String upsertQuery;
    private final String getByTransactionIdQuery;
    private final String getByConsentIdQuery;
    private final String updateStatusQuery;

    private final PostgresDAO postgresDAO;
    private final Gson gson;

    @Autowired
    public HealthInformationRequestDao(PostgresDAO postgresDAO, Gson gson) {
        this.postgresDAO = postgresDAO;
        this.gson = gson;
        this.upsertQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (?, ?, ?::jsonb, ?)" +
                " ON CONFLICT (transaction_id) DO UPDATE SET " +
                "consent_id = EXCLUDED.consent_id, " +
                "request = EXCLUDED.request, " +
                "status = EXCLUDED.status";
        this.getByTransactionIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE transaction_id = ?";
        this.getByConsentIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE consent_id = ?";
        this.updateStatusQuery = "UPDATE " + table + " SET status = ? WHERE transaction_id = ?";
    }

    public CompletionStage<Void> upsert(String transactionId, String consentId, HIPConsentWebhookController.HealthInformationRequestBody requestObject, String status) {
        String requestJson = gson.toJson(requestObject);
        return postgresDAO.update(dbMetricsGroupName, "upsert", upsertQuery,
                        transactionId,
                        consentId,
                        requestJson,
                        status)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<HealthInformationRequest>> getByTransactionId(String transactionId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByTransactionId",
                getByTransactionIdQuery, this::mapRow, transactionId);
    }

    public CompletionStage<List<HealthInformationRequest>> getByConsentId(String consentId) {
        return postgresDAO.query(dbMetricsGroupName, "getByConsentId",
                getByConsentIdQuery, this::mapRow, consentId);
    }

    public CompletionStage<Void> updateStatus(String transactionId, String status) {
        return postgresDAO.update(dbMetricsGroupName, "updateStatus", updateStatusQuery, status, transactionId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    private HealthInformationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String requestJsonStr = rs.getString(Column.REQUEST.getColumnName());
            HIPConsentWebhookController.HealthInformationRequestBody healthInformationRequestBody = gson.fromJson(requestJsonStr, HIPConsentWebhookController.HealthInformationRequestBody.class);

            return HealthInformationRequest.builder()
                    .transactionId(rs.getString(Column.TRANSACTION_ID.getColumnName()))
                    .consentId(rs.getString(Column.CONSENT_ID.getColumnName()))
                    .request(healthInformationRequestBody)
                    .status(rs.getString(Column.STATUS.getColumnName()))
                    .build();
        } catch (Exception e) {
            throw new SQLException("Failed to map health information request row", e);
        }
    }

    public enum Column {
        TRANSACTION_ID("transaction_id"),
        CONSENT_ID("consent_id"),
        REQUEST("request"),
        STATUS("status"),
        CREATED_AT("created_at"),
        UPDATED_AT("updated_at");

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
                    .filter(column -> !column.equals(CREATED_AT) && !column.equals(UPDATED_AT))
                    .map(ignore -> "?")
                    .collect(Collectors.joining(","));
        }
    }
}