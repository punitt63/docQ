package in.docq.data.setup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.docq.data.setup.client.HealthFacilityRestClient;
import in.docq.keycloak.rest.client.api.AuthenticationApi;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class DataSetupOrchestrator {
    private final HealthFacilityRestClient healthFacilityRestClient;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${keycloak.base.url:http://localhost:8080}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm:health-facility}")
    private String realm;


    @Value("${keycloak.desktop.client.id:health-facility-desktop-app}")
    private String desktopClientId;

    @Value("${keycloak.desktop.client.secret}")
    private String desktopClientSecret;

    @Autowired
    public DataSetupOrchestrator(HealthFacilityRestClient healthFacilityRestClient,
                                 ResourceLoader resourceLoader,
                                 ObjectMapper objectMapper) {
        this.healthFacilityRestClient = healthFacilityRestClient;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    /**
     * Execute the complete data setup flow via REST API calls
     */
    public void executeDataSetup() {
        try {
            log.info("Starting data setup process...");

            // Step 1: Get admin token from Keycloak
            String adminToken = getAdminToken();
            log.info("Admin token obtained successfully");

            // Step 2: Load configuration
            DataSetupConfig config = loadDataSetupConfig();
            String facilityId = config.getHealthFacility().getFacilityId();
            
            log.info("Loaded configuration for facility: {}", facilityId);

            // Step 3: Onboard facility managers
            log.info("Onboarding facility managers...");
            for (FacilityManagerData managerData : config.getFacilityManagers()) {
                try {
                    log.info("Attempting to onboard facility manager: {}", managerData.getFacilityManagerID());
                    healthFacilityRestClient.onboardFacilityManager(
                            facilityId,
                            managerData.getFacilityManagerID(),
                            managerData.getPassword(),
                            adminToken
                    ).block();
                    log.info("Facility manager {} onboarded successfully", managerData.getFacilityManagerID());
                } catch (Exception e) {
                    log.error("Failed to onboard facility manager {}: {}", managerData.getFacilityManagerID(), e.getMessage());
                }
            }

            // Step 4: Login as facility manager and onboard doctors
            log.info("Onboarding doctors...");
            for (DoctorData doctorData : config.getDoctors()) {
                try {
                    // First, login as facility manager
                    String facilityManagerId = doctorData.getFacilityManagerID();
                    String facilityManagerPassword = getFacilityManagerPassword(config, facilityManagerId);

                    var loginResponse = healthFacilityRestClient.loginFacilityManager(
                            facilityId,
                            facilityManagerId,
                            facilityManagerPassword
                    ).block();

                    String managerToken = loginResponse.getAccessToken();

                    // Then onboard the doctor using manager's token
                    healthFacilityRestClient.onboardDoctor(
                            facilityId,
                            doctorData.getDoctorID(),
                            doctorData.getPassword(),
                            doctorData.getFacilityManagerID(),
                            managerToken
                    ).block();

                    log.info("Doctor {} onboarded successfully under facility manager {}",
                            doctorData.getDoctorID(), doctorData.getFacilityManagerID());
                } catch (Exception e) {
                    log.error("Failed to onboard doctor {}: {}", doctorData.getDoctorID(), e.getMessage());
                }
            }

            log.info("Data setup completed successfully");

        } catch (Exception e) {
            log.error("Data setup failed: ", e);
            throw new RuntimeException("Data setup failed", e);
        }
    }

    /**
     * Get admin token from Keycloak using direct access grant
     * Uses the same approach as test code: getUserAccessToken on desktopKeycloakRestClient
     */
    private String getAdminToken() {
        try {
            log.info("Getting admin token for user: docq-admin");
            
            // Create ApiClient with OkHttpClient
            okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient();
            in.docq.keycloak.rest.client.ApiClient apiClient = new in.docq.keycloak.rest.client.ApiClient(keycloakBaseUrl, httpClient);
            AuthenticationApi authApi = new AuthenticationApi(apiClient);
            
            // Get token using docq-admin credentials
            // Same credentials as used in tests
            GetAccessToken200Response response = authApi.getUserAccessTokenAsync(
                realm, 
                "docq-admin",  // Username from RolesToResourcesMapper
                "xf~8QgK^]gw@,",  // Password from RolesToResourcesMapper  
                desktopClientId, 
                desktopClientSecret
            ).toCompletableFuture().get();

            log.info("Admin token obtained successfully");
            return response.getAccessToken();
        } catch (Exception e) {
            log.error("Failed to get admin token: ", e);
            throw new RuntimeException("Failed to get admin token", e);
        }
    }

    /**
     * Load data setup configuration from JSON file
     */
    private DataSetupConfig loadDataSetupConfig() throws IOException {
        var resource = resourceLoader.getResource("classpath:data-setup-config.json");
        return objectMapper.readValue(resource.getInputStream(), DataSetupConfig.class);
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

