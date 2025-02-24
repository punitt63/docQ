package in.docq.health.facility.model;

import lombok.Builder;

@Builder
public class HealthProfessional {
    private final String abhaID;
    private final String abhaHealthFacilityID;
    private final HealthProfessionalType type;

    public String getKeyCloakUserName() {
        return abhaID + "_" + abhaHealthFacilityID;
    }

    public String getKeycloakRole() {
        return type.getKeyCloakRole();
    }
}
