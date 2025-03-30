package in.docq.health.facility.dao;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import in.docq.health.facility.exception.OPDNotFound;
import in.docq.health.facility.model.OPD;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
    private final PostgresDAO postgresDAO;
    private static Gson gson = new Gson();

    @Autowired
    public OPDDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertOPDQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (" + Column.allColumnValuesSeparatedByComma() + ") on conflict do nothing";
        this.getOPDQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and health_professional_id = ? and name = ?";
        this.updateOPDQuery = "UPDATE " + table + " set " + Column.allModifiableColumnNamesSeparatedByComma() + " WHERE health_facility_id = ? and health_professional_id = ? and name = ?";
    }

    public CompletionStage<Void> insert(OPD opd) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertOPDQuery,
                        opd.getHealthFacilityID(),
                        opd.getHealthProfessionalID(),
                        opd.getName(),
                        opd.getStartHour(),
                        opd.getEndHour(),
                        opd.getStartMinute(),
                        opd.getEndMinute(),
                        opd.isRecurring(),
                        opd.getStartDate(),
                        opd.getScheduleType().name(),
                        getJsonbObject(gson.toJson(opd.getWeeklyTemplate())),
                        opd.getMaxSlots(),
                        opd.getMinutesPerSlot(),
                        opd.getInstanceCreationMinutesBeforeStart(),
                        opd.getState().name())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<OPD> get(String healthFacilityID, String healthProfessionalID, String name) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getOPDQuery, (rs, rowNum) ->
                        OPD.builder()
                                .healthFacilityID(rs.getString("health_facility_id"))
                                .healthProfessionalID(rs.getString("health_professional_id"))
                                .name(rs.getString("name"))
                                .startHour(rs.getInt("start_hour"))
                                .endHour(rs.getInt("end_hour"))
                                .startMinute(rs.getInt("start_minute"))
                                .endMinute(rs.getInt("end_minute"))
                                .startDate(rs.getDate("start_date").toLocalDate())
                                .recurring(rs.getBoolean("recurring"))
                                .scheduleType(OPD.ScheduleType.valueOf(rs.getString("schedule_type")))
                                .weeklyTemplate(gson.fromJson(rs.getString("weekly_template"), new TypeToken<List<Boolean>>(){}.getType()))
                                .maxSlots(rs.getInt("max_slots"))
                                .minutesPerSlot(rs.getInt("minutes_per_slot"))
                                .instanceCreationMinutesBeforeStart(rs.getInt("instance_creation_minutes_before_start"))
                                .state(OPD.State.valueOf(rs.getString("state")))
                                .build(), healthFacilityID, healthProfessionalID, name)
                .exceptionally((throwable) -> {
                    throwable = throwable.getCause();
                    if (throwable instanceof EmptyResultDataAccessException) {
                        throw new CompletionException(new OPDNotFound(healthFacilityID, healthProfessionalID, name));
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
                        opd.isRecurring(),
                        opd.getScheduleType().name(),
                        getJsonbObject(gson.toJson(opd.getWeeklyTemplate())),
                        opd.getMaxSlots(),
                        opd.getMinutesPerSlot(),
                        opd.getInstanceCreationMinutesBeforeStart(),
                        opd.getState().name(),
                        opd.getHealthFacilityID(),
                        opd.getHealthProfessionalID(),
                        opd.getName())
                .thenAccept(ignore -> {});
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
        HEALTH_FACILITY_ID("health_facility_id", false),
        HEALTH_PROFESSIONAL_ID("health_professional_id", false),
        NAME("name", false),
        START_HOUR("start_hour", true),
        END_HOUR("end_hour", true),
        START_MINUTE("start_minute", true),
        END_MINUTE("end_minute", true),
        RECURRING("recurring", true),
        START_DATE("start_date", false),
        SCHEDULE_TYPE("schedule_type", true),
        WEEKLY_TEMPLATE("weekly_template", true),
        MAX_SLOTS("max_slots", true),
        MINUTES_PER_SLOT("minutes_per_slot", true),
        INSTANCE_CREATION_MINUTES_BEFORE_START("instance_creation_minutes_before_start", true),
        STATE("state", true);

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
