package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Doctor {
    private final String id;
    private final String healthFacilityID;
    private final String facilityManagerID;
    private final String specialization;
    private final String name;
}
