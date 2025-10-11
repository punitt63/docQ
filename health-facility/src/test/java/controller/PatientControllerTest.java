package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.controller.PatientController;
import in.docq.health.facility.dao.PatientDao;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.Patient;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testDoctorID;
import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testHealthFacilityID;
import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.testHealthFacilityManagerID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @Before
    public void beforeEach() {
        patientDao.truncate().toCompletableFuture().join();
    }

    @Test
    public void testCreateAndSearchPatient() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();

        // Create patient
        PatientController.CreatePatientRequestBody createPatientRequestBody = PatientController.CreatePatientRequestBody.builder()
                .abhaNo("1234-5678-9011")
                .abhaAddress("patient@abdm")
                .name("Test Patient")
                .mobileNo("9876543210")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("M")
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/patients")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createPatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Search patient
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

        assertEquals(1, patients.size());
        assertEquals("Test Patient", patients.get(0).getName());
        assertEquals("9876543210", patients.get(0).getMobileNo());
        assertEquals(LocalDate.of(1990, 1, 1), patients.get(0).getDob());
        assertEquals("M", patients.get(0).getGender());
    }

    @Test
    public void testReplacePatient() throws Exception {
        String facilityManagerToken = onboardFacilityManagerAndGetToken();

        // Create initial patient
        PatientController.CreatePatientRequestBody createPatientRequestBody = PatientController.CreatePatientRequestBody.builder()
                .abhaNo("1234-5678-9012")
                .abhaAddress("patient@abdm")
                .name("Initial Patient")
                .mobileNo("9876543210")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("M")
                .build();

        Patient patient = gson.fromJson(handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/patients")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createPatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse()
                .getContentAsString(), Patient.class);

        // Replace patient with ABHA details
        PatientController.ReplacePatientRequestBody replacePatientRequestBody = PatientController.ReplacePatientRequestBody.builder()
                .name("Updated Patient")
                .mobileNo("9876543211")
                .dob(LocalDate.of(1990, 1, 2))
                .abhaNo("1234-5678-9012")
                .abhaAddress("patient@abdm")
                .gender("F")
                .build();

        handleAsyncProcessing(mockMvc.perform(put("/health-facilities/" + testHealthFacilityID + "/patients/" + patient.getId())
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(replacePatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Search by new mobile number
        List<Patient> patients = gson.fromJson(
                handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/patients")
                        .header("Authorization", "Bearer " + facilityManagerToken)
                        .param("mobile-no", "9876543211")
                        .contentType(MediaType.APPLICATION_JSON)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeToken<List<Patient>>(){}.getType());

        assertEquals(1, patients.size());
        assertEquals("Updated Patient", patients.get(0).getName());
        assertEquals("9876543211", patients.get(0).getMobileNo());
        assertEquals(LocalDate.of(1990, 1, 2), patients.get(0).getDob());
        assertEquals("F", patients.get(0).getGender());
        assertNotNull(patients.get(0).getAbhaNo());
        assertNotNull(patients.get(0).getAbhaAddress());
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
        return mockMvc.perform(asyncDispatch(mvcResult));
    }
}