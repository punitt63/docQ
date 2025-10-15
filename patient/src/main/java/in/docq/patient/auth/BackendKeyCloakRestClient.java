package in.docq.patient.auth;

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
    private static final String requestingPartyTokenCacheKey = "requestingPartyToken";

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
        generateAndCacheRequestingPartyToken();
    }

    private CompletionStage<Void> generateAndCacheRequestingPartyToken() {
        return authenticationApi
                .getRequestingPartyTokenAsync(
                        realm,
                        "health-facility-backend-app",
                        clientID,
                        clientSecret
                )
                .thenAccept(response -> tokenCache.put(requestingPartyTokenCacheKey, response.getAccessToken()));
    }

    public CompletionStage<String> getRequestingPartyToken() {
        String currentCachedRpt = tokenCache.getIfPresent(requestingPartyTokenCacheKey);
        if(currentCachedRpt == null || shouldRefreshAccessToken(currentCachedRpt)) {
            return generateAndCacheRequestingPartyToken()
                    .thenApply(ignore -> tokenCache.getIfPresent(requestingPartyTokenCacheKey));
        }
        return completedFuture(currentCachedRpt);
    }

    private boolean shouldRefreshAccessToken(String accessToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(accessToken);
            return Instant.now().isAfter(decodedJWT.getExpiresAtAsInstant());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

}
