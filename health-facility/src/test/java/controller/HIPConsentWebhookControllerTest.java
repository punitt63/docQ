package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestKeyMaterial;
import in.docq.abha.rest.client.model.AbdmConsentManagement6RequestKeyMaterialDhPublicKey;
import in.docq.abha.rest.client.model.AbdmSessions3200Response;
import in.docq.abha.rest.client.model.AbdmSessions3200ResponseKeysInner;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.dao.ConsentDao;
import in.docq.health.facility.dao.HealthInformationRequestDao;
import in.docq.health.facility.model.Consent;
import in.docq.health.facility.model.HealthInformationRequest;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
class HIPConsentWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestAbhaClientConfiguration.MockAbhaRestClient mockAbhaRestClient;

    @Autowired
    private ConsentDao consentDao;

    @Autowired
    private HealthInformationRequestDao healthInformationRequestDao;

    private final String REQUEST_ID = "test-request-id";
    private final String TIMESTAMP = "2024-01-01T10:00:00.000Z";
    private final String HIP_ID = "test-hip-id";
    private final String CONSENT_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    private final String TRANSACTION_ID = "test-transaction-id";

    private HIPConsentWebhookController.ConsentNotifyRequest sampleConsentRequest;
    private HIPConsentWebhookController.HealthInformationRequestBody healthInfoRequest;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter())
            .create();

    @BeforeEach
    void setUp() {
        // Clean up database
        consentDao.truncate().toCompletableFuture().join();
        healthInformationRequestDao.truncate().toCompletableFuture().join();

        // Reset mock client counters
        mockAbhaRestClient.sendConsentGrantAcknowledgementCount = 0;
        mockAbhaRestClient.healthInfoRequestAcknowledgementCount = 0;
        mockAbhaRestClient.getGatewayPublicCertsCount = 0;
        mockAbhaRestClient.gatewayPublicKeysResponse = new AbdmSessions3200Response()
                .addKeysItem(new AbdmSessions3200ResponseKeysInner()
                        .e("AQAB")
                        .kid("test-key-1759528566541")
                        .kty("RSA")
                        .n("sNImhKvAGvKcl2BG5AReYc1P8acpP_PDdn6Bw-ge_f79GW2j3sU96H7-vVSRvMkKxhcVhREfhFGT2iiwWo-xjQzOgFKFvURilnwzn2ES93lt-L8JWIaFexnv7C-E5FErMVo5FjmSmRAW7-kkJfeoBxgGzbjqccH0BhBbiWEmHhJ-SW4vzyafnvK2201QYQtQ5CRCal_UFR0PH1gILGGxbEAa_QRVX7i2go8rztncjUp1wTih_lJA7IRVvz-HAii-ki3pugkcWD2sDRJIwAngp_dKf8XUApVqYPsI5UnBZcKglddh3XuDhDxcNnUNDNuH3poEvva5Rd6RU1daHKB0mw")
                        .use("sig")
                        .addX5cItem("MIIDQTCCAimgAwIBAgITBmyfz5m/jAo54vB4ikPmljZbyjANBgkqhkiG9w0BAQsFADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6b24gUm9vdCBDQSAxMB4XDTE1MDUyNjAwMDAwMFoXDTM4MDExNzAwMDAwMFowOTELMAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJvb3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXjca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qwIFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6VOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQmjgSubJrIqg0CAwEAAaNCMEAwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMCAYYwHQYDVR0OBBYEFIQYzIU07LwMlJQuCFmcx7IQTgoIMA0GCSqGSIb3DQEBCwUAA4IBAQCY8jdaQZChGsV2USggNiMOruYou6r4lK5IpDB/G/wkjUu0yKGX9rbxenDIU5PMCCjjmCXPI6T53iHTfIuJruydjsw2hUwsHReHTxZd8CYd4KJtfN8s2o3pKKX9KLXI")
                        .x5t("dGVzdC10aHVtYnByaW50LXNoYTE")
                        .x5t2("dGVzdC10aHVtYnByaW50LXNoYTI1Ng")
                        .alg("RS256"));

        // Create sample ConsentNotifyRequest matching the JSON structure
        sampleConsentRequest = HIPConsentWebhookController.ConsentNotifyRequest.builder()
                .notification(HIPConsentWebhookController.Notification.builder()
                        .status("GRANTED")
                        .consentId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .consent(Consent.builder()
                                .consentId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                                .schemaVersion("v3")
                                .createdAt("2024-05-01T05:10:20.123Z")
                                .patient(Consent.Patient.builder()
                                        .id("abdulkalam@abdm")
                                        .build())
                                .careContexts(List.of(
                                        Consent.CareContext.builder()
                                                .patientReference("batman@tmh")
                                                .careContextReference("Episode1")
                                                .build()
                                ))
                                .purpose(Consent.Purpose.builder()
                                        .text("Care Management")
                                        .code("CAREMGT")
                                        .refUri("www.abc.com")
                                        .build())
                                .hip(Consent.Hip.builder()
                                        .id("cowin_hip_01")
                                        .name("Cowin")
                                        .type("HIP")
                                        .build())
                                .hiu(Consent.Hiu.builder()
                                        .id("cowin_hiu_01")
                                        .name("Cowin")
                                        .type("HIU")
                                        .build())
                                .consentManager(Consent.ConsentManager.builder()
                                        .id("abdm")
                                        .build())
                                .requester(Consent.Requester.builder()
                                        .name("abdulkalam@abdm")
                                        .identifier(Consent.Identifier.builder()
                                                .value("REG1")
                                                .type("MH1001")
                                                .system("https://www.sample.com")
                                                .build())
                                        .build())
                                .hiTypes(List.of("Prescription"))
                                .permission(Consent.Permission.builder()
                                        .accessMode("VIEW")
                                        .dateRange(Consent.DateRange.builder()
                                                .from("2021-09-28T12:30:08.573Z")
                                                .to("2022-09-28T12:30:08.573Z")
                                                .build())
                                        .dataEraseAt("2021-09-28T12:30:08.573Z")
                                        .frequency(Consent.Frequency.builder()
                                                .unit("HOUR")
                                                .value(1)
                                                .repeats(0)
                                                .build())
                                        .build())
                                .build())
                        .signature("YmuLByLdOV3eraPPgsytRVYwTQUn9hxtZEGZkTRKoFqd0nezGPLGcrucxcIVY1MdX-pT_IwTmBLCwAPoDNvzswkw4jkVZxWkGP_CD9wEh3qyvYClASeyRY30CCEDWmIiqUe4zQLA8T1O9T0saCuBdF844HhagxwL5vMmduTMePaLnpsY6GcUCB-IShnFbAAeDuKmQJgLZvR7PhQjdKTbHm38wryii7ZPsNEfc2aCmpmqZGxBRmQkr3vL665wJmcJzz2kF46gM8ZV_40Pv31753k9I3of6hjxKVy0GHz9yLFYZJyA0a5CAbQ62EVmpWVYMn1bcuedLa7miCcHYqoQnA==")
                        .grantAcknowledgement(false)
                        .build())
                .build();
    }

