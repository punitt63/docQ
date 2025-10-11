package in.docq.health.facility.controller;

import in.docq.health.facility.service.HiuConsentService;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v3")
public class HiuConsentWebhookController {
    private final static Logger logger = LoggerFactory.getLogger(HiuConsentWebhookController.class);
    private final HiuConsentService hiuConsentService;

    @Autowired
    public HiuConsentWebhookController(HiuConsentService hiuConsentService) {
        this.hiuConsentService = hiuConsentService;
    }

    @PostMapping("/hiu/consent/request/on-init")
    public CompletionStage<ResponseEntity<Void>> onInitConsentRequest(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIU-ID") String hiuId,
            @RequestBody ConsentRequestOnInitBody request) {

        logger.info("Received consent request on-init callback for requestId: {}", requestId);

        return hiuConsentService.processConsentRequestOnInit(requestId, timestamp, hiuId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/hiu/consent/request/notify")
    public CompletionStage<ResponseEntity<Void>> onNotifyConsentRequest(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIU-ID") String hiuId,
            @RequestBody ConsentRequestNotifyBody request) {

        logger.info("Received consent request notify callback for requestId: {}", requestId);

        return hiuConsentService.processConsentRequestNotify(requestId, timestamp, hiuId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/hiu/consent/on-fetch")
    public CompletionStage<ResponseEntity<Void>> onFetchConsent(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIU-ID") String hiuId,
            @RequestBody ConsentOnFetchBody request) {

        logger.info("Received consent on-fetch callback for requestId: {}", requestId);

        return hiuConsentService.processConsentOnFetch(requestId, timestamp, hiuId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/hiu/health-information/on-request")
    public CompletionStage<ResponseEntity<Void>> onRequestHealthInformation(
            @RequestHeader("REQUEST-ID") String requestId,
            @RequestHeader("TIMESTAMP") String timestamp,
            @RequestHeader("X-HIU-ID") String hiuId,
            @RequestBody HealthInformationOnRequestBody request) {

        logger.info("Received health information on-request callback for requestId: {}", requestId);

        return hiuConsentService.processHealthInformationOnRequest(requestId, timestamp, hiuId, request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @PostMapping("/hiu/data-push-webhook")
    public CompletionStage<ResponseEntity<Void>> onDataPush(@RequestBody DataPushBody request) {
        logger.info("Received data push webhook for transactionId: {}", request.getTransactionId());

        return hiuConsentService.processDataPush(request)
                .thenApply(ignore -> ResponseEntity.ok().build());
    }

    @Builder
    @Getter
    public static class HealthInformationOnRequestBody {
        private final HiRequestInfo hiRequest;
        private final ErrorResponse error;
        private final ResponseInfo response;
    }

    @Builder
    @Getter
    public static class HiRequestInfo {
        private final String transactionId;
        private final String sessionStatus;
    }

    @Builder
    @Getter
    public static class ConsentOnFetchBody {
        private final ConsentInfo consent;
        private final ErrorResponse error;
        private final ResponseInfo response;
        private final Object resp;
    }

    @Builder
    @Getter
    public static class ConsentInfo {
        private final String status;
        private final ConsentDetail consentDetail;
        private final String signature;
    }

    @Builder
    @Getter
    public static class ConsentDetail {
        private final String consentId;
        private final HipInfo hip;
        private final HiuInfo hiu;
        private final List<String> hiTypes;
        private final PatientInfo patient;
        private final PurposeInfo purpose;
        private final String createdAt;
        private final RequesterInfo requester;
        private final PermissionInfo permission;
        private final String lastUpdated;
        private final List<CareContextInfo> careContexts;
        private final String schemaVersion;
        private final ConsentManagerInfo consentManager;
    }

    @Builder
    @Getter
    public static class HipInfo {
        private final String id;
    }

    @Builder
    @Getter
    public static class HiuInfo {
        private final String id;
    }

    @Builder
    @Getter
    public static class PatientInfo {
        private final String id;
    }

    @Builder
    @Getter
    public static class PurposeInfo {
        private final String text;
        private final String code;
        private final String refUri;
    }

    @Builder
    @Getter
    public static class RequesterInfo {
        private final String name;
        private final IdentifierInfo identifier;
    }

    @Builder
    @Getter
    public static class IdentifierInfo {
        private final String value;
        private final String type;
        private final String system;
    }

    @Builder
    @Getter
    public static class PermissionInfo {
        private final String accessMode;
        private final DateRangeInfo dateRange;
        private final String dataEraseAt;
        private final FrequencyInfo frequency;
    }

    @Builder
    @Getter
    public static class DateRangeInfo {
        private final String from;
        private final String to;
    }

    @Builder
    @Getter
    public static class FrequencyInfo {
        private final String unit;
        private final String value;
        private final String repeats;
    }

    @Builder
    @Getter
    public static class CareContextInfo {
        private final String patientReference;
        private final String careContextReference;
    }

    @Builder
    @Getter
    public static class ConsentManagerInfo {
        private final String id;
    }

    @Builder
    @Getter
    public static class ConsentRequestNotifyBody {
        private final NotificationInfo notification;
    }

    @Builder
    @Getter
    public static class NotificationInfo {
        private final String consentRequestId;
        private final String status;
        private final String reason;
        private final List<ConsentArtefactRef> consentArtefacts;
    }

    @Builder
    @Getter
    public static class ConsentArtefactRef {
        private final String id;
    }

    @Builder
    @Getter
    public static class ConsentRequestOnInitBody {
        private final ConsentRequestRef consentRequest;
        private final ErrorResponse error;
        private final ResponseInfo response;
    }

    @Builder
    @Getter
    public static class ConsentRequestRef {
        private final String id;
    }

    @Builder
    @Getter
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }

    @Builder
    @Getter
    public static class ResponseInfo {
        private final String requestId;
    }

    @Builder
    @Getter
    public static class DataPushBody {
        private final Integer pageNumber;
        private final Integer pageCount;
        private final String transactionId;
        private final List<DataEntry> entries;
        private final KeyMaterialInfo keyMaterial;
    }

    @Builder
    @Getter
    public static class DataEntry {
        private final String content;
        private final String media;
        private final String checksum;
        private final String careContextReference;
    }

    @Builder
    @Getter
    public static class KeyMaterialInfo {
        private final String cryptoAlg;
        private final String curve;
        private final DhPublicKeyInfo dhPublicKey;
        private final String nonce;
    }

    @Builder
    @Getter
    public static class DhPublicKeyInfo {
        private final String expiry;
        private final String parameters;
        private final String keyValue;
    }
}