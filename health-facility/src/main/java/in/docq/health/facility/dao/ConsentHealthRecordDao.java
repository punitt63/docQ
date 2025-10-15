package in.docq.health.facility.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import in.docq.health.facility.fidelius.keys.KeyMaterial;
import in.docq.health.facility.model.ConsentHealthRecord;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class ConsentHealthRecordDao {
    private final String dbMetricsGroupName = "consent_health_record";
    private final String table = "consent_health_record";
    private final String insertQuery;
    private final String getByConsentIdQuery;
    private final String getByConsentRequestIdQuery;
    private final String updateStatusQuery;
    private final String updateHealthDataRequestIdQuery;
    private final String updateHealthRecordsAndStatusQuery;
    private final String updateKeyMaterialQuery;
    private final String getByHealthDataRequestIdQuery;
    private final String updateTransactionIdQuery;
    private final String getByTransactionIdQuery;

    private final PostgresDAO postgresDAO;
    private final Gson gson;

    @Autowired
    public ConsentHealthRecordDao(PostgresDAO postgresDAO, Gson gson) {
        this.postgresDAO = postgresDAO;
        this.gson = gson;
        this.insertQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")";
        this.getByConsentIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE consent_id = ?";
        this.getByConsentRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE consent_request_id = ?";
        this.updateStatusQuery = "UPDATE " + table + " SET status = ? WHERE consent_id = ?";
        this.updateHealthDataRequestIdQuery = "UPDATE " + table + " SET health_data_request_id = ? WHERE consent_id = ?";
        this.updateHealthRecordsAndStatusQuery = "UPDATE " + table + " SET health_records = ?::jsonb, status = ? WHERE consent_id = ?";
        this.updateKeyMaterialQuery = "UPDATE " + table + " SET key_material = ?::jsonb WHERE consent_id = ?";
        this.getByHealthDataRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_data_request_id = ?";
        this.updateTransactionIdQuery = "UPDATE " + table + " SET transaction_id = ? WHERE consent_id = ?";
        this.getByTransactionIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE transaction_id = ?";
    }

    public CompletionStage<Void> insert(ConsentHealthRecord consentHealthRecord) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertQuery,
                        consentHealthRecord.getConsentId(),
                        consentHealthRecord.getConsentRequestId(),
                        consentHealthRecord.getPaginatedHealthRecords() != null ? getJsonbObject(gson.toJson(consentHealthRecord.getPaginatedHealthRecords())) : null,
                        consentHealthRecord.getStatus().name(),
                        consentHealthRecord.getKeyMaterial() != null ? getJsonbObject(gson.toJson(consentHealthRecord.getKeyMaterial())) : null,
                        consentHealthRecord.getTransactionId(),
                        consentHealthRecord.getHealthDataRequestId(),
                        consentHealthRecord.getHiuId(),
                        consentHealthRecord.getHipId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<ConsentHealthRecord>> getByConsentId(String consentId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByConsentId",
                getByConsentIdQuery, this::mapRow, consentId);
    }

    public CompletionStage<List<ConsentHealthRecord>> getByConsentRequestId(String consentRequestId) {
        return postgresDAO.query(dbMetricsGroupName, "getByConsentRequestId",
                getByConsentRequestIdQuery, this::mapRow, consentRequestId);
    }

    public CompletionStage<Void> updateStatus(String consentId, ConsentHealthRecord.Status status) {
        return postgresDAO.update(dbMetricsGroupName, "updateStatus", updateStatusQuery, status.name(), consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateHealthDataRequestId(String consentId, String healthDataRequestId) {
        return postgresDAO.update(dbMetricsGroupName, "updateStatus", updateHealthDataRequestIdQuery, healthDataRequestId, consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateHealthRecordsAndStatus(ConsentHealthRecord consentHealthRecord) {
        Object paginatedHealthRecordsJson = getJsonbObject(gson.toJson(consentHealthRecord.getPaginatedHealthRecords()));
        return postgresDAO.update(dbMetricsGroupName, "updateHealthRecord", updateHealthRecordsAndStatusQuery,
                        paginatedHealthRecordsJson, consentHealthRecord.getStatus().name(), consentHealthRecord.getConsentId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateKeyMaterial(String consentId, KeyMaterial keyMaterial) {
        Object keyMaterialJsonObject = keyMaterial != null ? getJsonbObject(gson.toJson(keyMaterial)) : null;
        return postgresDAO.update(dbMetricsGroupName, "updateKeyMaterial", updateKeyMaterialQuery,
                        keyMaterialJsonObject, consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateConsentHealthRecord(String consentId, KeyMaterial keyMaterial, String healthInfoRequestId, String hipId) {
        String keyMaterialJson = gson.toJson(keyMaterial);
        String updateQuery = "UPDATE " + table + " SET key_material = ?::jsonb, health_data_request_id = ?, hip_id = ? WHERE consent_id = ?";
        return postgresDAO.update(dbMetricsGroupName, "updateConsentHealthRecord", updateQuery,
                        keyMaterialJson, healthInfoRequestId, hipId, consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> deleteByConsentId(String consentId) {
        String deleteQuery = "DELETE FROM " + table + " WHERE consent_id = ?";
        return postgresDAO.update(dbMetricsGroupName, "deleteByConsentId", deleteQuery, consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateTransactionId(String consentId, String transactionId) {
        return postgresDAO.update(dbMetricsGroupName, "updateTransactionId", updateTransactionIdQuery,
                        transactionId, consentId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<ConsentHealthRecord>> getByHealthDataRequestId(String healthDataRequestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByHealthDataRequestId",
                getByHealthDataRequestIdQuery, this::mapRow, healthDataRequestId);
    }

    public CompletionStage<Optional<ConsentHealthRecord>> getByTransactionId(String transactionId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByTransactionId",
                getByTransactionIdQuery, this::mapRow, transactionId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    private ConsentHealthRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String consentId = rs.getString(Column.CONSENT_ID.getColumnName());
            String consentRequestId = rs.getString(Column.CONSENT_REQUEST_ID.getColumnName());
            String healthRecordJsonStr = rs.getString(Column.HEALTH_RECORDS.getColumnName());
            String status = rs.getString(Column.STATUS.getColumnName());
            String keyMaterialJsonStr = rs.getString(Column.KEY_MATERIAL.getColumnName());
            String transactionId = rs.getString(Column.TRANSACTION_ID.getColumnName());
            String healthDataRequestId = rs.getString(Column.HEALTH_DATA_REQUEST_ID.getColumnName());
            String hipId = rs.getString(Column.HIP_ID.getColumnName());
            List<ConsentHealthRecord.PaginatedHealthRecords> healthRecords = healthRecordJsonStr != null ? gson.fromJson(healthRecordJsonStr, new TypeToken<List<ConsentHealthRecord.PaginatedHealthRecords>>(){}.getType()) : new ArrayList<>();
            JsonObject keyMaterial = keyMaterialJsonStr != null ? gson.fromJson(keyMaterialJsonStr, JsonObject.class) : null;

            return ConsentHealthRecord.builder()
                    .consentId(consentId)
                    .consentRequestId(consentRequestId)
                    .paginatedHealthRecords(healthRecords)
                    .status(ConsentHealthRecord.Status.valueOf(status))
                    .keyMaterial(keyMaterial)
                    .transactionId(transactionId)
                    .healthDataRequestId(healthDataRequestId)
                    .hipId(hipId)
                    .build();
        } catch (Exception e) {
            throw new SQLException("Failed to map consent health record row", e);
        }
    }

    public static Object getJsonbObject(String json) {
        try {
            PGobject pGobject = new PGobject();
            pGobject.setType("jsonb");
            pGobject.setValue(json);
            return pGobject;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Column {
        CONSENT_ID("consent_id", false),
        CONSENT_REQUEST_ID("consent_request_id", false),
        HEALTH_RECORDS("health_records", true),
        STATUS("status", true),
        KEY_MATERIAL("key_material", true),
        TRANSACTION_ID("transaction_id", true),
        HEALTH_DATA_REQUEST_ID("health_data_request_id", true),
        HIU_ID("hiu_id", false),
        HIP_ID("hip_id", true);
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
                    .map(col -> "?")
                    .collect(Collectors.joining(","));
        }
    }
}