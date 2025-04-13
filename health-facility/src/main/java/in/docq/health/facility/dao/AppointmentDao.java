package in.docq.health.facility.dao;

import in.docq.health.facility.controller.AppointmentController;
import in.docq.health.facility.exception.ErrorCodes;
import in.docq.health.facility.exception.HealthFacilityException;
import in.docq.health.facility.model.Appointment;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class AppointmentDao {
    private final static Logger logger = LoggerFactory.getLogger(AppointmentDao.class);
    private final String dbMetricsGroupName = "appointment";
    private final String table = "appointment";
    private final String insertAppointmentQuery;
    private final String listAppointmentsQuery;
    private final String getAppointmentQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public AppointmentDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertAppointmentQuery = """
        WITH update_count AS (
        UPDATE opd set appointments_count = appointments_count + 1 WHERE opd_date = ? and id = ?
        )
        INSERT INTO appointment (opd_date, opd_id, patient_id, state) VALUES (?, ?, ?, ?) RETURNING *
        """;
        this.listAppointmentsQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE opd_date >= ? and opd_date <= ?";
        this.getAppointmentQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE opd_date >= ? and opd_id = ? and id = ?";
    }

    public CompletionStage<Appointment> insert(Appointment appointment) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "insert", insertAppointmentQuery, (rs, rowNum) -> Appointment.builder()
                                .id(rs.getInt(Column.ID.columnName))
                                .opdDate(appointment.getOpdDate())
                                .opdId(appointment.getOpdId())
                                .patientId(appointment.getPatientId())
                                .state(Appointment.State.valueOf(rs.getString(Column.STATE.columnName)))
                                .startTime(Optional.ofNullable(rs.getTimestamp(Column.START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .endTime(Optional.ofNullable(rs.getTimestamp(Column.END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .cancellationReason(rs.getString(Column.CANCELLATION_REASON.columnName))
                                .priority(rs.getInt(Column.PRIORITY.columnName))
                                .build(),
                        Date.valueOf(appointment.getOpdDate()),
                        appointment.getOpdId(),
                        Date.valueOf(appointment.getOpdDate()),
                        appointment.getOpdId(),
                        appointment.getPatientId(),
                        appointment.getState().name())
                .handle((res, throwable) -> {
                    if(throwable != null) {
                        throwable = throwable.getCause();
                        if(throwable instanceof DuplicateKeyException) {
                            return null;
                        }
                        if(throwable.getMessage().contains("APPOINTMENTS_COUNT_EXCEEDED")) {
                            throw new CompletionException(new HealthFacilityException(ErrorCodes.APPOINTMENTS_LIMIT_BREACHED));
                        }
                        throw new CompletionException(throwable);
                    }
                    return null;
                });
    }

    public CompletionStage<Appointment> update(LocalDate opdDate, String opdID, Integer id, AppointmentController.UpdateAppointmentRequestBody updateAppointmentRequestBody) {
        String query = "UPDATE appointment set ";
        List<String> settableColumns = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        if(updateAppointmentRequestBody.getState() != null) {
            settableColumns.add("state = ?");
            args.add(updateAppointmentRequestBody.getState().name());
        }
        if(updateAppointmentRequestBody.getStartTime() != null) {
            settableColumns.add("start_time = ?");
            args.add(new Timestamp(updateAppointmentRequestBody.getStartTime()));
        }
        if(updateAppointmentRequestBody.getEndTime() != null) {
            settableColumns.add("end_time = ?");
            args.add(new Timestamp(updateAppointmentRequestBody.getEndTime()));
        }
        if(updateAppointmentRequestBody.getCancellationReason() != null) {
            settableColumns.add("cancellation_reason = ?");
            args.add(updateAppointmentRequestBody.getCancellationReason());
        }
        String whereClause = " WHERE opd_date = ? and opd_id = ? and id = ? RETURNING *";
        args.add(opdDate);
        args.add(opdID);
        args.add(id);
        return postgresDAO.queryForObject(dbMetricsGroupName, "update", query + String.join(",", settableColumns) + whereClause, (rs, rowNum) -> Appointment.builder()
                                .id(id)
                                .opdDate(opdDate)
                                .opdId(opdID)
                                .patientId(rs.getString(Column.PATIENT_ID.columnName))
                                .state(Appointment.State.valueOf(rs.getString(Column.STATE.columnName)))
                                .startTime(Optional.ofNullable(rs.getTimestamp(Column.START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .endTime(Optional.ofNullable(rs.getTimestamp(Column.END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .cancellationReason(rs.getString(Column.CANCELLATION_REASON.columnName))
                                .priority(rs.getInt(Column.PRIORITY.columnName))
                        .build(),
                args.toArray());
    }

    public CompletionStage<Appointment> get(LocalDate opdDate, String opdID, Integer id) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getAppointmentQuery, (rs, rowNum) -> Appointment.builder()
                .id(rs.getInt(Column.ID.columnName))
                .opdDate(opdDate)
                .opdId(opdID)
                .patientId(rs.getString(Column.PATIENT_ID.columnName))
                .state(Appointment.State.valueOf(rs.getString(Column.STATE.columnName)))
                .startTime(Optional.ofNullable(rs.getTimestamp(Column.START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                .endTime(Optional.ofNullable(rs.getTimestamp(Column.END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                .cancellationReason(rs.getString(Column.CANCELLATION_REASON.columnName))
                .priority(rs.getInt(Column.PRIORITY.columnName))
                .build(), opdDate, opdID, id)
                .handle((res, throwable) -> {
                    if(throwable != null) {
                        if(throwable.getCause() instanceof EmptyResultDataAccessException) {
                            throw new HealthFacilityException(ErrorCodes.APPOINTMENT_NOT_FOUND);
                        }
                        throw new CompletionException(throwable.getCause());
                    }
                    return res;
                });
    }

    public CompletionStage<List<Appointment>> list(LocalDate startOpdDate, LocalDate endOpdDate, List<String> opdIDs, String patientID, List<Appointment.State> states, int limit) {
        String query = listAppointmentsQuery;
        List<Object> args = new ArrayList<>();
        args.add(Date.valueOf(startOpdDate));
        args.add(Date.valueOf(endOpdDate));
        if(opdIDs != null && !opdIDs.isEmpty()) {
            query = query + " and opd_id IN (" + opdIDs.stream().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + ")";
        }
        if(patientID != null) {
            query = query + " and patient_id = ? ";
            args.add(patientID);
        }
        if(states != null) {
            query = query + " and state IN (" + states.stream().map(s -> "'" + s.name() + "'").collect(Collectors.joining(",")) + ")";
        }
        args.add(limit);
        query = query + " ORDER BY opd_date, priority desc, id limit ?";
        return postgresDAO.query(dbMetricsGroupName, "list", query, (rs, rowNum) -> Appointment.builder()
                        .id(rs.getInt(Column.ID.columnName))
                        .opdDate(startOpdDate)
                        .opdId(rs.getString(Column.OPD_ID.columnName))
                        .patientId(rs.getString(Column.PATIENT_ID.columnName))
                        .state(Appointment.State.valueOf(rs.getString(Column.STATE.columnName)))
                        .startTime(Optional.ofNullable(rs.getTimestamp(Column.START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                        .endTime(Optional.ofNullable(rs.getTimestamp(Column.END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                        .cancellationReason(rs.getString(Column.CANCELLATION_REASON.columnName))
                        .priority(rs.getInt(Column.PRIORITY.columnName))
                        .build(),
                args.toArray());
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> postgresDAO.update(dbMetricsGroupName, "delete", "ALTER SEQUENCE appointment_id_seq RESTART WITH 1"));
    }

    public enum Column {
        ID("id", false),
        OPD_DATE("opd_date", false),
        OPD_ID("opd_id", false),
        PATIENT_ID("patient_id", false),
        STATE("state", true),
        START_TIME("start_time", true),
        END_TIME("end_time", true),
        CANCELLATION_REASON("cancellation_reason", true),
        PRIORITY("priority", true);

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
                    .map(AppointmentDao.Column::getColumnName)
                    .collect(Collectors.joining(","));
        }

        public static String allColumnValuesSeparatedByComma() {
            return Arrays.stream(values())
                    .map(ignore -> "?")
                    .collect(Collectors.joining(","));
        }

        public static String allModifiableColumnNamesSeparatedByComma() {
            return Arrays.stream(values())
                    .filter(AppointmentDao.Column::isModifiable)
                    .map(column -> column.getColumnName() + " = ?")
                    .collect(Collectors.joining(","));
        }
    }
}
