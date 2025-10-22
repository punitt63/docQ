package in.docq.patient.controller;

import in.docq.abha.rest.client.model.ProfileShare3200Response;
import in.docq.patient.mapper.PatientShareMapper;
import in.docq.patient.service.PatientShareService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/patients/share")
public class PatientShareController {
    private final PatientShareService patientShareService;

    @Autowired
    public PatientShareController(PatientShareService patientShareService) {
        this.patientShareService = patientShareService;
    }

    @PostMapping("/hip")
    public CompletionStage<ResponseEntity<Void>> abdmPatientShareHip1(
            @RequestHeader("X-CM-ID") String xCmId,
            @RequestHeader("X-HIU-ID") String xHiuId,
            @RequestHeader("X-AUTH-TOKEN") String xAuthToken,
            @RequestBody AbdmPatientShareHip1RequestBody requestBody) {
        return patientShareService.abdmPatientShareHip1(xCmId, xHiuId, xAuthToken, PatientShareMapper.toAbdmPatientShareHip1Request(requestBody))
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/webhook/on-share")
    public CompletionStage<ResponseEntity<Void>> profileShare2(
            @RequestHeader("X-HIU-ID") String xHiuId,
            @RequestBody PatientShare2RequestBody requestBody) {
        return patientShareService.profileShare2(xHiuId, PatientShareMapper.toPatientShare2Request(requestBody))
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/getTokenDetails")
    public CompletionStage<ResponseEntity<ProfileShare3200Response>> profileShare3(
            @RequestHeader("X-CM-ID") String xCmId,
            @RequestHeader("X-AUTH-TOKEN") String xAuthToken,
            @RequestParam(value = "limit", required = false) String limit) {
        return patientShareService.profileShare3(xCmId, xAuthToken, limit)
                .thenApply(ResponseEntity::ok);
    }

    // DTOs

    @Builder
    @Getter
    public static class AbdmPatientShareHip1RequestBody {
        String intent; // PROFILE_SHARE, RECORD_SHARE, PAYMENT_SHARE
        MetaData metaData;
        Profile profile;
    }

    @Builder
    @Getter
    public static class MetaData {
        String hipId;
        String context;
        String hprId;
        Float latitude;
        Float longitude;
    }

    @Builder
    @Getter
    public static class Profile {
        Patient patient;
    }

    @Builder
    @Getter
    public static class Patient {
        String abhaNumber;
        String abhaAddress;
        String name;
        String gender;
        String dayOfBirth;
        String monthOfBirth;
        String yearOfBirth;
        Address address;
        String phoneNumber;
    }

    @Builder
    @Getter
    public static class Address {
        String line;
        String district;
        String state;
        String pincode;
    }

    @Builder
    @Getter
    public static class PatientShare2RequestBody {
        // Based on PatientShare2Request structure - this is a oneOf type
        // We'll support acknowledgement type for now (most common use case)
        
        // Acknowledgement fields
        String status;
        String abhaAddress;
        String profileName;
        String profileGender;
        String profileYearOfBirth;
        String profileMobile;
        String profileAddress;
        
        // Error fields (alternative to acknowledgement)
        String errorCode;
        String errorMessage;
        
        // Response fields (alternative to acknowledgement)
        String requestId;
    }
}
