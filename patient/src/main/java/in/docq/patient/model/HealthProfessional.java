package in.docq.patient.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HealthProfessional {
    private final String id;
    private final String healthFacilityID;
    private final String healthFacilityName;
    private final HealthProfessionalType type;
    private final int stateCode;
    private final int districtCode;
    private final String speciality;
    private final String healthProfessionalName;
    private final String address;
    private final String pincode;
    private final Double latitude;
    private final Double longitude;
}


