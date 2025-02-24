package in.docq.health.facility.model;

import lombok.Getter;

public enum HealthProfessionalType {
    DOCTOR("doctor"),
    FACILITY_MANAGER("facility-manager");
    @Getter
    private final String keyCloakRole;

    HealthProfessionalType(String keyCloakRole) {
        this.keyCloakRole = keyCloakRole;
    }
}
