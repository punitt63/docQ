package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.model.Patient;
import in.docq.health.facility.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class CareContextService {
    private final OPDService opdService;
    private final HealthFacilityService healthFacilityService;
    private final PatientService patientService;
    private final AbhaRestClient abhaRestClient;
    private final String xCmId;
    private final Cache<String, OPD> opdCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .build();

    @Autowired
    public CareContextService(OPDService opdService, HealthFacilityService healthFacilityService, PatientService patientService,
                              AbhaRestClient abhaRestClient,
                              @Value("${x.cm.id}") String xCmId) {
        this.opdService = opdService;
        this.healthFacilityService = healthFacilityService;
        this.patientService = patientService;
        this.abhaRestClient = abhaRestClient;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> linkCareContext(Appointment appointment, Prescription prescription) {
        return getOPD(appointment.getOpdDate(), appointment.getOpdId())
                        .thenCompose(opd -> patientService.getPatient(appointment.getPatientId())
                                .thenCompose(patient -> {
                                    if(patient.isAbhaOnboarded()) {
                                        if(patient.isHipLinkTokenExpired()) {
                                            return generateLinkingToken(opd, patient);
                                        }
                                        return linkCareContextToAbha(opd, patient, prescription);
                                    }
                                    return sendDeepLinkNotification(opd, patient);
                                }));
    }

    private CompletionStage<OPD> getOPD(LocalDate opdDate, String opdId) {
        OPD cachedOpd = opdCache.getIfPresent(opdDate.toString() + "_" + opdId);
        if(cachedOpd != null) {
            return completedFuture(cachedOpd);
        }
        return opdService.get(opdDate, opdId)
                .thenApply(opd -> {
                    opdCache.put(opdDate + "_" + opdId, opd);
                    return opd;
                });
    }

    private CompletionStage<Void> generateLinkingToken(OPD opd, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        return patientService.replacePatient(patient.getId(), patient.toBuilder().lastHipLinkTokenRequestId(requestId).build())
                .thenCompose(ignore -> abhaRestClient.generateLinkingToken(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), opd.getHealthFacilityID(), xCmId,
                        new HIPInitiatedGenerateTokenRequest()
                                .abhaNumber(patient.getAbhaNo())
                                .abhaAddress(patient.getAbhaAddress())
                                .name(patient.getName())
                                .gender(HIPInitiatedGenerateTokenRequest.GenderEnum.valueOf(patient.getGender()))
                                .yearOfBirth(patient.getYearOfBirth())));
    }

    private CompletionStage<Void> sendDeepLinkNotification(OPD opd, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
        return healthFacilityService.getHealthFacility(opd.getHealthFacilityID())
                .thenCompose(healthFacility -> abhaRestClient.sendDeepLinkNotification(requestId,
                        timestamp,
                        xCmId,
                        new SendSmsNotificationRequest()
                                .requestId(requestId)
                                .timestamp(timestamp)
                                .notification(new SendSmsNotificationRequestNotification().phoneNo(patient.getMobileNo())
                                        .hip(new SendSmsNotificationRequestNotificationHip().id(opd.getHealthFacilityID()).name(healthFacility.getFacilityName())))));
    }

    private CompletionStage<Void> linkCareContextToAbha(OPD opd, Patient patient, Prescription prescription) {
        String requestId = UUID.randomUUID().toString();
        return healthFacilityService.getHealthFacility(opd.getHealthFacilityID())
                .thenCompose(healthFacility -> abhaRestClient.linkHIPInitiatedCareContext(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), opd.getHealthFacilityID(), xCmId, patient.getLastHipToken(),
                new AbdmHipInitiatedLinkingHip1Request()
                        .abhaNumber(new BigDecimal(patient.getAbhaNo()))
                        .abhaAddress(patient.getAbhaAddress())
                        .careContexts(ImmutableList.of(new AbdmHipInitiatedLinkingHip1RequestPatientInner()
                                .referenceNumber(prescription.getId())
                                .display("Prescription from " + healthFacility.getFacilityName())))));
    }
}
