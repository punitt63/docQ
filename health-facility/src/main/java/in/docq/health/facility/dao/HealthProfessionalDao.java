package in.docq.health.facility.dao;

import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class HealthProfessionalDao {
    private final String dbMetricsGroupName = "healthProfessional";
    private final String table = "health_professional";
    private final String insertHealthProfessionalQuery;
    private final PostgresDAO postgresDAO;

    @Autowired
    public HealthProfessionalDao(PostgresDAO postgresDAO) {
        this.postgresDAO = postgresDAO;
        this.insertHealthProfessionalQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" + " VALUES (?, ?, ?)";
    }

    public CompletionStage<Void> insert(String healthFacilityID, String healthProfessionalID, HealthProfessionalType type) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertHealthProfessionalQuery, healthProfessionalID, healthProfessionalID, type.name())
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
