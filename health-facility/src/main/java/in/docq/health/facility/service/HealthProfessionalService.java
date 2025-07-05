package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.health.facility.auth.BackendKeyCloakRestClient;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class HealthProfessionalService {
    private final AbhaRestClient abhaRestClient;
    private final BackendKeyCloakRestClient backendKeyCloakRestClient;
    private final DesktopKeycloakRestClient desktopKeyCloakRestClient;
    private final HealthProfessionalDao healthProfessionalDao;
    private final Cache<String, String> healthProfessionalFacilityMappingCache;

    @Autowired
    public HealthProfessionalService(AbhaRestClient abhaRestClient,
                                     BackendKeyCloakRestClient backendKeyCloakRestClient,
                                     DesktopKeycloakRestClient desktopKeyCloakRestClient,
                                     HealthProfessionalDao healthProfessionalDao) {
        this.abhaRestClient = abhaRestClient;
        this.backendKeyCloakRestClient = backendKeyCloakRestClient;
        this.desktopKeyCloakRestClient = desktopKeyCloakRestClient;
        this.healthProfessionalDao = healthProfessionalDao;
        this.healthProfessionalFacilityMappingCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(10000)
                .maximumSize(100000)
                .build();
    }

    public CompletionStage<HealthProfessionalController.LoginResponse> login(String healthFacilityID, String healthFacilityProfessionalID, String password) {
        HealthProfessional healthProfessional = HealthProfessional.builder()
                .id(healthFacilityProfessionalID)
                .healthFacilityID(healthFacilityID)
                .build();
        return desktopKeyCloakRestClient.getUserAccessToken(healthProfessional.getKeyCloakUserName(), password)
                .thenCompose(desktopClientAccessTokenResponse -> backendKeyCloakRestClient.getUserPermissions(desktopClientAccessTokenResponse.getAccessToken())
                        .thenApply(permissions -> HealthProfessionalController.LoginResponse.builder()
                                .accessToken(desktopClientAccessTokenResponse.getAccessToken())
                                .refreshToken(desktopClientAccessTokenResponse.getRefreshToken())
                                .permissions(permissions)
                                .build()));
    }

    public CompletionStage<GetAccessToken200Response> refreshUserAccessToken(String refreshToken) {
        return desktopKeyCloakRestClient.refreshUserAccessToken(refreshToken);
    }

    public CompletionStage<Void> logout(String bearerToken, String refreshToken) {
        return desktopKeyCloakRestClient.logoutUser(bearerToken, refreshToken);
    }

    public CompletionStage<Void> onBoard(String healthFacilityID, HealthProfessionalController.OnBoardHealthProfessionalRequestBody onBoardHealthProfessionalRequestBody) {
        HealthProfessional healthProfessional = HealthProfessional.builder()
                .id(onBoardHealthProfessionalRequestBody.getHealthProfessionalID())
                .healthFacilityID(healthFacilityID)
                .type(onBoardHealthProfessionalRequestBody.getType())
                .build();
        return abhaRestClient.getHealthFacility(healthFacilityID)
                .thenCompose(ignore -> abhaRestClient.getHealthProfessionalExists(onBoardHealthProfessionalRequestBody.getHealthProfessionalID()))
                .thenCompose(ignore -> backendKeyCloakRestClient.createUserIfNotExists(healthProfessional.getKeyCloakUserName(), onBoardHealthProfessionalRequestBody.getPassword(), List.of(healthProfessional.getKeycloakRole())))
                .thenCompose(ignore -> backendKeyCloakRestClient.mapRealmRole(healthProfessional.getKeyCloakUserName(), healthProfessional.getKeycloakRole()))
                .thenCompose(ignore -> healthProfessionalDao.insert(healthFacilityID, onBoardHealthProfessionalRequestBody.getHealthProfessionalID(), healthProfessional.getType()));
    }

    public CompletionStage<HealthProfessional> get(String healthFacilityID, String healthProfessionalID) {
        return healthProfessionalDao.get(healthFacilityID, healthProfessionalID);
    }
}
