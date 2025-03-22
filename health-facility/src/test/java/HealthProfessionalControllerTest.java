import com.google.gson.Gson;
import configuration.TestAbhaClientConfiguration;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.model.HealthProfessionalType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testHealthFacilityManagerID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static Gson gson = new Gson();

    @Before
    public void beforeEach() {
        healthProfessionalDao.truncate().toCompletableFuture().join();
    }

    @Test
    public void testFacilityManagerOnBoarding() throws Exception {
        HealthProfessionalController.OnBoardHealthProfessionalRequestBody requestBody = HealthProfessionalController.OnBoardHealthProfessionalRequestBody.builder()
                        .type(HealthProfessionalType.FACILITY_MANAGER)
                        .healthProfessionalID(testHealthFacilityManagerID)
                        .password("test-pass")
                        .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/onboard")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJGeUVKWk0tTkgyWE5FbElvamQyM0I5eWNuakRKbFgxWEVaMW9LbzQ3dzBjIn0.eyJleHAiOjE3NDI1MTA1MjUsImlhdCI6MTc0MjUxMDIyNSwianRpIjoiNTg2MmQ3YTMtNmJjNC00ZjdjLTg1MDctYzJkMWRmMWYwYzcxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9oZWFsdGgtZmFjaWxpdHkiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiM2M3Mzg3MDEtYWNmZS00YTY2LTlhYWMtZTZmMzNjMjNkZDUzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaGVhbHRoLWZhY2lsaXR5LWRlc2t0b3AtYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjMyNDFjMWM4LTVjYTgtNDQ3OS04NTFlLWEzN2ViMWNlZGM4ZCIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1oZWFsdGgtZmFjaWxpdHkiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiMzI0MWMxYzgtNWNhOC00NDc5LTg1MWUtYTM3ZWIxY2VkYzhkIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0LWFiaGEtaWRfaW4yMzEwMDIwMDQwIn0.AO-3R702Eim_1YD7xmLUvIivoLFVMcf0Uf6LzDZtu-HcbmayWIiVlaEl8E104vlYzM4h6NFIsRHeEhxnukmSMFf2Zodao_0MKPbKtezmeo333ES8wk4hhvS0f7lAy7RJzzoBAlWi4mbR_xxr_liui2SapMMp0-9vRMTYsEpEvT61nJNlrpgI288HJ-zyFnNDnP1EuQSy09TSPLaeiMt-Fl9GlZevdZfD2KOhmzJXuX1KxY-cgEI_9FbCIeMFV6PHtgfY_OV_PcjR1wbSEwwF4_kOv5ZEludCFE1S8ybu2QU8UhgCf6ZFXXvOX7dZk7hkJGvPqMYdakI_e_lVBtaBMw")
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
