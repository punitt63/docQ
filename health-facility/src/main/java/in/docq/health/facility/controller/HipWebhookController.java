package in.docq.health.facility.controller;

import in.docq.health.facility.service.CareContextService;
import in.docq.health.facility.service.HIPLinkingTokenService;
import in.docq.health.facility.service.UserInitiatedLinkingService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v3")
public class HipWebhookController {
    private final UserInitiatedLinkingService userInitiatedLinkingService;
    private final CareContextService careContextService;

    @Autowired
    public HipWebhookController(UserInitiatedLinkingService userInitiatedLinkingService, CareContextService careContextService) {
        this.userInitiatedLinkingService = userInitiatedLinkingService;
        this.careContextService = careContextService;
    }

    @PostMapping("/hip/token/on-generate-token")
    public CompletionStage<ResponseEntity<Void>> onGenerateToken(@RequestHeader("X-HIP-ID") String healthFacilityId,
                                                                 @RequestBody OnGenerateTokenRequest request) {
        return careContextService.onGenerateLinkingToken(healthFacilityId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/link/on_carecontext")
    public CompletionStage<ResponseEntity<Void>> onLinkCareContext(@RequestBody OnLinkCareContextRequest request) {
        return careContextService.onLinkCareContext(request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/patients/sms/on-notify")
    public CompletionStage<ResponseEntity<Void>> onSmsNotify(@RequestBody OnSmsNotifyRequest request) {
        return careContextService.onSmsNotify(request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/hip/patient/care-context/discover")
    public CompletionStage<ResponseEntity<Void>> discoverCareContext(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIP-ID") String healthFacilityId,
            @RequestBody UserInitiatedLinkingRequest request) {
        return userInitiatedLinkingService.discoverCareContext(requestId, healthFacilityId, request)
                .thenApply(careContexts -> ResponseEntity.accepted().build());
    }

    @PostMapping("/hip/link/care-context/init")
    public CompletionStage<ResponseEntity<Void>> initCareContextLink(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIP-ID") String healthFacilityId,
            @RequestBody InitCareContextLinkRequest request) {
        return userInitiatedLinkingService.initCareContextLink(requestId, healthFacilityId, request)
                .thenApply(ignore -> ResponseEntity.accepted().build());
    }

    @PostMapping("/hip/link/care-context/confirm")
    public CompletionStage<ResponseEntity<Void>> confirmCareContextLink(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIP-ID") String healthFacilityId,
            @RequestBody ConfirmCareContextLinkRequest request) {
        return userInitiatedLinkingService.confirmCareContextLink(requestId, timestamp, healthFacilityId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @Builder
    @Getter
    public static class ConfirmCareContextLinkRequest {
        private final Confirmation confirmation;

        @Builder
        @Getter
        public static class Confirmation {
            private final String token;
            private final String linkRefNumber;
        }
    }

    @Builder
    @Getter
    public static class OnGenerateTokenRequest {
        private final String abhaAddress;
        private final String linkToken;
        private final Response response;

        @Builder
        @Getter
        public static class Response {
            private final String requestId;
        }
    }

    @Builder
    @Getter
    public static class OnLinkCareContextRequest {
        private final String abhaAddress;
        private final String status;
        private final Response response;
        private final Error error;

        @Builder
        @Getter
        public static class Response {
            private final String requestId;
        }

        @Builder
        @Getter
        public static class Error {
            private final String code;
            private final String message;
        }
    }

    @Builder
    @Getter
    public static class OnSmsNotifyRequest {
        private final Acknowledgement acknowledgement;
        private final Error error;
        private final Response response;

        @Builder
        @Getter
        public static class Acknowledgement {
            private final String status;
        }

        @Builder
        @Getter
        public static class Error {
            private final String code;
            private final String message;
        }

        @Builder
        @Getter
        public static class Response {
            private final String requestId;
        }
    }

    @Builder
    @Getter
    public static class UserInitiatedLinkingRequest {
        private final String transactionId;
        private final Patient patient;

        @Builder
        @Getter
        public static class Patient {
            private final String id;
            private final List<Identifier> verifiedIdentifiers;
            private final List<Identifier> unverifiedIdentifiers;
            private final String name;
            private final String gender;
            private final Integer yearOfBirth;
        }

        @Builder
        @Getter
        public static class Identifier {
            private final Type type;
            private final String value;
        }

        public enum Type {
            MR("MR"),
            MOBILE("MOBILE"),
            ABHA_NUMBER("ABHA_NUMBER"),
            ABHA_ADDRESS("ABHA_ADDRESS"),
            EMAIL("EMAIL");

            private String value;

            Type(String value) {
                this.value = value;
            }

            public String getValue() {
                return this.value;
            }
        }
    }

    @Builder
    @Getter
    public static class InitCareContextLinkRequest {
        private final String transactionId;
        private final String abhaAddress;
        private final List<PatientInfo> patient;
        private final Error error;

        @Builder
        @Getter
        public static class PatientInfo {
            private final String referenceNumber;
            private final String display;
            private final List<CareContextInfo> careContexts;
            private final String hiType;
            private final Integer count;
        }

        @Builder
        @Getter
        public static class CareContextInfo {
            private final String referenceNumber;
            private final String display;
        }

        @Builder
        @Getter
        public static class Error {
            private final String code;
            private final String message;
        }
    }
}