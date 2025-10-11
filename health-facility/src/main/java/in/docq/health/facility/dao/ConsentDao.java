package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.Consent;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class ConsentDao {
    private final String dbMetricsGroupName = "consent";
    private final String table = "consent";
    private final String insertQuery;
    private final String getByIdQuery;
    private final String updateStatusQuery;

    private final PostgresDAO postgresDAO;
    private final Gson gson;

    @Autowired
    public ConsentDao(PostgresDAO postgresDAO, Gson gson) {
        this.postgresDAO = postgresDAO;
        this.gson = gson;
        this.insertQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (?, ?::jsonb, ?)";
        this.getByIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE id = ?";
        this.updateStatusQuery = "UPDATE " + table + " SET status = ? WHERE id = ?";
    }

    public CompletionStage<Void> insert(String consentId, Consent consent, String status) {
        String requestJson = gson.toJson(consent);
        return postgresDAO.update(dbMetricsGroupName, "insert", insertQuery,
                        consentId,
                        requestJson,
                        status)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<Consent>> getById(String id) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "getById",
                getByIdQuery, this::mapRow, id);
    }

    public CompletionStage<Void> updateStatus(String id, String status) {
        return postgresDAO.update(dbMetricsGroupName, "updateStatus", updateStatusQuery, status, id)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> deleteById(String id) {
        String deleteQuery = "DELETE FROM " + table + " WHERE id = ?";
        return postgresDAO.update(dbMetricsGroupName, "deleteById", deleteQuery, id)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    private Consent mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String requestJsonStr = rs.getString(Column.CONTENT.getColumnName());
            String status = rs.getString(Column.STATUS.getColumnName());
            Consent consent = gson.fromJson(requestJsonStr, Consent.class);
            return consent.toBuilder().status(status).build();
        } catch (Exception e) {
            throw new SQLException("Failed to map consent row", e);
        }
    }

    public enum Column {
        ID("id"),
        CONTENT("content"),
        STATUS("status");

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