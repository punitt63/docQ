package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.HIPInitiatedGenerateTokenRequest;
import in.docq.abha.rest.client.model.SendSmsNotificationRequest;
import in.docq.abha.rest.client.model.SendSmsNotificationRequestNotification;
import in.docq.abha.rest.client.model.SendSmsNotificationRequestNotificationHip;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Service
public class CareContextService {
    private final HealthFacilityService healthFacilityService;
    private final PatientService patientService;
    private final AbhaRestClient abhaRestClient;
    private final String xHipId;
    private final String xCmId;

    @Autowired
    public CareContextService(HealthFacilityService healthFacilityService, PatientService patientService,
                              AbhaRestClient abhaRestClient,
                              @Value("${x.hip.id}") String xHipId,
                              @Value("${x.cm.id}") String xCmId) {
        this.healthFacilityService = healthFacilityService;
        this.patientService = patientService;
        this.abhaRestClient = abhaRestClient;
        this.xHipId = xHipId;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> linkCareContext(OPD opd, Appointment appointment, Prescription prescription) {
        return patientService.getPatient(appointment.getPatientId())
                .thenCompose(patient -> {
                    if(patient.isAbhaOnboarded()) {
                        if(patient.isHipLinkTokenExpired()) {
                            String requestId = UUID.randomUUID().toString();
                            return patientService.replacePatient(patient.getId(), patient.toBuilder().lastHipLinkTokenRequestId(requestId).build())
                                            .thenCompose(ignore -> abhaRestClient.generateLinkingToken(requestId, OffsetDateTime.now(), xHipId, xCmId,
                                                    new HIPInitiatedGenerateTokenRequest()
                                                            .abhaNumber(patient.getAbhaNo())
                                                            .abhaAddress(patient.getAbhaAddress())
                                                            .name(patient.getName())
                                                            .gender(HIPInitiatedGenerateTokenRequest.GenderEnum.valueOf(patient.getGender()))
                                                            .yearOfBirth(patient.getYearOfBirth())));
                        }
                    }
                    UUID requestId = UUID.randomUUID();
                    return healthFacilityService.getHealthFacility(opd.getHealthFacilityID())
                                    .thenCompose(healthFacility -> abhaRestClient.sendDeepLinkNotification(requestId,
                                            OffsetDateTime.now(),
                                            xCmId,
                                            new SendSmsNotificationRequest()
                                            .requestId(requestId)
                                            .timestamp(OffsetDateTime.now())
                                            .notification(new SendSmsNotificationRequestNotification().phoneNo(patient.getMobileNo())
                                                    .hip(new SendSmsNotificationRequestNotificationHip().id(opd.getHealthFacilityID()).name(healthFacility.getFacilityName())))));
                });
    }
}
