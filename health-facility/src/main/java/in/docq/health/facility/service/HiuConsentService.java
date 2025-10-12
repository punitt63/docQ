package in.docq.health.facility.service;

import com.google.gson.JsonObject;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HiuConsentWebhookController;

import in.docq.health.facility.dao.ConsentRequestDao;
import in.docq.health.facility.fidelius.keys.KeyMaterial;
import in.docq.health.facility.fidelius.keys.KeysService;
import in.docq.health.facility.model.ConsentHealthRecord;
import in.docq.health.facility.model.ConsentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import in.docq.health.facility.fidelius.decryption.DecryptionService;
import in.docq.abha.rest.client.model.AbdmDataFlow8Request;
import in.docq.abha.rest.client.model.AbdmDataFlow8RequestNotification;
import in.docq.abha.rest.client.model.AbdmDataFlow8RequestNotificationNotifier;
import in.docq.abha.rest.client.model.AbdmDataFlow8RequestNotificationStatusNotification;
import in.docq.health.facility.dao.ConsentHealthRecordDao;

import static com.google.common.base.Preconditions.checkState;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
@Slf4j
public class HiuConsentService {

    private final AbhaRestClient abhaRestClient;
    private final ConsentRequestDao consentRequestDao;
    private final ConsentHealthRecordDao consentHealthRecordDao;
    private final KeysService keysService;
    private final String dataPushBaseUrl;
    private final DecryptionService decryptionService;
    public final static String DATA_PUSH_ENDPOINT = "/hiu/data-push-webhook";

    @Autowired
    public HiuConsentService(AbhaRestClient abhaRestClient,
                             ConsentRequestDao consentRequestDao,
                             ConsentHealthRecordDao consentHealthRecordDao,
                             KeysService keysService,
                             DecryptionService decryptionService,
                             @Value("${app.data-push.base-url}") String dataPushBaseUrl) {
        this.abhaRestClient = abhaRestClient;
        this.consentRequestDao = consentRequestDao;
        this.consentHealthRecordDao = consentHealthRecordDao;
        this.keysService = keysService;
        this.dataPushBaseUrl = dataPushBaseUrl;
        this.decryptionService = decryptionService;
    }

    public CompletionStage<Void> processConsentRequest(
            String healthFacilityId,
            String healthFacilityProfessionalId,
            AbdmConsentManagement1Request consentRequest) {

        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

        return abhaRestClient.sendConsentRequest(requestId, timestamp, consentRequest)
                .thenCompose(ignore -> {
                    ConsentRequest consentRequestEntity = ConsentRequest.builder()
                            .requestId(requestId)
                            .hiuId(healthFacilityId)
                            .request(consentRequest)
                            .requesterId(healthFacilityProfessionalId)
                            .build();
                    return consentRequestDao.insert(consentRequestEntity);
                });
    }

    public CompletionStage<Void> processConsentRequestOnInit(
            String requestId,
            String timestamp,
            String hiuId,
            HiuConsentWebhookController.ConsentRequestOnInitBody request) {

        if (request.getError() != null) {
            log.error("error found in on-init callback");
            return completedFuture(null);
        }

        String responseRequestId = request.getResponse() != null ? request.getResponse().getRequestId() : null;
        if (responseRequestId == null) {
            log.error("No response requestId found in on-init callback");
            return completedFuture(null);
        }

        return consentRequestDao.getByRequestId(responseRequestId)
                .thenCompose(consentRequestOpt -> {
                    if (consentRequestOpt.isEmpty()) {
                        log.error("Consent request not found for requestId: {}", responseRequestId);
                        throw new RuntimeException("Consent request not found: " + responseRequestId);
                    }

                    // 2. Update the consent request id in entry with consent request id mentioned in the body
                    String consentRequestId = request.getConsentRequest() != null ?
                            request.getConsentRequest().getId() : null;

                    if (consentRequestId == null) {
                        log.error("No consent request id found in on-init callback");
                        return completedFuture(null);
                    }

                    return consentRequestDao.updateConsentRequestId(responseRequestId, consentRequestId)
                            .thenCompose(ignore -> consentRequestDao.updateStatus(responseRequestId, ConsentRequest.Status.REQUESTED));
                });
    }

