package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HealthProfessional {
    private final String id;
    private final String healthFacilityID;
    private final HealthProfessionalType type;

    public String getKeyCloakUserName() {
        return healthFacilityID + "_" + id;
    }

    public String getKeycloakRole() {
        return type.getKeyCloakRole();
    }
}
