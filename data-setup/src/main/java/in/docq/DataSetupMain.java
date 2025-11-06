package in.docq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.docq.data.setup.client.HealthFacilityRestClient;
import in.docq.keycloak.rest.client.ApiClient;
import in.docq.keycloak.rest.client.api.AuthenticationApi;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Standalone main class for data setup.
 * Similar to KeycloakSetupOrchestrator - runs outside Spring context.
 */
public class DataSetupMain {

    private static final String KEYCLOAK_BASE_URL = getEnv("KEYCLOAK_BASE_URL", "http://keycloak:8080");
    private static final String REALM = getEnv("KEYCLOAK_REALM", "health-facility");
    private static final String DESKTOP_CLIENT_ID = getEnv("KEYCLOAK_DESKTOP_CLIENT_ID", "health-facility-desktop-app");
    private static final String DESKTOP_CLIENT_SECRET = getEnv("KEYCLOAK_DESKTOP_CLIENT_SECRET", "");
    private static final String HEALTH_FACILITY_BASE_URL = getEnv("HEALTH_FACILITY_BASE_URL", "http://health-facility:9097");

    private static final String ADMIN_USERNAME = "docq-admin";
    private static final String ADMIN_PASSWORD = "xf~8QgK^]gw@,";

    // Configuration model classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class DataSetupConfig {
        public HealthFacilityData healthFacility;
        public List<FacilityManagerData> facilityManagers;
        public List<DoctorData> doctors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HealthFacilityData {
        public String facilityId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class FacilityManagerData {
        public String facilityManagerID;
        public String password;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class DoctorData {
        public String doctorID;
        public String password;
        public String facilityManagerID;
    }

    public static void main(String[] args) {
        System.out.println("Starting data setup...");
        
        try {
            // Step 1: Get admin token from Keycloak
            System.out.println("Step 1: Getting admin token from Keycloak...");
            String adminToken = getAdminToken();
            System.out.println("Admin token obtained successfully");

            // Step 2: Load configuration
            System.out.println("Step 2: Loading data setup configuration...");
            DataSetupConfig config = loadDataSetupConfig();
            String facilityId = config.healthFacility.facilityId;
            System.out.println("Loaded configuration for facility: " + facilityId);

            // Step 3: Create REST client
            HealthFacilityRestClient restClient = new HealthFacilityRestClient(HEALTH_FACILITY_BASE_URL);

            // Step 4: Onboard facility managers
            System.out.println("Step 3: Onboarding facility managers...");
            for (FacilityManagerData managerData : config.facilityManagers) {
                try {
                    System.out.println("Attempting to onboard facility manager: " + managerData.facilityManagerID);
                    restClient.onboardFacilityManager(
                            facilityId,
                            managerData.facilityManagerID,
                            managerData.password,
                            adminToken
                    ).block();
                    System.out.println("Facility manager " + managerData.facilityManagerID + " onboarded successfully");
                } catch (Exception e) {
                    System.err.println("Failed to onboard facility manager " + managerData.facilityManagerID + ": " + e.getMessage());
                }
            }

            // Step 5: Onboard doctors
            System.out.println("Step 4: Onboarding doctors...");
            for (DoctorData doctorData : config.doctors) {
                try {
                    // First, login as facility manager
                    String facilityManagerId = doctorData.facilityManagerID;
                    String facilityManagerPassword = getFacilityManagerPassword(config, facilityManagerId);

                    var loginResponse = restClient.loginFacilityManager(
                            facilityId,
                            facilityManagerId,
                            facilityManagerPassword
                    ).block();

                    String managerToken = loginResponse.getAccessToken();

                    // Then onboard the doctor using manager's token
                    restClient.onboardDoctor(
                            facilityId,
                            doctorData.doctorID,
                            doctorData.password,
                            doctorData.facilityManagerID,
                            managerToken
                    ).block();

                    System.out.println("Doctor " + doctorData.doctorID + " onboarded successfully under facility manager " + doctorData.facilityManagerID);
                } catch (Exception e) {
                    System.err.println("Failed to onboard doctor " + doctorData.doctorID + ": " + e.getMessage());
                }
            }

            System.out.println("Data setup completed successfully!");

        } catch (Exception e) {
            System.err.println("Error during data setup: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String getAdminToken() {
        try {
            System.out.println("Getting admin token for user: " + ADMIN_USERNAME);
            
            // Create ApiClient with OkHttpClient
            OkHttpClient httpClient = new OkHttpClient();
            ApiClient apiClient = new ApiClient(KEYCLOAK_BASE_URL, httpClient);
            AuthenticationApi authApi = new AuthenticationApi(apiClient);
            
            // Get token using docq-admin credentials
            GetAccessToken200Response response = authApi.getUserAccessTokenAsync(
                REALM, 
                ADMIN_USERNAME,
                ADMIN_PASSWORD,
                DESKTOP_CLIENT_ID, 
                DESKTOP_CLIENT_SECRET
            ).toCompletableFuture().get();

            System.out.println("Admin token obtained successfully");
            return response.getAccessToken();
            
        } catch (Exception e) {
            System.err.println("Failed to get admin token: " + e.getMessage());
            throw new RuntimeException("Failed to get admin token", e);
        }
    }

    private static DataSetupConfig loadDataSetupConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            // Try to load from classpath
            InputStream inputStream = DataSetupMain.class.getClassLoader()
                    .getResourceAsStream("data-setup-config.json");
            
            if (inputStream == null) {
                throw new IOException("data-setup-config.json not found in classpath");
            }
            
            return mapper.readValue(inputStream, DataSetupConfig.class);
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    private static String getFacilityManagerPassword(DataSetupConfig config, String facilityManagerId) {
        return config.facilityManagers.stream()
                .filter(fm -> fm.facilityManagerID.equals(facilityManagerId))
                .findFirst()
                .map(fm -> fm.password)
                .orElseThrow(() -> new RuntimeException("Facility manager not found: " + facilityManagerId));
    }

    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}

