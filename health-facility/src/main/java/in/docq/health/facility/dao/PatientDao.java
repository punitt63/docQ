package in.docq.health.facility.dao;

import in.docq.health.facility.model.Patient;
import in.docq.spring.boot.commons.postgres.PostgresDAO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class PatientDao {
    private final static Logger logger = LoggerFactory.getLogger(PatientDao.class);
    private final String dbMetricsGroupName = "patient";
    private final String table = "patient";
    private final String insertPatientQuery;
    private final String getPatientQuery;
    private final String getPatientByIdQuery;
    private final String updatePatientQuery;
    private final String getPatientsByMobileQuery;
    private final String getPatientByAbhaAddressQuery;
    private final PostgresDAO postgresDAO;
    private final SecretKey aesEncryptionKey;

    @Autowired
    public PatientDao(PostgresDAO postgresDAO, SecretKey aesEncryptionKey) {
        this.aesEncryptionKey = aesEncryptionKey;
        this.postgresDAO = postgresDAO;
        this.insertPatientQuery = "INSERT INTO " + table + "(" + Column.allColumNamesSeparatedByComma() + ")" +
                " VALUES (" + Column.allColumnValuesSeparatedByComma() + ")";
        this.updatePatientQuery = "UPDATE " + table + " SET " + Column.updateableColumnsSeparatedByComma() +
                " WHERE " + Column.ID.getColumnName() + " = ?";
        this.getPatientsByMobileQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.MOBILE_NO.getColumnName() + " = ?";
        this.getPatientByAbhaAddressQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.ABHA_ADDRESS.getColumnName() + " = ?";
        this.getPatientQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.MOBILE_NO.getColumnName() + " = ?" +
                " AND " + Column.NAME.getColumnName() + " = ?" +
                " AND " + Column.DOB.getColumnName() + " = ?";
        this.getPatientByIdQuery = "SELECT " + Column.allColumNamesSeparatedByComma() + " FROM " + table +
                " WHERE " + Column.ID.getColumnName() + " = ?";
    }

    public CompletionStage<Void> insert(Patient patient) {
        return postgresDAO.update(dbMetricsGroupName, "insert", insertPatientQuery,
                        patient.getId(),
                        patient.getAbhaNo(),
                        patient.getAbhaAddress(),
                        patient.getName(),
                        patient.getMobileNo(),
                        Date.valueOf(patient.getDob()),
                        patient.getGender())
                .thenAccept(ignore -> {});
    }

    public CompletionStage<Void> update(String id, Patient newpatient) {
        return postgresDAO.update(dbMetricsGroupName, "update", updatePatientQuery,
                        newpatient.getAbhaNo(),
                        newpatient.getAbhaAddress(),
                        newpatient.getName(),
                        newpatient.getMobileNo(),
                        Date.valueOf(newpatient.getDob()),
                        newpatient.getGender(),
                        id)
                .thenAccept(ignore -> {});
    }

    public CompletionStage<List<Patient>> searchByMobile(String mobileNo) {
        return postgresDAO.query(dbMetricsGroupName, "getByMobile", getPatientsByMobileQuery,
                (rs, rowNum) -> Patient.builder()
                        .id(rs.getString(Column.ID.getColumnName()))
                        .abhaNo(rs.getString(Column.ABHA_NO.getColumnName()))
                        .abhaAddress(rs.getString(Column.ABHA_ADDRESS.getColumnName()))
                        .name(rs.getString(Column.NAME.getColumnName()))
                        .mobileNo(rs.getString(Column.MOBILE_NO.getColumnName()))
                        .dob(rs.getDate(Column.DOB.getColumnName()).toLocalDate())
                        .gender(rs.getString(Column.GENDER.getColumnName()))
                        .build(),
                mobileNo);
    }

    public CompletionStage<Patient> getByAbhaAddress(String abhaAddress) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "getByAbhaAddress", getPatientByAbhaAddressQuery,
                (rs, rowNum) -> Patient.builder()
                        .id(rs.getString(Column.ID.getColumnName()))
                        .abhaNo(rs.getString(Column.ABHA_NO.getColumnName()))
                        .abhaAddress(rs.getString(Column.ABHA_ADDRESS.getColumnName()))
                        .name(rs.getString(Column.NAME.getColumnName()))
                        .mobileNo(rs.getString(Column.MOBILE_NO.getColumnName()))
                        .dob(rs.getDate(Column.DOB.getColumnName()).toLocalDate())
                        .gender(rs.getString(Column.GENDER.getColumnName()))
                        .build(),
                abhaAddress);
    }

    public CompletionStage<Patient> search(String mobileNo, String name, LocalDate dob) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getPatientQuery,
                (rs, rowNum) -> Patient.builder()
                        .abhaNo(rs.getString(Column.ABHA_NO.getColumnName()))
                        .abhaAddress(rs.getString(Column.ABHA_ADDRESS.getColumnName()))
                        .name(rs.getString(Column.NAME.getColumnName()))
                        .mobileNo(rs.getString(Column.MOBILE_NO.getColumnName()))
                        .dob(rs.getDate(Column.DOB.getColumnName()).toLocalDate())
                        .gender(rs.getString(Column.GENDER.getColumnName()))
                        .build(),
                mobileNo, name, Date.valueOf(dob));
    }

    public CompletionStage<Patient> get(String id) {
        return postgresDAO.queryForObject(dbMetricsGroupName, "get", getPatientByIdQuery,
                (rs, rowNum) -> Patient.builder()
                        .id(rs.getString(Column.ID.getColumnName()))
                        .abhaNo(rs.getString(Column.ABHA_NO.getColumnName()))
                        .abhaAddress(rs.getString(Column.ABHA_ADDRESS.getColumnName()))
                        .name(rs.getString(Column.NAME.getColumnName()))
                        .mobileNo(rs.getString(Column.MOBILE_NO.getColumnName()))
                        .dob(rs.getDate(Column.DOB.getColumnName()).toLocalDate())
                        .gender(rs.getString(Column.GENDER.getColumnName()))
                        .build(),
                id);
    }

    public CompletionStage<Integer> truncate() {
        return postgresDAO.update(dbMetricsGroupName, "truncate", "DELETE FROM " + table);
    }

    public enum Column {
        ID("id", false),
        ABHA_NO("abha_no", true),
        ABHA_ADDRESS("abha_address", true),
        NAME("name", true),
        MOBILE_NO("mobile_no", true),
        DOB("dob", true),
        GENDER("gender", true);

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

        public static String updateableColumnsSeparatedByComma() {
            return Arrays.stream(values())
                    .filter(Column::isModifiable)
                    .map(column -> column.getColumnName() + " = ?")
                    .collect(Collectors.joining(","));
        }
    }
}