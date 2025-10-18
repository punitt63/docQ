package in.docq;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeycloakSetup {

    private static final String KEYCLOAK_URL = "http://localhost:8080";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_CLIENT = "admin-cli";
    private static final String MASTER_REALM = "master";

    private static final String HEALTH_FACILITY_REALM = "health-facility";
    private static final String HEALTH_FACILITY_BACKEND_APP = "health-facility-backend-app";
    private static final String HEALTH_FACILITY_BACKEND_APP_ID = "de51cfc8-70d2-41c8-b251-2e93861fc311";
    private static final String PATIENT_BACKEND_APP = "patient-backend-app";
    private static final String PATIENT_BACKEND_APP_ID = "ae51cfc8-70d2-41c8-b251-2e93861fc313";
    private static final String HEALTH_FACILITY_DESKTOP_APP = "health-facility-desktop-app";
    private static final String HEALTH_FACILITY_DESKTOP_APP_ID = "fe51cfc8-80d2-41c8-b251-2e93861fc312";

    public static void setupRealm() {
        // Connect to Keycloak using admin credentials
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_URL)
                .realm(MASTER_REALM)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .clientId(ADMIN_CLIENT)
                .build();

        System.out.println("Admin User Access Token Created");

        try {
            // Create Health Facility Realm
            //createHealthFacilityRealm(keycloak);

            // Create Health Facility Backend App Client
            createHealthFacilityBackendAppClient(keycloak);

            // Create Patient Backend App Client
            createPatientBackendAppClient(keycloak);

            // Get service account user and assign manage-users role
            assignManageUsersRoleToServiceAccount(keycloak);

            assignViewRealmRoleToServiceAccount(keycloak);

            // Create Health Facility Desktop App Client
            createHealthFacilityDesktopAppClient(keycloak);

            // Create roles for Health Facility realm
            createHealthFacilityRoles(keycloak);

            // Create user for patient-backend-service
            createPatientAdminUser(keycloak);

            System.out.println("Keycloak basic setup completed successfully");
            System.out.println("NOTE: Run RolesToResourcesMapper to configure authorization resources and permissions");

        } catch (Exception e) {
            System.err.println("Error setting up Keycloak: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void assignViewRealmRoleToServiceAccount(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            // Get realm-management client
            List<ClientRepresentation> realmManagementClients = realmResource.clients().findByClientId("realm-management");
            if (realmManagementClients.isEmpty()) {
                System.err.println("realm-management client not found, cannot assign manage-users role");
                return;
            }

            ClientRepresentation realmManagementClient = realmManagementClients.get(0);
            String realmManagementClientId = realmManagementClient.getId();

            System.out.println("Realm Management Client ID: " + realmManagementClientId);

            // Get manage-users role
            RoleRepresentation viewRealmRole = realmResource.clients().get(realmManagementClientId)
                    .roles().get("view-realm").toRepresentation();

            System.out.println("View Realm Role ID: " + viewRealmRole.getId());

            // Get service account user
            UserRepresentation serviceAccountUser = realmResource.clients()
                    .get(HEALTH_FACILITY_BACKEND_APP_ID)
                    .getServiceAccountUser();

            // Check if the role is already assigned
            List<RoleRepresentation> currentRoles = realmResource.users().get(serviceAccountUser.getId())
                    .roles().clientLevel(realmManagementClientId).listAll();

            boolean roleAlreadyAssigned = currentRoles.stream()
                    .anyMatch(role -> "view-realm".equals(role.getName()));

            if (roleAlreadyAssigned) {
                System.out.println("view-realm role already assigned to service account user");
                return;
            }

            // Assign role to service account
            realmResource.users().get(serviceAccountUser.getId())
                    .roles().clientLevel(realmManagementClientId)
                    .add(Collections.singletonList(viewRealmRole));

            System.out.println("Added view-realm role to health-facility-backend-app service account user");
        } catch (Exception e) {
            System.err.println("Error assigning view-realm role: " + e.getMessage());
        }
    }

    private static void createHealthFacilityRealm(Keycloak keycloak) {
        try {
            if (keycloak.realms().findAll().stream()
                    .anyMatch(realm -> HEALTH_FACILITY_REALM.equals(realm.getRealm()))) {
                System.out.println("Realm " + HEALTH_FACILITY_REALM + " already exists.");
                return;
            }
        } catch (Exception e) {
            // Alternative approach if deserialization fails
            try {
                // Check for realm existence directly
                keycloak.realms().realm(HEALTH_FACILITY_REALM).toRepresentation();
                System.out.println("Realm " + HEALTH_FACILITY_REALM + " already exists.");
                return;
            } catch (jakarta.ws.rs.NotFoundException nfe) {
                // Realm doesn't exist, continue with creation
                System.out.println("Realm " + HEALTH_FACILITY_REALM + " not found, will create it.");
                // Create new realm
                RealmRepresentation realm = new RealmRepresentation();
                realm.setRealm(HEALTH_FACILITY_REALM);
                realm.setEnabled(true);

                List<RequiredActionProviderRepresentation> requiredActions = new ArrayList<>();

                RequiredActionProviderRepresentation verifyProfile = new RequiredActionProviderRepresentation();
                verifyProfile.setAlias("VERIFY_PROFILE");
                verifyProfile.setName("Verify Profile");
                verifyProfile.setEnabled(false);
                verifyProfile.setDefaultAction(false); // Will be required for new users
                verifyProfile.setPriority(10);
                requiredActions.add(verifyProfile);

                realm.setRequiredActions(requiredActions);

                keycloak.realms().create(realm);
                System.out.println("Realm for Health Facility Created");
            }
        }
    }

    private static void createHealthFacilityBackendAppClient(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        // Check if client already exists
        List<ClientRepresentation> existingClients = realmResource.clients().findByClientId(HEALTH_FACILITY_BACKEND_APP);
        if (!existingClients.isEmpty()) {
            System.out.println("Client " + HEALTH_FACILITY_BACKEND_APP + " already exists.");

            // Ensure authorization is enabled on existing client
            ClientRepresentation existingClient = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).toRepresentation();
            if (!Boolean.TRUE.equals(existingClient.getAuthorizationServicesEnabled())) {
                existingClient.setAuthorizationServicesEnabled(true);
                realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID).update(existingClient);
                System.out.println("Enabled authorization services for " + HEALTH_FACILITY_BACKEND_APP);
            }

            return;
        }

        // Create new client
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(HEALTH_FACILITY_BACKEND_APP);
        client.setId(HEALTH_FACILITY_BACKEND_APP_ID);
        client.setServiceAccountsEnabled(true);
        client.setAuthorizationServicesEnabled(true); // Enable authorization services
        //client.setSecret(""); // Will be generated by Keycloak

        realmResource.clients().create(client);
        System.out.println("Health Facility Backend App Client Created");

        // Get client secret
        String clientSecret = realmResource.clients().get(HEALTH_FACILITY_BACKEND_APP_ID)
                .getSecret().getValue();

        System.out.println("Health Facility Backend App Client ID: " + HEALTH_FACILITY_BACKEND_APP);
        System.out.println("Health Facility Backend App Client Secret: " + clientSecret);

        // Get service account user
        UserRepresentation serviceAccountUser = realmResource.clients()
                .get(HEALTH_FACILITY_BACKEND_APP_ID)
                .getServiceAccountUser();

        System.out.println("Health Facility Backend App Service Account User: " + serviceAccountUser.getId());
    }

    private static void createPatientBackendAppClient(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        // Check if client already exists
        List<ClientRepresentation> existingClients = realmResource.clients().findByClientId(PATIENT_BACKEND_APP);
        if (!existingClients.isEmpty()) {
            System.out.println("Client " + PATIENT_BACKEND_APP + " already exists.");

            // Ensure authorization and direct access grants are enabled on existing client
            ClientRepresentation existingClient = realmResource.clients().get(PATIENT_BACKEND_APP_ID).toRepresentation();
            boolean needsUpdate = false;
            
            if (!Boolean.TRUE.equals(existingClient.getAuthorizationServicesEnabled())) {
                existingClient.setAuthorizationServicesEnabled(true);
                needsUpdate = true;
                System.out.println("Enabled authorization services for " + PATIENT_BACKEND_APP);
            }
            
            if (!Boolean.TRUE.equals(existingClient.isDirectAccessGrantsEnabled())) {
                existingClient.setDirectAccessGrantsEnabled(true);
                needsUpdate = true;
                System.out.println("Enabled direct access grants for " + PATIENT_BACKEND_APP);
            }
            
            if (needsUpdate) {
                realmResource.clients().get(PATIENT_BACKEND_APP_ID).update(existingClient);
            }

            return;
        }

        // Create new client
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(PATIENT_BACKEND_APP);
        client.setId(PATIENT_BACKEND_APP_ID);
        client.setServiceAccountsEnabled(true);
        client.setDirectAccessGrantsEnabled(true); // Enable direct access grants for user authentication
        client.setAuthorizationServicesEnabled(true); // Enable authorization services
        //client.setSecret(""); // Will be generated by Keycloak

        realmResource.clients().create(client);
        System.out.println("Patient Backend App Client Created");

        // Get client secret
        String clientSecret = realmResource.clients().get(PATIENT_BACKEND_APP_ID)
                .getSecret().getValue();

        System.out.println("Patient Backend App Client ID: " + PATIENT_BACKEND_APP);
        System.out.println("Patient Backend App Client Secret: " + clientSecret);

        // Get service account user
        UserRepresentation serviceAccountUser = realmResource.clients()
                .get(PATIENT_BACKEND_APP_ID)
                .getServiceAccountUser();

        System.out.println("Patient Backend App Service Account User: " + serviceAccountUser.getId());
    }

    private static void assignManageUsersRoleToServiceAccount(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            // Get realm-management client
            List<ClientRepresentation> realmManagementClients = realmResource.clients().findByClientId("realm-management");
            if (realmManagementClients.isEmpty()) {
                System.err.println("realm-management client not found, cannot assign manage-users role");
                return;
            }

            ClientRepresentation realmManagementClient = realmManagementClients.get(0);
            String realmManagementClientId = realmManagementClient.getId();

            System.out.println("Realm Management Client ID: " + realmManagementClientId);

            // Get manage-users role
            RoleRepresentation manageUsersRole = realmResource.clients().get(realmManagementClientId)
                    .roles().get("manage-users").toRepresentation();

            System.out.println("Manage Users Role ID: " + manageUsersRole.getId());

            // Get service account user
            UserRepresentation serviceAccountUser = realmResource.clients()
                    .get(HEALTH_FACILITY_BACKEND_APP_ID)
                    .getServiceAccountUser();

            // Check if the role is already assigned
            List<RoleRepresentation> currentRoles = realmResource.users().get(serviceAccountUser.getId())
                    .roles().clientLevel(realmManagementClientId).listAll();

            boolean roleAlreadyAssigned = currentRoles.stream()
                    .anyMatch(role -> "manage-users".equals(role.getName()));

            if (roleAlreadyAssigned) {
                System.out.println("manage-users role already assigned to service account user");
                return;
            }

            // Assign role to service account
            realmResource.users().get(serviceAccountUser.getId())
                    .roles().clientLevel(realmManagementClientId)
                    .add(Collections.singletonList(manageUsersRole));

            System.out.println("Added manage-users role to health-facility-backend-app service account user");
        } catch (Exception e) {
            System.err.println("Error assigning manage-users role: " + e.getMessage());
        }
    }

    private static void createHealthFacilityDesktopAppClient(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        // Check if client already exists
        if (!realmResource.clients().findByClientId(HEALTH_FACILITY_DESKTOP_APP).isEmpty()) {
            System.out.println("Client " + HEALTH_FACILITY_DESKTOP_APP + " already exists.");
            return;
        }

        // Create new client
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(HEALTH_FACILITY_DESKTOP_APP);
        client.setId(HEALTH_FACILITY_DESKTOP_APP_ID);
        client.setDirectAccessGrantsEnabled(true);
       // client.setSecret(""); // Will be generated by Keycloak

        realmResource.clients().create(client);
        System.out.println("Health Facility Desktop App Client Created");

        // Get client secret
        String clientSecret = realmResource.clients().get(HEALTH_FACILITY_DESKTOP_APP_ID)
                .getSecret().getValue();

        System.out.println("Health Facility Desktop App Client ID: " + HEALTH_FACILITY_DESKTOP_APP);
        System.out.println("Health Facility Desktop App Client Secret: " + clientSecret);
    }

    private static void createHealthFacilityRoles(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        // Define roles to create (including patient-backend-service role)
        List<String> rolesToCreate = Arrays.asList("admin", "facility-manager", "doctor", "patient-backend-service");

        for (String roleName : rolesToCreate) {
            try {
                // Check if role already exists
                realmResource.roles().get(roleName).toRepresentation();
                System.out.println("Role " + roleName + " already exists");
            } catch (Exception e) {
                // Create role if it doesn't exist
                RoleRepresentation role = new RoleRepresentation();
                role.setName(roleName);
                if ("patient-backend-service".equals(roleName)) {
                    role.setDescription("Role for patient backend service to access resources");
                }
                realmResource.roles().create(role);
                System.out.println("Role " + roleName + " created");
            }
        }

        // Assign patient-backend-service role to patient backend service account
        try {
            UserRepresentation patientServiceAccount = realmResource.clients()
                    .get(PATIENT_BACKEND_APP_ID)
                    .getServiceAccountUser();

            RoleRepresentation patientBackendRole = realmResource.roles().get("patient-backend-service").toRepresentation();
            List<RoleRepresentation> currentRoles = realmResource.users()
                    .get(patientServiceAccount.getId())
                    .roles().realmLevel().listAll();

            boolean roleAlreadyAssigned = currentRoles.stream()
                    .anyMatch(role -> "patient-backend-service".equals(role.getName()));

            if (!roleAlreadyAssigned) {
                realmResource.users().get(patientServiceAccount.getId())
                        .roles().realmLevel()
                        .add(Collections.singletonList(patientBackendRole));
                System.out.println("Assigned patient-backend-service role to patient backend service account");
            } else {
                System.out.println("patient-backend-service role already assigned to patient backend service account");
            }
        } catch (Exception e) {
            System.err.println("Error assigning patient-backend-service role: " + e.getMessage());
        }
    }

    private static void createPatientAdminUser(Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(HEALTH_FACILITY_REALM);

        try {
            // Check if user already exists
            List<UserRepresentation> existingUsers = realmResource.users().search("patient-admin", 0, 1);
            if (!existingUsers.isEmpty()) {
                System.out.println("User patient-admin already exists");
                return;
            }

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername("patient-admin");
            user.setFirstName("Patient");
            user.setLastName("Admin");
            user.setEmail("patient-admin@docq.in");

            // Set credentials with random password
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue("Kx9#mP2$vL8@nQ5!");
            credential.setTemporary(false);
            user.setCredentials(Arrays.asList(credential));

            // Create user
            Response response = realmResource.users().create(user);
            System.out.println("Created patient-admin user");

            // Get created user ID
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.println("Patient Admin User ID: " + userId);

            // Assign patient-backend-service role to the user
            UserResource userResource = realmResource.users().get(userId);
            RoleRepresentation patientBackendRole = realmResource.roles().get("patient-backend-service").toRepresentation();
            
            userResource.roles().realmLevel().add(Arrays.asList(patientBackendRole));
            System.out.println("Assigned patient-backend-service role to patient-admin user");

            System.out.println("Patient Admin User Credentials:");
            System.out.println("Username: patient-admin");
            System.out.println("Password: Kx9#mP2$vL8@nQ5!");

        } catch (Exception e) {
            System.err.println("Error creating patient-admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}