package in.docq.health.facility.model;

import in.docq.health.facility.dao.PrescriptionDAO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CareContext {
    private final String appointmentID;
    private final String healthFacilityId;
    private final String patientId;
    private final boolean isLinked;
    private final Long appointmentStartTime;

    public static List<PrescriptionDAO.PrescriptionIdentifier> toPrescriptionIdentifiers(List<CareContext> careContexts) {
        return careContexts.stream()
                .map(CareContext::toPrescriptionIdentifier)
                .toList();
    }

    public PrescriptionDAO.PrescriptionIdentifier toPrescriptionIdentifier() {
        String[] appointmentIdParts = this.appointmentID.split("_");
        return PrescriptionDAO.PrescriptionIdentifier.builder()
                .opdDate(LocalDate.parse(appointmentIdParts[0]))
                .opdId(appointmentIdParts[1])
                .appointmentId(Integer.valueOf(appointmentIdParts[2]))
                .build();
    }

    public static CareContext from(OPD opd, Appointment appointment) {
        return CareContext.builder()
                .appointmentID(appointment.getUniqueId())
                .healthFacilityId(opd.getHealthFacilityID())
                .patientId(appointment.getPatientId())
                .appointmentStartTime(appointment.getStartTime())
                .build();
    }
}
