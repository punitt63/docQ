package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.*;
import in.docq.health.facility.dao.*;
import in.docq.health.facility.model.*;
import in.docq.health.facility.service.AppointmentService;
import in.docq.health.facility.service.OPDService;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class PrescriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OPDDao opdDao;

    @Autowired
    private OPDService opdService;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private PrescriptionDAO prescriptionDAO;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    @Autowired
    private TestAbhaClientConfiguration.MockAbhaRestClient abhaRestClient;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private HIPLinkingTokenDao hipLinkingTokenDao;

    @Autowired
    private CareContextDao careContextDao;

    @Autowired
    private HipInitiatedLinkingDao hipInitiatedLinkingDao;

    private final String testPatientId = "test-patient-id";

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @Before
    public void beforeEach() {
        opdDao.truncate().toCompletableFuture().join();
        appointmentDao.truncate().toCompletableFuture().join();
        prescriptionDAO.truncate().toCompletableFuture().join();
        patientDao.truncate().toCompletableFuture().join();
        careContextDao.truncate().toCompletableFuture().join();
        abhaRestClient.linkCareContextCount = 0;
        abhaRestClient.sendDeepLinkNotificationCount = 0;
        abhaRestClient.generateLinkingTokenCount = 0;
    }

    @Test
    public void testCreateAndGetPrescriptionForNonAbhaPatient() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        Patient nonAbhaPatient = createNonAbhaPatient(facilityManagerToken);
        Appointment appointment = createAppointment(testOPD, facilityManagerToken, nonAbhaPatient.getId());
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointment.getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        Optional<CareContext> careContext = getCareContext(appointment.getUniqueId());
        Optional<HipInitiatedLinking> hipInitiatedLinking = getHipInitiatedLinking(appointment.getUniqueId());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointment.getId(), prescription.getAppointmentID().intValue());
        assertEquals("{}", prescription.getContent());
        assertEquals(1, abhaRestClient.sendDeepLinkNotificationCount);
        assertTrue(careContext.isPresent());
        assertEquals(nonAbhaPatient.getId(), careContext.get().getPatientId());
        assertNull(hipInitiatedLinking.get().getLinkRequestId());
        assertFalse(careContext.get().isLinked());
        assertFalse(hipInitiatedLinking.get().isPatientNotified());
    }

    @Test
    public void testCreateAndGetPrescriptionForAbhaPatientForFirstTime() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        Patient abhaPatient = createAbhaPatient(facilityManagerToken);
        Appointment appointment = createAppointment(testOPD, facilityManagerToken, abhaPatient.getId());
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointment.getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        Optional<HIPLinkingToken> hipLinkingToken = getHIPLinkingToken(testHealthFacilityID, abhaPatient.getId());
        Optional<CareContext> careContext = getCareContext(appointment.getUniqueId());
        Optional<HipInitiatedLinking> hipInitiatedLinking = getHipInitiatedLinking(appointment.getUniqueId());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointment.getId(), prescription.getAppointmentID().intValue());
        assertEquals("{}", prescription.getContent());
        assertEquals(1, abhaRestClient.generateLinkingTokenCount);
        assertTrue(hipLinkingToken.isPresent());
        assertTrue(careContext.isPresent());
        assertEquals(abhaPatient.getId(), careContext.get().getPatientId());
        assertNull(hipInitiatedLinking.get().getLinkRequestId());
        assertFalse(careContext.get().isLinked());
        assertFalse(hipInitiatedLinking.get().isPatientNotified());
    }

    @Test
    public void testCreateAndGetPrescriptionForAbhaPatientWithLinkingTokenNotExpired() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        Patient abhaPatient = createAbhaPatient(facilityManagerToken);
        createHIPLinkingToken(HIPLinkingToken.builder()
                .healthFacilityId(testHealthFacilityID)
                .patientId(abhaPatient.getId())
                .lastTokenRequestAppointmentId("test-appointment-id")
                .lastTokenRequestId("test-link-token-req-id")
                .lastToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3NTYwMDcyMzQsImV4cCI6NDk0MzIxNjgzNCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.AAFSH9G5t9jbWrfc9PN6rhVs40BthdHR01xEtwMHqyeTPtmgHf4-MLyYubAh9JNuHIpxYXIs718Dc1j94mVhuQ")
                .build());
        Appointment appointment = createAppointment(testOPD, facilityManagerToken, abhaPatient.getId());
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointment.getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        Optional<CareContext> careContext = getCareContext(appointment.getUniqueId());
        Optional<HipInitiatedLinking> hipInitiatedLinking = getHipInitiatedLinking(appointment.getUniqueId());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointment.getId(), prescription.getAppointmentID().intValue());
        assertEquals("{}", prescription.getContent());
        assertEquals(1, abhaRestClient.linkCareContextCount);
        assertTrue(careContext.isPresent());
        assertNotNull(hipInitiatedLinking.get().getLinkRequestId());
        assertEquals(abhaPatient.getId(), careContext.get().getPatientId());
        assertFalse(careContext.get().isLinked());
        assertFalse(hipInitiatedLinking.get().isPatientNotified());
    }

    @Test
    public void testCreatePrescriptionUnAuthByFacilityManager() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 1);
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().is(401));
    }

    @Test
    public void testReplacePrescription() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        Patient nonAbhaPatient = createNonAbhaPatient(facilityManagerToken);
        Appointment appointment = createAppointment(testOPD, facilityManagerToken, nonAbhaPatient.getId());
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        handleAsyncProcessing(mockMvc.perform(put("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{\"key\":\"value\"}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointment.getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointment.getId(), prescription.getAppointmentID().intValue());
        assertEquals("{\"key\": \"value\"}", prescription.getContent());
    }

    private List<Appointment> createAppointments(OPD testOPD, String facilityManagerToken, int appointmentCounts) throws Exception {
        List<CompletableFuture<ResultActions>> list = new ArrayList<>();
        for(int i = 1;i <= appointmentCounts;i++) {
            int finalI = i;
            list.add(CompletableFuture.supplyAsync(() -> {
                try {
                    return handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                            .header("Authorization", "Bearer " + facilityManagerToken)
                            .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(testPatientId + "-" + finalI).build()))
                            .contentType(MediaType.APPLICATION_JSON)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))
                .join();
        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        return appointments;
    }

    private Appointment createAppointment(OPD testOPD, String facilityManagerToken, String patientId) throws Exception {
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(patientId).build()))
                .contentType(MediaType.APPLICATION_JSON)));

        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("patient-id", patientId)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        return appointments.get(0);
    }

    private String onboardDoctorAndGetToken(String facilityManagerToken) throws Exception {
        //onboard
        HealthProfessionalController.OnBoardHealthProfessionalRequestBody requestBody = HealthProfessionalController.OnBoardHealthProfessionalRequestBody.builder()
                .type(HealthProfessionalType.DOCTOR)
                .healthProfessionalID(testDoctorID)
                .password("test-doc-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/onboard")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        // get token
        HealthProfessionalController.LoginHealthProfessionalRequestBody loginHealthProfessionalRequestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-doc-pass")
                .build();
        MockHttpServletResponse mockHttpServletResponse = handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/login")
                .content(gson.toJson(loginHealthProfessionalRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse();
        return gson.fromJson(mockHttpServletResponse.getContentAsString(), HealthProfessionalController.LoginResponse.class)
                .getAccessToken();
    }

    private String onboardFacilityManagerAndGetToken() throws Exception {
        String adminUserToken = getAdminUserToken();
        HealthProfessionalController.OnBoardHealthProfessionalRequestBody onBoardFacilityManagerRequestBody = HealthProfessionalController.OnBoardHealthProfessionalRequestBody.builder()
                .type(HealthProfessionalType.FACILITY_MANAGER)
                .healthProfessionalID(testHealthFacilityManagerID)
                .password("test-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/onboard")
                .header("Authorization", "Bearer " + adminUserToken)
                .content(gson.toJson(onBoardFacilityManagerRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn();

        return getFacilityManagerToken();
    }

    private String getFacilityManagerToken() throws Exception {
        HealthProfessionalController.LoginHealthProfessionalRequestBody requestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-pass")
                .build();
        MockHttpServletResponse mockHttpServletResponse = handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testHealthFacilityManagerID + "/login")
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse();
        return gson.fromJson(mockHttpServletResponse.getContentAsString(), HealthProfessionalController.LoginResponse.class)
                .getAccessToken();
    }

    private String getAdminUserToken() {
        return desktopKeycloakRestClient.getUserAccessToken("docq-admin", "xf~8QgK^]gw@,")
                .thenApply(GetAccessToken200Response::getAccessToken)
                .toCompletableFuture().join();
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

    private OPD createTestOPD(int maxSlots) {
        LocalDate currDate = LocalDate.now();
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate(currDate.plusDays(1).toString())
                .endDate(currDate.plusDays(1).plusWeeks(1).toString())
                .minutesToActivate(60 * 24)
                .maxSlots(maxSlots)
                .minutesPerSlot(5)
                .scheduleType(OPD.ScheduleType.WEEKLY)
                .weeklyTemplate(Arrays.asList(true, true, true, true, true, false, false))
                .build();
        opdService.insert(testHealthFacilityID, testDoctorID, createOPDRequestBody).toCompletableFuture().join();
        return opdService.list(testHealthFacilityID, testDoctorID, currDate.plusDays(1), currDate.plusDays(1).plusWeeks(1))
                .toCompletableFuture().join()
                .get(0);
    }

    private Patient createNonAbhaPatient(String facilityManagerToken) throws Exception {
        PatientController.CreatePatientRequestBody createPatientRequestBody = PatientController.CreatePatientRequestBody.builder()
                .name("NonAbhaTestPatient")
                .mobileNo("9876543210")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("M")
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/patients")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createPatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        List<Patient> patients = gson.fromJson(
                handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/patients")
                        .header("Authorization", "Bearer " + facilityManagerToken)
                        .param("mobile-no", "9876543210")
                        .contentType(MediaType.APPLICATION_JSON)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeToken<List<Patient>>(){}.getType());
        return patients.get(0);
    }

    private Patient createAbhaPatient(String facilityManagerToken) throws Exception {
        PatientController.CreatePatientRequestBody createPatientRequestBody = PatientController.CreatePatientRequestBody.builder()
                .name("AbhaTestPatient")
                .mobileNo("9876543210")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("M")
                .abhaAddress("test-abha@sbx")
                .abhaNo("91536782361862")
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/patients")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createPatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        List<Patient> patients = gson.fromJson(
                handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/patients")
                        .header("Authorization", "Bearer " + facilityManagerToken)
                        .param("mobile-no", "9876543210")
                        .contentType(MediaType.APPLICATION_JSON)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeToken<List<Patient>>(){}.getType());
        return patients.get(0);
    }

    private Patient getPatient(String patientId) {
        return patientDao.get(patientId).toCompletableFuture().join();
    }

    private Optional<HIPLinkingToken> getHIPLinkingToken(String healthFacilityId, String patientId) {
        return hipLinkingTokenDao.get(healthFacilityId, patientId).toCompletableFuture().join();
    }

    private HIPLinkingToken createHIPLinkingToken(HIPLinkingToken hipLinkingToken) {
        hipLinkingTokenDao.upsert(hipLinkingToken).toCompletableFuture().join();
        return hipLinkingToken;
    }

    private Optional<CareContext> getCareContext(String appointmentId) {
        return careContextDao.get(appointmentId).toCompletableFuture().join();
    }

    private Optional<HipInitiatedLinking> getHipInitiatedLinking(String appointmentId) {
        return hipInitiatedLinkingDao.getByAppointmentId(appointmentId).toCompletableFuture().join();
    }
}
