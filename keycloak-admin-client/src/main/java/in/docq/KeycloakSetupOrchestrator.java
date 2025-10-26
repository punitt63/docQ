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
            
            System.out.println("Keycloak setup orchestration completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during Keycloak setup orchestration: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