    public CompletionStage<Void> processConsentRequestNotify(
            String requestId,
            String timestamp,
            String hiuId,
            HiuConsentWebhookController.ConsentRequestNotifyBody request) {

        String consentRequestId = request.getNotification() != null ?
                request.getNotification().getConsentRequestId() : null;

        if (consentRequestId == null) {
            log.error("No consent request id found in notify callback");
            return completedFuture(null);
        }

        // 1. Get consent request with consentRequestId mentioned in body
        return consentRequestDao.getByConsentRequestId(consentRequestId)
                .thenCompose(consentRequestOpt -> {
                    if (consentRequestOpt.isEmpty()) {
                        log.error("Consent request not found for consentRequestId: {}", consentRequestId);
                        throw new RuntimeException("Consent request not found: " + consentRequestId);
                    }

                    List<HiuConsentWebhookController.ConsentArtefactRef> consentArtefacts =
                            request.getNotification() != null ? request.getNotification().getConsentArtefacts() : null;

                    if (consentArtefacts == null || consentArtefacts.isEmpty()) {
                        log.warn("No consent artifacts found in notify callback");
                        return completedFuture(null);
                    }

                    // 2. For each consent artifact, process in parallel
                    List<CompletionStage<Void>> artifactProcessingTasks = consentArtefacts.stream()
                            .map(artifact -> processConsentArtifact(consentRequestId, artifact.getId(), hiuId))
                            .collect(Collectors.toList());

                    // Wait for all parallel tasks to complete
                    return CompletableFuture.allOf(artifactProcessingTasks.toArray(new CompletableFuture[0]))
                            .thenApply(ignore -> null);
                });
    }

    private CompletionStage<Void> processConsentArtifact(String consentRequestId, String consentArtifactId, String hiuId) {

        String artifactRequestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

        // a.) Call fetchConsentArtifact method in abha rest client
        AbdmConsentManagement5Request1 fetchRequest = new AbdmConsentManagement5Request1()
                .consentId(consentArtifactId);

        return abhaRestClient.fetchConsentArtifact(artifactRequestId, timestamp, hiuId, fetchRequest)
                .thenCompose(ignore -> {
                    // b.) Update status as FETCHED in consent health record table
                    ConsentHealthRecord consentHealthRecord = ConsentHealthRecord.builder()
                            .consentId(consentArtifactId)
                            .consentRequestId(consentRequestId)
                            .status(ConsentHealthRecord.Status.AWAITING_FETCH)
                            .hiuId(hiuId)
                            .build();

                    return consentHealthRecordDao.insert(consentHealthRecord);
                })
                .exceptionally(ex -> {
                    log.error("Failed to process consent artifact: {}", consentArtifactId, ex);
                    throw new RuntimeException("Failed to process consent artifact: " + consentArtifactId, ex);
                });
    }

    public CompletionStage<Void> processConsentOnFetch(
            String requestId,
            String timestamp,
            String hiuId,
            HiuConsentWebhookController.ConsentOnFetchBody request) {

        log.info("Processing consent on-fetch for requestId: {}", requestId);

        if (request.getError() != null) {
            log.error("Error found in on-fetch callback: {}", request.getError().getMessage());
            return completedFuture(null);
        }

        String consentId = request.getConsent() != null && request.getConsent().getConsentDetail() != null ?
                request.getConsent().getConsentDetail().getConsentId() : null;

        if (consentId == null) {
            log.error("No consent ID found in on-fetch callback");
            return completedFuture(null);
        }

        // 1. Get consent id mentioned in body from consent health record table
        return consentHealthRecordDao.getByConsentId(consentId)
                .thenCompose(consentHealthRecordOpt -> {
                    if (consentHealthRecordOpt.isEmpty()) {
                        log.error("Consent health record not found for consentId: {}", consentId);
                        throw new RuntimeException("Consent health record not found: " + consentId);
                    }

                    // 2. Send health info request
                    return sendHealthInfoRequest(consentId, hiuId, request.getConsent().getConsentDetail());
                });
    }

    private CompletionStage<Void> sendHealthInfoRequest(String consentId, String hiuId,
                                                        HiuConsentWebhookController.ConsentDetail consentDetail) {

        log.info("Sending health info request for consentId: {}", consentId);

        // a.) Generate a key pair from KeysService
        KeyMaterial keyMaterial = keysService.generate();

        // b.) Get date range from permission object
        HiuConsentWebhookController.DateRangeInfo dateRange = consentDetail.getPermission().getDateRange();

        // c.) Create AbdmDataFlow7Request
        String healthInfoRequestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

        // d.) Get data push URL from environment variables
        String dataPushUrl = dataPushBaseUrl + DATA_PUSH_ENDPOINT;

        AbdmDataFlow7Request dataFlow7Request = createHealthInfoRequest(
                consentId,
                healthInfoRequestId,
                dataPushUrl,
                dateRange,
                keyMaterial,
                consentDetail
        );

        // e.) Call sendHealthInfoRequest method in abha rest client
        return abhaRestClient.sendHealthInfoRequest(healthInfoRequestId, timestamp, hiuId, dataFlow7Request)
                .thenCompose(ignore -> {
                    // 3.) Update key material in consent health record table
                    return consentHealthRecordDao.updateConsentHealthRecord(consentId, keyMaterial, healthInfoRequestId, consentDetail.getHip().getId());
                })
                .exceptionally(ex -> {
                    log.error("Failed to send health info request for consentId: {}", consentId, ex);
                    throw new RuntimeException("Failed to send health info request: " + consentId, ex);
                });
    }

