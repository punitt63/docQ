package in.docq.health.facility.dao;

import in.docq.health.facility.model.UserInitiatedLinking;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class UserInitiatedLinkingDao {
    private static final String table = "user_initiated_linking";
    private static final String dbMetricsGroupName = "UserInitiatedLinking";

    private final PostgresDAO postgresDAO;

    private final String insertQuery;
    private final String getByTransactionIdQuery;
    private final String updateUserInitiatedLinkingQuery = "UPDATE " + table + " SET " +
            Column.PATIENT_ID.getColumnName() + " = ?, " +
            Column.STATUS.getColumnName() + " = ?, " +
            Column.LINK_REFERENCE_NUMBER.getColumnName() + " = ?, " +
            Column.OTP.getColumnName() + " = ? " +
            "WHERE " + Column.TRANSACTION_ID.getColumnName() + " = ?";

    @Autowired
    public UserInitiatedLinkingDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertQuery = "INSERT INTO " + table +
                " (" + Column.allColumNamesSeparatedByComma() + ") VALUES (?, ?, ?, ?, ?)";
        this.getByTransactionIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() +
                " FROM " + table + " WHERE transaction_id = ?";
    }

    public CompletionStage<Void> insert(UserInitiatedLinking userInitiatedLinking) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertQuery,
                        userInitiatedLinking.getTransactionId(),
                        userInitiatedLinking.getPatientId(),
                        userInitiatedLinking.getStatus(),
                        userInitiatedLinking.getLinkReferenceNumber(),
                        userInitiatedLinking.getOtp(),
                        new Timestamp(userInitiatedLinking.getOtpExpiryTime()))
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> updateUserInitiatedLinking(UserInitiatedLinking userInitiatedLinking) {
        return postgresDAO.update(dbMetricsGroupName, "updateUserInitiatedLinking", updateUserInitiatedLinkingQuery,
                        userInitiatedLinking.getPatientId(),
                        userInitiatedLinking.getStatus(),
                        userInitiatedLinking.getLinkReferenceNumber(),
                        userInitiatedLinking.getOtp(),
                        new Timestamp(userInitiatedLinking.getOtpExpiryTime()),
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
                                .build(), transactionId);
    }

    public enum Column {
        TRANSACTION_ID("appointment_id", false),
        PATIENT_ID("patient_id", false),
        STATUS("status", true),
        LINK_REFERENCE_NUMBER("link_reference_number", true),
        OTP("otp", true),
        OTP_EXPIRY_TIME("otp_expiry_time", true);

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
