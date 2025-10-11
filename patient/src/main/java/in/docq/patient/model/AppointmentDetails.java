package in.docq.patient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder(toBuilder = true)
@Getter
@JsonDeserialize(builder = AppointmentDetails.AppointmentDetailsBuilder.class)
public class AppointmentDetails {
    private final int id;
    private final LocalDate opdDate;
    private final String opdId;
    private final String patientId;
    private final Long startTime;
    private final Long endTime;
    private final int priority;
    private final State state;
    private final String content; // Prescription content (JSON)
    private final String healthFacilityName;
    private final String healthProfessionalName;

    public enum State {
        WAITING(false),
        IN_PROGRESS(false),
        COMPLETED(true),
        CANCELLED(true),
        NO_SHOW(false);

        @Getter
        private boolean isTerminal;

        State(boolean isTerminal) {
            this.isTerminal = isTerminal;
        }
    }
    
    // Computed fields for mobile app convenience
    public boolean hasPrescription() {
        return content != null && !content.trim().isEmpty();
    }
    
    public LocalDateTime getStartDateTime() {
        return startTime != null ? 
            LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(startTime), ZoneId.systemDefault()) : 
            null;
    }
    
    public LocalDateTime getEndDateTime() {
        return endTime != null ? 
            LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(endTime), ZoneId.systemDefault()) : 
            null;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AppointmentDetailsBuilder {
    }
}
