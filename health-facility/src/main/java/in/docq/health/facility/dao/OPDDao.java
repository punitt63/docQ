package in.docq.health.facility.dao;

import com.google.gson.Gson;
import in.docq.health.facility.exception.OPDNotFound;
import in.docq.health.facility.model.OPD;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class OPDDao {
    private final String dbMetricsGroupName = "opd";
    private final String table = "opd";
    private final String insertOPDQuery;
    private final String getOPDQuery;
    private final String updateOPDQuery;
    private final String listOPDQuery;
    private final String createSequenceQuery;
    private final PostgresDAO postgresDAO;
    private static Gson gson = new Gson();

    @Autowired
    public OPDDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertOPDQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (" + Column.allColumnValuesSeparatedByComma() + ") on conflict do nothing";
        this.getOPDQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE opd_date = ? and id = ?";
        this.updateOPDQuery = "UPDATE " + table + " set " + Column.allModifiableColumnNamesSeparatedByComma() + " WHERE opd_date = ? and id = ?";
        this.listOPDQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and health_professional_id = ? and opd_date >= ? and opd_date <= ? order by opd_date";
        this.createSequenceQuery = "CREATE SEQUENCE IF NOT EXISTS %s";
    }

    public CompletionStage<Void> insert(List<OPD> opds) {
        return postgresDAO.batchUpdate(dbMetricsGroupName, "batchInsert", insertOPDQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OPD opd = opds.get(i);
                ps.setString(1, opd.getId());
                ps.setString(2, opd.getHealthFacilityID());
                ps.setString(3, opd.getHealthProfessionalID());
                ps.setString(4, opd.getName());
                ps.setInt(5, opd.getStartHour());
                ps.setInt(6, opd.getEndHour());
                ps.setInt(7, opd.getStartMinute());
                ps.setInt(8, opd.getEndMinute());
                ps.setDate(9, Date.valueOf(opd.getDate()));
                ps.setInt(10, opd.getMaxSlots());
                ps.setInt(11, opd.getMinutesPerSlot());
                ps.setTimestamp(12, new Timestamp(opd.getActivateTime()));
                ps.setString(13, opd.getState().name());
                ps.setTimestamp(14, Optional.ofNullable(opd.getActualStartTime()).map(Timestamp::new).orElse(null));
                ps.setTimestamp(15, Optional.ofNullable(opd.getActualEndTime()).map(Timestamp::new).orElse(null));
                ps.setInt(16, opd.getAppointmentsCount());
            }

            @Override
            public int getBatchSize() {
                return opds.size();
            }
        })
        .thenCompose(ignore -> createSequences(opds))
        .thenAccept(ignore -> {});
    }

    private CompletionStage<Void> createSequences(List<OPD> opds) {
        return CompletableFuture.allOf(opds.stream()
                .map(this::createSequence)
                .toList().toArray(new CompletableFuture[0]));
    }

    private CompletionStage<Integer> createSequence(OPD opd) {
        return postgresDAO.update(dbMetricsGroupName, "create", String.format(createSequenceQuery, opd.getSequenceName()));
    }

    public CompletionStage<OPD> get(LocalDate opdDate, String id) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getOPDQuery, (rs, rowNum) ->
                        OPD.builder()
                                .id(rs.getString(Column.ID.columnName))
                                .healthFacilityID(rs.getString(Column.HEALTH_FACILITY_ID.columnName))
                                .healthProfessionalID(rs.getString(Column.HEALTH_PROFESSIONAL_ID.columnName))
                                .name(rs.getString(Column.NAME.columnName))
                                .startHour(rs.getInt(Column.START_HOUR.columnName))
                                .endHour(rs.getInt(Column.END_HOUR.columnName))
                                .startMinute(rs.getInt(Column.START_MINUTE.columnName))
                                .endMinute(rs.getInt(Column.END_MINUTE.columnName))
                                .date(rs.getDate(Column.OPD_DATE.columnName).toLocalDate())
                                .maxSlots(rs.getInt(Column.MAX_SLOTS.columnName))
                                .minutesPerSlot(rs.getInt(Column.MINUTES_PER_SLOT.columnName))
                                .activateTime(rs.getTimestamp(Column.ACTIVATE_TIME.columnName).getTime())
                                .state(OPD.State.valueOf(rs.getString(Column.STATE.columnName)))
                                .actualStartTime(Optional.ofNullable(rs.getTimestamp(Column.ACTUAL_START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .actualEndTime(Optional.ofNullable(rs.getTimestamp(Column.ACTUAL_END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                                .appointmentsCount(rs.getInt(Column.APPOINTMENTS_COUNT.columnName))
                                .build(), Date.valueOf(opdDate), id)
                .exceptionally((throwable) -> {
                    throwable = throwable.getCause();
                    if (throwable instanceof EmptyResultDataAccessException) {
                        throw new CompletionException(new OPDNotFound(id));
                    }
                    throw new CompletionException(throwable);
                });
    }

    public CompletionStage<Void> update(OPD opd) {
        return postgresDAO.update(dbMetricsGroupName, "update", updateOPDQuery,
                        opd.getStartHour(),
                        opd.getEndHour(),
                        opd.getStartMinute(),
                        opd.getEndMinute(),
                        opd.getMaxSlots(),
                        opd.getMinutesPerSlot(),
                        new Timestamp(opd.getActivateTime()),
                        opd.getState().name(),
                        Optional.ofNullable(opd.getActualStartTime()).map(Timestamp::new).orElse(null),
                        Optional.ofNullable(opd.getActualEndTime()).map(Timestamp::new).orElse(null),
                        Date.valueOf(opd.getDate()),
                        opd.getId())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<List<OPD>> list(String healthFacilityID, String healthProfessionalID, LocalDate startDate, LocalDate endDate) {
        return postgresDAO.query(dbMetricsGroupName, "list", listOPDQuery, (rs, rowNum) ->
                OPD.builder()
                        .id(rs.getString(Column.ID.columnName))
                        .healthFacilityID(rs.getString(Column.HEALTH_FACILITY_ID.columnName))
                        .healthProfessionalID(rs.getString(Column.HEALTH_PROFESSIONAL_ID.columnName))
                        .name(rs.getString(Column.NAME.columnName))
                        .startHour(rs.getInt(Column.START_HOUR.columnName))
                        .endHour(rs.getInt(Column.END_HOUR.columnName))
                        .startMinute(rs.getInt(Column.START_MINUTE.columnName))
                        .endMinute(rs.getInt(Column.END_MINUTE.columnName))
                        .date(rs.getDate(Column.OPD_DATE.columnName).toLocalDate())
                        .maxSlots(rs.getInt(Column.MAX_SLOTS.columnName))
                        .minutesPerSlot(rs.getInt(Column.MINUTES_PER_SLOT.columnName))
                        .activateTime(rs.getTimestamp(Column.ACTIVATE_TIME.columnName).getTime())
                        .state(OPD.State.valueOf(rs.getString(Column.STATE.columnName)))
                        .actualStartTime(Optional.ofNullable(rs.getTimestamp(Column.ACTUAL_START_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                        .actualEndTime(Optional.ofNullable(rs.getTimestamp(Column.ACTUAL_END_TIME.columnName)).map(Timestamp::getTime).orElse(null))
                        .appointmentsCount(rs.getInt(Column.APPOINTMENTS_COUNT.columnName))
                        .build(), healthFacilityID, healthProfessionalID, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    private Object getJsonbObject(String json) {
        try {
            PGobject pGobject = new PGobject();
            pGobject.setType("jsonb");
            pGobject.setValue(json);
            return pGobject;
        } catch (SQLException var2) {
            throw new RuntimeException(var2);
        }
    }

    public enum Column {
        ID("id", false),
        HEALTH_FACILITY_ID("health_facility_id", false),
        HEALTH_PROFESSIONAL_ID("health_professional_id", false),
        NAME("name", false),
        START_HOUR("start_hour", true),
        END_HOUR("end_hour", true),
        START_MINUTE("start_minute", true),
        END_MINUTE("end_minute", true),
        OPD_DATE("opd_date", false),
        MAX_SLOTS("max_slots", true),
        MINUTES_PER_SLOT("minutes_per_slot", true),
        ACTIVATE_TIME("activate_time", true),
        STATE("state", true),
        ACTUAL_START_TIME("actual_start_time", true),
        ACTUAL_END_TIME("actual_end_time", true),
        APPOINTMENTS_COUNT("appointments_count", false);

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

        public static String allModifiableColumnNamesSeparatedByComma() {
            return Arrays.stream(values())
                    .filter(Column::isModifiable)
                    .map(column -> column.getColumnName() + " = ?")
                    .collect(Collectors.joining(","));
        }
    }
}
