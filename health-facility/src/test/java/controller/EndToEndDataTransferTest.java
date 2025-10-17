package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import configuration.TestAbhaClientConfiguration;
import in.docq.abha.rest.client.JSON;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.HealthFacilityApplication;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.*;
import in.docq.health.facility.dao.*;
import in.docq.health.facility.model.*;
import in.docq.health.facility.service.HiuConsentService;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static configuration.TestAbhaClientConfiguration.MockAbhaRestClient.*;
import static org.assertj.core.util.Preconditions.checkState;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HealthFacilityApplication.class, TestAbhaClientConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
public class EndToEndDataTransferTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OPDService opdService;

    @Autowired
    private ConsentHealthRecordDao consentHealthRecordDao;

    @Autowired
    private ConsentRequestDao consentRequestDao;

    @Autowired
    private HealthInformationRequestDao healthInformationRequestDao;

    @Autowired
    private TestAbhaClientConfiguration.MockAbhaRestClient mockAbhaRestClient;

    @Autowired
    private DesktopKeycloakRestClient desktopKeycloakRestClient;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new TypeAdapter<OffsetDateTime>() {
                @Override
                public void write(JsonWriter out, OffsetDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString());
                    }
                }

                @Override
                public OffsetDateTime read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return OffsetDateTime.parse(in.nextString());
                }
            })
            .create();

    private static final String HIU_ID = "IN2310020019";
    private static final String HIP_ID_1 = "IN2310020040";
    private static final String HIP_ID_2 = "IN2310020041";
    private static final String PATIENT_ID = "john.doe@abdm";

    private String facilityManagerTokenHIU, facilityManagerTokenHIP1, facilityManagerTokenHIP2;
    private String doctorTokenHIU, doctorTokenHIP1, doctorTokenHIP2;
    private Patient testPatient;

    @Autowired
    private OPDDao opdDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private PrescriptionDAO prescriptionDAO;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private HIPLinkingTokenDao hipLinkingTokenDao;

    @Autowired
    private HiuConsentService hiuConsentService;

    @Autowired
    private CareContextDao careContextDao;

    @Autowired
    private ConsentDao consentDao;

    @Before
    public void setUp() throws Exception {
        opdDao.truncate().toCompletableFuture().join();
        appointmentDao.truncate().toCompletableFuture().join();
        prescriptionDAO.truncate().toCompletableFuture().join();
        patientDao.truncate().toCompletableFuture().join();
        careContextDao.truncate().toCompletableFuture().join();
        consentRequestDao.truncate().toCompletableFuture().join();
        consentHealthRecordDao.truncate().toCompletableFuture().join();
        healthInformationRequestDao.truncate().toCompletableFuture().join();
        facilityManagerTokenHIU = onboardFacilityManagerAndGetToken(testThirdHealthFacilityID, testThirdHealthFacilityManagerID);
        facilityManagerTokenHIP1 = onboardFacilityManagerAndGetToken(testHealthFacilityID, testHealthFacilityManagerID);
        facilityManagerTokenHIP2 = onboardFacilityManagerAndGetToken(testSecondHealthFacilityID, testSecondHealthFacilityManagerID);
        doctorTokenHIU = onboardDoctorAndGetToken(testThirdHealthFacilityID, testThirdHealthFacilityManagerID, facilityManagerTokenHIU, testThirdDoctorID);
        doctorTokenHIP1 = onboardDoctorAndGetToken(testHealthFacilityID, testHealthFacilityManagerID, facilityManagerTokenHIP1, testDoctorID);
        doctorTokenHIP2 = onboardDoctorAndGetToken(testSecondHealthFacilityID, testSecondHealthFacilityManagerID, facilityManagerTokenHIP2, testSecondDoctorID);
        testPatient = createAbhaPatient(facilityManagerTokenHIP1);
        setupPrescriptionData();
        mockAbhaRestClient.sendConsentRequestCount = 0;
        mockAbhaRestClient.fetchConsentRequestCount = 0;
        mockAbhaRestClient.notifyDataTransferCount = 0;
        mockAbhaRestClient.healthInfoRequestAcknowledgementCount = 0;
        mockAbhaRestClient.notifyHealthInfoTransferCount = 0;
        mockAbhaRestClient.setHiuConsentService(hiuConsentService);
    }

    @Test
    public void testEndToEndDataTransfer() throws Exception {
        // 1. Call consent-request API
        callConsentRequestApi();
        String requestID = assertConsentRequestApi();

        // 2. Call on-init API
        String consentRequestID = callOnInitApi(requestID);
        assertCallOnInitApi(consentRequestID);

        // 3. Call notify API with GRANTED status
        String consentArtifactId1 = UUID.randomUUID().toString();
        String consentArtifactId2 = UUID.randomUUID().toString();
        callNotifyApi(consentRequestID, consentArtifactId1, consentArtifactId2);
        assertCallNotifyApi(consentRequestID, consentArtifactId1, consentArtifactId2);

        // 4. Call on-fetch API for both HIPs
        callOnFetchApi(consentArtifactId1, HIP_ID_1);
        callOnFetchApi(consentArtifactId2, HIP_ID_2);
        assertEquals(2, mockAbhaRestClient.sendHealthInfoRequestCount);
        assertCallOnFetchApi(consentArtifactId1, HIP_ID_1);
        assertCallOnFetchApi(consentArtifactId2, HIP_ID_2);

        // 5. Call health-information/on-request for both HIPs
        String transactionId1 = UUID.randomUUID().toString();
        String transactionId2 = UUID.randomUUID().toString();
        callHealthInformationOnRequestApi(consentArtifactId1, transactionId1);
        callHealthInformationOnRequestApi(consentArtifactId2, transactionId2);
        assertCallHealthInformationOnRequestApi(transactionId1);
        assertCallHealthInformationOnRequestApi(transactionId2);

        // 6. Call HIP consent notify for both HIPs
        callHipConsentNotifyApi(consentArtifactId1, HIP_ID_1);
        callHipConsentNotifyApi(consentArtifactId2, HIP_ID_2);
        assertCallHipConsentNotifyApi(consentArtifactId1);
        assertCallHipConsentNotifyApi(consentArtifactId2);

        // 7. Call HIP health-information request for both HIPs
        callHipHealthInformationRequestApi(consentArtifactId1, transactionId1, HIP_ID_1);
        callHipHealthInformationRequestApi(consentArtifactId2, transactionId2, HIP_ID_2);
        assertCallHipHealthInformationRequestApi(consentArtifactId1, transactionId1, HIP_ID_1);
        assertCallHipHealthInformationRequestApi(consentArtifactId2, transactionId2, HIP_ID_2);
        assertEquals(2, mockAbhaRestClient.healthInfoRequestAcknowledgementCount);

        // Assertions
        await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> verifyConsentHealthRecords(transactionId1, consentArtifactId1, transactionId2, consentArtifactId2));
    }

    private void setupPrescriptionData() throws Exception {
        LocalDate currentYearStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);

        LocalDate nextYearStart = currentYearStart.plusYears(1);
        LocalDate nextYearEnd = nextYearStart.plusMonths(1).minusDays(1);

        LocalDate nextToNextYearStart = currentYearStart.plusYears(2);
        LocalDate nextToNextYearEnd = nextToNextYearStart.plusMonths(1).minusDays(1);

        // Create prescriptions for HIP_ID_1
        createPrescriptionsForHip(testHealthFacilityID, facilityManagerTokenHIP1, doctorTokenHIP1, testDoctorID, 12, nextYearStart, nextYearEnd);
        createPrescriptionsForHip(testHealthFacilityID, facilityManagerTokenHIP1, doctorTokenHIP1, testDoctorID, 5, nextToNextYearStart, nextToNextYearEnd);

        // Create prescriptions for HIP_ID_2
        createPrescriptionsForHip(testSecondHealthFacilityID, facilityManagerTokenHIP2, doctorTokenHIP2, testSecondDoctorID, 5, nextYearStart, nextYearEnd);
        createPrescriptionsForHip(testSecondHealthFacilityID, facilityManagerTokenHIP2, doctorTokenHIP2, testSecondDoctorID, 12, nextToNextYearStart, nextToNextYearEnd);
    }

    private void createPrescriptionsForHip(String testHealthFacilityID, String facilityManagerToken, String doctorToken, String doctorID, int count, LocalDate startDate, LocalDate endDate) throws Exception {
        createTestOPDs(testHealthFacilityID, doctorID, facilityManagerToken, startDate, endDate);
        List<OPD> testOPDs = getOPDs(facilityManagerToken, testHealthFacilityID, doctorID, startDate, endDate);

        Collections.shuffle(testOPDs);

        for (int i = 0; i < count; i++) {
            OPD testOPD = testOPDs.get(i);
            Appointment appointment = createAppointment(testOPD, facilityManagerToken, doctorID, testPatient.getId());
            startAppointment(appointment);
            createPrescriptionWithFhirContent(testOPD, appointment, doctorToken, doctorID);
        }
    }

    private void createPrescriptionWithFhirContent(OPD opd, Appointment appointment, String doctorToken, String doctorID) throws Exception {
        JsonObject fhirContent = createPrescriptionBundle(appointment, doctorID);

        PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody createPrescriptionRequestBody =
                PrescriptionController.CreateOrReplaceOPDPrescriptionRequestBody.builder()
                        .content(gson.toJson(fhirContent))
                        .build();

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + opd.getHealthFacilityID() +
                "/opd-dates/" + opd.getDate().toString() +
                "/opds/" + opd.getId() +
                "/appointments/" + appointment.getId() + "/prescriptions")
                .header("Authorization", "Bearer " + doctorToken)
                .content(gson.toJson(createPrescriptionRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        CareContext careContext = CareContext.builder()
                .appointmentID(appointment.getUniqueId())
                .healthFacilityId(opd.getHealthFacilityID())
                .patientId(PATIENT_ID)
                .isLinked(true)
                .appointmentStartTime(appointment.getOpdDate().atTime(10, 0).toInstant(ZoneOffset.UTC).toEpochMilli())
                .build();

        careContextDao.upsert(careContext).toCompletableFuture().join();
    }

    private JsonObject createPrescriptionBundle(Appointment appointment, String doctorId) {
        String uniqueId = appointment.getUniqueId();
        LocalDate prescriptionDate = appointment.getOpdDate();
        String prescriptionJson = String.format("""
        {
          "resourceType": "Bundle",
          "id": "bundle-prescription-%s",
          "type": "document",
          "timestamp": "%sT10:15:00Z",
          "entry": [
            {
              "fullUrl": "urn:uuid:patient-%s",
              "resource": {
                "resourceType": "Patient",
                "id": "patient-%s",
                "identifier": [
                  {
                    "system": "https://healthid.abdm.gov.in",
                    "value": "%s"
                  }
                ],
                "name": [
                  {
                    "text": "John Doe"
                  }
                ],
                "gender": "male",
                "birthDate": "1984-09-12"
              }
            },
            {
              "fullUrl": "urn:uuid:practitioner-%s",
              "resource": {
                "resourceType": "Practitioner",
                "id": "practitioner-%s",
                "identifier": [
                  {
                    "system": "https://nmc.gov.in/doctor-registry",
                    "value": "%s"
                  }
                ],
                "name": [
                  {
                    "text": "Dr. %s"
                  }
                ],
                "qualification": [
                  {
                    "code": {
                      "coding": [
                        {
                          "system": "http://terminology.hl7.org/CodeSystem/v2-0360",
                          "code": "MBBS",
                          "display": "Bachelor of Medicine and Surgery"
                        }
                      ],
                      "text": "MBBS, MD (General Medicine)"
                    }
                  }
                ]
              }
            },
            {
              "fullUrl": "urn:uuid:encounter-%s",
              "resource": {
                "resourceType": "Encounter",
                "id": "encounter-%s",
                "status": "finished",
                "class": {
                  "code": "AMB",
                  "display": "Ambulatory"
                },
                "subject": {
                  "reference": "urn:uuid:patient-%s"
                },
                "participant": [
                  {
                    "individual": {
                      "reference": "urn:uuid:practitioner-%s"
                    }
                  }
                ],
                "period": {
                  "start": "%sT09:00:00Z",
                  "end": "%sT09:20:00Z"
                }
              }
            },
            {
              "fullUrl": "urn:uuid:medication-%s",
              "resource": {
                "resourceType": "Medication",
                "id": "medication-%s",
                "code": {
                  "coding": [
                    {
                      "system": "http://snomed.info/sct",
                      "code": "387517004",
                      "display": "Paracetamol"
                    }
                  ],
                  "text": "Paracetamol 500mg Tablet"
                }
              }
            },
            {
              "fullUrl": "urn:uuid:medicationrequest-%s",
              "resource": {
                "resourceType": "MedicationRequest",
                "id": "medicationrequest-%s",
                "status": "active",
                "intent": "order",
                "medicationReference": {
                  "reference": "urn:uuid:medication-%s"
                },
                "subject": {
                  "reference": "urn:uuid:patient-%s"
                },
                "encounter": {
                  "reference": "urn:uuid:encounter-%s"
                },
                "authoredOn": "%sT09:10:00Z",
                "requester": {
                  "reference": "urn:uuid:practitioner-%s"
                },
                "dosageInstruction": [
                  {
                    "text": "Take one tablet by mouth every 6 hours as needed for fever or pain",
                    "timing": {
                      "repeat": {
                        "frequency": 1,
                        "period": 6,
                        "periodUnit": "h"
                      }
                    },
                    "route": {
                      "coding": [
                        {
                          "system": "http://snomed.info/sct",
                          "code": "26643006",
                          "display": "Oral route"
                        }
                      ]
                    },
                    "doseAndRate": [
                      {
                        "doseQuantity": {
                          "value": 1,
                          "unit": "tablet"
                        }
                      }
                    ]
                  }
                ],
                "dispenseRequest": {
                  "quantity": {
                    "value": 10,
                    "unit": "tablets"
                  },
                  "expectedSupplyDuration": {
                    "value": 3,
                    "unit": "days"
                  }
                },
                "note": [
                  {
                    "text": "Avoid alcohol while taking this medication."
                  }
                ]
              }
            }
          ]
        }
        """,
                uniqueId, prescriptionDate,
                uniqueId, uniqueId, testPatient.getId(),
                uniqueId, uniqueId, doctorId, doctorId,
                uniqueId, uniqueId, uniqueId, uniqueId,
                prescriptionDate, prescriptionDate,
                uniqueId, uniqueId,
                uniqueId, uniqueId, uniqueId, uniqueId, uniqueId, prescriptionDate, uniqueId);

        return gson.fromJson(prescriptionJson, JsonObject.class);
    }

    private void callConsentRequestApi() throws Exception {

        AbdmConsentManagement1Request consentRequest = new AbdmConsentManagement1Request()
                .consent(new AbdmConsentManagement1RequestConsent()
                        .purpose(new AbdmConsentManagement1RequestConsentPurpose()
                                .text(AbdmConsentManagement1RequestConsentPurpose.TextEnum.CARE_MANAGEMENT)
                                .code(AbdmConsentManagement1RequestConsentPurpose.CodeEnum.CAREMGT))
                        .patient(new AbdmConsentManagement1RequestConsentPatient()
                                .id(testPatient.getId()))
                        .hiu(new AbdmConsentManagement1RequestConsentHiu()
                                .id(HIU_ID))
                        .requester(new AbdmConsentManagement1RequestConsentRequester()
                                .name("Dr Manjula Bedi")
                                .identifier(new AbdmConsentManagement1RequestConsentRequesterIdentifier()
                                        .value("MH1001")
                                        .type("REGNO1")))
                        .hiTypes(Arrays.asList(AbdmConsentManagement1RequestConsent.HiTypesEnum.PRESCRIPTION))
                        .permission(new AbdmConsentManagement1RequestConsentPermission()
                                .accessMode(AbdmConsentManagement1RequestConsentPermission.AccessModeEnum.VIEW)
                                .dateRange(new AbdmConsentManagement1RequestConsentPermissionDateRange()
                                        .from(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                                        .to(OffsetDateTime.parse("2027-01-01T00:00:00Z")))));

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testThirdHealthFacilityID + "/health-facility-professionals/" + testThirdDoctorID + "/consent-request")
                .header("Authorization", "Bearer " + doctorTokenHIU)
                .content(gson.toJson(consentRequest))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private String assertConsentRequestApi() throws Exception {
        assertEquals(1, mockAbhaRestClient.sendConsentRequestCount);
        List<ConsentRequest> consentRequests = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testThirdHealthFacilityID + "/health-facility-professionals/" + testThirdDoctorID + "/consent-request")
                .header("Authorization", "Bearer " + doctorTokenHIU)
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<ConsentRequest>>(){}.getType());
        assertEquals(1, consentRequests.size());
        return consentRequests.get(0).getRequestId();
    }

    private String callOnInitApi(String requestId) throws Exception {
        String consentID = UUID.randomUUID().toString();
        HiuConsentWebhookController.ConsentRequestOnInitBody request =
                HiuConsentWebhookController.ConsentRequestOnInitBody.builder()
                        .consentRequest(HiuConsentWebhookController.ConsentRequestRef.builder().id(consentID).build())
                        .response(HiuConsentWebhookController.ResponseInfo.builder().requestId(requestId).build())
                        .build();

        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hiu/consent/request/on-init")
                        .header("REQUEST-ID", UUID.randomUUID().toString())
                        .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                        .header("X-HIU-ID", testThirdHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        return consentID;
    }

    private void assertCallOnInitApi(String expectedConsentRequestID) throws Exception {
        List<ConsentRequest> consentRequests = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testThirdHealthFacilityID + "/health-facility-professionals/" + testThirdDoctorID + "/consent-request")
                .header("Authorization", "Bearer " + doctorTokenHIU)
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<ConsentRequest>>(){}.getType());
        assertEquals(1, consentRequests.size());
        ConsentRequest consentRequest = consentRequests.get(0);
        assertEquals(ConsentRequest.Status.REQUESTED, consentRequest.getStatus());
        assertEquals(expectedConsentRequestID, consentRequest.getConsentRequestId());
    }

    private void callNotifyApi(String consentRequestId, String consentArtifactId1, String consentArtifactId2) throws Exception {
        HiuConsentWebhookController.ConsentRequestNotifyBody request =
                HiuConsentWebhookController.ConsentRequestNotifyBody.builder()
                        .notification(HiuConsentWebhookController.NotificationInfo.builder()
                                .consentRequestId(consentRequestId)
                                .status("GRANTED")
                                .consentArtefacts(List.of(HiuConsentWebhookController.ConsentArtefactRef.builder().id(consentArtifactId1).build(), HiuConsentWebhookController.ConsentArtefactRef.builder().id(consentArtifactId2).build()))
                        .build())
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hiu/consent/request/notify")
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                .header("X-HIU-ID", testThirdHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void assertCallNotifyApi(String consentRequestId, String consentArtifactId1, String consentArtifactId2) {
        assertEquals(2, mockAbhaRestClient.fetchConsentRequestCount);
        List<ConsentHealthRecord> consentHealthRecords = consentHealthRecordDao.getByConsentRequestId(consentRequestId)
                .toCompletableFuture()
                .join();
        assertEquals(2, consentHealthRecords.size());
        assertTrue(consentHealthRecords.stream().anyMatch(r -> r.getConsentId().equals(consentArtifactId1) && r.getStatus().equals( ConsentHealthRecord.Status.AWAITING_FETCH)));
        assertTrue(consentHealthRecords.stream().anyMatch(r -> r.getConsentId().equals(consentArtifactId2) && r.getStatus().equals( ConsentHealthRecord.Status.AWAITING_FETCH)));
    }

    private String callOnFetchApi(String consentArtifactId, String hipId) throws Exception {
        String requestId = UUID.randomUUID().toString();

        HiuConsentWebhookController.ConsentOnFetchBody request = createConsentOnFetchBody(consentArtifactId, hipId, requestId);

        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hiu/consent/on-fetch")
                .header("REQUEST-ID", requestId)
                .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                .header("X-HIU-ID", testThirdHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        return requestId;
    }

    private void assertCallOnFetchApi(String consentArtifactId, String hipId) {
        Optional<ConsentHealthRecord> recordOpt = consentHealthRecordDao.getByConsentId(consentArtifactId).toCompletableFuture().join();
        assertTrue("Consent health record should exist for HIP " + hipId, recordOpt.isPresent());
        ConsentHealthRecord record = recordOpt.get();
        assertEquals(hipId, record.getHipId());
        assertNotNull(record.getHealthDataRequestId());
        assertNotNull(record.getKeyMaterial());
    }

    private HiuConsentWebhookController.ConsentOnFetchBody createConsentOnFetchBody(String consentArtifactId, String hipId, String requestId) {
        return HiuConsentWebhookController.ConsentOnFetchBody.builder()
                .consent(HiuConsentWebhookController.ConsentInfo.builder()
                        .status("GRANTED")
                        .consentDetail(HiuConsentWebhookController.ConsentDetail.builder()
                                .consentId(consentArtifactId)
                                .hip(HiuConsentWebhookController.HipInfo.builder()
                                        .id(hipId)
                                        .build())
                                .hiu(HiuConsentWebhookController.HiuInfo.builder()
                                        .id(HIU_ID)
                                        .build())
                                .patient(HiuConsentWebhookController.PatientInfo.builder()
                                        .id(PATIENT_ID)
                                        .build())
                                .hiTypes(Arrays.asList("Prescription"))
                                .permission(HiuConsentWebhookController.PermissionInfo.builder()
                                        .accessMode("VIEW")
                                        .dateRange(HiuConsentWebhookController.DateRangeInfo.builder()
                                                .from("2026-01-01T00:00:00Z")
                                                .to("2027-01-01T00:00:00Z")
                                                .build())
                                        .build())
                                .build())
                        .build())
                .response(HiuConsentWebhookController.ResponseInfo.builder()
                        .requestId(requestId)
                        .build())
                .build();
    }

    private boolean verifyConsentHealthRecords(String healthInfoTxnId1, String consentArtifactId1, String healthInfoTxnId2, String consentArtifactId2) throws Exception {
        // Get consent health records from database
        Optional<ConsentHealthRecord> record1 = consentHealthRecordDao.getByConsentId(consentArtifactId1).toCompletableFuture().get();
        Optional<ConsentHealthRecord> record2 = consentHealthRecordDao.getByConsentId(consentArtifactId2).toCompletableFuture().get();
        Optional<HealthInformationRequest> healthInfoRequest1 = healthInformationRequestDao.getByTransactionId(healthInfoTxnId1).toCompletableFuture().get();
        Optional<HealthInformationRequest> healthInfoRequest2 = healthInformationRequestDao.getByTransactionId(healthInfoTxnId2).toCompletableFuture().get();

        try {
            assertEquals(2, mockAbhaRestClient.notifyDataTransferCount);
            assertEquals(2, mockAbhaRestClient.notifyHealthInfoTransferCount);
            assertTrue(healthInfoRequest1.isPresent());
            assertTrue(healthInfoRequest2.isPresent());
            assertEquals("TRANSFERRED", healthInfoRequest1.get().getStatus());
            assertEquals("TRANSFERRED", healthInfoRequest2.get().getStatus());

            // Verify records exist
            assertTrue("Consent health record should exist for HIP " + HIP_ID_1, record1.isPresent());
            assertTrue("Consent health record should exist for HIP " + HIP_ID_2, record2.isPresent());

            // Verify HIP_ID_1 consent has 12 records
            ConsentHealthRecord hipRecord1 = record1.get();
            assertEquals(HIP_ID_1, hipRecord1.getHipId());
            assertEquals(2, hipRecord1.getPaginatedHealthRecords().size());
            assertTrue(hipRecord1.getPaginatedHealthRecords().stream().allMatch(r -> r.getHealthRecords().size() == 10 || r.getHealthRecords().size() == 2));
            assertEquals(ConsentHealthRecord.Status.TRANSFERRED, hipRecord1.getStatus());

            // Verify HIP_ID_2 consent has 5 records
            ConsentHealthRecord hipRecord2 = record2.get();
            assertEquals(HIP_ID_2, hipRecord2.getHipId());
            assertEquals(1, hipRecord2.getPaginatedHealthRecords().size());
            assertEquals(5, hipRecord2.getPaginatedHealthRecords().get(0).getHealthRecords().size());
            assertEquals(ConsentHealthRecord.Status.TRANSFERRED, hipRecord2.getStatus());

            return true;
        } catch (AssertionError assertionError) {
            return false;
        }
    }

    private int countHealthRecordEntries(JsonObject healthRecord) {
        return Optional.ofNullable(healthRecord).map(hr -> hr.entrySet().size()).orElse(0);
    }

//    private void verifyNotifyHealthInfoTransferCalls(String consentArtifactId1, String consentArtifactId2) {
//        assertTrue("notifyHealthInfoTransfer should be called for consent 1",
//                mockAbhaRestClient.wasNotifyHealthInfoTransferCalled(consentArtifactId1));
//        assertTrue("notifyHealthInfoTransfer should be called for consent 2",
//                mockAbhaRestClient.wasNotifyHealthInfoTransferCalled(consentArtifactId2));
//
//        // Verify notification requests have correct session status
//        AbdmDataFlow8Request notification1 = mockAbhaRestClient.getNotifyHealthInfoTransferRequest(consentArtifactId1);
//        AbdmDataFlow8Request notification2 = mockAbhaRestClient.getNotifyHealthInfoTransferRequest(consentArtifactId2);
//
//        assertEquals("TRANSFERRED", notification1.getNotification().getStatusNotification().getSessionStatus());
//        assertEquals("TRANSFERRED", notification2.getNotification().getStatusNotification().getSessionStatus());
//        assertEquals(HIP_ID_1, notification1.getNotification().getStatusNotification().getHipId());
//        assertEquals(HIP_ID_2, notification2.getNotification().getStatusNotification().getHipId());
//    }

    // Helper methods from PrescriptionControllerTest
    private void createTestOPDs(String healthFacilityID, String doctorID, String facilityManagerToken, LocalDate startDate, LocalDate endDate) throws Exception {
        OPDController.CreateOPDRequestBody createOPDRequestBody = OPDController.CreateOPDRequestBody.builder()
                .name("test-opd")
                .startHour(9)
                .endHour(12)
                .startMinute(0)
                .endMinute(30)
                .startDate(startDate.toString())
                .endDate(endDate.toString())
                .minutesToActivate(60 * 24)
                .maxSlots(50)
                .minutesPerSlot(5)
                .scheduleType(OPD.ScheduleType.WEEKLY)
                .weeklyTemplate(Arrays.asList(true, true, true, true, true, true, false))
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + healthFacilityID + "/health-facility-professionals/" + doctorID + "/opds")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createOPDRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
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

    private Patient createAbhaPatient(String facilityManagerToken) throws Exception {
        PatientController.CreatePatientRequestBody createPatientRequestBody = PatientController.CreatePatientRequestBody.builder()
                .name("AbhaTestPatient")
                .mobileNo("9876543210")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("M")
                .abhaAddress(PATIENT_ID)
                .abhaNo("91536782361862")
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testHealthFacilityID + "/patients")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(createPatientRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

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
        return patients.get(0);
    }

    private String getFacilityManagerToken(String healthFacilityID, String facilityManagerID) throws Exception {
        HealthProfessionalController.LoginHealthProfessionalRequestBody requestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-pass")
                .build();
        MockHttpServletResponse mockHttpServletResponse = handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + healthFacilityID + "/health-facility-professionals/" + facilityManagerID + "/login")
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

    private String onboardFacilityManagerAndGetToken(String healthFacilityID, String facilityManagerID) throws Exception {
        String adminUserToken = getAdminUserToken();
        HealthProfessionalController.OnBoardFacilityManagerRequestBody requestBody = HealthProfessionalController.OnBoardFacilityManagerRequestBody.builder()
                .facilityManagerID(facilityManagerID)
                .password("test-pass")
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + healthFacilityID + "/health-facility-professionals/facility-manager/onboard")
                .header("Authorization", "Bearer " + adminUserToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        return getFacilityManagerToken(healthFacilityID, facilityManagerID);
    }

    private String onboardDoctorAndGetToken(String healthFacilityID, String facilityManagerId, String facilityManagerToken, String doctorID) throws Exception {
        //onboard
        HealthProfessionalController.OnBoardDoctorRequestBody requestBody = HealthProfessionalController.OnBoardDoctorRequestBody.builder()
                .doctorID(doctorID)
                .password("test-doc-pass")
                .facilityManagerID(facilityManagerId)
                .build();
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + healthFacilityID + "/health-facility-professionals/doctor/onboard")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        // get token
        HealthProfessionalController.LoginHealthProfessionalRequestBody loginHealthProfessionalRequestBody = HealthProfessionalController.LoginHealthProfessionalRequestBody.builder()
                .password("test-doc-pass")
                .build();
        MockHttpServletResponse mockHttpServletResponse = handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + healthFacilityID + "/health-facility-professionals/" + doctorID + "/login")
                .content(gson.toJson(loginHealthProfessionalRequestBody))
                .contentType(MediaType.APPLICATION_JSON)))
                .andReturn()
                .getResponse();
        return gson.fromJson(mockHttpServletResponse.getContentAsString(), HealthProfessionalController.LoginResponse.class)
                .getAccessToken();
    }

    private Appointment createAppointment(OPD testOPD, String facilityManagerToken, String doctorID, String patientId) throws Exception {
        handleAsyncProcessing(mockMvc.perform(post("/health-facilities/" + testOPD.getHealthFacilityID() + "/opd-dates/" + testOPD.getDate().toString() + "/opds/" + testOPD.getId() + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .content(gson.toJson(AppointmentController.CreateAppointmentRequestBody.builder().patientID(patientId).build()))
                .contentType(MediaType.APPLICATION_JSON)));

        List<Appointment> appointments = gson.fromJson(handleAsyncProcessing(mockMvc.perform(get("/health-facilities/" + testOPD.getHealthFacilityID() + "/health-facility-professionals/" + doctorID + "/appointments")
                .header("Authorization", "Bearer " + facilityManagerToken)
                .param("start-opd-date", testOPD.getDate().toString())
                .param("end-opd-date", testOPD.getDate().toString())
                .param("opd-id", testOPD.getId())
                .param("patient-id", patientId)
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeToken<List<Appointment>>(){}.getType());
        return appointments.get(0);
    }

    private void callHealthInformationOnRequestApi(String consentArtifactId, String transactionId) throws Exception {
        Optional<ConsentHealthRecord> consentHealthRecord = consentHealthRecordDao.getByConsentId(consentArtifactId).toCompletableFuture().join();
        checkState(consentHealthRecord.isPresent(), "Consent health record should exist for consent: " + consentArtifactId);
        HiuConsentWebhookController.HealthInformationOnRequestBody request =
                HiuConsentWebhookController.HealthInformationOnRequestBody.builder()
                        .hiRequest(HiuConsentWebhookController.HiRequestInfo.builder()
                                .transactionId(transactionId)
                                .sessionStatus("REQUESTED")
                                .build())
                        .response(HiuConsentWebhookController.ResponseInfo.builder()
                                .requestId(consentHealthRecord.get().getHealthDataRequestId())
                                .build())
                        .build();

        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hiu/health-information/on-request")
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                .header("X-HIU-ID", testThirdHealthFacilityID)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void assertCallHealthInformationOnRequestApi(String transactionId) {
        Optional<ConsentHealthRecord> consentHealthRecord = consentHealthRecordDao.getByTransactionId(transactionId).toCompletableFuture().join();
        assertTrue(consentHealthRecord.isPresent());
        assertEquals(ConsentHealthRecord.Status.REQUESTED, consentHealthRecord.get().getStatus());
    }

    private void callHipConsentNotifyApi(String consentArtifactId, String hipId) throws Exception {
        handleAsyncProcessing(mockMvc.perform(post("/api/v3/consent/request/hip/notify")
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                .header("X-HIP-ID", hipId)
                .content(gson.toJson(createConsentForHipNotify(consentArtifactId, hipId)))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void assertCallHipConsentNotifyApi(String consentArtifactId) {
        Optional<Consent> consent = consentDao.getById(consentArtifactId).toCompletableFuture().join();
        assertTrue(consent.isPresent());
    }

    private void callHipHealthInformationRequestApi(String consentArtifactId, String transactionId, String hipId) throws Exception {
        // Retrieve the stored request from MockAbhaRestClient
        AbdmDataFlow7Request storedRequest = mockAbhaRestClient.getStoredHealthInfoRequest(consentArtifactId);
        assertNotNull("Stored health info request should exist for consent: " + consentArtifactId, storedRequest);

        HIPConsentWebhookController.HealthInformationRequestBody request = HIPConsentWebhookController.HealthInformationRequestBody.builder()
                .transactionId(transactionId)
                .hiRequest(HIPConsentWebhookController.HIRequest.builder()
                        .consent(HIPConsentWebhookController.Consent.builder().id(consentArtifactId).build())
                        .dateRange(HIPConsentWebhookController.DateRange.builder()
                                .from(storedRequest.getHiRequest().getDateRange().getFrom())
                                .to(storedRequest.getHiRequest().getDateRange().getTo())
                                .build())
                        .dataPushUrl(storedRequest.getHiRequest().getDataPushUrl())
                        .keyMaterial(new AbdmConsentManagement6RequestKeyMaterial()
                                .nonce(storedRequest.getHiRequest().getKeyMaterial().getNonce())
                                .curve(AbdmConsentManagement6RequestKeyMaterial.CurveEnum.fromValue(storedRequest.getHiRequest().getKeyMaterial().getCurve()))
                                .cryptoAlg(AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.valueOf(storedRequest.getHiRequest().getKeyMaterial().getCryptoAlg()))
                                .dhPublicKey(new AbdmConsentManagement6RequestKeyMaterialDhPublicKey()
                                        .expiry(storedRequest.getHiRequest().getKeyMaterial().getDhPublicKey().getExpiry())
                                        .parameters(AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.fromValue(
                                                storedRequest.getHiRequest().getKeyMaterial().getDhPublicKey().getParameters()))
                                        .keyValue(storedRequest.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue())
                                        ))
                        .build())
                .build();

        handleAsyncProcessing(mockMvc.perform(post("/api/v3/hip/health-information/request")
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())
                .header("X-HIP-ID", hipId)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    private void assertCallHipHealthInformationRequestApi(String consentArtifactId, String transactionId, String hipId) {
        Optional<HealthInformationRequest> healthInformationRequestOptional =  healthInformationRequestDao.getByTransactionId(transactionId).toCompletableFuture().join();
        assertTrue(healthInformationRequestOptional.isPresent());
    }

    private HIPConsentWebhookController.ConsentNotifyRequest createConsentForHipNotify(String consentArtifactId, String hipId) {
        return HIPConsentWebhookController.ConsentNotifyRequest.builder()
                .notification(HIPConsentWebhookController.Notification.builder()
                        .consentId(consentArtifactId)
                        .status("GRANTED")
                        .consent(in.docq.health.facility.model.Consent.builder()
                                .consentId(consentArtifactId)
                                .hip(Consent.Hip.builder()
                                        .id(hipId)
                                        .build())
                                .hiu(Consent.Hiu.builder()
                                        .id(HIU_ID)
                                        .build())
                                .hiTypes(Arrays.asList("Prescription"))
                                .patient(Consent.Patient.builder()
                                        .id(PATIENT_ID)
                                        .build())
                                .purpose(Consent.Purpose.builder()
                                        .text("Care Management")
                                        .code("CAREMGT")
                                        .refUri("www.abdm.gov.in")
                                        .build())
                                .createdAt("2024-05-09T10:09:37.714Z")
                                .requester(Consent.Requester.builder()
                                        .name("Dr Manjula Bedi")
                                        .identifier(Consent.Identifier.builder()
                                                .value("MH1001")
                                                .type("REGNO1")
                                                .system("https://www.mciindia.9985")
                                                .build())
                                        .build())
                                .permission(Consent.Permission.builder()
                                        .accessMode("VIEW")
                                        .dateRange(Consent.DateRange.builder()
                                                .from("2026-01-01T00:00:00.000Z")
                                                .to("2027-01-01T00:00:00.000Z")
                                                .build())
                                        .dataEraseAt("2027-01-01T00:00:00.000Z")
                                        .frequency(Consent.Frequency.builder()
                                                .unit("HOUR")
                                                .value(0)
                                                .repeats(0)
                                                .build())
                                        .build())
                                .schemaVersion("v3")
                                .consentManager(Consent.ConsentManager.builder()
                                        .id("sbx")
                                        .build())
                                .build())
                        .grantAcknowledgement(false)
                        .signature("h1hD734o0NZufMJtTC95e+5al2rg83vmKLwo0Kuv7qYxj5X9FtSPTM8DMhuULF23fObmKy2Ksyn3xgmefzmU/YNcNBfd3PEJun3cM7NyFXBN/KoUZWnB4lnQmqkrJqvCqZwB9AOy877AGGPaq7PGWTrfXEBH+tc+Am3NB4zFUR7pCtGRFEqTW90q8W4CLEGWtri4Yab0G6msjx1Jja0q2BeOFWLJDSJBHgQbOlEiT9y9tM86lBRPbdQnSq4EIUM8XODajnyFD3nQtuNbp1sPR3W1js2tcWsR3FMvyybStr4ALd1qegmuUFn2dpztm3fk6WrCoN2UbUecOAHZZwrIgQ==")
                        .build())
                .build();
    }

    private void startAppointment(Appointment appointment) throws Exception {
        appointmentDao.update(appointment.getOpdDate(), appointment.getOpdId(), appointment.getId(), AppointmentController.UpdateAppointmentRequestBody.builder()
                        .startTime(appointment.getOpdDate().atTime(10, 0).toInstant(ZoneOffset.UTC).toEpochMilli())
                .build()).toCompletableFuture().join();
    }
}