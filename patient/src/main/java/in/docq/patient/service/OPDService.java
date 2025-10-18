package in.docq.patient.service;

import in.docq.patient.client.HealthFacilityRestClient;
import in.docq.patient.model.OPD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class OPDService {

    private final HealthFacilityRestClient healthFacilityRestClient;

    @Autowired
    public OPDService(HealthFacilityRestClient healthFacilityRestClient) {
        this.healthFacilityRestClient = healthFacilityRestClient;
    }

    public CompletionStage<List<OPD>> listOPDs(String healthFacilityId,
                                              String healthFacilityProfessionalId,
                                              LocalDate startDate,
                                              LocalDate endDate) {
        return healthFacilityRestClient.listOPDs(healthFacilityId, healthFacilityProfessionalId, startDate, endDate);
    }
}
