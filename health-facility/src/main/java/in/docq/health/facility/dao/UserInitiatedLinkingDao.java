package in.docq.health.facility.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.model.UserInitiatedLinking;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class UserInitiatedLinkingDao {
    private static Gson gson = new Gson();
    private static final String table = "user_initiated_linking";
    private static final String dbMetricsGroupName = "UserInitiatedLinking";

    private final PostgresDAO postgresDAO;

    private final String insertQuery;
    private final String getByTransactionIdQuery;
    private final String getByLinkReferenceNumberQuery;
    private final String updateUserInitiatedLinkingQuery = "UPDATE " + table + " SET " +
            Column.PATIENT_ID.getColumnName() + " = ?, " +
            Column.STATUS.getColumnName() + " = ?, " +
            Column.LINK_REFERENCE_NUMBER.getColumnName() + " = ?, " +
            Column.OTP.getColumnName() + " = ? " +
            Column.OTP_EXPIRY_TIME.getColumnName() + " = ? " +
            Column.INIT_LINK_REQUEST.getColumnName() + " = ?::jsonb" +
            "WHERE " + Column.TRANSACTION_ID.getColumnName() + " = ?";

    @Autowired
    public UserInitiatedLinkingDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertQuery = "INSERT INTO " + table +
                " (" + Column.allColumNamesSeparatedByComma() + ") VALUES (?, ?, ?, ?, ?, ?, ?::jsonb)";
        this.getByTransactionIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE transaction_id = ?";
        this.getByLinkReferenceNumberQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE link_reference_number = ?";
    }

    public CompletionStage<Void> insert(UserInitiatedLinking userInitiatedLinking) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertQuery,
                        userInitiatedLinking.getTransactionId(),
                        userInitiatedLinking.getPatientId(),
                        userInitiatedLinking.getStatus(),
                        userInitiatedLinking.getLinkReferenceNumber(),
                        userInitiatedLinking.getOtp(),
                        Optional.ofNullable(userInitiatedLinking.getOtpExpiryTime()).map(Timestamp::new).orElse(null),
                        getJsonbObject(gson.toJson(userInitiatedLinking.getInitLinkRequest())))
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateUserInitiatedLinking(UserInitiatedLinking userInitiatedLinking) {
        return postgresDAO.update(dbMetricsGroupName, "updateUserInitiatedLinking", updateUserInitiatedLinkingQuery,
                        userInitiatedLinking.getPatientId(),
                        userInitiatedLinking.getStatus(),
                        userInitiatedLinking.getLinkReferenceNumber(),
                        userInitiatedLinking.getOtp(),
                        new Timestamp(userInitiatedLinking.getOtpExpiryTime()),
                        getJsonbObject(gson.toJson(userInitiatedLinking.getInitLinkRequest())),
                        userInitiatedLinking.getTransactionId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<UserInitiatedLinking> getByTransactionId(String transactionId) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "getByTransactionId",
                        getByTransactionIdQuery, (rs, rowNum) ->
                        UserInitiatedLinking.builder()
                                .transactionId(rs.getString("transaction_id"))
                                .patientId(rs.getString("patient_id"))
                                .status(rs.getString("status"))
                                .linkReferenceNumber(rs.getString("link_reference_number"))
                                .otp(rs.getString("otp"))
                                .otpExpiryTime(rs.getTimestamp("otp_expiry_time") != null ? rs.getTimestamp("otp_expiry_time").getTime() : null)
                                .initLinkRequest(rs.getString("init_link_request") != null ? gson.fromJson(rs.getString("init_link_request"), HipWebhookController.InitCareContextLinkRequest.class) : null)
                                .build(), transactionId);
    }

    public CompletionStage<UserInitiatedLinking> getByLinkReferenceNumber(String linkReferenceNumber) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "getByLinkReferenceNumber",
                getByLinkReferenceNumberQuery, (rs, rowNum) ->
                        UserInitiatedLinking.builder()
                                .transactionId(rs.getString("transaction_id"))
                                .patientId(rs.getString("patient_id"))
                                .status(rs.getString("status"))
                                .linkReferenceNumber(rs.getString("link_reference_number"))
                                .otp(rs.getString("otp"))
                                .otpExpiryTime(rs.getTimestamp("otp_expiry_time") != null ? rs.getTimestamp("otp_expiry_time").getTime() : null)
                                .initLinkRequest(rs.getString("init_link_request") != null ? gson.fromJson(rs.getString("init_link_request"), HipWebhookController.InitCareContextLinkRequest.class) : null)
                                .build(), linkReferenceNumber);
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

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table);
    }

    public enum Column {
        TRANSACTION_ID("transaction_id", false),
        PATIENT_ID("patient_id", false),
        STATUS("status", true),
        LINK_REFERENCE_NUMBER("link_reference_number", true),
        OTP("otp", true),
        OTP_EXPIRY_TIME("otp_expiry_time", true),
        INIT_LINK_REQUEST("init_link_request", true);

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
