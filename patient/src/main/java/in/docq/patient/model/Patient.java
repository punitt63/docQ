package in.docq.patient.model;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder(toBuilder = true)
@Getter
public class Patient {
    private final String id;
    private final String abhaNo;
    private final String abhaAddress;
    private final String name;
    private final String mobileNo;
    private final LocalDate dob;
    private final String gender;
}