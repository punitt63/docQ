package in.docq.health.facility.controller;

import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.AbhaProfile;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.Patient;
import in.docq.health.facility.service.PatientService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
public class PatientController {
    private final PatientService patientService;
    private final SecretKey aesEncryptionKey;

    @Autowired
    public PatientController(PatientService patientService, SecretKey aesEncryptionKey) {
        this.aesEncryptionKey = aesEncryptionKey;
        this.patientService = patientService;
    }

    @GetMapping("/health-facilities/{health-facility-id}/patients")
    @Authorized(resource = "patient", scope = "read")
    public CompletionStage<ResponseEntity<List<Patient>>> searchPatients(@PathVariable("health-facility-id") String healthFacilityID,
                                                                         @RequestParam("mobile-no") String mobileNo) {
        return patientService.searchPatients(mobileNo)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/patients")
    @Authorized(resource = "patient", scope = "create")
    public CompletionStage<ResponseEntity<Void>> createPatient(@PathVariable("health-facility-id") String healthFacilityID,
                                                               @RequestBody CreatePatientRequestBody createPatientRequestBody) {
        return patientService.createPatient(Patient.fromRequestBody(createPatientRequestBody))
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/health-facilities/{health-facility-id}/patients")
    @Authorized(resource = "patient", scope = "create")
    public CompletionStage<ResponseEntity<Void>> replacePatient(@PathVariable("health-facility-id") String healthFacilityID,
                                                               @RequestBody ReplacePatientRequestBody replacePatientRequestBody) {
        return patientService.replacePatient(replacePatientRequestBody.getOldMobileNo(), replacePatientRequestBody.getOldName(), replacePatientRequestBody.getOldDob(), Patient.fromRequestBody(replacePatientRequestBody))
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/patients/abha-login/request-otp")
    @Authorized(resource = "patient", scope = "abha-login-request-otp")
    public CompletionStage<ResponseEntity<RequestOtpResponseBody>> requestAbhaProfileOtp(@RequestBody RequestAbhaLoginOtpRequestBody requestAbhaLoginOtpRequestBody) {
        return patientService.requestAbhaLoginOtp(requestAbhaLoginOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/patients/abha-login/verify-otp")
    @Authorized(resource = "patient", scope = "abha-login-verify-otp")
    public CompletionStage<ResponseEntity<VerifyOtpResponseBody>> verifyAbhaProfileOtp(@RequestBody VerifyOtpRequestBody verifyOtpRequestBody) {
        return patientService.verifyAbhaLoginOtp(verifyOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/patients/abha-signup/request-otp")
    @Authorized(resource = "patient", scope = "abha-signup-request-otp")
    public CompletionStage<ResponseEntity<RequestOtpResponseBody>> requestOtp(@RequestBody AbhaSignupRequestOtpBody requestBody) throws Exception {
        return patientService.abhaSignupRequestOtp(requestBody.getAadharNumber())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/patients/abha-signup/verify-otp")
    @Authorized(resource = "patient", scope = "abha-signup-verify-otp")
    public CompletionStage<ResponseEntity<EnrolByAadharResponseBody>> enrolByAadhaar(@RequestBody EnrolByAadharRequestBody enrolByAadharRequestBody) throws Exception {
        return patientService.enrolByAadhaar(enrolByAadharRequestBody.getAuthMethods(), enrolByAadharRequestBody.getTxnId(), enrolByAadharRequestBody.getOtpValue(), enrolByAadharRequestBody.getMobile())
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class CreatePatientRequestBody {
        private String name;
        private String mobileNo;
        private LocalDate dob;
        private String gender;
        private String abhaNo;
        private String abhaAddress;
    }

    @Builder
    @Getter
    public static class ReplacePatientRequestBody {
        private String oldMobileNo;
        private String newMobileNo;
        private String oldName;
        private String newName;
        private String abhaNo;
        private String abhaAddress;
        private LocalDate newDob;
        private LocalDate oldDob;
        private String gender;
    }

    @Builder
    @Getter
    public static class RequestAbhaLoginOtpRequestBody {
        List<String> scopes;
        String loginHint;
        String loginId;
        String otpSystem;
    }

    @Builder
    @Getter
    public static class RequestOtpResponseBody {
        String txnId;
        String message;
    }

    @Builder
    @Getter
    public static class VerifyOtpRequestBody {
        List<String> scopes;
        List<String> authMethods;
        String txnId;
        String otpValue;
    }

    @Builder
    @Getter
    public static class VerifyOtpResponseBody {
        String authResult;
        String message;
        Tokens tokens;
        List<AbhaAccount> accounts;
    }

    @Builder
    @Getter
    public static class AbhaSignupRequestOtpBody {
        String aadharNumber;
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
    public static class EnrolByAadharRequestBody {
        List<String> authMethods;
        String txnId;
        String otpValue;
        String mobile;
    }
}
