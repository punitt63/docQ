package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.dao.CareContextDao;
import in.docq.health.facility.dao.UserInitiatedLinkingDao;
import in.docq.health.facility.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class CareContextService {
    private static final Logger logger = LoggerFactory.getLogger(CareContextService.class);
    private final CareContextDao careContextDao;
    private final OPDService opdService;
    private final Cache<String, OPD> opdCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .build();

    @Autowired
    public CareContextService(CareContextDao careContextDao, OPDService opdService) {
        this.careContextDao = careContextDao;
        this.opdService = opdService;
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

    public CompletionStage<List<CareContext>> getUnlinkedCareContexts(String patientId, String healthFacilityId) {
        return careContextDao.getUnlinkedByPatientAndFacility(patientId, healthFacilityId);
    }

    public CompletionStage<List<CareContext>> getLinkedCareContexts(String healthFacilityId, String patientId, Long fromAppointmentTime, Long toAppointmentTime) {
        return careContextDao.getLinkedByPatientAndFacility(patientId, healthFacilityId, fromAppointmentTime, toAppointmentTime);
    }

    public CompletionStage<Void> upsert(String healthFacilityId, String appointmentId, String patientId,  boolean isLinked) {
        return careContextDao.upsert(CareContext.builder()
                .healthFacilityId(healthFacilityId)
                .appointmentID(appointmentId)
                .patientId(patientId)
                .isLinked(isLinked)
                .build());
    }
}
