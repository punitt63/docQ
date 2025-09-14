package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.HIPInitiatedGenerateTokenRequest;
import in.docq.health.facility.dao.HIPLinkingTokenDao;
import in.docq.health.facility.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Service
public class HIPLinkingTokenService {
    private static final Logger logger = LoggerFactory.getLogger(HIPLinkingTokenService.class);

    private final HIPLinkingTokenDao hipLinkingTokenDao;
    private final AbhaRestClient abhaRestClient;
    private final String xCmId;

    @Autowired
    public HIPLinkingTokenService(HIPLinkingTokenDao hipLinkingTokenDao, AbhaRestClient abhaRestClient, @Value("${x.cm.id}") String xCmId) {
        this.hipLinkingTokenDao = hipLinkingTokenDao;
        this.abhaRestClient = abhaRestClient;
        this.xCmId = xCmId;
    }

    public CompletionStage<Void> upsert(HIPLinkingToken hipLinkingToken) {
        return hipLinkingTokenDao.upsert(hipLinkingToken);
    }

    public CompletionStage<Void> updateToken(String newToken, String patientId, String lastTokenRequestId) {
        return hipLinkingTokenDao.update(newToken, patientId, lastTokenRequestId)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to update HIP linking token for patient: {}", patientId, throwable);
                    } else {
                        logger.info("Successfully updated HIP linking token for patient: {}", patientId);
                    }
                });
    }

    public CompletionStage<Optional<HIPLinkingToken>> getToken(String healthFacilityId, String patientId) {
        return hipLinkingTokenDao.get(healthFacilityId, patientId)
                .whenComplete((token, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to retrieve HIP linking token for patient: {}", patientId, throwable);
                    } else if (token.isPresent()) {
                        logger.debug("Retrieved HIP linking token for patient: {}", patientId);
                    } else {
                        logger.debug("No HIP linking token found for patient: {}", patientId);
                    }
                });
    }

    public CompletionStage<HIPLinkingToken> getByRequestId(String patientId, String lastTokenRequestId) {
        return hipLinkingTokenDao.getByRequestId(patientId, lastTokenRequestId);
    }

    public CompletionStage<Void> generateLinkingToken(String healthFacilityID, String appointmentID, Patient patient) {
        String requestId = UUID.randomUUID().toString();
        return upsert(HIPLinkingToken.builder()
                        .healthFacilityId(healthFacilityID)
                        .patientId(patient.getId())
                        .lastTokenRequestAppointmentId(appointmentID)
                        .lastTokenRequestId(requestId)
                        .lastToken(null)
                        .build())
                .thenCompose(ignore -> abhaRestClient.generateLinkingToken(requestId, Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), healthFacilityID,
                        new HIPInitiatedGenerateTokenRequest()
                                .abhaNumber(patient.getAbhaNo())
                                .abhaAddress(patient.getAbhaAddress())
                                .name(patient.getName())
                                .gender(HIPInitiatedGenerateTokenRequest.GenderEnum.valueOf(patient.getGender()))
                                .yearOfBirth(patient.getYearOfBirth())));
    }
}