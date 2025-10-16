package in.docq.health.facility.service;

import in.docq.health.facility.controller.OPDController;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class OPDService {
    private final OPDDao opdDao;
    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public OPDService(OPDDao opdDao, HealthProfessionalService healthProfessionalService) {
        this.opdDao = opdDao;
        this.healthProfessionalService = healthProfessionalService;
    }

    public CompletionStage<Void> insert(String healthFacilityID, String healthProfessionalID, OPDController.CreateOPDRequestBody createOPDRequestBody) {
        return opdDao.insert(OPD.getEffectiveOPDs(healthFacilityID, healthProfessionalID, createOPDRequestBody));
    }

    public CompletionStage<OPD> update(HealthProfessionalType originator, HealthProfessional doctor, LocalDate opdDate, String id, OPDController.UpdateOPDRequestBody updateOPDRequestBody) {
        return opdDao.get(opdDate, id)
                .thenCompose(existingOPD -> opdDao.update(OPD.builder().from(existingOPD, updateOPDRequestBody).build())
                        .thenApply(updatedOPD -> {
                            sendStateChangeMessage(originator, doctor, updatedOPD, existingOPD);
                            return updatedOPD;
                        }));
    }

    private void sendStateChangeMessage(HealthProfessionalType originator, HealthProfessional doctor, OPD newOPD, OPD oldOPD) {
        if(newOPD.getState() != oldOPD.getState()) {
            healthProfessionalService.sendMessageToCounterPart(originator, doctor, WsConnectionHandler.StateChangeMessage.builder()
                    .objectId(newOPD.getId())
                    .objectType("OPD")
                    .fromState(oldOPD.getState().name())
                    .toState(newOPD.getState().name())
                    .build());
        }
    }

    public CompletionStage<List<OPD>> list(String healthFacilityID, String healthProfessionalID, LocalDate startDate, LocalDate endDate) {
        return opdDao.list(healthFacilityID, healthProfessionalID, startDate, endDate);
    }

    public CompletionStage<OPD> get(LocalDate opdDate, String id) {
        return opdDao.get(opdDate, id);
    }
}
