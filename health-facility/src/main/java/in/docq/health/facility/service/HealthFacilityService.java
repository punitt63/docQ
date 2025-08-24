package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiException;
import in.docq.abha.rest.client.model.SearchFacilitiesData;
import in.docq.abha.rest.client.model.SearchForFacilitiesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class HealthFacilityService {
    private final AbhaRestClient abhaRestClient;
    private final Cache<String, SearchFacilitiesData> facilityCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .build();

    @Autowired
    public HealthFacilityService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<Void> onboardHealthFacility(String facilityName, String facilityId) {
        return abhaRestClient.registerHFRToBridge(facilityId, facilityName);
    }

    public CompletionStage<SearchFacilitiesData> getHealthFacility(String facilityID) {
        SearchFacilitiesData cachedFacility = facilityCache.getIfPresent(facilityID);
        if(cachedFacility != null) {
            return completedFuture(cachedFacility);
        }
        return abhaRestClient.getHealthFacility(facilityID);
    }
}
