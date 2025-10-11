package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.abha.rest.client.model.AbdmConsentManagement1Request;
import in.docq.health.facility.model.ConsentRequest;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class ConsentRequestDao {
    private final String dbMetricsGroupName = "consent_request";
    private final String table = "consent_request";
    private final String insertQuery;
    private final String getByRequestIdQuery;
    private final String getByConsentRequestIdQuery;
    private final String updateStatusQuery;
    private final String updateConsentRequestIdQuery;
    private final String updateConsentArtifactIdQuery;
    private final String getByHiuIdQuery;

    private final PostgresDAO postgresDAO;
    private final Gson gson;

    @Autowired
    public ConsentRequestDao(PostgresDAO postgresDAO, Gson gson) {
        this.postgresDAO = postgresDAO;
        this.gson = gson;
        this.insertQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")";
        this.getByRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE request_id = ?";
        this.getByConsentRequestIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE consent_request_id = ?";
        this.updateStatusQuery = "UPDATE " + table + " SET status = ? WHERE request_id = ?";
        this.updateConsentRequestIdQuery = "UPDATE " + table + " SET consent_request_id = ? WHERE request_id = ?";
        this.updateConsentArtifactIdQuery = "UPDATE " + table + " SET consent_artifact_id = ? WHERE request_id = ?";
        this.getByHiuIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE hiu_id = ? AND requester_id = ?";
    }

    public CompletionStage<Void> insert(ConsentRequest consentRequest) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertQuery,
                        consentRequest.getRequestId(),
                        consentRequest.getConsentRequestId(),
                        consentRequest.getHiuId(),
                        getJsonbObject(gson.toJson(consentRequest.getRequest())),
                        consentRequest.getStatus(),
                        consentRequest.getRequesterId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<ConsentRequest>> getByRequestId(String requestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByRequestId",
                getByRequestIdQuery, this::mapRow, requestId);
    }

    public CompletionStage<Optional<ConsentRequest>> getByConsentRequestId(String consentRequestId) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getByConsentRequestId",
                getByConsentRequestIdQuery, this::mapRow, consentRequestId);
    }

    public CompletionStage<Void> updateStatus(String requestId, ConsentRequest.Status status) {
        return postgresDAO.update(dbMetricsGroupName, "updateStatus", updateStatusQuery, status.name(), requestId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateConsentRequestId(String requestId, String consentRequestId) {
        return postgresDAO.update(dbMetricsGroupName, "updateConsentRequestId", updateConsentRequestIdQuery,
                        consentRequestId, requestId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateConsentArtifactId(String requestId, String[] consentArtifactId) {
        return postgresDAO.update(dbMetricsGroupName, "updateConsentArtifactId", updateConsentArtifactIdQuery,
                        consentArtifactId, requestId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> deleteByRequestId(String requestId) {
        String deleteQuery = "DELETE FROM " + table + " WHERE request_id = ?";
        return postgresDAO.update(dbMetricsGroupName, "deleteByRequestId", deleteQuery, requestId)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<List<ConsentRequest>> getByHiuAndRequesterId(String hiuId, String requesterId) {
        return postgresDAO.query(dbMetricsGroupName, "getByHiuId", getByHiuIdQuery, this::mapRow, hiuId, requesterId);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    private ConsentRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String requestId = rs.getString(Column.REQUEST_ID.getColumnName());
            String consentRequestId = rs.getString(Column.CONSENT_REQUEST_ID.getColumnName());
            String hiuId = rs.getString(Column.HIU_ID.getColumnName());
            String requestJsonStr = rs.getString(Column.REQUEST.getColumnName());
            String status = rs.getString(Column.STATUS.getColumnName());

            AbdmConsentManagement1Request request = gson.fromJson(requestJsonStr, AbdmConsentManagement1Request.class);

            return ConsentRequest.builder()
                    .requestId(requestId)
                    .consentRequestId(consentRequestId)
                    .hiuId(hiuId)
                    .request(request)
                    .status(Optional.ofNullable(status).map(ConsentRequest.Status::valueOf).orElse(null))
                    .build();
        } catch (Exception e) {
            throw new SQLException("Failed to map consent request row", e);
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
        REQUEST_ID("request_id", false),
        CONSENT_REQUEST_ID("consent_request_id", true),
        HIU_ID("hiu_id", false),
        REQUEST("request", false),
        STATUS("status", true),
        REQUESTER_ID("requester_id", false);

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
                    .map(ignore -> "?")
                    .collect(Collectors.joining(","));
        }
    }
}