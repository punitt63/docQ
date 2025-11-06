package in.docq;

/**
 * Main orchestrator class for Keycloak setup.
 * This class coordinates the execution of realm setup and resource configuration.
 */
public class KeycloakSetupOrchestrator {

    public static void main(String[] args) {
        System.out.println("Starting Keycloak setup orchestration...");
        
        try {
            // Step 1: Setup basic realm, clients, and roles
            System.out.println("Step 1: Setting up realm, clients, and roles...");
            KeycloakSetup.setupRealm();
            
            // Step 2: Configure resources, scopes, and permissions
            System.out.println("Step 2: Configuring resources, scopes, and permissions...");
            RolesToResourcesMapper.configureResources();
            
            // Step 3: Export secrets to shared file
            System.out.println("Step 3: Exporting client secrets...");
            String secretsPath = System.getenv("SECRETS_OUTPUT_PATH");
            if (secretsPath == null || secretsPath.isEmpty()) {
                secretsPath = "/secrets/keycloak-secrets.properties";
            }
            KeycloakSetup.exportSecretsToFile(secretsPath);
            
            System.out.println("Keycloak setup orchestration completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during Keycloak setup orchestration: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
