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
import in.docq.health.facility.controller.PrescriptionController;
import in.docq.health.facility.dao.AppointmentDao;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.dao.PrescriptionDAO;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.model.Prescription;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    private final String testPatientId = "test-patient-id";

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter()).create();

    @Before
    public void beforeEach() {
        opdDao.truncate().toCompletableFuture().join();
        appointmentDao.truncate().toCompletableFuture().join();
        prescriptionDAO.truncate().toCompletableFuture().join();
    }

    @Test
    public void testCreateAndGetPrescription() throws Exception {
        OPD testOPD = createTestOPD(1);
        String facilityManagerToken = onboardFacilityManagerAndGetToken();
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 1);
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointments.get(0).getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointments.get(0).getId(), prescription.getAppointmentID().intValue());
        assertEquals("{}", prescription.getContent());
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
        List<Appointment> appointments = createAppointments(testOPD, facilityManagerToken, 1);
        String doctorToken = onboardDoctorAndGetToken(facilityManagerToken);
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        handleAsyncProcessing(mockMvc.perform(put("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder().content("{\"key\":\"value\"}").build()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Prescription prescription = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testHealthFacilityID + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments/" + appointments.get(0).getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .param("opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("appointment-id", String.valueOf(appointments.get(0).getId()))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<Prescription>(){}.getType());
        assertEquals(testOPD.getDate(), prescription.getDate());
        assertEquals(appointments.get(0).getId(), prescription.getAppointmentID().intValue());
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
}
