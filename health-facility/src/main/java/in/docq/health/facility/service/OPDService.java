package in.docq.health.facility.service;

import in.docq.health.facility.controller.OPDController;
import in.docq.health.facility.dao.OPDDao;
import in.docq.health.facility.model.OPD;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class OPDService {
    private final OPDDao opdDao;

    public OPDService(OPDDao opdDao) {
        this.opdDao = opdDao;
    }

    public CompletionStage<Void> insert(String healthFacilityID, String healthProfessionalID, OPDController.CreateOPDRequestBody createOPDRequestBody) {
        return opdDao.insert(OPD.builder().from(healthFacilityID, healthProfessionalID, createOPDRequestBody).build());
    }

    public CompletionStage<OPD> update(String healthFacilityID, String healthProfessionalID, String name, OPDController.UpdateOPDRequestBody updateOPDRequestBody) {
        return opdDao.get(healthFacilityID, healthProfessionalID, name)
                .thenCompose(existingOPD -> opdDao.update(OPD.builder().from(existingOPD, updateOPDRequestBody).build()))
                .thenCompose(ignore -> opdDao.get(healthFacilityID, healthProfessionalID, name));
    }

    public CompletionStage<OPD> get(String healthFacilityID, String healthProfessionalID, String name) {
        return opdDao.get(healthFacilityID, healthProfessionalID, name);
    }
}
