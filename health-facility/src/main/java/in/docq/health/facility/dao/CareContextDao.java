package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.CareContext;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class CareContextDao {
    private final static Logger logger = LoggerFactory.getLogger(CareContextDao.class);
    private final String dbMetricsGroupName = "care_context";
    private final String table = "care_context";
    private final String insertCareContextQuery;
    private final String replaceCareContextQuery;
    private final String getCareContextQuery;
    private final Gson gson = new Gson();
    private final PostgresDAO postgresDAO;

    @Autowired
    public CareContextDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertCareContextQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")";
        this.replaceCareContextQuery = "UPDATE " + table + " SET is_hip_linked = ?, is_user_linked = ? WHERE opd_date = ? and opd_id = ? and appointment_id = ?";
        this.getCareContextQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE opd_date = ? and opd_id = ? and appointment_id = ?";
    }

    public CompletionStage<Void> insert(CareContext careContext) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertCareContextQuery, Date.valueOf(careContext.getDate()), careContext.getOpdID(), careContext.getAppointmentID(), careContext.isHipLinked(), careContext.isUserLinked())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> replace(CareContext careContext) {
        return postgresDAO.update(dbMetricsGroupName, "replace", replaceCareContextQuery, careContext.isHipLinked(), careContext.isUserLinked(), Date.valueOf(careContext.getDate()), careContext.getOpdID(), careContext.getAppointmentID())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<CareContext> get(String opdID, Integer appointmentID, Date opdDate) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getCareContextQuery, (rs, rowNum) -> CareContext.builder()
                .opdID(rs.getString(Column.OPD_ID.getColumnName()))
                .appointmentID(rs.getInt(Column.APPOINTMENT_ID.getColumnName()))
                .date(rs.getDate(Column.OPD_DATE.getColumnName()).toLocalDate())
                .isHipLinked(rs.getBoolean(Column.IS_HIP_LINKED.getColumnName()))
                .build(), opdDate, opdID, appointmentID);
    }

    public enum Column {
        OPD_DATE("opd_date", false),
        OPD_ID("opd_id", false),
        APPOINTMENT_ID("appointment_id", false),
        IS_HIP_LINKED("content", true),
        IS_USER_LINKED("content", true);

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
