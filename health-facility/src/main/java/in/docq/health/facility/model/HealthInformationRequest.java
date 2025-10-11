package in.docq.health.facility.model;

import in.docq.health.facility.controller.HIPConsentWebhookController;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class HealthInformationRequest {
    private final String transactionId;
    private final String consentId;
    private final HIPConsentWebhookController.HealthInformationRequestBody request;
    private final String status;
}