package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
public class CareContext {
    private final String appointmentID;
    private final String healthFacilityId;
    private final String patientId;
    private final String requestId;
    private final boolean isLinked;
    private final boolean isPatientNotified;

    public static CareContext from(OPD opd, Appointment appointment) {
        return CareContext.builder()
                .appointmentID(appointment.getUniqueId())
                .healthFacilityId(opd.getHealthFacilityID())
                .patientId(appointment.getPatientId())
                .build();
    }
}
