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
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.service.AppointmentService;
import in.docq.health.facility.service.OPDService;
import in.docq.health.facility.service.WsConnectionHandler;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class AppointmentControllerTest {

    @LocalServerPort
    private int port;

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

    private WebSocketIntegrationTest.TestWebSocketClient facilityManagerWsClient;
    private WebSocketIntegrationTest.TestWebSocketClient doctorSessionWsClient;

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
    public void testCreate100AppointmentConcurrentlyInTwoOPDs() throws Exception {
        OPD testOPD = createTestOPD(100);
        OPD secondTestOPD = getSecondTestOPD(100);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();

        // 1st OPD 100 appointments
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

        // 2nd OPD 100 appointments
        for(int i = 1;i <= 100;i++) {
            int finalI = i;
            list.add(CompletableFuture.supplyAsync(() -> {
                try {
                    return handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + secondTestOPD.getDate().toString() + "/opds/" + secondTestOPD.getId() + "/appointments")
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

        // assert 1st OPD appointments
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

        // assert 2nd OPD appointments
        assertEquals(100, opdService.get(secondTestOPD.getDate(), secondTestOPD.getId())
                .toCompletableFuture().join()
                .getAppointmentsCount());
        Set<Integer> sset = new HashSet<>();
        for(int i = 1;i <= 100;i++) {
            List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/appointments")
                    .header("Authorization", "Bearer " + facilityManagerToken)
                    .param("start-opd-date", secondTestOPD.getDate().toString())
                    .param("end-opd-date", secondTestOPD.getDate().toString())
                    .param("opd-id", secondTestOPD.getId())
                    .param("patient-id", testPatientId + "-" + i)
                    .contentType(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
            sset.add(appointments.get(0).getId());
        }
        for(int i = 1;i <=100;i++) {
            assertTrue("id " + i + " not present ", sset.contains(i));
        }
    }

    @Test
    public void testTopAppointmentWhenInProgress() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        onboardDoctorAndGetToken(facilityManagerToken);
        OPD testOPD = createTestOPD(3);
        createAppointments(testOPD, facilityManagerToken, 3);
        Appointment firstAppointment = getTopAppointment(facilityManagerToken, testOPD);
        assertEquals(1, firstAppointment.getId());
        startAppointment(facilityManagerToken, firstAppointment);
        assertEquals(1, getTopAppointment(facilityManagerToken, testOPD).getId());
        awaitWsStateChangeMessage(doctorSessionWsClient, firstAppointment, Appointment.State.WAITING, Appointment.State.IN_PROGRESS);
    }

    private void awaitWsStateChangeMessage(WebSocketIntegrationTest.TestWebSocketClient webSocketClient, Appointment firstAppointment, Appointment.State from, Appointment.State to) {
        WsConnectionHandler.StateChangeMessage stateChangeMessage = WsConnectionHandler.StateChangeMessage.builder()
                .objectId(firstAppointment.getUniqueId())
                .objectType("APPOINTMENT")
                .fromState(from.name())
                .toState(to.name())
                .build();
        await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> webSocketClient.receivedMessage(gson.toJson(stateChangeMessage)));
    }

    // 3 appointments
    // Getting Completed one by one
    @Test
    public void testCompletingAllAppointments() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        onboardDoctorAndGetToken(facilityManagerToken);
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
        HealthProfessionalController.OnBoardFacilityManagerRequestBody requestBody = HealthProfessionalController.OnBoardFacilityManagerRequestBody.builder()
                .facilityManagerID(testHealthFacilityManagerID)
                .healthProfessionalID(testHealthFacilityManagerID)
                .healthProfessionalName("Ms. Emily Davis")
                .healthFacilityName("City General Hospital")
                .stateCode(testStateCode)
                .districtCode(testDistrictCode)
                .speciality("Facility Manager")
                .address("123 Main Street, City Center")
                .pincode("123456")
                .latitude(12.9716)
                .longitude(77.5946)
                .password("test-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/facility-manager/onboard")
                .header("Authorization", "Bearer " + adminUserToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

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
        String token = gson.fromJson(mockHttpServletResponse.getContentAsString(), HealthProfessionalController.LoginResponse.class)
                .getAccessToken();
        facilityManagerWsClient = createWsConnection(token);
        return token;
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

    private OPD getSecondTestOPD(int maxSlots) {
        LocalDate currDate = LocalDate.now();
        return opdService.list(testHealthFacilityID, testDoctorID, currDate.plusDays(1), currDate.plusDays(1).plusWeeks(1))
                .toCompletableFuture().join()
                .get(1);
    }

    private WebSocketIntegrationTest.TestWebSocketClient createWsConnection(String token) throws DeploymentException, IOException {
        URI uri = URI.create("ws://localhost:" + port + "/ws?token=" + token);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketIntegrationTest.TestWebSocketClient client = new WebSocketIntegrationTest.TestWebSocketClient();
        container.connectToServer(client, uri);
        return client;
    }

    private String onboardDoctorAndGetToken(String facilityManagerToken) throws Exception {
        //onboard
        HealthProfessionalController.OnBoardDoctorRequestBody requestBody = HealthProfessionalController.OnBoardDoctorRequestBody.builder()
                .doctorID(testDoctorID)
                .password("test-doc-pass")
                .facilityManagerID(testHealthFacilityManagerID)
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/doctor/onboard")
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
        String token = gson.fromJson(mockHttpServletResponse.getContentAsString(), HealthProfessionalController.LoginResponse.class)
                .getAccessToken();
        doctorSessionWsClient = createWsConnection(token);
        return token;
    }
}
