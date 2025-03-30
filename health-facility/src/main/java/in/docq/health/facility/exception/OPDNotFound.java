package in.docq.health.facility.exception;

public class OPDNotFound extends RuntimeException {
    public OPDNotFound(String healthFacility, String healthProfessionalID, String name) {
        super(String.format("HealthFacility:%s, HealthProfessionalID:%s, Name:%s does not Exist", healthFacility, healthProfessionalID, name));
    }
}
