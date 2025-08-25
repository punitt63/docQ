package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HipGenerateTokenWebhookController;
import in.docq.health.facility.model.*;
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
    private final HIPLinkingTokenService hipLinkingTokenService;
    private final AbhaRestClient abhaRestClient;
    private final String xCmId;
    private final Cache<String, OPD> opdCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .build();

    @Autowired
    public CareContextService(OPDService opdService, HealthFacilityService healthFacilityService, PatientService patientService, HIPLinkingTokenService hipLinkingTokenService,
                              AbhaRestClient abhaRestClient,
                              @Value("${x.cm.id}") String xCmId) {
        this.opdService = opdService;
        this.healthFacilityService = healthFacilityService;
        this.patientService = patientService;
        this.hipLinkingTokenService = hipLinkingTokenService;
        this.abhaRestClient = abhaRestClient;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> linkCareContext(HipGenerateTokenWebhookController.OnGenerateTokenRequest request) {
        return hipLinkingTokenService.getByRequestId(request.getAbhaAddress(), request.getResponse().getRequestId())
                .thenCompose(hipLinkingToken -> patientService.getPatient(hipLinkingToken.getPatientId())
                        .thenCompose(patient -> linkCareContextToAbha(hipLinkingToken.getHealthFacilityId(), hipLinkingToken.getLastTokenRequestAppointmentId(), patient, hipLinkingToken.getLastToken())));
    }

    public CompletionStage<Void> linkCareContext(Appointment appointment) {
        return getOPD(appointment.getOpdDate(), appointment.getOpdId())
                .thenCompose(opd -> hipLinkingTokenService.getToken(opd.getHealthFacilityID(), appointment.getPatientId())
                        .thenCompose(linkingToken -> patientService.getPatient(appointment.getPatientId())
                                .thenCompose(patient -> {
                                    boolean isLinkingTokenExpired = linkingToken.map(HIPLinkingToken::isHipLinkTokenExpired).orElse(true);
                                    if(patient.isAbhaOnboarded()) {
                                        if(isLinkingTokenExpired) {
                                            return generateLinkingToken(opd, appointment, patient);
                                        }
                                        return linkCareContextToAbha(opd, appointment, patient, linkingToken.get().getLastToken());
                                    }
                                    return sendDeepLinkNotification(opd, patient);
                                })));
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

    private CompletionStage<Void> generateLinkingToken(OPD opd, Appointment appointment, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        return hipLinkingTokenService.upsert(HIPLinkingToken.builder()
                        .healthFacilityId(opd.getHealthFacilityID())
                        .patientId(patient.getId())
                        .lastTokenRequestAppointmentId(appointment.getUniqueId())
                        .lastTokenRequestId(requestId)
                        .lastToken(null)
                        .build())
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

    private CompletionStage<Void> linkCareContextToAbha(OPD opd, Appointment appointment, Patient patient, String linkingToken) {
        String requestId = UUID.randomUUID().toString();
        return healthFacilityService.getHealthFacility(opd.getHealthFacilityID())
                .thenCompose(healthFacility -> abhaRestClient.linkHIPInitiatedCareContext(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), opd.getHealthFacilityID(), xCmId, linkingToken,
                new AbdmHipInitiatedLinkingHip1Request()
                        .abhaNumber(new BigDecimal(patient.getAbhaNo()))
                        .abhaAddress(patient.getAbhaAddress())
                        .patients(ImmutableList.of(new AbdmHipInitiatedLinkingHip1RequestPatientInner()
                                .referenceNumber(appointment.getUniqueId())
                                .display("Prescription from " + healthFacility.getFacilityName())
                                .hiTypes(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.PRESCRIPTION)
                                .count(BigDecimal.ONE)
                        ))));
    }

    private CompletionStage<Void> linkCareContextToAbha(String healthFacilityId, String appointmentId, Patient patient, String linkingToken) {
        String requestId = UUID.randomUUID().toString();
        return healthFacilityService.getHealthFacility(healthFacilityId)
                .thenCompose(healthFacility -> abhaRestClient.linkHIPInitiatedCareContext(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), healthFacilityId, xCmId, linkingToken,
                new AbdmHipInitiatedLinkingHip1Request()
                        .abhaNumber(new BigDecimal(patient.getAbhaNo()))
                        .abhaAddress(patient.getAbhaAddress())
                        .patients(ImmutableList.of(new AbdmHipInitiatedLinkingHip1RequestPatientInner()
                                .referenceNumber(appointmentId)
                                .display("Prescription from " + healthFacility.getFacilityName())
                                .hiTypes(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.PRESCRIPTION)
                                .count(BigDecimal.ONE)
                        ))));
    }
}