//    void testConsentNotifyWithIncorrectSignatureThrowsBadRequest() throws Exception {
//        // Given
//        HIPConsentWebhookController.ConsentNotifyRequest invalidSignatureRequest =
//                sampleConsentRequest.toBuilder()
//                        .notification(sampleConsentRequest.getNotification().toBuilder()
//                                .signature("invalid-signature")
//                                .build())
//                        .build();
//
//        // When & Then
//        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
//                        .header("REQUEST-ID", REQUEST_ID)
//                        .header("TIMESTAMP", TIMESTAMP)
//                        .header("X-HIP-ID", HIP_ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(gson.toJson(invalidSignatureRequest))))
//                .andExpect(status().isBadRequest());
//
//        // Verify gateway public certs was called
//        assertEquals(0, mockAbhaRestClient.getGatewayPublicCertsCount);
//
//        // Verify no further actions were taken
//        assertEquals(0, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);
//
//        // Verify consent was not saved
//        Optional<Consent> savedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
//        assertTrue(savedConsent.isEmpty());
//    }

    @Test
    void testConsentNotifyWithCorrectSignatureAndGrantAcknowledgementFalse() throws Exception {

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sampleConsentRequest))))
                .andExpect(status().isOk());

        // Then - Verify ABDM client calls
        assertEquals(0, mockAbhaRestClient.getGatewayPublicCertsCount);
        assertEquals(0, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);

        // Verify consent is saved in database
        Optional<Consent> savedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
        assertTrue(savedConsent.isPresent());
        assertEquals("GRANTED", savedConsent.get().getStatus());
        assertEquals(CONSENT_ID, savedConsent.get().getConsentId());
    }

    @Test
    void testConsentNotifyWithCorrectSignatureAndGrantAcknowledgementTrue() throws Exception {
        // Given
        HIPConsentWebhookController.ConsentNotifyRequest requestWithoutAck =
                sampleConsentRequest.toBuilder()
                        .notification(sampleConsentRequest.getNotification().toBuilder()
                                .grantAcknowledgement(true)
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestWithoutAck))))
                .andExpect(status().isOk());

        // Then - Verify signature verification happened but no acknowledgement sent
        assertEquals(0, mockAbhaRestClient.getGatewayPublicCertsCount);
        assertEquals(1, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);

        // Verify consent is still saved
        Optional<Consent> savedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
        assertTrue(savedConsent.isPresent());
        assertEquals("GRANTED", savedConsent.get().getStatus());
    }

    @Test
    void testConsentNotifyWithRevokedStatusDeletesConsent() throws Exception {
        // Given - Insert a consent first
        consentDao.insert(CONSENT_ID, sampleConsentRequest.getNotification().getConsent(), "GRANTED").toCompletableFuture().join();

        HIPConsentWebhookController.ConsentNotifyRequest revokedRequest =
                HIPConsentWebhookController.ConsentNotifyRequest.builder()
                        .notification(HIPConsentWebhookController.Notification.builder()
                                .consentId(CONSENT_ID)
                                .status("REVOKED")
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(revokedRequest))))
                .andExpect(status().isOk());

        // Then - Verify consent is deleted and no ABDM calls made
        Optional<Consent> deletedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
        assertTrue(deletedConsent.isEmpty());

        assertEquals(0, mockAbhaRestClient.getGatewayPublicCertsCount);
        assertEquals(0, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);
    }

    @Test
    void testConsentNotifyWithExpiredStatusDeletesConsent() throws Exception {
        // Given - Insert a consent first
        consentDao.insert(CONSENT_ID, sampleConsentRequest.getNotification().getConsent(), "GRANTED").toCompletableFuture().join();

        HIPConsentWebhookController.ConsentNotifyRequest expiredRequest =
                HIPConsentWebhookController.ConsentNotifyRequest.builder()
                        .notification(HIPConsentWebhookController.Notification.builder()
                                .consentId(CONSENT_ID)
                                .status("EXPIRED")
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(expiredRequest))))
                .andExpect(status().isOk());

        // Then - Verify consent is deleted and no ABDM calls made
        Optional<Consent> deletedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
        assertTrue(deletedConsent.isEmpty());

        assertEquals(0, mockAbhaRestClient.getGatewayPublicCertsCount);
        assertEquals(0, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);
    }

    @Test
    void testHealthInformationRequestSuccess() throws Exception {
        // Given - Insert consent first
        consentDao.insert(CONSENT_ID, sampleConsentRequest.getNotification().getConsent(), "GRANTED").toCompletableFuture().join();

        // Create the health information request body
        HIPConsentWebhookController.HealthInformationRequestBody healthInfoRequest =
                HIPConsentWebhookController.HealthInformationRequestBody.builder()
                        .transactionId(TRANSACTION_ID)
                        .hiRequest(HIPConsentWebhookController.HIRequest.builder()
                                .consent(HIPConsentWebhookController.Consent.builder()
                                        .id(CONSENT_ID)
                                        .build())
                                .dateRange(HIPConsentWebhookController.DateRange.builder()
                                        .from("2021-10-28T12:30:08.573Z")
                                        .to("2022-08-28T12:30:08.573Z")
                                        .build())
                                .dataPushUrl("https://live.ndhm.gov.in/api-hiu/data/notification")
                                .keyMaterial(new AbdmConsentManagement6RequestKeyMaterial()
                                        .cryptoAlg(AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.ECDH)
                                        .curve(AbdmConsentManagement6RequestKeyMaterial.CurveEnum.CURVE25519)
                                        .dhPublicKey(new AbdmConsentManagement6RequestKeyMaterialDhPublicKey()
                                                .expiry("2022-12-28T13:18:20.742Z")
                                                .parameters(AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.CURVE25519_32BYTE_RANDOM_KEY)
                                                .keyValue("BFN7KTdOT0jIAExG2A8Jg+01wMPWxptiGqwHRVvtiVEsUq2FR7P2UdqZxJyPJSeR6muai21iQhasNxnhh8I5M+g="))
                                        .nonce("28236d89-cb13-479d-ad71-7a57d5f669a9"))
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/health-information/request")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(healthInfoRequest))))
                .andExpect(status().isOk());

        // Then - Verify health information request is saved
        Optional<HealthInformationRequest> savedRequest =
                healthInformationRequestDao.getByTransactionId(TRANSACTION_ID).toCompletableFuture().join();
        assertTrue(savedRequest.isPresent());
        assertEquals(TRANSACTION_ID, savedRequest.get().getTransactionId());
        assertEquals(CONSENT_ID, savedRequest.get().getConsentId());
        assertEquals("ACKNOWLEDGED", savedRequest.get().getStatus());

        // Verify ABDM client call
        assertEquals(1, mockAbhaRestClient.healthInfoRequestAcknowledgementCount);
    }

    @Test
    void testHealthInformationRequestWithInvalidDate() throws Exception {
        // Given - Insert consent first
        consentDao.insert(CONSENT_ID, sampleConsentRequest.getNotification().getConsent(), "GRANTED").toCompletableFuture().join();

        // Create the health information request body
        HIPConsentWebhookController.HealthInformationRequestBody healthInfoRequest =
                HIPConsentWebhookController.HealthInformationRequestBody.builder()
                        .transactionId(TRANSACTION_ID)
                        .hiRequest(HIPConsentWebhookController.HIRequest.builder()
                                .consent(HIPConsentWebhookController.Consent.builder()
                                        .id(CONSENT_ID)
                                        .build())
                                .dateRange(HIPConsentWebhookController.DateRange.builder()
                                        .from("2021-10-28T12:30:08.573Z")
                                        .to("2023-08-28T12:30:08.573Z")
                                        .build())
                                .dataPushUrl("https://live.ndhm.gov.in/api-hiu/data/notification")
                                .keyMaterial(new AbdmConsentManagement6RequestKeyMaterial()
                                        .cryptoAlg(AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.ECDH)
                                        .curve(AbdmConsentManagement6RequestKeyMaterial.CurveEnum.CURVE25519)
                                        .dhPublicKey(new AbdmConsentManagement6RequestKeyMaterialDhPublicKey()
                                                .expiry("2022-12-28T13:18:20.742Z")
                                                .parameters(AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.CURVE25519_32BYTE_RANDOM_KEY)
                                                .keyValue("BFN7KTdOT0jIAExG2A8Jg+01wMPWxptiGqwHRVvtiVEsUq2FR7P2UdqZxJyPJSeR6muai21iQhasNxnhh8I5M+g="))
                                        .nonce("28236d89-cb13-479d-ad71-7a57d5f669a9"))
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/health-information/request")
                .header("REQUEST-ID", REQUEST_ID)
                .header("TIMESTAMP", TIMESTAMP)
                .header("X-HIP-ID", HIP_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(healthInfoRequest))))
                .andExpect(status().isOk());

        // Then - Verify health information request is saved
        Optional<HealthInformationRequest> savedRequest =
                healthInformationRequestDao.getByTransactionId(TRANSACTION_ID).toCompletableFuture().join();
        assertFalse(savedRequest.isPresent());

        // Verify ABDM client call
        assertEquals(1, mockAbhaRestClient.healthInfoRequestAcknowledgementCount);
    }

    @Test
    void testHealthInformationRequestWithNonExistentConsent() throws Exception {
        // Given - No consent in database
        // Create the health information request body
        HIPConsentWebhookController.HealthInformationRequestBody healthInfoRequest =
                HIPConsentWebhookController.HealthInformationRequestBody.builder()
                        .transactionId(TRANSACTION_ID)
                        .hiRequest(HIPConsentWebhookController.HIRequest.builder()
                                .consent(HIPConsentWebhookController.Consent.builder()
                                        .id("non-existent-consent-id")
                                        .build())
                                .dateRange(HIPConsentWebhookController.DateRange.builder()
                                        .from("2021-10-28T12:30:08.573Z")
                                        .to("2022-08-28T12:30:08.573Z")
                                        .build())
                                .dataPushUrl("https://live.ndhm.gov.in/api-hiu/data/notification")
                                .keyMaterial(new AbdmConsentManagement6RequestKeyMaterial()
                                        .cryptoAlg(AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.ECDH)
                                        .curve(AbdmConsentManagement6RequestKeyMaterial.CurveEnum.CURVE25519)
                                        .dhPublicKey(new AbdmConsentManagement6RequestKeyMaterialDhPublicKey()
                                                .expiry("2022-12-28T13:18:20.742Z")
                                                .parameters(AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.CURVE25519_32BYTE_RANDOM_KEY)
                                                .keyValue("BFN7KTdOT0jIAExG2A8Jg+01wMPWxptiGqwHRVvtiVEsUq2FR7P2UdqZxJyPJSeR6muai21iQhasNxnhh8I5M+g="))
                                        .nonce("28236d89-cb13-479d-ad71-7a57d5f669a9"))
                                .build())
                        .build();

        // When
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/health-information/request")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(healthInfoRequest))))
                .andExpect(status().isOk());

        // Then - Verify error acknowledgement sent
        assertEquals(1, mockAbhaRestClient.healthInfoRequestAcknowledgementCount);

        // Verify no health information request saved with ACKNOWLEDGED status
        Optional<HealthInformationRequest> savedRequest =
                healthInformationRequestDao.getByTransactionId(TRANSACTION_ID).toCompletableFuture().join();
        assertTrue(savedRequest.isEmpty());
    }

    protected ResultActions handleAsyncProcessing(ResultActions resultActions) throws Exception {
        MvcResult mvcResult = resultActions.andReturn();
        while (mvcResult.getAsyncResult() != null && mvcResult.getAsyncResult() instanceof CompletableFuture) {
            resultActions = mockMvc.perform(asyncDispatch(mvcResult));
            mvcResult = resultActions.andReturn();
        }
        ResultActions resultActionsUpdated = mockMvc.perform(asyncDispatch(mvcResult));
        return resultActionsUpdated;
    }
}