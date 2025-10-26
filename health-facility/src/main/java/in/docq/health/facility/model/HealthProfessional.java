package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HealthProfessional {
    private final String id;
    private final String healthProfessionalName;
    private final HealthProfessionalType type;
    private final String healthFacilityID;

    public String getKeyCloakUserName() {
        return healthFacilityID.toLowerCase() + "_" + id.toLowerCase();
    }

    public String getKeycloakRole() {
        return type.getKeyCloakRole();
    }
}
