package in.docq.health.facility.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.health.facility.auth.BackendKeyCloakRestClient;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthProfessionalController;
import in.docq.health.facility.dao.DoctorDao;
import in.docq.health.facility.dao.FacilityManagerDao;
import in.docq.health.facility.dao.HealthProfessionalDao;
import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import static in.docq.health.facility.model.HealthProfessionalType.DOCTOR;
import static in.docq.health.facility.model.HealthProfessionalType.FACILITY_MANAGER;

@Service
public class HealthProfessionalService {
    private final AbhaRestClient abhaRestClient;
    private final BackendKeyCloakRestClient backendKeyCloakRestClient;
    private final DesktopKeycloakRestClient desktopKeyCloakRestClient;
    private final HealthProfessionalDao healthProfessionalDao;
    private final FacilityManagerDao facilityManagerDao;
    private final DoctorDao doctorDao;
    private final Cache<String, String> healthProfessionalFacilityMappingCache;
    private final WsConnectionHandler wsConnectionHandler;

    @Autowired
    public HealthProfessionalService(AbhaRestClient abhaRestClient,
                                     BackendKeyCloakRestClient backendKeyCloakRestClient,
                                     DesktopKeycloakRestClient desktopKeyCloakRestClient,
                                     HealthProfessionalDao healthProfessionalDao, FacilityManagerDao facilityManagerDao, DoctorDao doctorDao, WsConnectionHandler wsConnectionHandler) {
        this.abhaRestClient = abhaRestClient;
        this.backendKeyCloakRestClient = backendKeyCloakRestClient;
        this.desktopKeyCloakRestClient = desktopKeyCloakRestClient;
        this.healthProfessionalDao = healthProfessionalDao;
        this.facilityManagerDao = facilityManagerDao;
        this.doctorDao = doctorDao;
        this.wsConnectionHandler = wsConnectionHandler;
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

    public CompletionStage<Void> onBoardFacilityManager(String healthFacilityID, HealthProfessionalController.OnBoardFacilityManagerRequestBody onBoardFacilityManagerRequestBody) {
        HealthProfessional healthProfessional = HealthProfessional.builder()
                .id(onBoardFacilityManagerRequestBody.getFacilityManagerID())
                .healthFacilityID(healthFacilityID)
                .type(FACILITY_MANAGER)
                .build();
        return abhaRestClient.getHealthFacility(healthFacilityID)
                .thenCompose(ignore -> abhaRestClient.getHealthProfessionalExists(onBoardFacilityManagerRequestBody.getFacilityManagerID()))
                .thenCompose(ignore -> backendKeyCloakRestClient.createUserIfNotExists(healthProfessional.getKeyCloakUserName(), onBoardFacilityManagerRequestBody.getPassword(), List.of(healthProfessional.getKeycloakRole())))
                .thenCompose(ignore -> backendKeyCloakRestClient.mapRealmRole(healthProfessional.getKeyCloakUserName(), healthProfessional.getKeycloakRole()))
                .thenCompose(ignore -> facilityManagerDao.insert(healthFacilityID, onBoardFacilityManagerRequestBody.getFacilityManagerID()));
    }

    public CompletionStage<Void> onBoardDoctor(String healthFacilityID, HealthProfessionalController.OnBoardDoctorRequestBody onBoardDoctorRequestBody) {
        HealthProfessional healthProfessional = HealthProfessional.builder()
                .id(onBoardDoctorRequestBody.getDoctorID())
                .healthFacilityID(healthFacilityID)
                .type(DOCTOR)
                .build();
        return abhaRestClient.getHealthFacility(healthFacilityID)
                .thenCompose(ignore -> abhaRestClient.getHealthProfessionalExists(onBoardDoctorRequestBody.getDoctorID()))
                .thenCompose(ignore -> backendKeyCloakRestClient.createUserIfNotExists(healthProfessional.getKeyCloakUserName(), onBoardDoctorRequestBody.getPassword(), List.of(healthProfessional.getKeycloakRole())))
                .thenCompose(ignore -> backendKeyCloakRestClient.mapRealmRole(healthProfessional.getKeyCloakUserName(), healthProfessional.getKeycloakRole()))
                .thenCompose(ignore -> doctorDao.insert(healthFacilityID, onBoardDoctorRequestBody.getDoctorID(), onBoardDoctorRequestBody.getFacilityManagerID()));
    }

    public CompletionStage<HealthProfessional> get(String healthFacilityID, String healthProfessionalID) {
        return facilityManagerDao.get(healthFacilityID, healthProfessionalID)
                .thenCompose(facilityManager -> doctorDao.get(healthFacilityID, healthProfessionalID)
                        .thenApply(doctor -> {
                            if(facilityManager.isPresent()) {
                                return HealthProfessional.builder()
                                        .id(facilityManager.get().getId())
                                        .healthFacilityID(facilityManager.get().getHealthFacilityID())
                                        .type(FACILITY_MANAGER)
                                        .build();
                            }
                            if(doctor.isPresent()) {
                                return HealthProfessional.builder()
                                        .id(doctor.get().getId())
                                        .healthFacilityID(doctor.get().getHealthFacilityID())
                                        .type(DOCTOR)
                                        .build();
                            }
                            throw new CompletionException(new HealthProfessionalNotFound(healthFacilityID, healthProfessionalID));
                        }));

    }

    public CompletionStage<Void> sendMessageToCounterPart(HealthProfessionalType originatorType, HealthProfessional doctor, WsConnectionHandler.StateChangeMessage stateChangeMessage) {
        return getCounterPart(originatorType, doctor)
                .thenAccept(hp -> wsConnectionHandler.sendMessage(hp, stateChangeMessage));
    }

    private CompletionStage<HealthProfessional> getCounterPart(HealthProfessionalType originatorType, HealthProfessional doctor) {
        if(originatorType.equals(DOCTOR)) {
            return doctorDao.get(doctor.getHealthFacilityID(), doctor.getId())
                    .thenCompose(doc -> facilityManagerDao.get(doctor.getHealthFacilityID(), doc.get().getFacilityManagerID()))
                    .thenApply(facilityManager -> HealthProfessional.builder()
                            .id(facilityManager.get().getId())
                            .healthFacilityID(facilityManager.get().getHealthFacilityID())
                            .type(FACILITY_MANAGER)
                            .build());
        }

        return doctorDao.get(doctor.getHealthFacilityID(), doctor.getId())
                .thenApply(doc -> HealthProfessional.builder()
                        .id(doc.get().getId())
                        .healthFacilityID(doc.get().getHealthFacilityID())
                        .type(DOCTOR)
                        .build());
    }
}
