package in.docq.health.facility.service;

import in.docq.abha.rest.client.api.HealthFacilitySearchApi;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.model.HealthProfessionalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class HealthProfessionalService {
    private final HealthProfessionalDao healthProfessionalDao;

    @Autowired
    public HealthProfessionalService(HealthProfessionalDao healthProfessionalDao) {
        this.healthProfessionalDao = healthProfessionalDao;
    }

    public CompletionStage<Void> onBoard(String healthFacilityID, HealthProfessionalController.OnBoardHealthProfessionalRequestBody onBoardHealthProfessionalRequestBody) {
        return healthProfessionalDao.insert(healthFacilityID, onBoardHealthProfessionalRequestBody.getHealthProfessionalID(), onBoardHealthProfessionalRequestBody.getType());
    }
}
