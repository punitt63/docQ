package in.docq.patient.controller;

import in.docq.abha.rest.client.model.AbhaUser;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.patient.service.AbhaAddressVerificationService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/patients/abha-address/login")
public class AbhaAddressVerificationController {

    private final AbhaAddressVerificationService abhaAddressVerificationService;

    @Autowired
    public AbhaAddressVerificationController(AbhaAddressVerificationService abhaAddressVerificationService) {
        this.abhaAddressVerificationService = abhaAddressVerificationService;
    }

    @GetMapping("/search/{abha-address}")
    public CompletionStage<ResponseEntity<AbhaAddressSearchResponseBody>> searchAbhaAddress(@PathVariable("abha-address") String abhaAddress) {
        return abhaAddressVerificationService.searchAbhaAddress(abhaAddress)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/request/otp")
    public CompletionStage<ResponseEntity<RequestOtpResponseBody>> abhaAddressLoginRequestOtp(@RequestBody RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        return abhaAddressVerificationService.abhaAddressLoginRequestOtp(requestOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/verify/otp")
    public CompletionStage<ResponseEntity<VerifyOtpResponseBody>> abhaAddressLoginVerifyOtp(@RequestBody VerifyOtpRequestBody verifyOtpRequestBody) throws Exception {
        return abhaAddressVerificationService.abhaAddressLoginVerifyOtp(verifyOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/abha-address-profile")
    public CompletionStage<ResponseEntity<AbhaAddressProfileResponseBody>> getAbhaAddressProfile(@RequestHeader("X-Token") String xToken) {
        return abhaAddressVerificationService.getAbhaAddressProfile(xToken)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/abha-card")
    public CompletionStage<ResponseEntity<String>> getAbhaCard(@RequestHeader("X-Token") String xToken) {
        return abhaAddressVerificationService.getAbhaCard(xToken)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class AbhaAddressSearchResponseBody {
        String healthIdNumber;
        String abhaAddress;
        List<String> authMethods;
        List<String> blockedAuthMethods;
        String status;
        String message;
        String fullName;
        String mobile;
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
        String message;
        String authResult;
        List<AbhaUser> users;
        Tokens tokens;
    }

    @Builder
    @Getter
    public static class AbhaAddressProfileResponseBody {
        String abhaAddress;
        String abhaNumber;
        String profilePhoto;
        List<String> authMethods;
        String status;
        String fullName;
        String mobile;
    }

}
