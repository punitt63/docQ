package in.docq.patient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
@JsonDeserialize(builder = Appointment.AppointmentBuilder.class)
public class Appointment {
    private final int id;
    private final LocalDate opdDate;
    private final String opdId;
    private final String patientId;
    private final State state;
    private final String cancellationReason;
    private final Long startTime;
    private final Long endTime;
    private final int priority;

    public enum State {
        WAITING, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AppointmentBuilder {
    }
}
