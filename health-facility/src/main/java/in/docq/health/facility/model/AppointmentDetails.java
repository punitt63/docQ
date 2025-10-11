package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
public class AppointmentDetails {

    private final int id;
    private final LocalDate opdDate;
    private final String opdId;
    private final String patientId;
    private final Long startTime;
    private final Long endTime;
    private final int priority;
    private final Appointment.State state;
    private final String content;
    private final String healthFacilityName;
    private final String healthProfessionalName;
}
