package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.service.WsConnectionHandler;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import lombok.Getter;
import org.junit.After;
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

import jakarta.websocket.*;
import org.springframework.web.socket.PingMessage;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    @Autowired
    private WsConnectionHandler wsConnectionHandler;

    private Session facilityManagerSession;
    private Session doctorSession;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @After
    public void tearDown() throws Exception {
        if (facilityManagerSession != null && facilityManagerSession.isOpen()) {
            facilityManagerSession.close();
        }
        if (doctorSession != null && doctorSession.isOpen()) {
            doctorSession.close();
        }
    }

    @ClientEndpoint
    public static class TestWebSocketClient {
        private final CountDownLatch connectionLatch;
        private final List<String> receivedMessages;
        @Getter
        private Session session;

        public TestWebSocketClient(CountDownLatch connectionLatch, List<String> receivedMessages) {
            this.connectionLatch = connectionLatch;
            this.receivedMessages = receivedMessages;
        }

        public TestWebSocketClient() {
            this.connectionLatch = new CountDownLatch(1);
            this.receivedMessages = new ArrayList<>();
        }

        @OnOpen
        public void onOpen(Session session) {
            this.session = session;
            connectionLatch.countDown();
        }

        @OnMessage
        public void onMessage(String message) {
            receivedMessages.add(message);
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            fail("WebSocket error: " + throwable.getMessage());
        }

        @OnClose
        public void onClose(Session session, CloseReason closeReason) {
            // Connection closed
        }

        public boolean receivedMessage(String message) {
            return receivedMessages.contains(message);
        }
    }

    @Test
    public void testFacilityManagerWebSocketConnection() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        List<String> receivedMessages = new ArrayList<>();

        String facilityManagerToken = onboardFacilityManagerAndGetToken();

        URI uri = URI.create("ws://localhost:" + port + "/ws?token=" + facilityManagerToken);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        TestWebSocketClient client = new TestWebSocketClient(connectionLatch, receivedMessages);
        facilityManagerSession = container.connectToServer(client, uri);

        // Verify connection is established
        assertTrue("Facility Manager connection should be established within 10 seconds",
                connectionLatch.await(10, TimeUnit.SECONDS));
        assertTrue("Facility Manager session should be open", facilityManagerSession.isOpen());
    }

    @Test
    public void testDoctorWebSocketConnection() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        List<String> receivedMessages = new ArrayList<>();

        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);

        URI uri = URI.create("ws://localhost:" + port + "/ws?token=" + doctorToken);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        TestWebSocketClient client = new TestWebSocketClient(connectionLatch, receivedMessages);
        doctorSession = container.connectToServer(client, uri);

        // Verify connection is established
        assertTrue("Doctor connection should be established within 10 seconds",
                connectionLatch.await(10, TimeUnit.SECONDS));
        assertTrue("Doctor session should be open", doctorSession.isOpen());
    }

    @Test
    public void testWebSocketPingPong() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        List<String> receivedMessages = new ArrayList<>();

        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        URI uri = URI.create("ws://localhost:" + port + "/ws?token=" + facilityManagerToken);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        TestWebSocketClient client = new TestWebSocketClient(connectionLatch, receivedMessages);
        facilityManagerSession = container.connectToServer(client, uri);

        // Verify connection is established
        assertTrue("Connection should be established within 10 seconds",
                connectionLatch.await(10, TimeUnit.SECONDS));

        await()
        .atMost(20, TimeUnit.SECONDS)
        .until(() -> {
            Long lastPongTime = wsConnectionHandler.getLastPongTimestamp(HealthProfessional.builder().healthFacilityID(testHealthFacilityID).id(testHealthFacilityManagerID).build());
            return lastPongTime != null;
        });

    }

    @Test
    public void testMultipleUserConnections() throws Exception {
        CountDownLatch facilityManagerLatch = new CountDownLatch(1);
        CountDownLatch doctorLatch = new CountDownLatch(1);
        List<String> receivedMessages = new ArrayList<>();

        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        // Connect facility manager
        URI facilityManagerUri = URI.create("ws://localhost:" + port + "/ws?token=" + facilityManagerToken);
        TestWebSocketClient facilityManagerClient = new TestWebSocketClient(facilityManagerLatch, receivedMessages);
        facilityManagerSession = container.connectToServer(facilityManagerClient, facilityManagerUri);

        // Connect doctor
        URI doctorUri = URI.create("ws://localhost:" + port + "/ws?token=" + doctorToken);
        TestWebSocketClient doctorClient = new TestWebSocketClient(doctorLatch, new ArrayList<>());
        doctorSession = container.connectToServer(doctorClient, doctorUri);

        // Verify both connections are established
        assertTrue("Facility Manager connection should be established",
                facilityManagerLatch.await(10, TimeUnit.SECONDS));
        assertTrue("Doctor connection should be established",
                doctorLatch.await(10, TimeUnit.SECONDS));

        assertTrue("Facility Manager session should be open", facilityManagerSession.isOpen());
        assertTrue("Doctor session should be open", doctorSession.isOpen());
    }

    @ClientEndpoint
    public static class ErrorTestWebSocketClient {
        private final CountDownLatch errorLatch;

        public ErrorTestWebSocketClient(CountDownLatch errorLatch) {
            this.errorLatch = errorLatch;
        }

        @OnOpen
        public void onOpen(Session session) {
            fail("Connection should not be established without token");
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            errorLatch.countDown();
        }
    }

    @Test
    public void testUnauthorizedConnection() throws Exception {
        CountDownLatch errorLatch = new CountDownLatch(1);

        URI uri = URI.create("ws://localhost:" + port + "/ws"); // No token
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            ErrorTestWebSocketClient client = new ErrorTestWebSocketClient(errorLatch);
            container.connectToServer(client, uri);
        } catch (Exception e) {
            // Expected exception for unauthorized access
            errorLatch.countDown();
        }

        assertTrue("Should receive error for unauthorized access within 5 seconds",
                errorLatch.await(5, TimeUnit.SECONDS));
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
        // Onboard doctor
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

        // Get doctor token
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
}