package in.docq.health.facility.dao;

import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class HealthProfessionalDao {
    private final String dbMetricsGroupName = "healthProfessional";
    private final String table = "health_professional";
    private final String insertHealthProfessionalQuery;
    private final String getHealthProfessionalQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public HealthProfessionalDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertHealthProfessionalQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (?, ?, ?)";
        this.getHealthProfessionalQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table + " WHERE health_facility_id = ? and health_professional_id = ?";
    }

    public CompletionStage<Void> insert(String healthFacilityID, String healthProfessionalID, HealthProfessionalType type) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertHealthProfessionalQuery, healthFacilityID, healthProfessionalID, type.name())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<HealthProfessional> get(String healthFacilityID, String healthProfessionalID) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getHealthProfessionalQuery, (rs, rowNum) ->
                HealthProfessional.builder()
                        .healthFacilityID(rs.getString("health_facility_id"))
                        .id(rs.getString("health_professional_id"))
                        .type(HealthProfessionalType.valueOf(rs.getString("type")))
                .build(),healthFacilityID, healthProfessionalID);
    }

    public CompletionStage<Void> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "delete", "DELETE FROM " + table)
                .thenAccept(ignore -> {});
    }

    public enum Column {
        HEALTH_FACILITY_ID("health_facility_id"),
        HEALTH_PROFESSIONAL_ID("health_professional_id"),
        TYPE("type");

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
