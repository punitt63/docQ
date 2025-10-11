package in.docq.health.facility.service;

import com.google.gson.Gson;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.dao.ConsentDao;
import in.docq.health.facility.dao.HealthInformationRequestDao;
import in.docq.health.facility.exception.ErrorCodes;
import in.docq.health.facility.exception.HealthFacilityException;
import in.docq.health.facility.fidelius.encryption.EncryptionService;
import in.docq.health.facility.fidelius.keys.KeyMaterial;
import in.docq.health.facility.fidelius.keys.KeysService;
import in.docq.health.facility.model.CareContext;
import in.docq.health.facility.model.Consent;
import in.docq.health.facility.model.Prescription;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.completedFuture;


@Service
public class HIPConsentService {
    private final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(HIPConsentService.class);
    private final ConsentDao consentDao;
    private final AbhaRestClient abhaRestClient;
    private final HealthInformationRequestDao healthInformationRequestDao;
    private final KeysService keysService;
    private final EncryptionService encryptionService;
    private final CareContextService careContextService;
    private final PrescriptionService prescriptionService;

    @Autowired
    public HIPConsentService(ConsentDao consentDao,
                             AbhaRestClient abhaRestClient, HealthInformationRequestDao healthInformationRequestDao, KeysService keysService, EncryptionService encryptionService, CareContextService careContextService, PrescriptionService prescriptionService) {
        this.consentDao = consentDao;
        this.abhaRestClient = abhaRestClient;
        this.healthInformationRequestDao = healthInformationRequestDao;
        this.keysService = keysService;
        this.encryptionService = encryptionService;
        this.careContextService = careContextService;
        this.prescriptionService = prescriptionService;
    }

    public CompletionStage<Void> processConsentNotification(String requestId, String timestamp, String hipId,
                                                            HIPConsentWebhookController.ConsentNotifyRequest request) {
        if ("GRANTED".equalsIgnoreCase(request.getNotification().getStatus())) {
            return verifySignature(request)
                    .thenCompose(ignore -> {
                        if (request.getNotification().isGrantAcknowledgement()) {
                            return abhaRestClient.sendConsentGrantAcknowledgement(
                                    UUID.randomUUID().toString(),
                                    timestamp,
                                    new AbdmConsentManagement2Request()
                                            .acknowledgement(new AbdmConsentManagement2RequestAcknowledgement()
                                                    .consentId(request.getNotification().getConsentId())
                                                    .status("OK")
                                            ).response(new AbdmConsentManagement2RequestResponse().requestId(requestId)))
                                    .thenCompose(ignore2 -> consentDao.insert(request.getNotification().getConsentId(), request.getNotification().getConsent(), request.getNotification().getStatus()));
                        }
                        return consentDao.insert(request.getNotification().getConsentId(), request.getNotification().getConsent(), request.getNotification().getStatus());
                    });
        } else {
            return consentDao.deleteById(request.getNotification().getConsentId());
        }
    }

    private CompletionStage<Void> verifySignature(HIPConsentWebhookController.ConsentNotifyRequest request) {
        return completedFuture(null);
//        return abhaRestClient.getGatewayPublicCerts()
//                .thenApply(response -> {
//                    boolean verdict = response.verifySignature(gson.toJson(request.getNotification().getConsent()), request.getNotification().getSignature());
//                    if (!verdict) {
//                        throw new HealthFacilityException(ErrorCodes.BAD_REQUEST);
//                    }
//                    return null;
//                });
    }

    public CompletionStage<Void> processHealthInformationRequest(String requestId, String timestamp, String hipId,
                                                                 HIPConsentWebhookController.HealthInformationRequestBody request) {
        String consentId = request.getHiRequest().getConsent().getId();
        String transactionId = request.getTransactionId();
        return healthInformationRequestDao.getByTransactionId(transactionId)
                .thenCompose(existingRequestOpt -> {
                    if (existingRequestOpt.isPresent() && "TRANSFERRED".equalsIgnoreCase(existingRequestOpt.get().getStatus())) {
                        return completedFuture(null);
                    }
                    return processNewHealthInformationRequest(requestId, timestamp, request, consentId, transactionId);
                });
    }

    private CompletionStage<Void> processNewHealthInformationRequest(String requestId, String timestamp, HIPConsentWebhookController.HealthInformationRequestBody request, String consentId, String transactionId) {
        return consentDao.getById(consentId)
                .thenCompose(consentOpt -> {
                    if (consentOpt.isEmpty()) {
                        return sendErrorAcknowledgement(requestId, timestamp, "Consent not found");
                    }

                    Consent consent = consentOpt.get();
                    String consentStatus = consent.getStatus();

                    if (!"GRANTED".equalsIgnoreCase(consentStatus)) {
                        String errorMessage = "REVOKED".equalsIgnoreCase(consentStatus) ?
                                "Consent revoked" : "Consent expired";
                        return sendErrorAcknowledgement(requestId, timestamp, errorMessage);
                    }

                    // Check date range validation
                    if (!isDateRangeValid(request.getHiRequest().getDateRange(), consent.getPermission().getDateRange())) {
                        return sendErrorAcknowledgement(requestId, timestamp,
                                "Requested date range is not within consent date range");
                    }

                    return healthInformationRequestDao.upsert(transactionId, consentId, request, "ACKNOWLEDGED")
                            .thenCompose(ignore -> sendSuccessAcknowledgement(requestId, timestamp, transactionId))
                            .thenAccept(ignore -> startDataPush(timestamp, request, consent));
                });
    }

