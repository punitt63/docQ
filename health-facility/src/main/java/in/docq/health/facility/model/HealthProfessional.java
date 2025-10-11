package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HealthProfessional {
    private final String id;
    private final String healthProfessionalName;
    private final HealthProfessionalType type;
    private final String speciality;
    private final String healthFacilityID;
    private final String healthFacilityName;
    private final int stateCode;
    private final int districtCode;
    private final String address;
    private final String pincode;
    private final Double latitude;
    private final Double longitude;

    public String getKeyCloakUserName() {
        return healthFacilityID + "_" + id;
    }

    public String getKeycloakRole() {
        return type.getKeyCloakRole();
    }
}
