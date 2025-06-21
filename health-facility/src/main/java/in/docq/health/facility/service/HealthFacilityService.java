package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class HealthFacilityService {
    private final AbhaRestClient abhaRestClient;

    @Autowired
    public HealthFacilityService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<Void> onboardHealthFacility(String facilityName, String facilityId) {
        return abhaRestClient.registerHFRToBridge(facilityId, facilityName);
    }
}
