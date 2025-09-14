package in.docq.health.facility.controller;

import in.docq.health.facility.model.ConsentDetail;
import in.docq.health.facility.service.HIPConsentService;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v3")
public class HIPConsentWebhookController {

    private final HIPConsentService hipConsentService;

    @Autowired
    public HIPConsentWebhookController(HIPConsentService hipConsentService) {
        this.hipConsentService = hipConsentService;
    }

    @PostMapping("/consent/request/hip/notify")
    public CompletionStage<ResponseEntity<Void>> consentNotify(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIP-ID") String hipId,
            @RequestBody ConsentNotifyRequest request) {
        return hipConsentService.processConsentNotification(requestId, timestamp, hipId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }


    @Builder
    @Getter
    @Jacksonized
    public static class ConsentNotifyRequest {
        private final Notification notification;
    }

    @Builder
    @Getter
    @Jacksonized
    public static class Notification {
        private final String status;
        private final String consentId;
        private final ConsentDetail consentDetail;
        private final String signature;
        private final boolean grantAcknowledgement;
    }


}