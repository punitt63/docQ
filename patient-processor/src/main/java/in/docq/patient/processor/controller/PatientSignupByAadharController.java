package in.docq.patient.processor.controller;

import in.docq.patient.processor.model.AbhaProfile;
import in.docq.patient.processor.model.Tokens;
import in.docq.patient.processor.service.PatientSignupByAadharService;
import in.docq.patient.processor.utils.RSAEncrypter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/signup")
public class PatientSignupByAadharController {
    private final PatientSignupByAadharService patientSignupByAadharService;

    @Autowired
    public PatientSignupByAadharController(PatientSignupByAadharService patientSignupByAadharService) {
        this.patientSignupByAadharService = patientSignupByAadharService;
    }

    @PostMapping("/request/otp")
    public CompletionStage<ResponseEntity<RequestOtpResponseBody>> requestOtp(@RequestBody RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        String encryptedAadharNumber = RSAEncrypter.encrypt(requestOtpRequestBody.getAadharNumber());
        return patientSignupByAadharService.requestOtp(encryptedAadharNumber)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/enrol/byAadhaar")
    public CompletionStage<ResponseEntity<EnrolByAadharResponseBody>> enrolByAadhaar(@RequestBody EnrolByAadharRequestBody enrolByAadharRequestBody) throws Exception {
        String encryptedOtpValue = RSAEncrypter.encrypt(enrolByAadharRequestBody.getOtpValue());
        return patientSignupByAadharService.enrolByAadhaar(enrolByAadharRequestBody.getAuthMethods(), enrolByAadharRequestBody.getTxnId(), encryptedOtpValue, enrolByAadharRequestBody.getMobile())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/abha-address-suggestions")
    public CompletionStage<ResponseEntity<AbhaAddressSuggestionsResponseBody>> getAbhaAddressSuggestions(@RequestHeader("Transaction-Id") String txnId) {
        return patientSignupByAadharService.getAbhaAddressSuggestions(txnId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/enrol/abhaAddress")
    public CompletionStage<ResponseEntity<EnrolAbhaAddressResponseBody>> enrolAbhaAddress(@RequestBody EnrolAbhaAddressRequestBody enrolAbhaAddressRequestBody) {
        return patientSignupByAadharService.enrolAbhaAddress(enrolAbhaAddressRequestBody.getTxnId(), enrolAbhaAddressRequestBody.getAbhaAddress(), enrolAbhaAddressRequestBody.getPreferred())
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class RequestOtpRequestBody {
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
    public static class EnrolByAadharRequestBody {
        List<String> authMethods;
        String txnId;
        String otpValue;
        String mobile;
    }

    @Builder
    @Getter
    public static class EnrolByAadharResponseBody {
        String txnId;
        String message;
        Tokens tokens;
        AbhaProfile abhaProfile;
        boolean isNew;
    }

    @Builder
    @Getter
    public static class AbhaAddressSuggestionsResponseBody {
        String txnId;
        List<String> abhaAddressSuggestions;
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
