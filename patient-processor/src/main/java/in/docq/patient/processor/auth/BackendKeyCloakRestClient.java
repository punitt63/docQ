package in.docq.patient.processor.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.keycloak.rest.client.ApiClient;
import in.docq.keycloak.rest.client.api.AuthenticationApi;
import in.docq.keycloak.rest.client.api.RoleMapperApi;
import in.docq.keycloak.rest.client.api.RolesApi;
import in.docq.keycloak.rest.client.api.UsersApi;
import in.docq.keycloak.rest.client.model.*;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Component
public class BackendKeyCloakRestClient {
    private final String realm;
    private final String clientID;
    private final String clientSecret;
    private final ApiClient apiClient;
    private final UsersApi usersApi;
    private final RoleMapperApi roleMapperApi;
    private final RolesApi rolesApi;
    private final AuthenticationApi authenticationApi;
    private final Cache<String, String> tokenCache;
    private final Cache<String, List<Permission>> userPermissionsCache;
    private static final String accessTokenCacheKey = "clientAccessToken";

    public BackendKeyCloakRestClient(@Value("${keycloak.base.url}") String baseUrl,
                                     @Value("${keycloak.realm}") String realm,
                                     @Value("${keycloak.backend.client.id}") String clientID,
                                     @Value("${keycloak.backend.client.secret}") String clientSecret,
                                     @Qualifier("KeyCloakOkHttpClient") OkHttpClient okHttpClient) {
        this.realm = realm;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.apiClient = new ApiClient(baseUrl, okHttpClient);
        this.usersApi = new UsersApi(apiClient);
        this.authenticationApi = new AuthenticationApi(apiClient);
        this.roleMapperApi = new RoleMapperApi(apiClient);
        this.rolesApi = new RolesApi(apiClient);
        this.tokenCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(10000)
                .maximumSize(100000)
                .build();
        this.userPermissionsCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(10000)
                .maximumSize(100000)
                .build();
        generateAndCacheAccessToken();
    }

    private CompletionStage<Void> generateAndCacheAccessToken() {
        return authenticationApi.getServiceAccountAccessTokenAsync(realm, clientID, clientSecret)
                .thenAccept(getAccessToken200Response -> tokenCache.put(accessTokenCacheKey, getAccessToken200Response.getAccessToken()));
    }

    private boolean shouldRefreshAccessToken(String accessToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(accessToken);
            return Instant.now().isAfter(decodedJWT.getExpiresAtAsInstant());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    private String getCachedAccessToken() {
        return tokenCache.getIfPresent(accessTokenCacheKey);
    }

    public CompletionStage<String> getAccessToken() {
        String currentCachedAccessToken = getCachedAccessToken();
        if(currentCachedAccessToken == null || shouldRefreshAccessToken(currentCachedAccessToken)) {
            return generateAndCacheAccessToken()
                    .thenApply(ignore -> this.getCachedAccessToken());
        }
        return completedFuture(currentCachedAccessToken);
    }

    public CompletionStage<Void> createUser(String userName, String password) {
        return getAccessToken()
                .thenCompose(token -> usersApi.adminRealmsRealmUsersPostAsync(token, realm, new UserRepresentation().username(userName).credentials(List.of(new CredentialRepresentation().type("password").value(password))).enabled(true)));
    }

    public CompletionStage<Void> createUserIfNotExists(String userName, String password, List<String> roles) {
        return getAccessToken()
                .thenCompose(token -> userExists(userName)
                        .thenCompose(userExists -> {
                            if(!userExists) {
                                return usersApi.adminRealmsRealmUsersPostAsync(token, realm,
                                        new UserRepresentation()
                                        .username(userName)
                                        .credentials(List.of(new CredentialRepresentation().type("password").value(password)))
                                        .enabled(true)
                                        .realmRoles(roles)
                                );
                            }
                            return completedFuture(null);
                        }));
    }

    public CompletionStage<Boolean> userExists(String userName) {
        return getAccessToken()
                .thenCompose(token -> usersApi.adminRealmsRealmUsersGetAsync(
                        token,
                        realm,
                        null,
                        null,
                        null,
                        true,
                        true,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        userName
                        ))
                .thenApply(userRepresentations -> userRepresentations.size() == 1 ? Boolean.TRUE : Boolean.FALSE);
    }

    public CompletionStage<List<Permission>> getUserPermissions(String userToken) {
        List<Permission> cachedPermissions = userPermissionsCache.getIfPresent(userToken);
        if(cachedPermissions != null) {
            return completedFuture(cachedPermissions);
        }
        return authenticationApi.getUserPermissionsAsync(realm, userToken, clientID)
                .thenApply(permissions -> {
                    userPermissionsCache.put(userToken, permissions);
                    return permissions;
                });
    }

    public CompletionStage<Void> mapRealmRole(String userName, String roleName) {
        return getAccessToken()
                .thenCompose(token -> usersApi.adminRealmsRealmUsersGetAsync(token, realm, userName)
                        .thenCompose(userRepresentation -> rolesApi.adminRealmsRealmRolesRoleNameGetAsync(token, realm, roleName)
                                .thenCompose(roleRepresentation -> roleMapperApi.adminRealmsRealmUsersUserIdRoleMappingsRealmPostAsync(token, realm, userRepresentation.getId(), List.of(roleRepresentation)))));
    }

}
