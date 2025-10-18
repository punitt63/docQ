package in.docq.patient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@JsonDeserialize(builder = OPD.OPDBuilder.class)
public class OPD {
    private final String id;
    private final String healthFacilityID;
    private final String healthProfessionalID;
    private final String name;
    private final int startHour;
    private final int endHour;
    private final int startMinute;
    private final int endMinute;
    private final LocalDate date;
    private final State state;
    private final int maxSlots;
    private final int minutesPerSlot;
    private final long activateTime;
    private final Long actualStartTime;
    private final Long actualEndTime;
    private final int appointmentsCount;

    public enum State {
        INACTIVE,
        ACTIVE,
        COMPLETED
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class OPDBuilder {
    }
}
