package in.docq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.*;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
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

    public static void main(String[] args) {
        // Default config file path
        String resourcesMappingFile = "resource-mappings.json";

        // Check if config file path is provided as argument
        if (args.length > 0) {
            resourcesMappingFile = args[0];
        }

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

                // Map admin role to all resources and scopes
                mapAdminToAllResourcesAndScopes(keycloak, config.resources);
                
                // Map roles to resources based on configuration
                mapRolesToResources(keycloak, config.roleResourceMappings);

            } catch (Exception e) {
                System.err.println("Error setting up Keycloak: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
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

                authzClient.policies().create(policy);
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

                    authzClient.policies().create(policyRep);
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
                    System.out.println("Scope " + scopeName + " already exists");
                    continue;
                }

                ScopeRepresentation scope = new ScopeRepresentation();
                scope.setName(scopeName);

                authzClient.scopes().create(scope);
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
                    ScopeRepresentation scope = new ScopeRepresentation();
                    scope.setName(scopeName);
                    scopeRepresentations.add(scope);
                }
                resource.setScopes(scopeRepresentations);

                // Check if resource already exists
                List<ResourceRepresentation> existingResources = authzClient.resources().findByName(resourceConfig.name);
                if (!existingResources.isEmpty()) {
                    // Update existing resource
                    String resourceId = existingResources.get(0).getId();
                    resource.setId(resourceId);
                    authzClient.resources().resource(resourceId).update(resource);
                    System.out.println("Updated resource: " + resourceConfig.name);
                } else {
                    // Create new resource
                    authzClient.resources().create(resource);
                    System.out.println("Created resource: " + resourceConfig.name);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating resources and scopes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mapRolesToResources(Keycloak keycloak, List<RoleResourceMapping> roleMappings) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            AuthorizationResource authzClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).authorization();

            // Process each role mapping
            for (RoleResourceMapping roleMapping : roleMappings) {
                String roleName = roleMapping.role;
                System.out.println("Processing role: " + roleName);

                // Get role representation
                RoleRepresentation role;
                try {
                    role = realmResource.roles().get(roleName).toRepresentation();
                } catch (Exception e) {
                    System.err.println("Role " + roleName + " not found, skipping");
                    continue;
                }

                // Create role policy if it doesn't exist
                String rolePolicyName = roleName + "-policy";
                String rolePolicyId = null;

                // Check if policy already exists
                try {
                    PolicyRepresentation existingPolicy = authzClient.policies().findByName(rolePolicyName);

                    if (existingPolicy != null) {
                        System.out.println("Role policy for " + roleName + " already exists");
                        rolePolicyId = existingPolicy.getId();
                    } else {
                        // Create new role policy
                        PolicyRepresentation policy = new PolicyRepresentation();
                        policy.setName(rolePolicyName);
                        policy.setType("role");

                        HashMap<String, String> config = new HashMap<>();
                        config.put("roles", "[{\"id\":\"" + role.getId() + "\",\"required\":false}]");
                        policy.setConfig(config);

                        authzClient.policies().create(policy);
                        System.out.println("Created role policy: " + rolePolicyName);

                        // Get the created policy ID
                        PolicyRepresentation createdPolicy = authzClient.policies().findByName(rolePolicyName);
                        if (createdPolicy != null) {
                            rolePolicyId = createdPolicy.getId();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error handling role policy: " + e.getMessage());
                    continue;
                }

                // Process each resource mapping for this role
                for (ResourceMapping resourceMapping : roleMapping.resourceMappings) {
                    String resourceName = resourceMapping.name;
                    List<String> scopes = resourceMapping.scopes;

                    System.out.println("  Mapping resource: " + resourceName + " with scopes: " + String.join(", ", scopes));

                    // Find resource by name
                    List<ResourceRepresentation> resources = authzClient.resources().findByName(resourceName);
                    if (resources.isEmpty()) {
                        System.err.println("Resource " + resourceName + " not found, skipping");
                        continue;
                    }

                    ResourceRepresentation resource = resources.get(0);
                    String resourceId = resource.getId();

                    // For each scope, create a scope-based permission
                    for (String scopeName : scopes) {
                        ScopeRepresentation scopeReps = authzClient.scopes().findByName(scopeName);
                        if (scopeReps == null) {
                            System.err.println("Scope " + scopeName + " not found, skipping");
                            continue;
                        }

                        String permissionName = roleName + "-" + resourceName + "-" + scopeName + "-permission";

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
                        config.put("scopes", "[\"" + scopeReps.getId() + "\"]");
                        config.put("applyPolicies", "[\"" + rolePolicyId + "\"]");
                        policyRep.setConfig(config);

                        authzClient.policies().create(policyRep);
                        System.out.println("  Created permission: " + permissionName);
                    }
                }
            }

            System.out.println("Role to resource mappings completed");
        } catch (Exception e) {
            System.err.println("Error mapping roles to resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
