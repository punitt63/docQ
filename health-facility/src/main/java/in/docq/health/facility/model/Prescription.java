package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class Prescription {
    private final String opdID;
    private final Integer appointmentID;
    private final LocalDate date;
    private final String content;
}