    private AbdmDataFlow7Request createHealthInfoRequest(String consentId, String requestId, String dataPushUrl,
                                                         HiuConsentWebhookController.DateRangeInfo dateRange, KeyMaterial keyMaterial,
                                                         HiuConsentWebhookController.ConsentDetail consentDetail) {

        // Create HI request with consent details and key material
        AbdmDataFlow7RequestHiRequest hiRequest = new AbdmDataFlow7RequestHiRequest()
                .consent(new AbdmDataFlow7RequestHiRequestConsent()
                        .id(consentId))
                .dateRange(new AbdmDataFlow7RequestHiRequestDateRange()
                        .from(dateRange.getFrom())
                        .to(dateRange.getTo()))
                .dataPushUrl(dataPushUrl)
                .keyMaterial(new AbdmDataFlow7RequestHiRequestKeyMaterial()
                        .cryptoAlg("ECDH")
                        .curve("Curve25519")
                        .nonce(keyMaterial.getNonce())
                        .dhPublicKey(new AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey()
                                .expiry(Instant.now().plus(1, ChronoUnit.HOURS).toString())
                                .parameters("Curve25519/32byte random key")
                                .keyValue(keyMaterial.getPublicKey())));

        return new AbdmDataFlow7Request().hiRequest(hiRequest);
    }

    public CompletionStage<Void> processHealthInformationOnRequest(
            String requestId,
            String timestamp,
            String hiuId,
            HiuConsentWebhookController.HealthInformationOnRequestBody request) {

        log.info("Processing health information on-request for requestId: {}", requestId);

        if (request.getError() != null) {
            log.error("Error found in health information on-request callback: {}", request.getError().getMessage());
            return completedFuture(null);
        }

        String responseRequestId = request.getResponse() != null ? request.getResponse().getRequestId() : null;
        if (responseRequestId == null) {
            log.error("No response requestId found in health information on-request callback");
            return completedFuture(null);
        }

        String transactionId = request.getHiRequest() != null ? request.getHiRequest().getTransactionId() : null;
        if (transactionId == null) {
            log.error("No transaction ID found in health information on-request callback");
            return completedFuture(null);
        }

        // 1. Get consent by the request id mentioned in body from consent health record table
        return consentHealthRecordDao.getByHealthDataRequestId(responseRequestId)
                .thenCompose(consentHealthRecordOpt -> {
                    if (consentHealthRecordOpt.isEmpty()) {
                        log.error("Consent health record not found for requestId: {}", responseRequestId);
                        throw new RuntimeException("Consent health record not found: " + responseRequestId);
                    }

                    ConsentHealthRecord consentHealthRecord = consentHealthRecordOpt.get();
                    String consentId = consentHealthRecord.getConsentId();

                    // 2. Update status as REQUESTED and transaction id in consent health record table
                    return consentHealthRecordDao.updateStatus(consentId, ConsentHealthRecord.Status.REQUESTED)
                            .thenCompose(ignore -> consentHealthRecordDao.updateTransactionId(consentId, transactionId));
                })
                .exceptionally(ex -> {
                    log.error("Failed to process health information on-request for requestId: {}", responseRequestId, ex);
                    throw new RuntimeException("Failed to process health information on-request: " + responseRequestId, ex);
                });
    }

