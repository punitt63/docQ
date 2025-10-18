package in.docq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.*;
import org.keycloak.representations.idm.authorization.DecisionStrategy;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import org.keycloak.representations.idm.authorization.ScopePermissionRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RolesToResourcesMapper {

    private static final String KEYCLOAK_URL = "http://localhost:8080";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_CLIENT = "admin-cli";
    private static final String MASTER_REALM = "master";

    private static final String HEALTH_FACILITY_REALM = "health-facility";
    private static final String HEALTH_FACILITY_BACKEND_APP_ID = "de51cfc8-70d2-41c8-b251-2e93861fc311";
    private static final String PATIENT_BACKEND_APP_ID = "ae51cfc8-70d2-41c8-b251-2e93861fc313";
    private static final Map<String, ResourceRepresentation> cachedResources = new HashMap<>();
    private static final Map<String, ScopeRepresentation> cachedScopes = new HashMap<>();
    // Configuration model classes
    static class ResourceConfig {
        public String name;
        public List<String> scopes;
    }

    static class ResourceMapping {
        public String name;
        public List<String> scopes;
    }

    static class RoleResourceMapping {
        public String role;

        @JsonProperty("resource-mappings")
        public List<ResourceMapping> resourceMappings;
    }

    static class Configuration {
        public List<ResourceConfig> resources;
        @JsonProperty("role-resource-mappings")
        public List<RoleResourceMapping> roleResourceMappings;
    }

    public static void configureResources() {
        // Default config file path
        String resourcesMappingFile = "resource-mappings.json";

        try {
            // Load configuration from JSON file
            Configuration config = loadConfiguration(resourcesMappingFile);

            // Connect to Keycloak using admin credentials
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(KEYCLOAK_URL)
                    .realm(MASTER_REALM)
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD)
                    .clientId(ADMIN_CLIENT)
                    .build();

            try {
                // Create resources and scopes based on configuration
                createResourcesAndScopes(keycloak, config.resources);

                // Create role policies for all roles
                Map<String, String> rolePolicies = createRolePolicies(keycloak, config.roleResourceMappings);

                // Create scope-based permissions for each resource-scope pair
                createScopePermissions(keycloak, config.roleResourceMappings, rolePolicies);

                // Configure patient backend app permissions
                configurePatientBackendPermissions(keycloak, config.roleResourceMappings);

                createAdminUser(keycloak);

            } catch (Exception e) {
                System.err.println("Error setting up Keycloak: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createAdminUser(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        List<UserRepresentation> users = realmResource.users().search("docq-admin", 0, 1);
        if(!users.isEmpty()) {
            return;
        }

        // Create user representation
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername("docq-admin");
        user.setFirstName("admin");
        user.setLastName("admin");

        // Set initial credentials (optional)
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue("xf~8QgK^]gw@,");
        credential.setTemporary(false);
        user.setCredentials(Arrays.asList(credential));

        // Create user
        Response response = realmResource.users().create(user);
        System.out.println("Response: " + response.getStatus());

        // Get created user ID from response
        String userId = CreatedResponseUtil.getCreatedId(response);

        System.out.println("Created userId: " + userId);

        UserResource userResource = realmResource.users().get(userId);

        RoleRepresentation realmRole = realmResource.roles().get("admin").toRepresentation();

        // Assign realm role to user
        userResource.roles().realmLevel().add(Arrays.asList(realmRole));

        System.out.println("Assigning admin role to user : " + userId);
    }

    private static void mapAdminToAllResourcesAndScopes(Keycloak keycloak, List<ResourceConfig> resources) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            // Get authorization management for backend app client
            AuthorizationResource authzClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).authorization();

            // Get admin role
            RoleRepresentation adminRole;
            try {
                adminRole = realmResource.roles().get("admin").toRepresentation();
            } catch (Exception e) {
                System.err.println("Admin role not found, skipping admin mapping");
                return;
            }

            // Create admin policy if it doesn't exist
            String adminPolicyName = "admin-policy";
            String adminPolicyId = null;

            // Check if policy already exists
            PolicyRepresentation existingPolicy = authzClient.policies().findByName(adminPolicyName);

            if (existingPolicy != null) {
                System.out.println("Admin policy already exists");
                adminPolicyId = existingPolicy.getId();
            } else {
                // Create new admin policy
                PolicyRepresentation policy = new PolicyRepresentation();
                policy.setName(adminPolicyName);
                policy.setType("role");

                HashMap<String, String> config = new HashMap<>();
                config.put("roles", "[{\"id\":\"" + adminRole.getId() + "\",\"required\":false}]");
                policy.setConfig(config);

                authzClient.policies().create(policy).close();
                System.out.println("Created admin policy: " + adminPolicyName);

                // Get the created policy ID
                PolicyRepresentation createdPolicy = authzClient.policies().findByName(adminPolicyName);
                if (createdPolicy != null) {
                    adminPolicyId = createdPolicy.getId();
                }
            }

            // Process each resource and grant admin all scopes
            for (ResourceConfig resourceConfig : resources) {
                String resourceName = resourceConfig.name;
                List<String> scopes = resourceConfig.scopes;

                System.out.println("Mapping admin to resource: " + resourceName + " with all scopes");

                // Find resource by name
                List<ResourceRepresentation> resourceList = authzClient.resources().findByName(resourceName);
                if (resourceList.isEmpty()) {
                    System.err.println("Resource " + resourceName + " not found, skipping admin mapping for this resource");
                    continue;
                }

                ResourceRepresentation resource = resourceList.get(0);
                String resourceId = resource.getId();

                // Grant admin access to all scopes for this resource
                for (String scopeName : scopes) {
                    ScopeRepresentation scopeRep = authzClient.scopes().findByName(scopeName);
                    if (scopeRep == null) {
                        System.err.println("Scope " + scopeName + " not found, skipping admin mapping for this scope");
                        continue;
                    }

                    String permissionName = "admin-" + resourceName + "-" + scopeName + "-permission";

                    // Check if permission already exists
                    PolicyRepresentation existingPermission = authzClient.policies().findByName(permissionName);
                    if (existingPermission != null) {
                        System.out.println("  Permission " + permissionName + " already exists");
                        continue;
                    }

                    // Create permission
                    PolicyRepresentation policyRep = new PolicyRepresentation();
                    policyRep.setName(permissionName);
                    policyRep.setType("scope");

                    HashMap<String, String> config = new HashMap<>();
                    config.put("resources", "[\"" + resourceId + "\"]");
                    config.put("scopes", "[\"" + scopeRep.getId() + "\"]");
                    config.put("applyPolicies", "[\"" + adminPolicyId + "\"]");
                    policyRep.setConfig(config);

                    authzClient.policies().create(policyRep).close();
                    System.out.println("  Created admin permission: " + permissionName);
                }
            }

            System.out.println("Admin to resource mappings completed");
        } catch (Exception e) {
            System.err.println("Error mapping admin to resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Configuration loadConfiguration(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File configFile = new ClassPathResource(configFilePath).getFile();

        if (!configFile.exists()) {
            throw new IOException("Configuration file not found: " + configFilePath);
        }

        try {
            return mapper.readValue(configFile, Configuration.class);
        } catch (Exception e) {
            throw new IOException("Error parsing configuration file: " + e.getMessage(), e);
        }
    }

    private static void createResourcesAndScopes(Keycloak keycloak, List<ResourceConfig> resources) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            // Get authorization management for backend app client
            AuthorizationResource authzClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).authorization();

            // Create all scopes first
            List<String> allScopes = resources.stream()
                    .flatMap(r -> r.scopes.stream())
                    .distinct()
                    .collect(Collectors.toList());

            for (String scopeName : allScopes) {
                // Check if scope already exists
                ScopeRepresentation scopeRepresentation = authzClient.scopes().findByName(scopeName);
                if (scopeRepresentation != null) {
                    cachedScopes.put(scopeName, authzClient.scopes().findByName(scopeName));
                    System.out.println("Scope " + scopeName + " already exists");
                    continue;
                }

                ScopeRepresentation scope = new ScopeRepresentation();
                scope.setName(scopeName);
                authzClient.scopes().create(scope).close();
                cachedScopes.put(scopeName, authzClient.scopes().findByName(scopeName));
                System.out.println("Created scope: " + scopeName);
            }

            // Now create resources and assign scopes
            for (ResourceConfig resourceConfig : resources) {
                // Get or create resource
                ResourceRepresentation resource = new ResourceRepresentation();
                resource.setName(resourceConfig.name);
                resource.setType(resourceConfig.name);

                // Handle scopes
                Set<ScopeRepresentation> scopeRepresentations = new HashSet<>();
                for (String scopeName : resourceConfig.scopes) {
                    ScopeRepresentation scope = cachedScopes.get(scopeName);
                    if (scope != null) {
                        scopeRepresentations.add(scope);
                    } else {
                        System.err.println("Scope " + scopeName + " not found, skipping for resource " + resourceConfig.name);
                    }
                }
                resource.setScopes(scopeRepresentations);

                // Check if resource already exists
                List<ResourceRepresentation> existingResources = authzClient.resources().findByName(resourceConfig.name);
                if (!existingResources.isEmpty()) {
                    // Update existing resource
                    String resourceId = existingResources.get(0).getId();
                    resource.setId(resourceId);
                    authzClient.resources().resource(resourceId).update(resource);
                    cachedResources.put(resourceConfig.name, resource);
                    System.out.println("Updated resource: " + resourceConfig.name);
                } else {
                    // Create new resource
                    authzClient.resources().create(resource).close();
                    cachedResources.put(resourceConfig.name, authzClient.resources().findByName(resourceConfig.name).get(0));
                    System.out.println("Created resource: " + resourceConfig.name);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating resources and scopes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, String> createRolePolicies(Keycloak keycloak, List<RoleResourceMapping> roleMappings) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);
        Map<String, String> rolePolicyMap = new HashMap<>();

        try {
            AuthorizationResource authzClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).authorization();

            // Create a policy for each role
            for (RoleResourceMapping roleMapping : roleMappings) {
                String roleName = roleMapping.role;
                System.out.println("Creating policy for role: " + roleName);

                // Get role representation
                RoleRepresentation role;
                try {
                    role = realmResource.roles().get(roleName).toRepresentation();
                } catch (Exception e) {
                    System.err.println("Role " + roleName + " not found, skipping policy creation");
                    continue;
                }

                // Create role policy if it doesn't exist
                String rolePolicyName = roleName + "-policy";

                // Check if policy already exists
                PolicyRepresentation existingPolicy = authzClient.policies().findByName(rolePolicyName);

                if (existingPolicy != null) {
                    System.out.println("Role policy for " + roleName + " already exists");
                    rolePolicyMap.put(roleName, existingPolicy.getId());
                } else {
                    // Create new role policy
                    PolicyRepresentation policy = new PolicyRepresentation();
                    policy.setName(rolePolicyName);
                    policy.setType("role");
                    policy.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);

                    HashMap<String, String> config = new HashMap<>();
                    config.put("roles", "[{\"id\":\"" + role.getId() + "\",\"required\":false}]");
                    policy.setConfig(config);

                    authzClient.policies().create(policy).close();
                    System.out.println("Created role policy: " + rolePolicyName);

                    // Get the created policy ID
                    PolicyRepresentation createdPolicy = authzClient.policies().findByName(rolePolicyName);
                    if (createdPolicy != null) {
                        rolePolicyMap.put(roleName, createdPolicy.getId());
                    }
                }
            }

            System.out.println("Role policies created successfully");
            return rolePolicyMap;
        } catch (Exception e) {
            System.err.println("Error creating role policies: " + e.getMessage());
            e.printStackTrace();
            return rolePolicyMap;
        }
    }

    private static void createScopePermissions(Keycloak keycloak, List<RoleResourceMapping> roleMappings, Map<String, String> rolePolicies) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            AuthorizationResource authzClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).authorization();

            // Create a map of resource-scope pairs to the list of roles that should have access
            Map<String, Set<String>> resourceScopeToRoles = new HashMap<>();

            // Populate the map
            for (RoleResourceMapping roleMapping : roleMappings) {
                String roleName = roleMapping.role;

                for (ResourceMapping resourceMapping : roleMapping.resourceMappings) {
                    String resourceName = resourceMapping.name;

                    for (String scopeName : resourceMapping.scopes) {
                        String key = resourceName + ":" + scopeName;
                        if (!resourceScopeToRoles.containsKey(key)) {
                            resourceScopeToRoles.put(key, new HashSet<>());
                        }
                        resourceScopeToRoles.get(key).add(roleName);
                    }
                }
            }

            // Now create permissions for each resource-scope pair
            for (Map.Entry<String, Set<String>> entry : resourceScopeToRoles.entrySet()) {
                String[] parts = entry.getKey().split(":");
                String resourceName = parts[0];
                String scopeName = parts[1];
                Set<String> roles = entry.getValue();

                System.out.println("Creating permission for resource: " + resourceName + ", scope: " + scopeName);

                // Find resource by name
                ResourceRepresentation resource = cachedResources.get(resourceName);
                String resourceId = resource.getId();

                // Find scope by name
                ScopeRepresentation scope = cachedScopes.get(scopeName);
                if (scope == null) {
                    System.err.println("Scope " + scopeName + " not found, skipping permission creation");
                    continue;
                }

                // Create a unified permission for this resource-scope pair
                String permissionName = resourceName + "-" + scopeName + "-permission";

                // Check if permission already exists
                PolicyRepresentation existingPermission = authzClient.policies().findByName(permissionName);
                if (existingPermission != null) {
                    System.out.println("Permission " + permissionName + " already exists, updating");
                    authzClient.policies().policy(existingPermission.getId()).remove();
                }

                // Create the permission with all applicable role policies
                PolicyRepresentation permission = new PolicyRepresentation();
                permission.setName(permissionName);
                permission.setType("scope");
                permission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);

                // Collect all policy IDs for roles that should have access
                List<String> policyIds = new ArrayList<>();
                for (String role : roles) {
                    if (rolePolicies.containsKey(role)) {
                        policyIds.add(rolePolicies.get(role));
                    } else {
                        System.err.println("Policy ID for role " + role + " not found, skipping");
                    }
                }

                if (policyIds.isEmpty()) {
                    System.err.println("No policies found for permission " + permissionName + ", skipping");
                    continue;
                }

                // Build the config
                HashMap<String, String> config = new HashMap<>();
                config.put("resources", "[\"" + resourceId + "\"]");
                config.put("scopes", "[\"" + scope.getId() + "\"]");

                // Create apply policies string "[\"policy-id-1\",\"policy-id-2\",...]"
                StringBuilder policyIdsString = new StringBuilder("[");
                for (int i = 0; i < policyIds.size(); i++) {
                    policyIdsString.append("\"").append(policyIds.get(i)).append("\"");
                    if (i < policyIds.size() - 1) {
                        policyIdsString.append(",");
                    }
                }
                policyIdsString.append("]");

                config.put("applyPolicies", policyIdsString.toString());
                permission.setConfig(config);

                // Create the permission
                authzClient.policies().create(permission).close();
                System.out.println("Created permission: " + permissionName + " with policies for roles: " + String.join(", ", roles));
            }

            System.out.println("Scope permissions created successfully");
        } catch (Exception e) {
            System.err.println("Error creating scope permissions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void configurePatientBackendPermissions(Keycloak keycloak, List<RoleResourceMapping> roleMappings) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            System.out.println("Configuring patient backend app permissions...");

            // Get the health-facility-backend-app client (where authorization resources are defined)
            AuthorizationResource authzResource = realmResource.clients()
                    .get(HEALTH_FACILITY_BACKEND_APP_ID)
                    .authorization();

            // Get patient backend app service account user
            UserRepresentation patientServiceAccount = realmResource.clients()
                    .get(PATIENT_BACKEND_APP_ID)
                    .getServiceAccountUser();

            System.out.println("Patient Backend Service Account User ID: " + patientServiceAccount.getId());

            // Find the patient-backend-service role mapping from configuration
            RoleResourceMapping patientBackendMapping = roleMappings.stream()
                    .filter(mapping -> "patient-backend-service".equals(mapping.role))
                    .findFirst()
                    .orElse(null);

            if (patientBackendMapping == null) {
                System.err.println("No patient-backend-service role mapping found in configuration");
                return;
            }

            // Create role-based policy for patient backend service
            String policyName = "Patient Backend Service Policy";
            try {
                authzResource.policies().role().findByName(policyName);
                System.out.println("Policy " + policyName + " already exists");
            } catch (Exception e) {
                // Get the patient-backend-service role
                RoleRepresentation patientBackendRole = realmResource.roles().get("patient-backend-service").toRepresentation();

                RolePolicyRepresentation policy = new RolePolicyRepresentation();
                policy.setName(policyName);
                policy.setDescription("Policy for patient backend service to access resources");
                policy.setDecisionStrategy(DecisionStrategy.UNANIMOUS);
                policy.setLogic(org.keycloak.representations.idm.authorization.Logic.POSITIVE);

                // Configure the policy to include the patient backend role
                policy.addRole(patientBackendRole.getName(), false);

                authzResource.policies().role().create(policy);
                System.out.println("Created policy: " + policyName);
            }

            // Create permissions for each resource-scope pair in the patient backend mapping
            for (ResourceMapping resourceMapping : patientBackendMapping.resourceMappings) {
                String resourceName = resourceMapping.name;

                for (String scopeName : resourceMapping.scopes) {
                    System.out.println("Creating patient backend permission for resource: " + resourceName + ", scope: " + scopeName);

                    // Find resource by name
                    ResourceRepresentation resource = cachedResources.get(resourceName);
                    if (resource == null) {
                        System.err.println("Resource " + resourceName + " not found, skipping");
                        continue;
                    }

                    // Find scope by name
                    ScopeRepresentation scope = cachedScopes.get(scopeName);
                    if (scope == null) {
                        System.err.println("Scope " + scopeName + " not found, skipping");
                        continue;
                    }

                    // Create scope-based permission for patient backend service
                    String permissionName = "Patient Backend " + resourceName + " " + scopeName + " Permission";
                    try {
                        authzResource.permissions().scope().findByName(permissionName);
                        System.out.println("Permission " + permissionName + " already exists");
                    } catch (Exception e) {
                        ScopePermissionRepresentation permission = new ScopePermissionRepresentation();
                        permission.setName(permissionName);
                        permission.setDescription("Allows patient backend service to " + scopeName + " " + resourceName + " resources");
                        permission.setDecisionStrategy(DecisionStrategy.UNANIMOUS);
                        permission.setLogic(org.keycloak.representations.idm.authorization.Logic.POSITIVE);
                        
                        permission.setResources(Collections.singleton(resource.getId()));
                        permission.setScopes(Collections.singleton(scope.getId()));
                        permission.setPolicies(Collections.singleton(policyName));

                        authzResource.permissions().scope().create(permission);
                        System.out.println("Created permission: " + permissionName);
                    }
                }
            }

            System.out.println("Successfully configured patient backend app permissions");

        } catch (Exception e) {
            System.err.println("Error configuring patient backend permissions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}