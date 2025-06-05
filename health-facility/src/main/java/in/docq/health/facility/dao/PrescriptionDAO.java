package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.model.Prescription;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class PrescriptionDAO {
    private final static Logger logger = LoggerFactory.getLogger(PrescriptionDAO.class);
    private final String dbMetricsGroupName = "prescription";
    private final String table = "prescription";
    private final String insertOPDPrescriptionQuery;
    private final String replaceOPDPrescriptionQuery;
    private final String getAppointmentQuery;
    private final Gson gson = new Gson();
    private final PostgresDAO postgresDAO;

    @Autowired
    public PrescriptionDAO(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertOPDPrescriptionQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")";
        this.replaceOPDPrescriptionQuery = "UPDATE " + table + " SET content = ? WHERE opd_date = ? and opd_id = ? and appointment_id = ?";
        this.getAppointmentQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE opd_date = ? and opd_id = ? and appointment_id = ?";
    }

    public CompletionStage<Void> insert(Prescription prescription) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertOPDPrescriptionQuery, Date.valueOf(prescription.getDate()), prescription.getOpdID(), prescription.getAppointmentID(), getJsonbObject(prescription.getContent()))
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> replace(Prescription prescription) {
        return postgresDAO.update(dbMetricsGroupName, "replace", replaceOPDPrescriptionQuery, getJsonbObject(prescription.getContent()), Date.valueOf(prescription.getDate()), prescription.getOpdID(), prescription.getAppointmentID())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Prescription> get(String opdID, Integer appointmentID, Date opdDate) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getAppointmentQuery, (rs, rowNum) -> Prescription.builder()
                .opdID(rs.getString(Column.OPD_ID.getColumnName()))
                .appointmentID(rs.getInt(Column.APPOINTMENT_ID.getColumnName()))
                .date(rs.getDate(Column.OPD_DATE.getColumnName()).toLocalDate())
                .content(rs.getString(Column.CONTENT.getColumnName()))
                .build(), opdDate, opdID, appointmentID);
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
        OPD_DATE("opd_date", false),
        OPD_ID("opd_id", false),
        APPOINTMENT_ID("appointment_id", false),
        CONTENT("content", true);

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
