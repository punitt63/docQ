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
@RequestMapping("/api/v3/hip/token")
public class HipGenerateTokenWebhookController {

    private final HIPLinkingTokenService hipLinkingTokenService;
    private final CareContextService careContextService;

    @Autowired
    public HipGenerateTokenWebhookController(HIPLinkingTokenService hipLinkingTokenService, CareContextService careContextService) {
        this.hipLinkingTokenService = hipLinkingTokenService;
        this.careContextService = careContextService;
    }

    @PostMapping("/on-generate-token")
    public CompletionStage<ResponseEntity<Void>> onGenerateToken(@RequestBody OnGenerateTokenRequest request) {
        return hipLinkingTokenService.updateToken(request.getAbhaAddress(), request.getResponse().getRequestId(), request.getLinkToken())
                .thenCompose(ignore -> careContextService.linkCareContext(request))
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