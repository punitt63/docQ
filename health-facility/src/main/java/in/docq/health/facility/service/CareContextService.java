package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.dao.CareContextDao;
import in.docq.health.facility.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class CareContextService {
    private final CareContextDao careContextDao;
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
    public CareContextService(CareContextDao careContextDao, OPDService opdService, HealthFacilityService healthFacilityService, PatientService patientService, HIPLinkingTokenService hipLinkingTokenService,
                              AbhaRestClient abhaRestClient,
                              @Value("${x.cm.id}") String xCmId) {
        this.careContextDao = careContextDao;
        this.opdService = opdService;
        this.healthFacilityService = healthFacilityService;
        this.patientService = patientService;
        this.hipLinkingTokenService = hipLinkingTokenService;
        this.abhaRestClient = abhaRestClient;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> onGenerateLinkingToken(String healthFacilityId,  HipWebhookController.OnGenerateTokenRequest request) {
        return patientService.getPatientByAbhaAddress(request.getAbhaAddress())
                .thenCompose(patient -> hipLinkingTokenService.getByRequestId(patient.getId(), request.getResponse().getRequestId())
                        .thenCompose(hipLinkingToken -> linkCareContextToAbha(healthFacilityId, hipLinkingToken.getLastTokenRequestAppointmentId(), patient, request.getLinkToken()))
                        .thenCompose(ignore -> hipLinkingTokenService.updateToken(request.getLinkToken(), patient.getId(), request.getResponse().getRequestId())));
    }

    public CompletionStage<Void> linkCareContext(Appointment appointment) {
        return getOrCreateCareContext(appointment)
                .thenCompose(careContext -> patientService.getPatient(appointment.getPatientId())
                        .thenCompose(patient -> hipLinkingTokenService.getToken(careContext.getHealthFacilityId(), appointment.getPatientId())
                                .thenCompose(linkingToken -> {
                                    if(careContext.isLinked()) {
                                        return completedFuture(null);
                                    }
                                    boolean isLinkingTokenExpired = linkingToken.map(HIPLinkingToken::isHipLinkTokenExpired).orElse(true);
                                    if(patient.isAbhaOnboarded()) {
                                        if(isLinkingTokenExpired) {
                                            return generateLinkingToken(careContext, patient);
                                        }
                                        return linkCareContextToAbha(careContext, patient, linkingToken.get().getLastToken());
                                    }
                                    return sendPatientNotification(careContext, patient);
                                }))
                        .thenCompose(careContextDao::upsert));
    }

    public CompletionStage<CareContext> getOrCreateCareContext(Appointment appointment) {
        return careContextDao.get(appointment.getUniqueId())
                .thenCompose(careContextOpt -> {
                    if(careContextOpt.isPresent()) {
                        return completedFuture(careContextOpt.get());
                    }
                    return getOPD(appointment.getOpdDate(), appointment.getOpdId())
                            .thenCompose(opd -> careContextDao.upsert(CareContext.from(opd, appointment))
                            .thenApply(ignore -> CareContext.from(opd, appointment)));
                });
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

    private CompletionStage<CareContext> generateLinkingToken(CareContext careContext, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        return hipLinkingTokenService.upsert(HIPLinkingToken.builder()
                        .healthFacilityId(careContext.getHealthFacilityId())
                        .patientId(patient.getId())
                        .lastTokenRequestAppointmentId(careContext.getAppointmentID())
                        .lastTokenRequestId(requestId)
                        .lastToken(null)
                        .build())
                .thenCompose(ignore -> abhaRestClient.generateLinkingToken(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), careContext.getHealthFacilityId(), xCmId,
                        new HIPInitiatedGenerateTokenRequest()
                                .abhaNumber(patient.getAbhaNo())
                                .abhaAddress(patient.getAbhaAddress())
                                .name(patient.getName())
                                .gender(HIPInitiatedGenerateTokenRequest.GenderEnum.valueOf(patient.getGender()))
                                .yearOfBirth(patient.getYearOfBirth())))
                .thenApply(ignore -> careContext);
    }

    private CompletionStage<CareContext> sendPatientNotification(CareContext careContext, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
        return healthFacilityService.getHealthFacility(careContext.getHealthFacilityId())
                .thenCompose(healthFacility -> abhaRestClient.sendDeepLinkNotification(requestId,
                        timestamp,
                        xCmId,
                        new SendSmsNotificationRequest()
                                .requestId(requestId)
                                .timestamp(timestamp)
                                .notification(new SendSmsNotificationRequestNotification().phoneNo(patient.getMobileNo())
                                        .hip(new SendSmsNotificationRequestNotificationHip().id(careContext.getHealthFacilityId()).name(healthFacility.getFacilityName())))))
                .thenApply(ignore -> careContext);
    }

    private CompletionStage<CareContext> linkCareContextToAbha(CareContext careContext, Patient patient, String linkingToken) {
        String requestId = UUID.randomUUID().toString();
        return healthFacilityService.getHealthFacility(careContext.getHealthFacilityId())
                .thenCompose(healthFacility -> abhaRestClient.linkHIPInitiatedCareContext(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), careContext.getHealthFacilityId(), xCmId, linkingToken,
                new AbdmHipInitiatedLinkingHip1Request()
                        .abhaNumber(new BigDecimal(patient.getAbhaNo()))
                        .abhaAddress(patient.getAbhaAddress())
                        .patients(ImmutableList.of(new AbdmHipInitiatedLinkingHip1RequestPatientInner()
                                .referenceNumber(careContext.getAppointmentID())
                                .display("Prescription from " + healthFacility.getFacilityName())
                                .hiTypes(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.PRESCRIPTION)
                                .count(BigDecimal.ONE)
                        ))))
                .thenApply(ignore -> careContext.toBuilder().requestId(requestId).build());
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
                        ))))
                .thenCompose(ignore -> careContextDao.upsert(CareContext.builder()
                        .appointmentID(appointmentId)
                        .healthFacilityId(healthFacilityId)
                        .patientId(patient.getId())
                        .requestId(requestId)
                        .isLinked(false)
                        .isPatientNotified(false)
                        .build()));
    }
}
