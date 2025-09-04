package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.dao.CareContextDao;
import in.docq.health.facility.dao.HIPLinkingTokenDao;
import in.docq.health.facility.dao.PatientDao;
import in.docq.health.facility.dao.UserInitiatedLinkingDao;
import in.docq.health.facility.model.CareContext;
import in.docq.health.facility.model.HIPLinkingToken;
import in.docq.health.facility.model.Patient;
import in.docq.health.facility.model.UserInitiatedLinking;
import in.docq.health.facility.service.OTPService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testHealthFacilityID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class HipWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HIPLinkingTokenDao hipLinkingTokenDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private CareContextDao careContextDao;

    @Autowired
    private UserInitiatedLinkingDao userInitiatedLinkingDao;

    @Autowired
    private TestAbhaClientConfiguration.MockOtpService otpService;


    @Autowired
    private TestAbhaClientConfiguration.MockAbhaRestClient abhaRestClient;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter())
            .create();

    private static final String TEST_ABHA_ADDRESS = "test-abha@sbx";
    private static final String TEST_REQUEST_ID = UUID.randomUUID().toString();
    private static final String TEST_LINK_TOKEN = "test-link-token";
    private static final String TEST_PATIENT_ID = "test-patient-id";
    private static final String TEST_APPOINTMENT_ID = "test-appointment-id";

    @Before
    public void setUp() {
        hipLinkingTokenDao.truncate().toCompletableFuture().join();
        patientDao.truncate().toCompletableFuture().join();
        careContextDao.truncate().toCompletableFuture().join();
        userInitiatedLinkingDao.truncate().toCompletableFuture().join();
        abhaRestClient.linkCareContextCount = 0;
        abhaRestClient.sendDeepLinkNotificationCount = 0;
        abhaRestClient.generateLinkingTokenCount = 0;
        abhaRestClient.linkUserInitiatedCareContextCount = 0;
        abhaRestClient.initiateUserLinking = 0;
        otpService.sendOtpCount = 0;
        otpService.otpToReturn = "123456";
        otpService.lastMobileNumber = "9876543210";
        abhaRestClient.confirmCareContextLinking = 0;
    }

    @Test
    public void testOnGenerateTokenSuccess() throws Exception {
        // Create existing HIP linking token
        HIPLinkingToken existingToken = HIPLinkingToken.builder()
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .lastTokenRequestAppointmentId(TEST_APPOINTMENT_ID)
                .lastTokenRequestId(TEST_REQUEST_ID)
                .lastToken(null)
                .build();
        hipLinkingTokenDao.upsert(existingToken).toCompletableFuture().join();

        // Create ABHA patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create request
        HipWebhookController.OnGenerateTokenRequest request =
                HipWebhookController.OnGenerateTokenRequest.builder()
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .linkToken(TEST_LINK_TOKEN)
                        .response(HipWebhookController.OnGenerateTokenRequest.Response.builder()
                                .requestId(TEST_REQUEST_ID)
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/token/on-generate-token")
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Verify token was updated
        HIPLinkingToken updatedToken = hipLinkingTokenDao.getByRequestId(TEST_PATIENT_ID, TEST_REQUEST_ID)
                .toCompletableFuture().join();
        Optional<CareContext> careContext = getCareContext(updatedToken.getLastTokenRequestAppointmentId());
        assertEquals(TEST_LINK_TOKEN, updatedToken.getLastToken());

        // Verify care context linking was attempted
        assertEquals(1, abhaRestClient.linkCareContextCount);
        assertTrue(careContext.isPresent());
        assertNotNull(careContext.get().getLinkRequestId());
        assertEquals(TEST_PATIENT_ID, careContext.get().getPatientId());
        assertFalse(careContext.get().isLinked());
        assertFalse(careContext.get().isPatientNotified());
    }

    @Test
    public void testOnLinkCareContextSuccess() throws Exception {
        // Create ABHA patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create care context
        CareContext careContext = CareContext.builder()
                .appointmentID("123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .linkRequestId(TEST_REQUEST_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request
        HipWebhookController.OnLinkCareContextRequest request =
                HipWebhookController.OnLinkCareContextRequest.builder()
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .status("SUCCESS")
                        .response(HipWebhookController.OnLinkCareContextRequest.Response.builder()
                                .requestId(TEST_REQUEST_ID)
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/link/on_carecontext")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Verify care context was marked as linked
        Optional<CareContext> updatedCareContext = careContextDao.getByLinkRequestId(TEST_REQUEST_ID)
                .toCompletableFuture().join();
        assertTrue(updatedCareContext.isPresent());
        assertTrue(updatedCareContext.get().isLinked());
        assertEquals(1, abhaRestClient.sendDeepLinkNotificationCount);
    }

    @Test
    public void testOnLinkCareContextFailure() throws Exception {
        // Create ABHA patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create care context
        CareContext careContext = CareContext.builder()
                .appointmentID("123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .linkRequestId(TEST_REQUEST_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with error
        HipWebhookController.OnLinkCareContextRequest request =
                HipWebhookController.OnLinkCareContextRequest.builder()
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .status("FAILED")
                        .response(HipWebhookController.OnLinkCareContextRequest.Response.builder()
                                .requestId(TEST_REQUEST_ID)
                                .build())
                        .error(HipWebhookController.OnLinkCareContextRequest.Error.builder()
                                .code("ABDM-1038")
                                .message("ABHA address and Link token mismatch")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/link/on_carecontext")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Verify care context remains unlinked (error should be logged but not processed)
        Optional<CareContext> updatedCareContext = careContextDao.getByLinkRequestId(TEST_REQUEST_ID)
                .toCompletableFuture().join();
        assertTrue(updatedCareContext.isPresent());
        assertFalse(updatedCareContext.get().isLinked());
        assertEquals(0, abhaRestClient.sendDeepLinkNotificationCount);
    }

    @Test
    public void testOnSmsNotifySuccess() throws Exception {
        // Create care context with notify request ID
        CareContext careContext = CareContext.builder()
                .appointmentID(TEST_APPOINTMENT_ID)
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .linkRequestId(TEST_REQUEST_ID)
                .isLinked(true)
                .isPatientNotified(false)
                .notifyRequestId("notify-request-123")
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request
        HipWebhookController.OnSmsNotifyRequest request =
                HipWebhookController.OnSmsNotifyRequest.builder()
                        .acknowledgement(HipWebhookController.OnSmsNotifyRequest.Acknowledgement.builder()
                                .status("SUCCESS")
                                .build())
                        .response(HipWebhookController.OnSmsNotifyRequest.Response.builder()
                                .requestId("notify-request-123")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/patients/sms/on-notify")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Verify patient notification status was updated
        Optional<CareContext> updatedCareContext = careContextDao.getByNotifyRequestId("notify-request-123")
                .toCompletableFuture().join();
        assertTrue(updatedCareContext.isPresent());
        assertTrue(updatedCareContext.get().isPatientNotified());
    }

    @Test
    public void testOnSmsNotifyFailure() throws Exception {
        // Create care context with notify request ID
        CareContext careContext = CareContext.builder()
                .appointmentID(TEST_APPOINTMENT_ID)
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .linkRequestId(TEST_REQUEST_ID)
                .isLinked(true)
                .isPatientNotified(false)
                .notifyRequestId("notify-request-456")
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with error
        HipWebhookController.OnSmsNotifyRequest request =
                HipWebhookController.OnSmsNotifyRequest.builder()
                        .error(HipWebhookController.OnSmsNotifyRequest.Error.builder()
                                .code("ABDM-1024")
                                .message("Dependent service unavailable")
                                .build())
                        .response(HipWebhookController.OnSmsNotifyRequest.Response.builder()
                                .requestId("notify-request-456")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/patients/sms/on-notify")
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Verify patient notification status remains unchanged
        Optional<CareContext> updatedCareContext = careContextDao.getByNotifyRequestId("notify-request-456")
                .toCompletableFuture().join();
        assertTrue(updatedCareContext.isPresent());
        assertFalse(updatedCareContext.get().isPatientNotified());
    }

    @Test
    public void testDiscoverCareContextNoUnlinkedCareContext() throws Exception {
        // Create ABHA patient with no care contexts
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create request
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert no linkUserInitiatedCareContext call
        assertEquals(0, abhaRestClient.linkUserInitiatedCareContextCount);
    }

    @Test
    public void testDiscoverCareContextOneUnlinkedCareContextMatchedByAbhaAddress() throws Exception {
        // Create ABHA patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with one care context
        assertEquals(1, abhaRestClient.linkUserInitiatedCareContextCount);
        assertEquals(1, abhaRestClient.lastLinkUserInitiatedCareContextRequest.getPatient().get(0).getCareContexts().size());
        UserInitiatedLinking userInitiatedLinking = getUserInitiatedLinking("txn-123");
        assertEquals("AWAITING_LINKING_INITIATION", userInitiatedLinking.getStatus());
    }

    @Test
    public void testDiscoverCareContextZeroUnlinkedCareContextNotMatchedByMobile() throws Exception {
        // Create patient without ABHA address
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .abhaAddress(null)
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with mobile identifier
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .verifiedIdentifiers(List.of(
                                        HipWebhookController.UserInitiatedLinkingRequest.Identifier.builder()
                                                .type(HipWebhookController.UserInitiatedLinkingRequest.Type.MOBILE)
                                                .value("9876543211")
                                                .build()
                                ))
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with one care context
        assertEquals(0, abhaRestClient.linkUserInitiatedCareContextCount);
        Optional<UserInitiatedLinking> userInitiatedLinking = getUserInitiatedLinkingOptional("txn-123");
        assertFalse(userInitiatedLinking.isPresent());
    }

    @Test
    public void testDiscoverCareContextOneUnlinkedCareContextMatchedByMobileMatchGenderNotMatch() throws Exception {
        // Create patient without ABHA address
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(null)
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with mobile identifier and gender
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .gender("F")
                                .verifiedIdentifiers(List.of(
                                        HipWebhookController.UserInitiatedLinkingRequest.Identifier.builder()
                                                .type(HipWebhookController.UserInitiatedLinkingRequest.Type.MOBILE)
                                                .value("9876543210")
                                                .build()
                                ))
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with one care context
        assertEquals(0, abhaRestClient.linkUserInitiatedCareContextCount);
        Optional<UserInitiatedLinking> userInitiatedLinking = getUserInitiatedLinkingOptional("txn-123");
        assertFalse(userInitiatedLinking.isPresent());
    }

    @Test
    public void testDiscoverCareContextOneUnlinkedCareContextMatchedByMobileGenderAndAge() throws Exception {
        // Create patient without ABHA address
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .abhaAddress(null)
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with mobile identifier, gender and year within +/-5 years
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .gender("M")
                                .yearOfBirth(1992) // Within 5 years of 1990
                                .verifiedIdentifiers(List.of(
                                        HipWebhookController.UserInitiatedLinkingRequest.Identifier.builder()
                                                .type(HipWebhookController.UserInitiatedLinkingRequest.Type.MOBILE)
                                                .value("9876543210")
                                                .build()
                                ))
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with one care context
        assertEquals(1, abhaRestClient.linkUserInitiatedCareContextCount);
        assertEquals(1, abhaRestClient.lastLinkUserInitiatedCareContextRequest.getPatient().get(0).getCareContexts().size());
        UserInitiatedLinking userInitiatedLinking = getUserInitiatedLinking("txn-123");
        assertEquals("AWAITING_LINKING_INITIATION", userInitiatedLinking.getStatus());
    }

    @Test
    public void testDiscoverCareContextZeroUnlinkedCareContextMatchedByMobileGenderAndAgeDoesntMatch() throws Exception {
        // Create patient without ABHA address
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .abhaAddress(null)
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with mobile identifier, gender and year within +/-10 years
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .gender("M")
                                .yearOfBirth(1912) // Outside 10 years of 1990
                                .verifiedIdentifiers(List.of(
                                        HipWebhookController.UserInitiatedLinkingRequest.Identifier.builder()
                                                .type(HipWebhookController.UserInitiatedLinkingRequest.Type.MOBILE)
                                                .value("9876543210")
                                                .build()
                                ))
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with zero care context
        assertEquals(0, abhaRestClient.linkUserInitiatedCareContextCount);
        Optional<UserInitiatedLinking> userInitiatedLinking = getUserInitiatedLinkingOptional("txn-123");
        assertFalse(userInitiatedLinking.isPresent());
    }

    @Test
    public void testDiscoverCareContextOneUnlinkedCareContextMatchedByPhoneticNameSearch() throws Exception {
        // Create patient with similar name (phonetic match)
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Jhon Doe") // Phonetically similar to "John Doe"
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create unlinked care context
        CareContext careContext = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext).toCompletableFuture().join();

        // Create request with phonetically similar name
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id("unknown-abha@sbx")
                                .name("John Doe")
                                .gender("M")
                                .yearOfBirth(1990)
                                .verifiedIdentifiers(List.of(
                                        HipWebhookController.UserInitiatedLinkingRequest.Identifier.builder()
                                                .type(HipWebhookController.UserInitiatedLinkingRequest.Type.MOBILE)
                                                .value("9876543210")
                                                .build()
                                ))
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with one care context
        assertEquals(1, abhaRestClient.linkUserInitiatedCareContextCount);
        assertEquals(1, abhaRestClient.lastLinkUserInitiatedCareContextRequest.getPatient().get(0).getCareContexts().size());
        UserInitiatedLinking userInitiatedLinking = getUserInitiatedLinking("txn-123");
        assertEquals("AWAITING_LINKING_INITIATION", userInitiatedLinking.getStatus());
    }

    @Test
    public void testDiscoverCareContextTwoUnlinkedCareContextsPresent() throws Exception {
        // Create ABHA patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Create two unlinked care contexts
        CareContext careContext1 = CareContext.builder()
                .appointmentID("appt-123")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext1).toCompletableFuture().join();

        CareContext careContext2 = CareContext.builder()
                .appointmentID("appt-456")
                .healthFacilityId(testHealthFacilityID)
                .patientId(TEST_PATIENT_ID)
                .isLinked(false)
                .isPatientNotified(false)
                .build();
        careContextDao.upsert(careContext2).toCompletableFuture().join();

        // Create request
        HipWebhookController.UserInitiatedLinkingRequest request =
                HipWebhookController.UserInitiatedLinkingRequest.builder()
                        .transactionId("txn-123")
                        .patient(HipWebhookController.UserInitiatedLinkingRequest.Patient.builder()
                                .id(TEST_ABHA_ADDRESS)
                                .name("Test Patient")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/patient/care-context/discover")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isAccepted());

        // Assert one linkUserInitiatedCareContext call with two care contexts
        assertEquals(1, abhaRestClient.linkUserInitiatedCareContextCount);
        assertEquals(2, abhaRestClient.lastLinkUserInitiatedCareContextRequest.getPatient().get(0).getCareContexts().size());
    }

    @Test
    public void testConfirmCareContextLinkOtpCorrectNotExpiredOnePatient() throws Exception {
        // Setup patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Setup user initiated linking with one patient and valid OTP
        HipWebhookController.InitCareContextLinkRequest initRequest =
                HipWebhookController.InitCareContextLinkRequest.builder()
                        .transactionId("txn-123")
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .patient(List.of(
                                HipWebhookController.InitCareContextLinkRequest.PatientInfo.builder()
                                        .referenceNumber(TEST_PATIENT_ID)
                                        .display("Test Patient")
                                        .careContexts(List.of(
                                                HipWebhookController.InitCareContextLinkRequest.CareContextInfo.builder()
                                                        .referenceNumber(TEST_APPOINTMENT_ID)
                                                        .display("Prescription from " + testHealthFacilityID)
                                                        .build()
                                        ))
                                        .hiType("PRESCRIPTION")
                                        .count(1)
                                        .build()
                        ))
                        .build();

        UserInitiatedLinking userInitiatedLinking = UserInitiatedLinking.builder()
                .transactionId("txn-123")
                .patientId(TEST_PATIENT_ID)
                .status("AWAITING_OTP_CONFIRMATION")
                .linkReferenceNumber("link-ref-123")
                .otp("123456")
                .otpExpiryTime(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli())
                .initLinkRequest(initRequest)
                .build();
        userInitiatedLinkingDao.insert(userInitiatedLinking).toCompletableFuture().join();

        // Create confirm request with correct OTP
        HipWebhookController.ConfirmCareContextLinkRequest request =
                HipWebhookController.ConfirmCareContextLinkRequest.builder()
                        .confirmation(HipWebhookController.ConfirmCareContextLinkRequest.Confirmation.builder()
                                .linkRefNumber("link-ref-123")
                                .token("123456")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/link/care-context/confirm")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Assert confirmCareContextLinking count
        assertEquals(1, abhaRestClient.confirmCareContextLinking);

        // Assert body contains one patient
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest);
        assertEquals(1, abhaRestClient.lastConfirmCareContextLinkingRequest.getPatient().size());
        assertNull(abhaRestClient.lastConfirmCareContextLinkingRequest.getError());
    }

    @Test
    public void testConfirmCareContextLinkOtpCorrectNotExpiredTwoPatients() throws Exception {
        // Setup patients
        Patient patient1 = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient 1")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient1).toCompletableFuture().join();

        // Setup user initiated linking with two patients and valid OTP
        HipWebhookController.InitCareContextLinkRequest initRequest =
                HipWebhookController.InitCareContextLinkRequest.builder()
                        .transactionId("txn-123")
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .patient(List.of(
                                HipWebhookController.InitCareContextLinkRequest.PatientInfo.builder()
                                        .referenceNumber(TEST_PATIENT_ID)
                                        .display("Test Patient 1")
                                        .careContexts(List.of(
                                                HipWebhookController.InitCareContextLinkRequest.CareContextInfo.builder()
                                                        .referenceNumber(TEST_APPOINTMENT_ID)
                                                        .display("Prescription from " + testHealthFacilityID)
                                                        .build()
                                        ))
                                        .hiType("PRESCRIPTION")
                                        .count(1)
                                        .build(),
                                HipWebhookController.InitCareContextLinkRequest.PatientInfo.builder()
                                        .referenceNumber("test-patient-id-2")
                                        .display("Test Patient 2")
                                        .careContexts(List.of(
                                                HipWebhookController.InitCareContextLinkRequest.CareContextInfo.builder()
                                                        .referenceNumber("test-appointment-id-2")
                                                        .display("Prescription from " + testHealthFacilityID)
                                                        .build()
                                        ))
                                        .hiType("PRESCRIPTION")
                                        .count(1)
                                        .build()
                        ))
                        .build();

        UserInitiatedLinking userInitiatedLinking = UserInitiatedLinking.builder()
                .transactionId("txn-123")
                .patientId(TEST_PATIENT_ID)
                .status("AWAITING_OTP_CONFIRMATION")
                .linkReferenceNumber("link-ref-123")
                .otp("123456")
                .otpExpiryTime(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli())
                .initLinkRequest(initRequest)
                .build();
        userInitiatedLinkingDao.insert(userInitiatedLinking).toCompletableFuture().join();

        // Create confirm request with correct OTP
        HipWebhookController.ConfirmCareContextLinkRequest request =
                HipWebhookController.ConfirmCareContextLinkRequest.builder()
                        .confirmation(HipWebhookController.ConfirmCareContextLinkRequest.Confirmation.builder()
                                .linkRefNumber("link-ref-123")
                                .token("123456")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/link/care-context/confirm")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Assert confirmCareContextLinking count
        assertEquals(1, abhaRestClient.confirmCareContextLinking);

        // Assert body contains two patients
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest);
        assertEquals(2, abhaRestClient.lastConfirmCareContextLinkingRequest.getPatient().size());
        assertNull(abhaRestClient.lastConfirmCareContextLinkingRequest.getError());
    }

    @Test
    public void testConfirmCareContextLinkOtpExpired() throws Exception {
        // Setup patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Setup user initiated linking with expired OTP
        HipWebhookController.InitCareContextLinkRequest initRequest =
                HipWebhookController.InitCareContextLinkRequest.builder()
                        .transactionId("txn-123")
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .patient(List.of(
                                HipWebhookController.InitCareContextLinkRequest.PatientInfo.builder()
                                        .referenceNumber(TEST_PATIENT_ID)
                                        .display("Test Patient")
                                        .careContexts(List.of(
                                                HipWebhookController.InitCareContextLinkRequest.CareContextInfo.builder()
                                                        .referenceNumber(TEST_APPOINTMENT_ID)
                                                        .display("Prescription from " + testHealthFacilityID)
                                                        .build()
                                        ))
                                        .hiType("PRESCRIPTION")
                                        .count(1)
                                        .build()
                        ))
                        .build();

        UserInitiatedLinking userInitiatedLinking = UserInitiatedLinking.builder()
                .transactionId("txn-123")
                .patientId(TEST_PATIENT_ID)
                .status("AWAITING_OTP_CONFIRMATION")
                .linkReferenceNumber("link-ref-123")
                .otp("123456")
                .otpExpiryTime(Instant.now().minus(5, ChronoUnit.MINUTES).toEpochMilli()) // Expired
                .initLinkRequest(initRequest)
                .build();
        userInitiatedLinkingDao.insert(userInitiatedLinking).toCompletableFuture().join();

        // Create confirm request with correct OTP but expired
        HipWebhookController.ConfirmCareContextLinkRequest request =
                HipWebhookController.ConfirmCareContextLinkRequest.builder()
                        .confirmation(HipWebhookController.ConfirmCareContextLinkRequest.Confirmation.builder()
                                .linkRefNumber("link-ref-123")
                                .token("123456")
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/link/care-context/confirm")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Assert confirmCareContextLinking count (should still be called with error)
        assertEquals(1, abhaRestClient.confirmCareContextLinking);

        // Assert body contains error for expired OTP
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest);
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest.getError());
        assertEquals("900902", abhaRestClient.lastConfirmCareContextLinkingRequest.getError().getCode());
        assertEquals("OTP has expired. Please request a new OTP", abhaRestClient.lastConfirmCareContextLinkingRequest.getError().getMessage());
        assertTrue(abhaRestClient.lastConfirmCareContextLinkingRequest.getPatient().isEmpty());
    }

    @Test
    public void testConfirmCareContextLinkOtpWrongNotExpired() throws Exception {
        // Setup patient
        Patient patient = Patient.builder()
                .id(TEST_PATIENT_ID)
                .name("Test Patient")
                .mobileNo("9876543210")
                .gender("M")
                .dob(LocalDate.of(1990, 1, 1))
                .abhaAddress(TEST_ABHA_ADDRESS)
                .abhaNo("91536782361862")
                .build();
        patientDao.insert(patient).toCompletableFuture().join();

        // Setup user initiated linking with valid OTP
        HipWebhookController.InitCareContextLinkRequest initRequest =
                HipWebhookController.InitCareContextLinkRequest.builder()
                        .transactionId("txn-123")
                        .abhaAddress(TEST_ABHA_ADDRESS)
                        .patient(List.of(
                                HipWebhookController.InitCareContextLinkRequest.PatientInfo.builder()
                                        .referenceNumber(TEST_PATIENT_ID)
                                        .display("Test Patient")
                                        .careContexts(List.of(
                                                HipWebhookController.InitCareContextLinkRequest.CareContextInfo.builder()
                                                        .referenceNumber(TEST_APPOINTMENT_ID)
                                                        .display("Prescription from " + testHealthFacilityID)
                                                        .build()
                                        ))
                                        .hiType("PRESCRIPTION")
                                        .count(1)
                                        .build()
                        ))
                        .build();

        UserInitiatedLinking userInitiatedLinking = UserInitiatedLinking.builder()
                .transactionId("txn-123")
                .patientId(TEST_PATIENT_ID)
                .status("AWAITING_OTP_CONFIRMATION")
                .linkReferenceNumber("link-ref-123")
                .otp("123456")
                .otpExpiryTime(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli())
                .initLinkRequest(initRequest)
                .build();
        userInitiatedLinkingDao.insert(userInitiatedLinking).toCompletableFuture().join();

        // Create confirm request with wrong OTP
        HipWebhookController.ConfirmCareContextLinkRequest request =
                HipWebhookController.ConfirmCareContextLinkRequest.builder()
                        .confirmation(HipWebhookController.ConfirmCareContextLinkRequest.Confirmation.builder()
                                .linkRefNumber("link-ref-123")
                                .token("654321") // Wrong OTP
                                .build())
                        .build();

        // Execute request
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/link/care-context/confirm")
                .header("REQUEST-ID", TEST_REQUEST_ID)
                .header("TIMESTAMP", Instant.now().toString())
                .header("X-HIP-ID", testHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Assert confirmCareContextLinking count (should still be called with error)
        assertEquals(1, abhaRestClient.confirmCareContextLinking);

        // Assert body contains error for wrong OTP
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest);
        assertNotNull(abhaRestClient.lastConfirmCareContextLinkingRequest.getError());
        assertEquals("900901", abhaRestClient.lastConfirmCareContextLinkingRequest.getError().getCode());
        assertEquals("Invalid Credentials. Make sure you have provided the correct security credentials", abhaRestClient.lastConfirmCareContextLinkingRequest.getError().getMessage());
        assertTrue(abhaRestClient.lastConfirmCareContextLinkingRequest.getPatient().isEmpty());
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

    private Optional<CareContext> getCareContext(String appointmentId) {
        return careContextDao.get(appointmentId).toCompletableFuture().join();
    }

    private UserInitiatedLinking getUserInitiatedLinking(String transactionId) {
        return userInitiatedLinkingDao.getByTransactionId(transactionId).toCompletableFuture().join();
    }

    private Optional<UserInitiatedLinking> getUserInitiatedLinkingOptional(String transactionId) {
        try {
            return Optional.ofNullable(userInitiatedLinkingDao.getByTransactionId(transactionId).toCompletableFuture().join());
        } catch (Exception e) {
            if(e.getCause() instanceof EmptyResultDataAccessException) {
                return Optional.empty();
            }
            return Optional.empty();
        }
    }
}