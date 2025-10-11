package in.docq.patient.service;

import in.docq.patient.client.HealthFacilityRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class PatientService {

    private final HealthFacilityRestClient healthFacilityRestClient;

    @Autowired
    public PatientService(HealthFacilityRestClient healthFacilityRestClient) {
        this.healthFacilityRestClient = healthFacilityRestClient;
    }

    public CompletionStage<Void> createPatientIfNotExists(String healthFacilityId, HealthFacilityRestClient.CreatePatientRequestBody requestBody) {
        return healthFacilityRestClient.createPatientIfNotExists(healthFacilityId, requestBody);
    }
}


