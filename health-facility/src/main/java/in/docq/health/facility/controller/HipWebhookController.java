package in.docq.health.facility.controller;

import in.docq.health.facility.service.CareContextService;
import in.docq.health.facility.service.HIPLinkingTokenService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v3")
public class HipWebhookController {
    private final CareContextService careContextService;

    @Autowired
    public HipWebhookController(CareContextService careContextService) {
        this.careContextService = careContextService;
    }

    @PostMapping("/hip/token/on-generate-token")
    public CompletionStage<ResponseEntity<Void>> onGenerateToken(@RequestHeader("X-HIP-ID") String healthFacilityId,
                                                                 @RequestBody OnGenerateTokenRequest request) {
        return careContextService.onGenerateLinkingToken(healthFacilityId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
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
}