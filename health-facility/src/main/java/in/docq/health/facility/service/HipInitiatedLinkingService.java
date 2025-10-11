package in.docq.health.facility.service;

import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.dao.HipInitiatedLinkingDao;
import in.docq.health.facility.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class HipInitiatedLinkingService {
    private static final Logger logger = LoggerFactory.getLogger(HipInitiatedLinkingService.class);

    private final HipInitiatedLinkingDao hipInitiatedLinkingDao;
    private final PatientService patientService;
    private final HIPLinkingTokenService hipLinkingTokenService;
    private final HealthFacilityService healthFacilityService;
    private final AbhaRestClient abhaRestClient;
    private final CareContextService careContextService;
    private final String xCmId;

    @Autowired
    public HipInitiatedLinkingService(HipInitiatedLinkingDao hipInitiatedLinkingDao,
                                      PatientService patientService, HIPLinkingTokenService hipLinkingTokenService,
                                      HealthFacilityService healthFacilityService,
                                      AbhaRestClient abhaRestClient, CareContextService careContextService,
                                      @Value("${x.cm.id}") String xCmId) {
        this.hipInitiatedLinkingDao = hipInitiatedLinkingDao;
        this.patientService = patientService;
        this.hipLinkingTokenService = hipLinkingTokenService;
        this.healthFacilityService = healthFacilityService;
        this.abhaRestClient = abhaRestClient;
        this.careContextService = careContextService;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> onGenerateLinkingToken(String healthFacilityId,  HipWebhookController.OnGenerateTokenRequest request) {
        return patientService.getPatientByAbhaAddress(request.getAbhaAddress())
                .thenCompose(patient -> hipLinkingTokenService.getByRequestId(patient.getId(), request.getResponse().getRequestId())
                        .thenCompose(hipLinkingToken -> linkCareContextToAbha(healthFacilityId, hipLinkingToken.getLastTokenRequestAppointmentId(), patient, request.getLinkToken()))
                        .thenCompose(ignore -> hipLinkingTokenService.updateToken(request.getLinkToken(), patient.getId(), request.getResponse().getRequestId())));
    }

    public CompletionStage<Void> linkCareContext(Appointment appointment) {
        return careContextService.getOrCreateCareContext(appointment)
                .thenCompose(careContext -> patientService.getPatient(appointment.getPatientId())
                        .thenCompose(patient -> hipLinkingTokenService.getToken(careContext.getHealthFacilityId(), appointment.getPatientId())
                                .thenCompose(linkingToken -> {
                                    if(careContext.isLinked()) {
                                        return completedFuture(null);
                                    }
                                    boolean isLinkingTokenExpired = linkingToken.map(HIPLinkingToken::isHipLinkTokenExpired).orElse(true);
                                    HipInitiatedLinking hipInitiatedLinking = HipInitiatedLinking.from(careContext);
                                    if(patient.isAbhaOnboarded()) {
                                        if(isLinkingTokenExpired) {
                                            return generateLinkingToken(careContext.getHealthFacilityId(), careContext.getAppointmentID(), patient);
                                        }
                                        return linkCareContextToAbha(careContext.getHealthFacilityId(), careContext.getAppointmentID(), patient, linkingToken.get().getLastToken());
                                    }
                                    return sendPatientNotification(hipInitiatedLinking, patient);
                                })));
    }

    private CompletionStage<Void> generateLinkingToken(String healthFacilityId, String appointmentId, Patient patient) {
        return hipLinkingTokenService.generateLinkingToken(healthFacilityId, appointmentId, patient)
                .thenCompose(ignore -> hipInitiatedLinkingDao.upsert(HipInitiatedLinking.builder()
                        .appointmentId(appointmentId)
                        .healthFacilityId(healthFacilityId)
                        .patientId(patient.getId())
                        .linkRequestId(null)
                        .isPatientNotified(false)
                        .build()));
    }

    private CompletionStage<Void> linkCareContextToAbha(String healthFacilityId, String appointmentId, Patient patient, String linkingToken) {
        String requestId = UUID.randomUUID().toString();
        return healthFacilityService.getHealthFacility(healthFacilityId)
                .thenCompose(healthFacility -> abhaRestClient.linkHIPInitiatedCareContext(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), healthFacilityId, linkingToken,
                        new AbdmHipInitiatedLinkingHip1Request()
                                .abhaNumber(new BigDecimal(patient.getAbhaNo()))
                                .abhaAddress(patient.getAbhaAddress())
                                .patients(ImmutableList.of(new AbdmHipInitiatedLinkingHip1RequestPatientInner()
                                        .referenceNumber(appointmentId)
                                        .display("Prescription from " + healthFacility.getFacilityName())
                                        .hiTypes(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.PRESCRIPTION)
                                        .count(BigDecimal.ONE)
                                ))))
                .thenCompose(ignore -> hipInitiatedLinkingDao.upsert(HipInitiatedLinking.builder()
                        .appointmentId(appointmentId)
                        .healthFacilityId(healthFacilityId)
                        .patientId(patient.getId())
                        .linkRequestId(requestId)
                        .isPatientNotified(false)
                        .build()));
    }

    public CompletionStage<Void> onLinkCareContext(HipWebhookController.OnLinkCareContextRequest request) {
        if (request.getError() != null) {
            logger.error("Care context linking failed - Code: {}, Message: {}, ABHA Address: {}, Request ID: {}",
                    request.getError().getCode(),
                    request.getError().getMessage(),
                    request.getAbhaAddress(),
                    request.getResponse() != null ? request.getResponse().getRequestId() : "N/A");
            return CompletableFuture.completedFuture(null);
        }
        return patientService.getPatientByAbhaAddress(request.getAbhaAddress())
                .thenCompose(patient -> hipInitiatedLinkingDao.getByLinkRequestId(request.getResponse().getRequestId())
                        .thenCompose(hipInitiatedLinkingOpt -> {
                            if(hipInitiatedLinkingOpt.isEmpty()) {
                                logger.warn("No care context found for request: {}", request.getResponse().getRequestId());
                                return completedFuture(null);
                            }
                            boolean isLinked = "SUCCESS".equalsIgnoreCase(request.getStatus());
                            return sendPatientNotification(hipInitiatedLinkingOpt.get(), patient)
                                    .thenCompose(ignore -> careContextService.update(hipInitiatedLinkingOpt.get().getHealthFacilityId(), hipInitiatedLinkingOpt.get().getAppointmentId(), hipInitiatedLinkingOpt.get().getPatientId(), isLinked));
                        }))
                .exceptionally(throwable -> {
                    logger.error("Failed to update care context link status for request: {}",
                            request.getResponse().getRequestId(), throwable);
                    return null;
                });
    }

    public CompletionStage<Void> onSmsNotify(HipWebhookController.OnSmsNotifyRequest request) {
        // Log error if present
        if (request.getError() != null) {
            logger.error("SMS notification failed - Code: {}, Message: {}, Request ID: {}",
                    request.getError().getCode(),
                    request.getError().getMessage(),
                    request.getResponse() != null ? request.getResponse().getRequestId() : "N/A");
            return CompletableFuture.completedFuture(null);
        }

        // Only proceed if acknowledgement indicates success
        if (request.getAcknowledgement() == null ||
                !"SUCCESS".equalsIgnoreCase(request.getAcknowledgement().getStatus())) {
            logger.warn("SMS notification not successful - Status: {}, Request ID: {}",
                    request.getAcknowledgement() != null ? request.getAcknowledgement().getStatus() : "N/A",
                    request.getResponse() != null ? request.getResponse().getRequestId() : "N/A");
            return CompletableFuture.completedFuture(null);
        }

        if (request.getResponse() == null || request.getResponse().getRequestId() == null) {
            logger.warn("SMS notification callback missing request ID");
            return CompletableFuture.completedFuture(null);
        }

        return hipInitiatedLinkingDao.getByNotifyRequestId(request.getResponse().getRequestId())
                .thenCompose(hipInitiatedLinkingOptional -> {
                    if(hipInitiatedLinkingOptional.isEmpty()) {
                        logger.warn("No care context found for SMS notify request: {}", request.getResponse().getRequestId());
                        return completedFuture(null);
                    }
                    HipInitiatedLinking hipInitiatedLinking = hipInitiatedLinkingOptional.get().toBuilder()
                            .isPatientNotified(true)
                            .build();
                    return hipInitiatedLinkingDao.upsert(hipInitiatedLinking)
                            .thenAccept(ignore -> {});
                })
                .exceptionally(throwable -> {
                    logger.error("Failed to update patient notification status for request: {}",
                            request.getResponse().getRequestId(), throwable);
                    return null;
                });
    }

    private CompletionStage<Void> sendPatientNotification(HipInitiatedLinking hipInitiatedLinking, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
        return healthFacilityService.getHealthFacility(hipInitiatedLinking.getHealthFacilityId())
                .thenCompose(healthFacility -> abhaRestClient.sendDeepLinkNotification(requestId,
                        timestamp,
                        new SendSmsNotificationRequest()
                                .requestId(requestId)
                                .timestamp(timestamp)
                                .notification(new SendSmsNotificationRequestNotification().phoneNo(patient.getMobileNo())
                                        .hip(new SendSmsNotificationRequestNotificationHip().id(hipInitiatedLinking.getHealthFacilityId()).name(healthFacility.getFacilityName())))))
                .thenCompose(ignore -> hipInitiatedLinkingDao.upsert(hipInitiatedLinking.toBuilder().notifyRequestId(requestId).build()));
    }
}