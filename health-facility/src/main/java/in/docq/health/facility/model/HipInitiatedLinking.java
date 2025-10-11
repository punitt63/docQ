package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class HipInitiatedLinking {
    private final String appointmentId;
    private final String healthFacilityId;
    private final String patientId;
    private final String linkRequestId;
    private final boolean isPatientNotified;
    private final String notifyRequestId;

    public static HipInitiatedLinking from(CareContext careContext) {
        return HipInitiatedLinking.builder()
                .appointmentId(careContext.getAppointmentID())
                .healthFacilityId(careContext.getHealthFacilityId())
                .patientId(careContext.getPatientId())
                .build();
    }
}