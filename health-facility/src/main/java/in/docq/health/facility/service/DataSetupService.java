package in.docq.health.facility.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.docq.health.facility.auth.DesktopKeycloakRestClient;
import in.docq.health.facility.controller.HealthFacilityController;
import in.docq.health.facility.controller.HealthProfessionalController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
@Service
public class DataSetupService {

    private final DesktopKeycloakRestClient desktopKeycloakRestClient;
    private final HealthFacilityService healthFacilityService;
    private final HealthProfessionalService healthProfessionalService;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${keycloak.admin.username:docq-admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:xf~8QgK^]gw@,}")
    private String adminPassword;

    @Autowired
    public DataSetupService(DesktopKeycloakRestClient desktopKeycloakRestClient,
                           HealthFacilityService healthFacilityService,
                           HealthProfessionalService healthProfessionalService,
                           ResourceLoader resourceLoader,
                           ObjectMapper objectMapper) {
        this.desktopKeycloakRestClient = desktopKeycloakRestClient;
        this.healthFacilityService = healthFacilityService;
        this.healthProfessionalService = healthProfessionalService;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    /**
     * Main method to execute the complete data setup flow
     */
    public CompletionStage<Void> executeDataSetup() {
        log.info("Starting data setup process...");
        
        return createAdminToken()
                /*.thenCompose(adminToken -> {
                    log.info("Admin token created successfully");
                    return onboardHealthFacility(adminToken);
                })*/
                .thenCompose(adminToken -> {
                    log.info("Health facility onboarded successfully");
                    return onboardFacilityManagers(adminToken);
                })
                .thenCompose(adminToken -> {
                    log.info("Facility managers onboarded successfully");
                    return onboardDoctors(adminToken);
                })
                .thenApply(ignore -> {
                    log.info("Data setup completed successfully");
                    return (Void) null;
                })
                .exceptionally(throwable -> {
                    log.error("Data setup failed: ", throwable);
                    throw new RuntimeException("Data setup failed", throwable);
                });
    }

    /**
     * Step 1: Create admin token using direct access grant
     */
    private CompletionStage<String> createAdminToken() {
        log.info("Creating admin token for user: {}", adminUsername);
        
        return desktopKeycloakRestClient.getUserAccessToken(adminUsername, adminPassword)
                .thenApply(response -> {
                    log.info("Admin token created successfully");
                    return response.getAccessToken();
                })
                .exceptionally(throwable -> {
                    log.error("Failed to create admin token: ", throwable);
                    throw new RuntimeException("Failed to create admin token", throwable);
                });
    }

    /**
     * Step 2: Use admin token to onboard health facility
     */
    private CompletionStage<String> onboardHealthFacility(String adminToken) {
        log.info("Onboarding health facility...");
        
        try {
            DataSetupConfig config = loadDataSetupConfig();
            
            HealthFacilityController.onBoardHealthFacilityRequestBody request = 
                HealthFacilityController.onBoardHealthFacilityRequestBody.builder()
                    .facilityId(config.getHealthFacility().getFacilityId())
                    .facilityName(config.getHealthFacility().getFacilityName())
                    .build();

            return healthFacilityService.onboardHealthFacility(
                request.getFacilityName(), 
                request.getFacilityId()
            ).thenApply(ignore -> {
                log.info("Health facility {} onboarded successfully", request.getFacilityId());
                return adminToken;
            });
            
        } catch (Exception e) {
            log.error("Failed to onboard health facility: ", e);
            return completedFuture(adminToken);
        }
    }

    /**
     * Step 3: Use admin token to onboard facility managers
     */
    private CompletionStage<String> onboardFacilityManagers(String adminToken) {
        log.info("Onboarding facility managers...");
        
        try {
            DataSetupConfig config = loadDataSetupConfig();
            String facilityId = config.getHealthFacility().getFacilityId();
            
            CompletionStage<String> result = completedFuture(adminToken);
            
            for (FacilityManagerData managerData : config.getFacilityManagers()) {
                result = result.thenCompose(token -> {
                    HealthProfessionalController.OnBoardFacilityManagerRequestBody request = 
                        HealthProfessionalController.OnBoardFacilityManagerRequestBody.builder()
                            .facilityManagerID(managerData.getFacilityManagerID())
                            .password(managerData.getPassword())
                            .build();

                    return healthProfessionalService.onBoardFacilityManager(facilityId, request)
                            .thenApply(ignore -> {
                                log.info("Facility manager {} onboarded successfully", managerData.getFacilityManagerID());
                                return token;
                            });
                });
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Failed to onboard facility managers: ", e);
            return completedFuture(adminToken);
        }
    }

    /**
     * Step 4: Login as facility manager and onboard doctors
     */
    private CompletionStage<String> onboardDoctors(String adminToken) {
        log.info("Onboarding doctors...");
        
        try {
            DataSetupConfig config = loadDataSetupConfig();
            String facilityId = config.getHealthFacility().getFacilityId();
            
            CompletionStage<String> result = completedFuture(adminToken);
            
            for (DoctorData doctorData : config.getDoctors()) {
                result = result.thenCompose(token -> {
                    // First login as facility manager to get their token
                    return healthProfessionalService.login(facilityId, doctorData.getFacilityManagerID(), 
                        getFacilityManagerPassword(config, doctorData.getFacilityManagerID()))
                        .thenCompose(loginResponse -> {
                            // Use facility manager token to onboard doctor
                            HealthProfessionalController.OnBoardDoctorRequestBody request = 
                                HealthProfessionalController.OnBoardDoctorRequestBody.builder()
                                    .doctorID(doctorData.getDoctorID())
                                    .password(doctorData.getPassword())
                                    .facilityManagerID(doctorData.getFacilityManagerID())
                                    .build();

                            return healthProfessionalService.onBoardDoctor(facilityId, request)
                                    .thenApply(ignore -> {
                                        log.info("Doctor {} onboarded successfully under facility manager {}", 
                                            doctorData.getDoctorID(), doctorData.getFacilityManagerID());
                                        return token;
                                    });
                        });
                });
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Failed to onboard doctors: ", e);
            return completedFuture(adminToken);
        }
    }

    /**
     * Helper method to get facility manager password from config
     */
    private String getFacilityManagerPassword(DataSetupConfig config, String facilityManagerID) {
        return config.getFacilityManagers().stream()
                .filter(fm -> fm.getFacilityManagerID().equals(facilityManagerID))
                .findFirst()
                .map(FacilityManagerData::getPassword)
                .orElseThrow(() -> new RuntimeException("Facility manager not found: " + facilityManagerID));
    }

    /**
     * Load data setup configuration from JSON file
     */
    private DataSetupConfig loadDataSetupConfig() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:data-setup-config.json");
        return objectMapper.readValue(resource.getInputStream(), DataSetupConfig.class);
    }

    // Data models for JSON deserialization
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSetupConfig {
        private HealthFacilityData healthFacility;
        private List<FacilityManagerData> facilityManagers;
        private List<DoctorData> doctors;
        private List<PatientData> patients;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthFacilityData {
        private String facilityId;
        private String facilityName;
        private int stateCode;
        private int districtCode;
        private String address;
        private String pincode;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityManagerData {
        private String facilityManagerID;
        private String password;
        private String name;
        private String email;
        private String phone;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorData {
        private String doctorID;
        private String password;
        private String facilityManagerID;
        private String name;
        private String speciality;
        private String email;
        private String phone;
        private String qualification;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientData {
        private String abhaAddress;
        private String name;
        private String phone;
        private String email;
        private String dateOfBirth;
        private String gender;
    }
}