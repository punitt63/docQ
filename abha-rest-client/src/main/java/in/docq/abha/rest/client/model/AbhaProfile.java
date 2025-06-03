package in.docq.abha.rest.client.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class AbhaProfile {

    private String firstName;
    private String middleName;
    private String lastName;
    private String dob;
    private String gender;
    private String mobile;
    private List<String> phrAddress = new ArrayList<>();
    private String districtCode;
    private String stateCode;
    private String abhaType;
    private String stateName;
    private String districtName;
    private String abhaNumber;
    private String abhaStatus;

    public static AbhaProfile toAbhaProfile(AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfABHAProfile abhaProfileResponse) {
        return AbhaProfile.builder()
                .firstName(abhaProfileResponse.getFirstName())
                .middleName(abhaProfileResponse.getMiddleName())
                .lastName(abhaProfileResponse.getLastName())
                .dob(abhaProfileResponse.getDob())
                .gender(abhaProfileResponse.getGender())
                .mobile(abhaProfileResponse.getMobile())
                .phrAddress(abhaProfileResponse.getPhrAddress())
                .districtCode(abhaProfileResponse.getDistrictCode())
                .stateCode(abhaProfileResponse.getStateCode())
                .abhaType(abhaProfileResponse.getAbhaType())
                .stateName(abhaProfileResponse.getStateName())
                .districtName(abhaProfileResponse.getDistrictName())
                .abhaNumber(abhaProfileResponse.getAbHANumber())
                .abhaStatus(abhaProfileResponse.getAbhaStatus())
                .build();
    }
}
