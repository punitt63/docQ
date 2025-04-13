package in.docq.health.facility.service;

import in.docq.health.facility.controller.OPDController;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.model.OPD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class OPDService {
    private final OPDDao opdDao;

    @Autowired
    public OPDService(OPDDao opdDao) {
        this.opdDao = opdDao;
    }

    public CompletionStage<Void> insert(String healthFacilityID, String healthProfessionalID, OPDController.CreateOPDRequestBody createOPDRequestBody) {
        return opdDao.insert(OPD.getEffectiveOPDs(healthFacilityID, healthProfessionalID, createOPDRequestBody));
    }

    public CompletionStage<OPD> update(String healthFacilityID, String healthProfessionalID, LocalDate opdDate, String id, OPDController.UpdateOPDRequestBody updateOPDRequestBody) {
        return opdDao.get(opdDate, id)
                .thenCompose(existingOPD -> opdDao.update(OPD.builder().from(existingOPD, updateOPDRequestBody).build()))
                .thenCompose(ignore -> opdDao.get(opdDate, id));
    }

    public CompletionStage<List<OPD>> list(String healthFacilityID, String healthProfessionalID, LocalDate startDate, LocalDate endDate) {
        return opdDao.list(healthFacilityID, healthProfessionalID, startDate, endDate);
    }

    public CompletionStage<OPD> get(LocalDate opdDate, String id) {
        return opdDao.get(opdDate, id);
    }
}
