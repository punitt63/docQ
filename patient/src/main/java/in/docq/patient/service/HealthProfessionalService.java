package in.docq.patient.service;

import in.docq.patient.client.HealthFacilityRestClient;
import in.docq.patient.model.Doctor;
import in.docq.patient.model.HealthProfessional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class HealthProfessionalService {

    private final HealthFacilityRestClient healthFacilityRestClient;

    @Autowired
    public HealthProfessionalService(HealthFacilityRestClient healthFacilityRestClient) {
        this.healthFacilityRestClient = healthFacilityRestClient;
    }

    public CompletionStage<List<Doctor>> list(int stateCode, int districtCode) {
        return healthFacilityRestClient.listDoctors(stateCode, districtCode);
    }
}


