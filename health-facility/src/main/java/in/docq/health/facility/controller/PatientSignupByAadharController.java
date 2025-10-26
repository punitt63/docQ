package in.docq.health.facility.controller;

import in.docq.abha.rest.client.model.AbhaProfile;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.service.PatientSignupByAadharService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}/patients/sign-up")
public class PatientSignupByAadharController {
    private final PatientSignupByAadharService patientSignupByAadharService;

    @Autowired
    public PatientSignupByAadharController(PatientSignupByAadharService patientSignupByAadharService) {
        this.patientSignupByAadharService = patientSignupByAadharService;
    }

    @PostMapping("/aadhar")
    @Authorized(resource = "patient", scope = "abha-signup-request-otp")
    public CompletionStage<ResponseEntity<RequestOtpResponseBody>> requestOtp(@RequestBody AbhaSignupRequestOtpBody requestBody) throws Exception {
        return patientSignupByAadharService.abhaSignupRequestOtp(requestBody.getAadharNumber())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/aadhar/verify")
    @Authorized(resource = "patient", scope = "abha-signup-verify-otp")
    public CompletionStage<ResponseEntity<EnrolByAadharResponseBody>> enrolByAadhaar(@RequestBody EnrolByAadharRequestBody enrolByAadharRequestBody) throws Exception {
        return patientSignupByAadharService.enrolByAadhaar(enrolByAadharRequestBody.getAuthMethods(), enrolByAadharRequestBody.getTxnId(), enrolByAadharRequestBody.getOtpValue(), enrolByAadharRequestBody.getMobile())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/abha-address-suggestions")
    @Authorized(resource = "patient", scope = "abha-signup-request-otp")
    public CompletionStage<ResponseEntity<AbhaAddressSuggestionsResponseBody>> getAbhaAddressSuggestions(@RequestHeader("txnId") String txnId) {
        return patientSignupByAadharService.getAbhaAddressSuggestions(txnId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/abha-address")
    @Authorized(resource = "patient", scope = "abha-signup-verify-otp")
    public CompletionStage<ResponseEntity<EnrolAbhaAddressResponseBody>> enrolAbhaAddress(@RequestBody EnrolAbhaAddressRequestBody enrolAbhaAddressRequestBody) {
        return patientSignupByAadharService.enrolAbhaAddress(enrolAbhaAddressRequestBody.getTxnId(), enrolAbhaAddressRequestBody.getAbhaAddress(), enrolAbhaAddressRequestBody.getPreferred())
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class AbhaSignupRequestOtpBody {
        String aadharNumber;
    }

    @Builder
    @Getter
    public static class RequestOtpResponseBody {
        String txnId;
        String message;
    }


    @Builder
    @Getter
    public static class AbhaAddressSuggestionsResponseBody {
        String txnId;
        List<String> abhaAddressSuggestions;
    }

    @Builder
    @Getter
    public static class EnrolByAadharRequestBody {
        List<String> authMethods;
        String txnId;
        String otpValue;
        String mobile;
    }

    @Builder
    @Getter
    public static class EnrolByAadharResponseBody {
        String patientId;
        String txnId;
        String message;
        Tokens tokens;
        AbhaProfile abhaProfile;
        boolean isNew;
    }

    @Builder
    @Getter
    public static class EnrolAbhaAddressRequestBody {
        String txnId;
        String abhaAddress;
        String preferred;
    }

    @Builder
    @Getter
    public static class EnrolAbhaAddressResponseBody {
        String txnId;
        String abhaAddress;
        String preferredAbhaAddress;
    }
}


