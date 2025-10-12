package in.docq.health.facility.model;

import com.google.gson.JsonObject;
import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestEntriesInner;
import in.docq.health.facility.controller.HiuConsentWebhookController;
import in.docq.health.facility.fidelius.decryption.DecryptionRequest;
import in.docq.health.facility.fidelius.decryption.DecryptionResponse;
import in.docq.health.facility.fidelius.decryption.DecryptionService;
import in.docq.health.facility.service.HiuConsentService;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Builder
@Getter
@Slf4j
public class ConsentHealthRecord {
    private String consentId;
    private String consentRequestId;
    private List<PaginatedHealthRecords> paginatedHealthRecords;
    private Status status;
    private JsonObject keyMaterial;
    private String transactionId;
    private String healthDataRequestId;
    private String hipId;
    private String hiuId;

    public boolean isTransferComplete() {
        return status == Status.TRANSFERRED;
    }

    public List<HealthRecord> getHealthRecords() {
        return paginatedHealthRecords.stream()
                .flatMap(page -> page.getHealthRecords().stream())
                .collect(Collectors.toList());
    }

    public PaginatedHealthRecords getPaginatedHealthRecords(int pageNumber) {
        return paginatedHealthRecords.stream()
                .filter(page -> page.getPageNumber() == pageNumber)
                .findFirst()
                .orElse(null);
    }

    public void add(HiuConsentWebhookController.DataPushBody request, DecryptionService decryptionService) {
        boolean pageAlreadyExist = paginatedHealthRecords.stream()
                .anyMatch(page -> page.getPageNumber() == request.getPageNumber());
        if(pageAlreadyExist) {
            log.warn("Page number {} already exists for consentId: {}, skipping addition", request.getPageNumber(), consentId);
            return;
        }
        List<DecryptedEntry> decryptedHealthRecords = decryptEntries(decryptionService, request.getEntries(), keyMaterial, request.getKeyMaterial());
        paginatedHealthRecords.add(DecryptedEntry.to(request.getPageNumber(), decryptedHealthRecords));
        if(paginatedHealthRecords.size() == request.getPageCount()) {
            status = Status.TRANSFERRED;
        }
    }

    private List<DecryptedEntry> decryptEntries(DecryptionService decryptionService, List<HiuConsentWebhookController.DataEntry> entries,
                                                                  JsonObject storedKeyMaterial,
                                                                  HiuConsentWebhookController.KeyMaterialInfo senderKeyMaterial) {

        String receiverPrivateKey = storedKeyMaterial.get("privateKey").getAsString();
        String receiverNonce = storedKeyMaterial.get("nonce") != null ? storedKeyMaterial.get("nonce").getAsString() : UUID.randomUUID().toString();
        String senderPublicKey = senderKeyMaterial.getDhPublicKey().getKeyValue();
        String senderNonce = senderKeyMaterial.getNonce();

        return  entries.stream()
                .map(entry -> decryptEntry(decryptionService, entry, receiverPrivateKey, receiverNonce, senderPublicKey, senderNonce))
                .collect(Collectors.toList());
    }

    private DecryptedEntry decryptEntry(DecryptionService decryptionService, HiuConsentWebhookController.DataEntry entry,
                                                          String receiverPrivateKey, String receiverNonce,
                                                          String senderPublicKey, String senderNonce) {
        try {
            DecryptionRequest decryptionRequest = new DecryptionRequest(
                    receiverPrivateKey, receiverNonce, senderPublicKey, senderNonce, entry.getContent());

            DecryptionResponse decryptionResponse = decryptionService.decrypt(decryptionRequest);

            return DecryptedEntry.builder()
                    .careContextReference(entry.getCareContextReference())
                    .decryptedContent(decryptionResponse.getDecryptedData())
                    .media(entry.getMedia())
                    .checksum(entry.getChecksum())
                    .build();
        } catch (Exception e) {
            log.error("Failed to decrypt entry for careContext: {}", entry.getCareContextReference(), e);
            throw new RuntimeException("Decryption failed for: " + entry.getCareContextReference(), e);
        }
    }

    @Builder
    @Getter
    public static class PaginatedHealthRecords {
        private int pageNumber;
        private List<HealthRecord> healthRecords;
    }

    @Getter
    @Builder
    public static class HealthRecord {
        private String content;
        private String careContextReference;
    }

    public enum Status {
        AWAITING_FETCH,
        REQUESTED,
        TRANSFERRED,
        FAILED
    }

    @Builder
    @Getter
    public static class DecryptedEntry {
        private final String careContextReference;
        private final String decryptedContent;
        private final AbdmConsentManagement6RequestEntriesInner.MediaEnum media;
        private final String checksum;

        public static DecryptedEntry from(HiuConsentWebhookController.DataEntry dataEntry) {
            return DecryptedEntry.builder()
                    .careContextReference(dataEntry.getCareContextReference())
                    .decryptedContent(dataEntry.getContent())
                    .media(dataEntry.getMedia())
                    .checksum(dataEntry.getChecksum())
                    .build();
        }

        private ConsentHealthRecord.HealthRecord to() {
            return ConsentHealthRecord.HealthRecord.builder()
                    .careContextReference(careContextReference)
                    .content(decryptedContent)
                    .build();
        }

        public static ConsentHealthRecord.PaginatedHealthRecords to(int pageNumber, List<DecryptedEntry> entries) {
            return ConsentHealthRecord.PaginatedHealthRecords.builder()
                    .pageNumber(pageNumber)
                    .healthRecords(entries.stream().map(DecryptedEntry::to).collect(Collectors.toList()))
                    .build();
        }
    }
}