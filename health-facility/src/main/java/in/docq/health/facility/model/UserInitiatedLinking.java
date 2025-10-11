package in.docq.health.facility.model;

import com.google.gson.JsonObject;
import in.docq.health.facility.controller.HipWebhookController;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserInitiatedLinking {
    private final String transactionId;
    private final String patientId;
    private final String status;
    private final String linkReferenceNumber;
    private final String otp;
    private final Long otpExpiryTime;
    private final HipWebhookController.InitCareContextLinkRequest initLinkRequest;
}
