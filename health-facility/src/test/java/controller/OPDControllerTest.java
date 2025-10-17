package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.controller.OPDController;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.service.WsConnectionHandler;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import jakarta.websocket.*;
import org.hamcrest.Matchers;
import org.junit.After;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class OPDControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OPDDao opdDao;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    private WebSocketIntegrationTest.TestWebSocketClient facilityManagerWsClient;
    private WebSocketIntegrationTest.TestWebSocketClient doctorSessionWsClient;

    @Before
    public void beforeEach() {
        opdDao.truncate().toCompletableFuture().join();
    }

    @After
    public void tearDown() throws Exception {
        if(facilityManagerWsClient != null && facilityManagerWsClient.getSession() != null && facilityManagerWsClient.getSession().isOpen()) {
            facilityManagerWsClient.getSession().close();
        }
        if(doctorSessionWsClient != null && doctorSessionWsClient.getSession() != null && doctorSessionWsClient.getSession().isOpen()) {
            doctorSessionWsClient.getSession().close();
        }
    }

    @Test
    public void testCreateOPDByFacilityManager() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        LocalDate currDate = LocalDate.now();
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate(currDate.plusDays(1).toString())
                .endDate(currDate.plusDays(1).plusYears(1).toString())
                .minutesToActivate(60 * 24)
                .maxSlots(50)
                .minutesPerSlot(5)
                .scheduleType(OPD.ScheduleType.WEEKLY)
                .weeklyTemplate(Arrays.asList(true, true, true, true, true, false, false))
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opds")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        for(LocalDate date = currDate.plusDays(1);date.isBefore(currDate.plusYears(1).plusDays(2));date = date.plusDays(1)) {
            validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, date, date, OPD.getDaysMatchingWeeklyTemplate(date, date, createOPDRequestBody.getWeeklyTemplate()).size());
        }
    }

    @Test
    public void testUpdateOPDByFacilityManager() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        onboardDoctorAndGetToken(facilityManagerToken);
        createTestOPDs(facilityManagerToken);
        List<OPD> opds = getOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.now().plusDays(1), LocalDate.now().plusMonths(1));
        OPDController.UpdateOPDRequestBody updateOPDRequestBody = OPDController.UpdateOPDRequestBody.builder()
                .startHour(10)
                .endHour(13)
                .startMinute(30)
                .endMinute(0)
                .minutesToActivate(60 * 24 * 2)
                .maxSlots(51)
                .minutesPerSlot(6)
                .state(OPD.State.ACTIVE)
                .build();
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opd-dates/" + opds.get(0).getDate().toString() + "/opds/" + opds.get(0).getId())
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(updateOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test-opd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startHour").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endHour").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startMinute").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endMinute").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(opds.get(0).getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activateTime").value(opds.get(0).getDate().atStartOfDay(ZoneId.of("Asia/Kolkata")).plusHours(10).plusMinutes(30).minusMinutes(60 * 24 * 2).toInstant().toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxSlots").value(51))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minutesPerSlot").value(6));
        WsConnectionHandler.StateChangeMessage stateChangeMessage = WsConnectionHandler.StateChangeMessage.builder()
                .objectId(opds.get(0).getId())
                .objectType("OPD")
                .fromState(OPD.State.INACTIVE.name())
                .toState(OPD.State.ACTIVE.name())
                .build();
        await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> doctorSessionWsClient.receivedMessage(gson.toJson(stateChangeMessage)));
    }

    @Test
    public void testUpdateOPDByDoctor() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        createTestOPDs(facilityManagerToken);
        List<OPD> opds = getOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.now().plusDays(1), LocalDate.now().plusMonths(1));
        OPDController.UpdateOPDRequestBody updateOPDRequestBody = OPDController.UpdateOPDRequestBody.builder()
                .startHour(10)
                .endHour(13)
                .startMinute(30)
                .endMinute(0)
                .minutesToActivate(60 * 24 * 2)
                .maxSlots(51)
                .minutesPerSlot(6)
                .state(OPD.State.ACTIVE)
                .build();
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opd-dates/" + opds.get(0).getDate().toString() + "/opds/" + opds.get(0).getId())
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(updateOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test-opd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startHour").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endHour").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startMinute").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endMinute").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(opds.get(0).getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activateTime").value(opds.get(0).getDate().atStartOfDay(ZoneId.of("Asia/Kolkata")).plusHours(10).plusMinutes(30).minusMinutes(60 * 24 * 2).toInstant().toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxSlots").value(51))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minutesPerSlot").value(6));
        WsConnectionHandler.StateChangeMessage stateChangeMessage = WsConnectionHandler.StateChangeMessage.builder()
                .objectId(opds.get(0).getId())
                .objectType("OPD")
                .fromState(OPD.State.INACTIVE.name())
                .toState(OPD.State.ACTIVE.name())
                .build();
        await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> facilityManagerWsClient.receivedMessage(gson.toJson(stateChangeMessage)));
    }

    @Test
    public void testGetOPDByDoctor() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        createTestOPDs(facilityManagerToken);
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        List<OPD> opds = getOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.now().plusDays(1), LocalDate.now().plusMonths(1));
        validateOPDs(doctorToken, testHealthFacilityID, testDoctorID, opds.get(0).getDate(), opds.get(0).getDate(), 1);
    }

    @Test
    public void testCreateOPDByDoctorThrowsUnAuthorized() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate("2025-03-31")
                .endDate("2026-03-31")
                .minutesToActivate(60 * 24)
                .maxSlots(50)
                .minutesPerSlot(5)
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opds")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(createOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().is(401));
    }

    private void validateOPDs(String token, String healthFacilityID, String healthProfessionalID, LocalDate startDate, LocalDate endDate, int expectedCount) throws Exception {
        handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + healthFacilityID + "/health-facility-professionals/opds")
                .param("health-facility-professional-id", healthProfessionalID)
                .param("start-date", startDate.toString())
                .param("end-date", endDate.toString())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expectedCount));
    }

    private List<OPD> getOPDs(String facilityManagerToken, String healthFacilityID, String healthProfessionalID, LocalDate startDate, LocalDate endDate) throws Exception {
        return gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + healthFacilityID + "/health-facility-professionals/opds")
                .param("health-facility-professional-id", healthProfessionalID)
                .param("start-date", startDate.toString())
                .param("end-date", endDate.toString())
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<OPD>>(){}.getType());
    }

    private void createTestOPDs(String facilityManagerToken) throws Exception {
        LocalDate currDate = LocalDate.now();
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate(currDate.plusDays(1).toString())
                .endDate(currDate.plusDays(1).plusYears(1).toString())
                .minutesToActivate(60 * 24)
                .maxSlots(50)
                .minutesPerSlot(5)
                .scheduleType(OPD.ScheduleType.WEEKLY)
                .weeklyTemplate(Arrays.asList(true, true, true, true, true, false, false))
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opds")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private String onboardFacilityManagerAndGetToken() throws Exception {
        String adminUserToken = getAdminUserToken();
        HealthProfessionalController.OnBoardFacilityManagerRequestBody requestBody = HealthProfessionalController.OnBoardFacilityManagerRequestBody.builder()
                .facilityManagerID(testHealthFacilityManagerID)
                .password("test-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/facility-manager/onboard")
                .header("Authorization", "Bearer " + adminUserToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        return getFacilityManagerToken();
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

    private WebSocketIntegrationTest.TestWebSocketClient createWsConnection(String token) throws DeploymentException, IOException {
        URI uri = URI.create("ws://localhost:" + port + "/ws?token=" + token);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketIntegrationTest.TestWebSocketClient client = new WebSocketIntegrationTest.TestWebSocketClient();
        container.connectToServer(client, uri);
        return client;
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
}
