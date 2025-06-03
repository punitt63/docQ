package in.docq.patient.controller;

import in.docq.patient.service.AbhaDeletionService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/patients/abha/delete")
public class AbhaDeletionController {

    private final AbhaDeletionService abhaDeletionService;

    @Autowired
    public AbhaDeletionController(AbhaDeletionService abhaDeletionService) {
        this.abhaDeletionService = abhaDeletionService;
    }

    @PostMapping("/request/otp")
    public CompletionStage<ResponseEntity<AbhaDeletionController.RequestOtpResponseBody>> requestOtp(@RequestHeader("X-token") String xToken, @RequestBody AbhaDeletionController.RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        return abhaDeletionService.requestOtp(xToken, requestOtpRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/verify/otp")
    public CompletionStage<ResponseEntity<AbhaDeletionController.VerifyOtpResponseBody>> verifyOtp(@RequestHeader("X-Token") String xToken, @RequestBody AbhaDeletionController.VerifyOtpRequestBody verifyOtpRequestBody) throws Exception {
        return abhaDeletionService.verifyOtp(xToken, verifyOtpRequestBody)
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
        List<String> reasons;
    }

    @Builder
    @Getter
    public static class VerifyOtpResponseBody {
        String txnId;
        String authResult;
        String message;
        List<String> abhaNumbers;
    }

}
