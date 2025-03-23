package in.docq.health.facility.exception;

public class HealthProfessionalNotFound extends RuntimeException {

    public HealthProfessionalNotFound(String healthFacility, String healthProfessionalID) {
        super(String.format("HealthFacility:%s, HealthProfessionalID:%s does not Exist", healthFacility, healthProfessionalID));
    }
}