    private boolean isDateRangeValid(HIPConsentWebhookController.DateRange requestRange,
                                     Consent.DateRange consentRange) {
        return !requestRange.getFromAsInstant().isBefore(consentRange.getFromAsInstant()) &&
                !requestRange.getToAsInstant().isAfter(consentRange.getToAsInstant());
    }

    private CompletionStage<Void> sendSuccessAcknowledgement(String requestId, String timestamp, String transactionId) {
        return abhaRestClient.healthInfoRequestAcknowledgement(
                requestId,
                timestamp,
                new AbdmConsentManagement5Request(new AbdmConsentManagement5RequestAnyOf()
                        .hiRequest(new AbdmConsentManagement5RequestAnyOfHiRequest()
                                .transactionId(transactionId)
                                .sessionStatus("ACKNOWLEDGED")
                        )
                        .response( new AbdmConsentManagement5RequestAnyOfResponse().requestId(requestId))
                )
        );
    }

    private CompletionStage<Void> sendErrorAcknowledgement(String requestId, String timestamp, String errorMessage) {
        return abhaRestClient.healthInfoRequestAcknowledgement(
                requestId,
                timestamp,
                new AbdmConsentManagement5Request(new AbdmConsentManagement5RequestAnyOf1()
                        .error(new AbdmConsentManagement5RequestAnyOf1Error().code("INVALID_REQUEST").message(errorMessage))
                        .response( new AbdmConsentManagement5RequestAnyOfResponse().requestId(requestId)))
        );
    }

    private void startDataPush(String timestamp,
                                                HIPConsentWebhookController.HealthInformationRequestBody request,
                                                Consent consent) {
            careContextService.getLinkedCareContexts(
                    consent.getHip().getId(),
                            consent.getPatient().getId(),
                            request.getHiRequest().getDateRange().getFromAsInstant().toEpochMilli(),
                            request.getHiRequest().getDateRange().getToAsInstant().toEpochMilli())
                    .thenCompose(linkedCareContexts -> prescriptionService.getPrescriptions(CareContext.toPrescriptionIdentifiers(linkedCareContexts)))
                    .thenCompose(prescriptions -> transferHealthRecordData(
                            request.getHiRequest().getDataPushUrl(),
                            request.getTransactionId(),
                            request.getHiRequest().getKeyMaterial(),
                            prescriptions)
                            .thenCompose(ignore -> abhaRestClient.notifyDataTransfer(
                                    UUID.randomUUID().toString(),
                                    timestamp,
                                    new AbdmDataFlow8Request()
                                            .notification(new AbdmDataFlow8RequestNotification()
                                                    .transactionId(request.getTransactionId())
                                                    .consentId(consent.getConsentId())
                                                    .doneAt(Instant.now().toString())
                                                    .notifier(new AbdmDataFlow8RequestNotificationNotifier().id(consent.getHip().getId()).type(AbdmDataFlow8RequestNotificationNotifier.TypeEnum.HIP))
                                                    .statusNotification(new AbdmDataFlow8RequestNotificationStatusNotification().sessionStatus("TRANSFERRED").hipId(consent.getHip().getId()).statusResponses(Prescription.toStatusResponseEntries(prescriptions)))
                                    )
                    )))
                    .thenCompose(ignore -> healthInformationRequestDao.updateStatus(request.getTransactionId(), "TRANSFERRED"));
        }

    private CompletionStage<Void> transferHealthRecordData(String dataPushUrl, String transactionId, AbdmConsentManagement6RequestKeyMaterial receiverKeyMaterial, List<Prescription> prescriptions) {
        KeyMaterial senderKeyMaterial = keysService.generate();
        List<CompletionStage<Void>> futures = new ArrayList<>();
        int pageSize = 10;
        List<List<Prescription>> prescriptionsList =  IntStream.range(0, (prescriptions.size() + pageSize - 1) / pageSize)
                .mapToObj(i -> prescriptions.subList(i * pageSize,
                        Math.min(prescriptions.size(), (i + 1) * pageSize)))
                .toList();
        int pageCount = 1;
        for (List<Prescription> chunk : prescriptionsList) {
            futures.add(abhaRestClient.healthRecordDataTransfer(
                    dataPushUrl,
                    new AbdmConsentManagement6Request()
                            .pageNumber(pageCount)
                            .pageCount(pageSize)
                            .transactionId(transactionId)
                            .keyMaterial(receiverKeyMaterial)
                            .entries(Prescription.toDataTransferEntries(chunk, encryptionService, receiverKeyMaterial, senderKeyMaterial))));
            pageCount++;
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}