import com.google.gson.Gson;
import configuration.TestAbhaClientConfiguration;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.model.HealthProfessionalType;
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

import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testHealthFacilityID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class HealthProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson = new Gson();

    @Test
    public void testFacilityManagerOnBoarding() throws Exception {
        HealthProfessionalController.OnBoardHealthProfessionalRequestBody requestBody = HealthProfessionalController.OnBoardHealthProfessionalRequestBody.builder()
                        .type(HealthProfessionalType.FACILITY_MANAGER)
                        .healthProfessionalID("test-abha-id")
                        .password("test-pass")
                        .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/onboard")
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
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
