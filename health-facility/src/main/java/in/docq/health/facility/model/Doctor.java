package in.docq.health.facility.model;

import in.docq.abha.rest.client.model.SearchFacilitiesData;
import in.docq.abha.rest.client.model.UserEntityResponseDTO;
import in.docq.health.facility.controller.HealthProfessionalController;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
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

    public static Doctor from(HealthProfessionalController.OnBoardDoctorRequestBody onBoardDoctorRequestBody, UserEntityResponseDTO userEntityResponseDTO, SearchFacilitiesData searchFacilitiesData) {
        return Doctor.builder()
                .id(userEntityResponseDTO.getHprId())
                .healthFacilityID(searchFacilitiesData.getFacilityId())
                .facilityName(searchFacilitiesData.getFacilityName())
                .facilityManagerID(onBoardDoctorRequestBody.getFacilityManagerID())
                .name(userEntityResponseDTO.getName())
                .facilityStateCode(Integer.parseInt(searchFacilitiesData.getStateLGDCode()))
                .facilityDistrictCode(Integer.parseInt(searchFacilitiesData.getDistrictLGDCode()))
                .facilityAddress(searchFacilitiesData.getAddress())
                .facilityPincode(searchFacilitiesData.getPincode())
                .facilityLatitude(Double.parseDouble(searchFacilitiesData.getLatitude()))
                .facilityLongitude(Double.parseDouble(searchFacilitiesData.getLongitude()))
                .build();
    }
}


