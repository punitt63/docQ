package in.docq.health.facility.dao;

import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.FacilityManager;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class FacilityManagerDao {
    private final String dbMetricsGroupName = "facilityManager";
    private final String table = "facility_manager";
    private final String insertFacilityManagerQuery;
    private final String getFacilityManagerQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public FacilityManagerDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertFacilityManagerQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (?, ?) on conflict do nothing";
        this.getFacilityManagerQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and facility_manager_id = ?";
    }

    public CompletionStage<Void> insert(String healthFacilityID, String facilityManagerID) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertFacilityManagerQuery, healthFacilityID, facilityManagerID)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Optional<FacilityManager>> get(String healthFacilityID, String facilityManagerID) {
        return postgresDAO.queryForOptionalObject(dbMetricsGroupName, "get", getFacilityManagerQuery, (rs, rowNum) ->
                        FacilityManager.builder()
                                .healthFacilityID(rs.getString("health_facility_id"))
                                .id(rs.getString("facility_manager_id"))
                                .build(), healthFacilityID, facilityManagerID);
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        FACILITY_MANAGER_ID("facility_manager_id");

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
    }
}