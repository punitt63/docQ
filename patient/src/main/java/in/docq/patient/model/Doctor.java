package in.docq.patient.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@Jacksonized
public class Doctor {
    private final String id;
    private final String healthFacilityID;
    private final String facilityName;
    private final String facilityManagerID;
    private final String name;
    private final int facilityStateCode;
    private final int facilityDistrictCode;
    private final String facilityAddress;
    private final String facilityPincode;
    private final Double facilityLatitude;
    private final Double facilityLongitude;
    private final String speciality;
}


