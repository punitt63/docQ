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
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class OPDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OPDDao opdDao;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @Before
    public void beforeEach() {
        opdDao.truncate().toCompletableFuture().join();
    }

    @Test
    public void testCreateOPDByFacilityManager() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate("2025-04-01")
                .endDate("2026-03-31")
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
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-04-01"), LocalDate.parse("2025-04-30"), 22);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-05-01"), LocalDate.parse("2025-05-31"), 22);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-30"), 21);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-07-01"), LocalDate.parse("2025-07-31"), 23);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-08-01"), LocalDate.parse("2025-08-30"), 21);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-09-01"), LocalDate.parse("2025-09-30"), 22);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-10-01"), LocalDate.parse("2025-10-31"), 23);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-11-01"), LocalDate.parse("2025-11-30"), 20);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-12-01"), LocalDate.parse("2025-12-31"), 23);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2026-01-01"), LocalDate.parse("2026-01-31"), 22);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2026-02-01"), LocalDate.parse("2026-02-28"), 20);
        validateOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2026-03-01"), LocalDate.parse("2026-03-31"), 22);
    }

    @Test
    public void testUpdateOPDByFacilityManager() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        createTestOPDs(facilityManagerToken);
        List<OPD> opds = getOPDs(facilityManagerToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-04-15"));
        OPDController.UpdateOPDRequestBody updateOPDRequestBody = OPDController.UpdateOPDRequestBody.builder()
                .startHour(10)
                .endHour(13)
                .startMinute(30)
                .endMinute(0)
                .minutesToActivate(60 * 24 * 2)
                .maxSlots(51)
                .minutesPerSlot(6)
                .build();
        handleAsyncProcessing(mockMvc.perform(patch("/health-facilities/" + testHealthFacilityID + "/health-facility-professionals/" + testDoctorID + "/opd-dates/2025-04-15/opds/" + opds.get(0).getId())
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(updateOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test-opd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startHour").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endHour").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startMinute").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endMinute").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2025-04-15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activateTime").value(LocalDate.parse("2025-04-15").atStartOfDay(ZoneId.of("Asia/Kolkata")).plusHours(10).plusMinutes(30).minusMinutes(60 * 24 * 2).toInstant().toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxSlots").value(51))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minutesPerSlot").value(6));
    }

    @Test
    public void testGetOPDByDoctor() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        createTestOPDs(facilityManagerToken);
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        validateOPDs(doctorToken, testHealthFacilityID, testDoctorID, LocalDate.parse("2025-04-15"), LocalDate.parse("2025-04-15"), 1);
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

    private List<OPD> getOPDs(String facilityManagerToken, String healthFacilityID, String healthProfessionalID, LocalDate date) throws Exception {
        return gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + healthFacilityID + "/health-facility-professionals/opds")
                .param("health-facility-professional-id", healthProfessionalID)
                .param("start-date", date.toString())
                .param("end-date", date.toString())
                .header("Authorization", "Bearer " + facilityManagerToken)
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<OPD>>(){}.getType());
    }

    private void createTestOPDs(String facilityManagerToken) throws Exception {
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate("2025-04-01")
                .endDate("2026-03-31")
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
