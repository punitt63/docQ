package in.docq.patient.controller;

import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.patient.service.PatientLoginService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/patients/login")
public class PatientLoginController {

    private final PatientLoginService patientLoginService;

    @Autowired
    public PatientLoginController(PatientLoginService patientLoginService) {
        this.patientLoginService = patientLoginService;
    }

    @PostMapping("/request")
    public CompletionStage<ResponseEntity<PatientLoginController.RequestOtpResponseBody>> requestOtp(@RequestBody PatientLoginController.RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        return patientLoginService.requestOtp(requestOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/verify")
    public CompletionStage<ResponseEntity<PatientLoginController.VerifyOtpResponseBody>> verifyOtp(@RequestBody PatientLoginController.VerifyOtpRequestBody verifyOtpRequestBody) throws Exception {
        return patientLoginService.verifyOtp(verifyOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/abha-number")
    public CompletionStage<ResponseEntity<PatientLoginController.VerifyUserResponseBody>> verifyUser(@RequestBody PatientLoginController.VerifyUserRequestBody verifyUserRequestBody,
                                                                                                     @RequestHeader("T-token") String tToken) {
        return patientLoginService.verifyUser(verifyUserRequestBody, tToken)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class RequestOtpRequestBody {
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
    public static class VerifyUserRequestBody {
        String abhaNumber;
        String txnId;
    }

    @Builder
    @Getter
    public static class VerifyUserResponseBody {
        Tokens tokens;
    }
}
