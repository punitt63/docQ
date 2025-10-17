package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FacilityManager {
    private String id;
    private String healthFacilityID;
}
