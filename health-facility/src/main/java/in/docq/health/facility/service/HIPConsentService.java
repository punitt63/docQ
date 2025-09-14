package in.docq.health.facility.service;

import com.google.gson.Gson;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.AbdmConsentManagement2Request;
import in.docq.abha.rest.client.model.AbdmConsentManagement2RequestAcknowledgement;
import in.docq.abha.rest.client.model.AbdmConsentManagement2RequestResponse;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.dao.ConsentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Service
public class HIPConsentService {
    private final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(HIPConsentService.class);
    private final ConsentDao consentDao;
    private final AbhaRestClient abhaRestClient;

    @Autowired
    public HIPConsentService(ConsentDao consentDao,
                          AbhaRestClient abhaRestClient) {
        this.consentDao = consentDao;
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<Void> processConsentNotification(String requestId, String timestamp, String hipId,
                                                            HIPConsentWebhookController.ConsentNotifyRequest request) {
        if ("GRANTED".equalsIgnoreCase(request.getNotification().getStatus())) {
            return verifySignature(request)
                    .thenCompose(ignore -> consentDao.insert(request.getNotification().getConsentId(), request.getNotification().getConsentDetail(), request.getNotification().getStatus()))
                    .thenCompose(ignore -> {
                        if (request.getNotification().isGrantAcknowledgement()) {
                            return abhaRestClient.sendConsentGrantAcknowledgement(
                                    UUID.randomUUID().toString(),
                                    timestamp,
                                    new AbdmConsentManagement2Request()
                                            .acknowledgement(new AbdmConsentManagement2RequestAcknowledgement()
                                                    .consentId(request.getNotification().getConsentId())
                                                    .status("OK")
                                            ).response(new AbdmConsentManagement2RequestResponse().requestId(requestId)));
                        } else {
                            return consentDao.updateStatus(request.getNotification().getConsentId(), request.getNotification().getStatus());
                        }
                    });
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    private CompletionStage<Void> verifySignature(HIPConsentWebhookController.ConsentNotifyRequest request) {
        return abhaRestClient.getGatewayPublicCerts()
                .thenApply(response -> {
                    boolean verdict = response.verifySignature(gson.toJson(request.getNotification()), request.getNotification().getSignature());
                    if (!verdict) {
                        throw new RuntimeException("Signature verification failed");
                    }
                    return null;
                });
    }
}