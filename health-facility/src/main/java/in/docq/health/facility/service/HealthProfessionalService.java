package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.keycloak.rest.client.KeyCloakRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class HealthProfessionalService {
    private final AbhaRestClient abhaRestClient;
    private final KeyCloakRestClient keyCloakRestClient;
    private final HealthProfessionalDao healthProfessionalDao;

    @Autowired
    public HealthProfessionalService(AbhaRestClient abhaRestClient, KeyCloakRestClient keyCloakRestClient, HealthProfessionalDao healthProfessionalDao) {
        this.abhaRestClient = abhaRestClient;
        this.keyCloakRestClient = keyCloakRestClient;
        this.healthProfessionalDao = healthProfessionalDao;
    }

    public CompletionStage<Void> onBoard(String healthFacilityID, HealthProfessionalController.OnBoardHealthProfessionalRequestBody onBoardHealthProfessionalRequestBody) {
        HealthProfessional healthProfessional = HealthProfessional.builder()
                .id(onBoardHealthProfessionalRequestBody.getHealthProfessionalID())
                .healthFacilityID(healthFacilityID)
                .type(onBoardHealthProfessionalRequestBody.getType())
                .build();
        return abhaRestClient.getHealthFacility(healthFacilityID)
                .thenCompose(ignore -> abhaRestClient.getHealthProfessionalExists(onBoardHealthProfessionalRequestBody.getHealthProfessionalID()))
                .thenCompose(ignore -> keyCloakRestClient.createUserIfNotExists(healthProfessional.getKeyCloakUserName(), onBoardHealthProfessionalRequestBody.getPassword(), List.of(healthProfessional.getKeycloakRole())))
                .thenCompose(ignore -> healthProfessionalDao.insert(healthFacilityID, onBoardHealthProfessionalRequestBody.getHealthProfessionalID(), healthProfessional.getType()));
    }

    public CompletionStage<HealthProfessional> get(String healthFacilityID, String healthProfessionalID) {
        return healthProfessionalDao.get(healthFacilityID, healthProfessionalID);
    }
}
