package in.docq.health.facility.controller;

import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestKeyMaterial;
import in.docq.health.facility.service.HIPConsentService;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v3")
public class HIPConsentWebhookController {
    private final static Logger logger = LoggerFactory.getLogger(HIPConsentWebhookController.class);
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

    @PostMapping("/hip/health-information/request")
    public CompletionStage<ResponseEntity<Void>> healthInformationRequest(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIP-ID") String hipId,
            @RequestBody HealthInformationRequestBody request) {

        return hipConsentService.processHealthInformationRequest(requestId, timestamp, hipId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @Builder
    @Getter
    public static class HealthInformationRequestBody {
        private final String transactionId;
        private final HIRequest hiRequest;
    }

    @Builder
    @Getter
    public static class HIRequest {
        private final Consent consent;
        private final DateRange dateRange;
        private final String dataPushUrl;
        private final AbdmConsentManagement6RequestKeyMaterial keyMaterial;
    }

    @Builder
    @Getter
    public static class Consent {
        private final String id;
    }

    @Builder
    @Getter
    public static class DateRange {
        private final String from;
        private final String to;

        public Instant getFromAsInstant() {
            return from != null ? Instant.parse(from) : null;
        }

        public Instant getToAsInstant() {
            return to != null ? Instant.parse(to) : null;
        }
    }

    @Builder
    @Getter
    public static class DHPublicKey {
        private final Instant expiry;
        private final String parameters;
        private final String keyValue;
    }


    @Builder(toBuilder = true)
    @Getter
    public static class ConsentNotifyRequest {
        private final Notification notification;
    }

    @Builder(toBuilder = true)
    @Getter
    public static class Notification {
        private final String status;
        private final String consentId;
        private final in.docq.health.facility.model.Consent consent;
        private final String signature;
        private final boolean grantAcknowledgement;
    }


}