package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.AppointmentController;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.controller.OPDController;
import in.docq.health.facility.dao.AppointmentDao;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.service.AppointmentService;
import in.docq.health.facility.service.OPDService;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OPDDao opdDao;

    @Autowired
    private OPDService opdService;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private final String testPatientId = "test-patient-id";

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @Before
    public void beforeEach() {
        opdDao.truncate().toCompletableFuture().join();
        appointmentDao.truncate().toCompletableFuture().join();
    }

    @Test
    public void testCreateAndGetAppointment() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(testPatientId).build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("patient-id", testPatientId)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        assertEquals(1, appointments.get(0).getId());
    }

    @Test
    public void testCreateAppointmentBreachViolation() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(testPatientId).build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(testPatientId + "-random").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreate100AppointmentConcurrently() throws Exception {
        OPD testOPD = createTestOPD(100);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        List<CompletableFuture<ResultActions>> list = new ArrayList<>();
        for(int i = 1;i <= 100;i++) {
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
        assertEquals(100, opdService.get(testOPD.getDate(), testOPD.getId())
                .toCompletableFuture().join()
                .getAppointmentsCount());
        Set<Integer> set = new HashSet<>();
        for(int i = 1;i <= 100;i++) {
            List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                    .header("Authorization", "Bearer " + facilityManagerToken)
                    .param("start-opd-date", testOPD.getDate().toString())
                    .param("end-opd-date", testOPD.getDate().toString())
                    .param("opd-id", testOPD.getId())
                    .param("patient-id", testPatientId + "-" + i)
                    .contentType(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
            set.add(appointments.get(0).getId());
        }
        for(int i = 1;i <=100;i++) {
            assertTrue("id " + i + " not present ", set.contains(i));
        }
    }

    @Test
    public void testTopAppointmentWhenInProgress() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        createAppointments(testOPD, facilityManagerToken, 3);
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        assertEquals(1, getTopAppointment(facilityManagerToken, testOPD).getId());
    }

    // 3 appointments
    // Getting Completed one by one
    @Test
    public void testCompletingAllAppointments() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        createAppointments(testOPD, facilityManagerToken, 3);
        // 1st Appointment
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        completeAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment
        Appointment secondAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(2, secondAppointment.getId());
        startAppointment(facilityManagerToken, secondAppointment);
        completeAppointment(facilityManagerToken, secondAppointment);
        // 3rd Appointment
        Appointment thirdAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(3, thirdAppointment.getId());
        startAppointment(facilityManagerToken, thirdAppointment);
        completeAppointment(facilityManagerToken, thirdAppointment);
        assertAllAppointmentsInTerminalState(facilityManagerToken, testOPD);
    }

    // 3 appointments
    // first appointmentCancelled
    // remaining two start and complete
    @Test
    public void testCompletingThreeAppointmentsWithFirstAppointmentCancelled() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        createAppointments(testOPD, facilityManagerToken, 3);
        // 1st Appointment Cancelled
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        cancelAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment Becomes First
        Appointment secondAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(2, secondAppointment.getId());
        startAppointment(facilityManagerToken, secondAppointment);
        completeAppointment(facilityManagerToken, secondAppointment);
        // 3rd Appointment Becomes Second
        Appointment thirdAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(3, thirdAppointment.getId());
        startAppointment(facilityManagerToken, thirdAppointment);
        completeAppointment(facilityManagerToken, thirdAppointment);
        assertAllAppointmentsInTerminalState(facilityManagerToken, testOPD);
    }

    // 3 appointments
    // 1 appointment in progress
    // 2 no show
    // 1 appointment complete
    // 3 appointment start
    // 2 appointment move to waiting
    // 3 appointment complete
    // 2 appointment start
    // 2 appointment complete
    @Test
    public void testNoShowCaseWithAlreadyInProgressAppointment() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 3);
        // 1st Appointment
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment No Show
        noShowAppointment(facilityManagerToken, appointments.get(1));
        completeAppointment(facilityManagerToken, firstAppointment);
        // 3rd Appointment is Top Appointment
        Appointment thirdAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(3, thirdAppointment.getId());
        startAppointment(facilityManagerToken, thirdAppointment);
        // 2nd Appointment arrives
        noShowToWaitingAppointment(facilityManagerToken, appointments.get(1));
        completeAppointment(facilityManagerToken, thirdAppointment);
        // 2nd Appointment is top
        Appointment secondAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(2, secondAppointment.getId());
        startAppointment(facilityManagerToken, secondAppointment);
        completeAppointment(facilityManagerToken, secondAppointment);
    }

    // 3 appointments
    // 1 appointment in progress
    // 2 no show
    // 2 appointment move to waiting
    // 1 appointment complete
    // 3 appointment start
    // 3 appointment complete
    @Test
    public void testCustomNoShowCase1() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 3);
        // 1st Appointment
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment No Show And Complete
        noShowAppointment(facilityManagerToken, appointments.get(1));
        noShowToWaitingAppointment(facilityManagerToken, appointments.get(1));
        completeAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment is Top Appointment
        Appointment secondAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(2, secondAppointment.getId());
        startAppointment(facilityManagerToken, secondAppointment);
        completeAppointment(facilityManagerToken, secondAppointment);
        // 3nd Appointment is top
        Appointment thirdAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(3, thirdAppointment.getId());
        startAppointment(facilityManagerToken, thirdAppointment);
        completeAppointment(facilityManagerToken, thirdAppointment);
    }

    // 3 appointments
    // 1 appointment in progress
    // 2 no show
    // 3 no show
    // 3 move to waiting
    // 2 move to waiting
    // 1 appointment complete
    // 3 appointment start
    // 3 appointment complete
    // 2 appointment start
    // 2 appointment complete
    @Test
    public void testCustomNoShowCase2() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPD testOPD = createTestOPD(3);
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 3);
        // 1st Appointment
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment No Show
        noShowAppointment(facilityManagerToken, appointments.get(1));
        noShowToWaitingAppointment(facilityManagerToken, appointments.get(1));
        completeAppointment(facilityManagerToken, firstAppointment);
        // 2nd Appointment is Top Appointment
        Appointment secondAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(2, secondAppointment.getId());
        startAppointment(facilityManagerToken, secondAppointment);
        completeAppointment(facilityManagerToken, secondAppointment);
        // 3nd Appointment is top
        Appointment thirdAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(3, thirdAppointment.getId());
        startAppointment(facilityManagerToken, thirdAppointment);
        completeAppointment(facilityManagerToken, thirdAppointment);
    }

    private void assertAllAppointmentsInTerminalState(String facilityManagerToken, OPD testOPD) throws Exception {
        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("state", "WAITING")
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        assertTrue("all Appointments Not In Terminal State", appointments.isEmpty());
    }

    private Appointment getTopAppointment(String facilityManagerToken, OPD testOPD) throws Exception {
        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("state", "WAITING, IN_PROGRESS")
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        return appointments.get(0);
    }

    private void startAppointment(String facilityManagerToken, Appointment appointment) throws Exception {
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + appointment.getOpdDate().toString() + "/opds/" + appointment.getOpdId() + "/appointments/" + appointment.getId() + "/start")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void completeAppointment(String facilityManagerToken, Appointment appointment) throws Exception {
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + appointment.getOpdDate().toString() + "/opds/" + appointment.getOpdId() + "/appointments/" + appointment.getId() + "/complete")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void cancelAppointment(String facilityManagerToken, Appointment appointment) throws Exception {
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + appointment.getOpdDate().toString() + "/opds/" + appointment.getOpdId() + "/appointments/" + appointment.getId() + "/cancel")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void noShowAppointment(String facilityManagerToken, Appointment appointment) throws Exception {
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + appointment.getOpdDate().toString() + "/opds/" + appointment.getOpdId() + "/appointments/" + appointment.getId() + "/no-show")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void noShowToWaitingAppointment(String facilityManagerToken, Appointment appointment) throws Exception {
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + appointment.getOpdDate().toString() + "/opds/" + appointment.getOpdId() + "/appointments/" + appointment.getId() + "/no-show-to-waiting")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
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
}
