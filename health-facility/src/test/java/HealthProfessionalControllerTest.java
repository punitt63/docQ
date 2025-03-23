import com.google.gson.Gson;
import configuration.TestAbhaClientConfiguration;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.BackendKeyCloakRestClient;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.exception.ErrorCodes;
import in.docq.health.facility.exception.ErrorResponse;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class HealthProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HealthProfessionalDao healthProfessionalDao;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private static Gson gson = new Gson();

    @Before
    public void beforeEach() {
        healthProfessionalDao.truncate().toCompletableFuture().join();
    }

    @Test
    public void testFacilityManagerOnBoarding() throws Exception {
        String adminUserToken = getAdminUserToken();
        HealthProfessionalController.OnBoardHealthProfessionalRequestBody requestBody = HealthProfessionalController.OnBoardHealthProfessionalRequestBody.builder()
                        .type(HealthProfessionalType.FACILITY_MANAGER)
                        .healthProfessionalID(testHealthFacilityManagerID)
                        .password("test-pass")
                        .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/onboard")
                .header("Authorization", "Bearer " + adminUserToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFacilityManagerLogin() throws Exception {
        HealthProfessionalController.LoginHealthProfessionalRequestBody requestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testHealthFacilityManagerID + "/login")
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFacilityManagerLoginWithWrongPassword() throws Exception {
        HealthProfessionalController.LoginHealthProfessionalRequestBody requestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-pass-1")
                .build();
        ErrorResponse expectedErrorResponse = new ErrorResponse(ErrorCodes.INVALID_USER_CREDENTIALS);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testHealthFacilityManagerID + "/login")
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().is(401))
                .andExpect(content().json(gson.toJson(expectedErrorResponse)));
    }

    @Test
    public void testFacilityManagerLoginWithWrongUsername() throws Exception {
        HealthProfessionalController.LoginHealthProfessionalRequestBody requestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-pass")
                .build();
        ErrorResponse expectedErrorResponse = new ErrorResponse(ErrorCodes.INVALID_USER_CREDENTIALS);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/random/login")
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().is(401))
                .andExpect(content().json(gson.toJson(expectedErrorResponse)));
    }

    @Test
    public void testDoctorOnBoarding() throws Exception {
        // onboard facility manager
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

        String facilityManagerToken = getFacilityManagerToken();
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
