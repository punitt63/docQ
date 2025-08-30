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
import in.docq.health.facility.model.CareContext;
import in.docq.health.facility.model.HIPLinkingToken;
import in.docq.health.facility.model.Patient;
import org.junit.Before;
import org.junit.Test;
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

import java.time.LocalDate;
import java.util.Optional;
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
    private TestAbhaClientConfiguration.MockAbhaRestClient abhaRestClient;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter())
            .create();

    private static final String TEST_ABHA_ADDRESS = "test-abha@sbx";
    private static final String TEST_REQUEST_ID = "test-request-id";
    private static final String TEST_LINK_TOKEN = "test-link-token";
    private static final String TEST_PATIENT_ID = "test-patient-id";
    private static final String TEST_APPOINTMENT_ID = "test-appointment-id";

    @Before
    public void setUp() {
        hipLinkingTokenDao.truncate().toCompletableFuture().join();
        patientDao.truncate().toCompletableFuture().join();
        careContextDao.truncate().toCompletableFuture().join();
        abhaRestClient.linkCareContextCount = 0;
        abhaRestClient.sendDeepLinkNotificationCount = 0;
        abhaRestClient.generateLinkingTokenCount = 0;
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
}