    public CompletionStage<Void> processDataPush(HiuConsentWebhookController.DataPushBody request) {
        log.info("Processing data push for transactionId: {}", request.getTransactionId());

        String transactionId = request.getTransactionId();
        if (transactionId == null) {
            log.error("No transaction ID found in data push request");
            return completedFuture(null);
        }

        // 1. Get key material for transaction id using ConsentHealthRecordDao
        return consentHealthRecordDao.getByTransactionId(transactionId)
                .thenCompose(consentHealthRecordOpt -> {
                    if (consentHealthRecordOpt.isEmpty()) {
                        log.error("Consent health record not found for transactionId: {}", transactionId);
                        throw new RuntimeException("Consent health record not found: " + transactionId);
                    }

                    ConsentHealthRecord consentHealthRecord = consentHealthRecordOpt.get();
                    JsonObject storedKeyMaterial = consentHealthRecord.getKeyMaterial();

                    if (storedKeyMaterial == null) {
                        log.error("No key material found for transactionId: {}", transactionId);
                        throw new RuntimeException("No key material found: " + transactionId);
                    }

                    consentHealthRecord.add(request, decryptionService);

                    // 3. Update health_records column
                    return consentHealthRecordDao.updateHealthRecordsAndStatus(consentHealthRecord)
                            .thenCompose(ignore -> {
                                if (consentHealthRecord.isTransferComplete()) {
                                    return notifyHealthInfoTransferSuccess(consentHealthRecord);
                                }
                                return completedFuture(null);
                            })
                            .exceptionally(ex -> {
                                log.error("Failed to process data push for transactionId: {}", transactionId, ex);
                                // Notify failure
                                notifyHealthInfoTransferFailure(consentHealthRecord, transactionId)
                                        .exceptionally(notifyEx -> {
                                            log.error("Failed to notify health info transfer failure", notifyEx);
                                            return null;
                                        });
                                throw new RuntimeException("Failed to process data push: " + transactionId, ex);
                            });
                });
    }

    private CompletionStage<Void> notifyHealthInfoTransferSuccess(ConsentHealthRecord consentHealthRecord) {

        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

        List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> statusResponses = consentHealthRecord.getHealthRecords().stream()
                .map(entry -> new AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner()
                        .careContextReference(entry.getCareContextReference())
                        .hiStatus("OK")
                        .description("Data received successfully"))
                .collect(Collectors.toList());

        AbdmDataFlow8Request notificationRequest = new AbdmDataFlow8Request()
                .notification(new AbdmDataFlow8RequestNotification()
                        .consentId(consentHealthRecord.getConsentId())
                        .transactionId(consentHealthRecord.getTransactionId())
                        .doneAt(timestamp)
                        .notifier(new AbdmDataFlow8RequestNotificationNotifier()
                                .type(AbdmDataFlow8RequestNotificationNotifier.TypeEnum.HIU)
                                .id(consentHealthRecord.getHiuId()))
                        .statusNotification(new AbdmDataFlow8RequestNotificationStatusNotification()
                                .sessionStatus("TRANSFERRED")
                                .hipId(consentHealthRecord.getHipId())
                                .statusResponses(statusResponses)));

        return abhaRestClient.notifyHealthInfoTransfer(requestId, timestamp, notificationRequest);
    }

    private CompletionStage<Void> notifyHealthInfoTransferFailure(ConsentHealthRecord consentHealthRecord,
                                                                  String transactionId) {

        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

        AbdmDataFlow8Request notificationRequest = new AbdmDataFlow8Request()
                .notification(new AbdmDataFlow8RequestNotification()
                        .consentId(consentHealthRecord.getConsentId())
                        .transactionId(transactionId)
                        .doneAt(timestamp)
                        .notifier(new AbdmDataFlow8RequestNotificationNotifier()
                                .type(AbdmDataFlow8RequestNotificationNotifier.TypeEnum.HIU)
                                .id(consentHealthRecord.getHiuId()))
                        .statusNotification(new AbdmDataFlow8RequestNotificationStatusNotification()
                                .sessionStatus("ERROR")
                                .hipId(consentHealthRecord.getHipId())
                                .statusResponses(List.of())));

        return abhaRestClient.notifyHealthInfoTransfer(requestId, timestamp, notificationRequest);
    }

    public CompletionStage<List<ConsentRequest>> listConsentRequests(String healthFacilityID, String healthFacilityProfessionalId) {
        return consentRequestDao.getByHiuAndRequesterId(healthFacilityID, healthFacilityProfessionalId);
    }

    public CompletionStage<List<ConsentHealthRecord>> listConsentHealthRecords(String healthFacilityID, String healthFacilityProfessionalId, String consentRequestId) {
        return consentRequestDao.getByConsentRequestId(consentRequestId)
                        .thenCompose(consentRequest -> {
                            checkState(consentRequest.isPresent(), "Consent request not found: " + consentRequestId);
                            checkState(consentRequest.get().getHiuId().equals(healthFacilityID), "Consent request does not belong to health facility: " + healthFacilityID);
                            checkState(consentRequest.get().getRequesterId().equals(healthFacilityProfessionalId), "Consent request does not belong to health facility professional: " + healthFacilityProfessionalId);
                            return consentHealthRecordDao.getByConsentRequestId(consentRequestId);
                        });

    }
}