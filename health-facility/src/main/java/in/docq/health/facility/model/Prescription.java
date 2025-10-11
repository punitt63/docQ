package in.docq.health.facility.model;

import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestEntriesInner;
import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestKeyMaterial;
import in.docq.abha.rest.client.model.AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.fidelius.encryption.EncryptionRequest;
import in.docq.health.facility.fidelius.encryption.EncryptionResponse;
import in.docq.health.facility.fidelius.encryption.EncryptionService;
import in.docq.health.facility.fidelius.keys.KeyMaterial;
import in.docq.health.facility.service.HIPConsentService;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Builder
@Getter
public class Prescription {
    private static final Logger logger = LoggerFactory.getLogger(Prescription.class);
    private final String opdID;
    private final Integer appointmentID;
    private final LocalDate date;
    private final String content;

    private EncryptionResponse encryptPrescription(EncryptionService encryptionService,
                                                   AbdmConsentManagement6RequestKeyMaterial receiverKeyMaterial,
                                                   KeyMaterial senderKeyMaterial) {
            try {
                EncryptionRequest encryptionRequest = EncryptionRequest.builder()
                        .senderPrivateKey(senderKeyMaterial.getPrivateKey())
                        .senderPublicKey(senderKeyMaterial.getPublicKey())
                        .senderNonce(senderKeyMaterial.getNonce())
                        .receiverPublicKey(receiverKeyMaterial.getDhPublicKey().getKeyValue())
                        .receiverNonce(receiverKeyMaterial.getNonce())
                        .plainTextData(content)
                        .build();

                return encryptionService.encrypt(encryptionRequest);
            } catch (Exception e) {
                logger.error("Failed to encrypt prescription: {}", getId(), e);
                throw new RuntimeException(e);
            }
    }

    public static List<AbdmConsentManagement6RequestEntriesInner> toDataTransferEntries(List<Prescription> prescriptions,
                                                                                     EncryptionService encryptionService,
                                                                                     AbdmConsentManagement6RequestKeyMaterial receiverKeyMaterial,
                                                                                     KeyMaterial senderKeyMaterial) {
        return prescriptions.stream()
                .map(prescription -> prescription.toDataTransferEntry(encryptionService, receiverKeyMaterial, senderKeyMaterial))
                .toList();
    }

    public static List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> toStatusResponseEntries(List<Prescription> prescriptions) {
        return prescriptions.stream()
                .map(prescription -> new AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner()
                        .careContextReference(prescription.getId())
                        .hiStatus("OK")
                        .description("Data received successfully"))
                .toList();
    }

    public AbdmConsentManagement6RequestEntriesInner toDataTransferEntry(EncryptionService encryptionService,
                                                                         AbdmConsentManagement6RequestKeyMaterial receiverKeyMaterial,
                                                                         KeyMaterial senderKeyMaterial) {
        EncryptionResponse encryptionResponse = encryptPrescription(encryptionService, receiverKeyMaterial, senderKeyMaterial);
        return new AbdmConsentManagement6RequestEntriesInner()
                .content(encryptionResponse.getEncryptedData())
                .careContextReference(getId())
                .media(AbdmConsentManagement6RequestEntriesInner.MediaEnum.APPLICATION_FHIR_JSON)
                .checksum(generateMD5(encryptionResponse.getEncryptedData()));
    }

    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public String getId() {
        return date.toString() + "_" + opdID + "_" + appointmentID;
    }
}
