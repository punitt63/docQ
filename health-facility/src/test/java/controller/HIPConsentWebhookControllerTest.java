package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.abha.rest.client.model.AbdmSessions3200Response;
import in.docq.abha.rest.client.model.AbdmSessions3200ResponseKeysInner;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.controller.HIPConsentWebhookController;
import in.docq.health.facility.dao.ConsentDao;
import in.docq.health.facility.dao.HealthInformationRequestDao;
import in.docq.health.facility.model.Consent;
import in.docq.health.facility.model.HealthInformationRequest;
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
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                        .kid("AlRb5WCm8Tm9EJ_IfO9z06j9oCv51pKK")
                        .kty("RSA")
                        .n("mgmW7W5ZGF_G5cJevwYi8HiPcI-6qS_psnZxa4v3bkwAkyOoOd8-6ketrOI-ZA2PbRbGnxFfZHiI94rdFXJ4Q9ampscsz9NocTIPMPmWydJ8A50pZaYWyikYDSJiDltq7i3WspPKSOuQHr")
                        .use("sig")
                        .addX5cItem("MIICrzCCAZcCBgFy/3WZBjANBgkqhkiG9w0BAQsFADAbMRkwFwYDVQQDDBBjZW50cmFsLXJlZ2lzdHJ5MB4XDTIwMDYyOTA5NDEzNloXDTMwMDYyOTA5NDMxNlowGzEZMBcGA1UEAwwQY2VudHJhbC1yZWdpc3RyeTCCASIwDQYJK")
                        .x5t("EaMhYGUIvMkp8tvS")
                        .x5t2("vGer6Pt8AhZn8RlbHhAFksOCcGf3u1UWU7Qq")
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
                                                .to("2021-09-28T12:30:08.573Z")
                                                .build())
                                        .dataEraseAt("2021-09-28T12:30:08.573Z")
                                        .frequency(Consent.Frequency.builder()
                                                .unit("HOUR")
                                                .value(1)
                                                .repeats(0)
                                                .build())
                                        .build())
                                .build())
                        .signature("e8nY601CYDsC0FKoDjSp+7GeQ2s2R8oZncLCz5ce+pEuDOr5bZV0aaHjwJg4b9S9V+twjt4hbojx3fl7egrt8+0c+lfPTi5/bBUAQXCABTfFmtFU7jn65HlTt8kgkiONx26ZBhJ0wX3xjYI72PPtzYIiT5Q08YtDoILA62KceioV7lwuKssw7wC4ECbBAvRuXT121TmtrPhf+0myJATSnaajS06S6OthrKfZLNTUFf3pFiJzqouSTrjNblOX6DT2+JuO3rom1Szz/03c0HQG+wWASv+PO3J6uRs0UI4JvKmM/4tP+Z+/HPKM15K5U5K+4pqf6czKrbIDpkT/kP8bGg==")
                        .grantAcknowledgement(false)
                        .build())
                .build();
    }

    @Test
    void testConsentNotifyWithIncorrectSignatureThrowsBadRequest() throws Exception {
        // Given
        HIPConsentWebhookController.ConsentNotifyRequest invalidSignatureRequest =
                sampleConsentRequest.toBuilder()
                        .notification(sampleConsentRequest.getNotification().toBuilder()
                                .signature("invalid-signature")
                                .build())
                        .build();

        // When & Then
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(invalidSignatureRequest))))
                .andExpect(status().isBadRequest());

        // Verify gateway public certs was called
        assertEquals(1, mockAbhaRestClient.getGatewayPublicCertsCount);

        // Verify no further actions were taken
        assertEquals(0, mockAbhaRestClient.sendConsentGrantAcknowledgementCount);

        // Verify consent was not saved
        Optional<Consent> savedConsent = consentDao.getById(CONSENT_ID).toCompletableFuture().join();
        assertTrue(savedConsent.isEmpty());
    }

    @Test
    void testConsentNotifyWithCorrectSignatureAndGrantAcknowledgementFalse() throws Exception {

        // When
        mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sampleConsentRequest)))
                .andExpect(status().isOk());

        // Then - Verify ABDM client calls
        assertEquals(1, mockAbhaRestClient.getGatewayPublicCertsCount);
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
        mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestWithoutAck)))
                .andExpect(status().isOk());

        // Then - Verify signature verification happened but no acknowledgement sent
        assertEquals(1, mockAbhaRestClient.getGatewayPublicCertsCount);
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
        mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(revokedRequest)))
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
        mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(expiredRequest)))
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

        // When
        mockMvc.perform(post("/api/v3/hip/health-information/request")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(healthInfoRequest)))
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
    void testHealthInformationRequestWithMissingHeaders() throws Exception {
        // When & Then - Missing REQUEST-ID header
        mockMvc.perform(post("/api/v3/hip/health-information/request")
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(healthInfoRequest)))
                .andExpect(status().isBadRequest());

        // Verify no service calls made
        assertEquals(0, mockAbhaRestClient.healthInfoRequestAcknowledgementCount);

        // Verify no health information request saved
        Optional<HealthInformationRequest> savedRequest =
                healthInformationRequestDao.getByTransactionId(TRANSACTION_ID).toCompletableFuture().join();
        assertTrue(savedRequest.isEmpty());
    }

    @Test
    void testHealthInformationRequestWithNonExistentConsent() throws Exception {
        // Given - No consent in database
        HIPConsentWebhookController.HealthInformationRequestBody requestWithBadConsent =
                HIPConsentWebhookController.HealthInformationRequestBody.builder()
                        .transactionId(TRANSACTION_ID)
                        .hiRequest(HIPConsentWebhookController.HIRequest.builder()
                                .consent(HIPConsentWebhookController.Consent.builder()
                                        .id("non-existent-consent")
                                        .build())
                                .dateRange(healthInfoRequest.getHiRequest().getDateRange())
                                .dataPushUrl(healthInfoRequest.getHiRequest().getDataPushUrl())
                                .keyMaterial(healthInfoRequest.getHiRequest().getKeyMaterial())
                                .build())
                        .build();

        // When
        mockMvc.perform(post("/api/v3/hip/health-information/request")
                        .header("REQUEST-ID", REQUEST_ID)
                        .header("TIMESTAMP", TIMESTAMP)
                        .header("X-HIP-ID", HIP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestWithBadConsent)))
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