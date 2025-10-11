package in.docq.patient.controller;

import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.abha.rest.client.model.phr.PhrUser;
import in.docq.abha.rest.client.model.phr.DeLinkRequest200Response;
import in.docq.abha.rest.client.model.phr.GetProfile200Response;
import in.docq.abha.rest.client.model.phr.LinkRequest200Response;
import in.docq.abha.rest.client.model.phr.Logout200Response;
import in.docq.abha.rest.client.model.phr.RefreshToken200Response;
import in.docq.abha.rest.client.model.phr.OtpRequestMobile200Response;
import in.docq.abha.rest.client.model.phr.VerifyOtpUpdateEmail200Response;
import in.docq.abha.rest.client.model.phr.SwitchProfile200Response;
import in.docq.abha.rest.client.model.phr.UpdateProfile200Response;
import in.docq.abha.rest.client.model.phr.OtpVerifyMobile200ResponseTokens;
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

    @PostMapping("/abha-address")
    public CompletionStage<ResponseEntity<PatientLoginController.VerifyUserResponseBody>> verifyUser(@RequestBody PatientLoginController.VerifyUserRequestBody verifyUserRequestBody,
                                                                                                     @RequestHeader("T-token") String tToken) {
        return patientLoginService.verifyUser(verifyUserRequestBody, tToken)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/search-auth-methods")
    public CompletionStage<ResponseEntity<PatientLoginController.SearchAuthMethodsResponseBody>> searchAuthMethodsByAbhaAddress(@RequestParam("abhaAddress") String abhaAddress) {
        return patientLoginService.searchAuthMethodsByAbhaAddress(abhaAddress)
                .thenApply(ResponseEntity::ok);
    }

    // ===== PHR Profile endpoints =====

    @PostMapping("/profile/de-link")
    public CompletionStage<ResponseEntity<DeLinkRequest200Response>> deLinkAbhaProfile(@RequestHeader("X-token") String xToken, @RequestParam("transactionId") String transactionId) {
        return patientLoginService.deLinkAbhaProfile(xToken, transactionId).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/profile/phr-card")
    public CompletionStage<ResponseEntity<byte[]>> getPhrCard(@RequestHeader("X-token") String xToken) {
        return patientLoginService.getPhrCard(xToken).thenApply(imageBytes -> 
            ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .header("Content-Disposition", "inline; filename=\"phr-card.png\"")
                .body(imageBytes)
        );
    }

    @GetMapping("/profile")
    public CompletionStage<ResponseEntity<GetProfile200Response>> getProfile(@RequestHeader("X-token") String xToken) {
        return patientLoginService.getProfile(xToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/profile/qr")
    public CompletionStage<ResponseEntity<byte[]>> getQrCode(@RequestHeader("X-token") String xToken) {
        return patientLoginService.getQrCode(xToken).thenApply(imageBytes -> 
            ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .header("Content-Disposition", "inline; filename=\"qrcode.png\"")
                .body(imageBytes)
        );
    }

    @PostMapping("/profile/link")
    public CompletionStage<ResponseEntity<LinkRequest200Response>> linkAbhaProfile(@RequestHeader("X-token") String xToken, @RequestParam("transactionId") String transactionId) {
        return patientLoginService.linkAbhaProfile(xToken, transactionId).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/logout")
    public CompletionStage<ResponseEntity<Logout200Response>> logout(@RequestHeader("X-token") String xToken) {
        return patientLoginService.logout(xToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/refresh-token")
    public CompletionStage<ResponseEntity<RefreshToken200Response>> refreshProfileToken(@RequestHeader("R-token") String rToken) {
        return patientLoginService.refreshProfileToken(rToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/update-email/send-otp")
    public CompletionStage<ResponseEntity<OtpRequestMobile200Response>> sendUpdateEmailOtp(@RequestHeader("X-token") String xToken, @RequestParam("email") String email, @RequestParam("txnId") String txnId) {
        return patientLoginService.sendUpdateEmailOtp(xToken, email, txnId).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/update-email/verify-otp")
    public CompletionStage<ResponseEntity<VerifyOtpUpdateEmail200Response>> verifyUpdateEmailOtp(@RequestHeader("X-token") String xToken, @RequestParam("otp") String otp, @RequestParam("txnId") String txnId) {
        return patientLoginService.verifyUpdateEmailOtp(xToken, otp, txnId).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/switch")
    public CompletionStage<ResponseEntity<SwitchProfile200Response>> switchProfile(@RequestHeader("X-token") String xToken) {
        return patientLoginService.switchProfile(xToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/update")
    public CompletionStage<ResponseEntity<UpdateProfile200Response>> updateProfile(@RequestHeader("X-token") String xToken, @RequestParam("email") String email, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        return patientLoginService.updateProfile(xToken, email, firstName, lastName).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/profile/verify-user-switch")
    public CompletionStage<ResponseEntity<OtpVerifyMobile200ResponseTokens>> verifyUserForSwitch(@RequestHeader("T-token") String tToken, @RequestParam("txnId") String txnId, @RequestParam("otp") String otp) {
        return patientLoginService.verifyUserForSwitch(tToken, txnId, otp).thenApply(ResponseEntity::ok);
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
        List<PhrUser> users;
        String preferredAbhaAddress;
    }

    @Builder
    @Getter
    public static class VerifyUserRequestBody {
        String abhaAddress;
        String txnId;
    }

    @Builder
    @Getter
    public static class VerifyUserResponseBody {
        Tokens tokens;
    }

    @Builder
    @Getter
    public static class SearchAuthMethodsResponseBody {
        String abhaAddress;
        List<String> authMethods;
        String fullName;
        String healthIdNumber;
        String mobile;
        String status;
    }
}
