package in.docq.abha.rest.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.api.GatewaySessionApi;
import in.docq.abha.rest.client.api.HealthFacilitySearchApi;
import in.docq.abha.rest.client.api.HealthProfessionalSearchApi;
import in.docq.abha.rest.client.model.ApiHiecmGatewayV3SessionsPostRequest;
import in.docq.abha.rest.client.model.SearchByHprIdRequest;
import in.docq.abha.rest.client.model.SearchFacilitiesData;
import in.docq.abha.rest.client.model.SearchForFacilitiesRequest;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class AbhaRestClient {

    private final String clientSecret;
    private final ApiClient apiClient;
    private final String clientId;
    private static final String accessTokenGrantType = "client_credentials";
    private static final String refreshTokenGrantType = "refresh_token";
    private static final String accessTokenCacheKey = "clientAccessToken";
    private static final String refreshTokenCacheKey = "clientRefreshToken";
    private final HealthFacilitySearchApi healthFacilitySearchApi;
    private final HealthProfessionalSearchApi healthProfessionalSearchApi;
    private final Cache<String, String> tokenCache;
    private final GatewaySessionApi gatewaySessionApi;

    public AbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
        this.apiClient = apiClient;
        this.healthFacilitySearchApi = new HealthFacilitySearchApi(apiClient);
        this.healthProfessionalSearchApi = new HealthProfessionalSearchApi(apiClient);
        this.gatewaySessionApi = new GatewaySessionApi(apiClient);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(1000)
                .maximumSize(10000)
                .build();
        //eagerInitiateToken();
    }

    private void eagerInitiateToken() {
        generateAndCacheAccessToken();
    }

    private CompletionStage<String> getAccessToken() {
        String currentCachedAccessToken = getCachedAccessToken();
        if(currentCachedAccessToken == null) {
            return generateAndCacheAccessToken()
                    .thenApply(ignore -> this.getCachedAccessToken());
        }
        if(shouldRefreshAccessToken(currentCachedAccessToken)) {
            return refreshToken()
                    .thenApply(ignore -> this.getCachedAccessToken());
        }
        return completedFuture(currentCachedAccessToken);
    }

    private boolean shouldRefreshAccessToken(String accessToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(accessToken);
            return Instant.now().isAfter(decodedJWT.getExpiresAtAsInstant());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    private CompletionStage<Void> generateAndCacheAccessToken() {
        ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest =
                new ApiHiecmGatewayV3SessionsPostRequest()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .grantType(refreshTokenGrantType);
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
                .thenAccept(response -> {
                    tokenCache.put(accessTokenCacheKey, response.getAccessToken());
                    tokenCache.put(refreshTokenCacheKey, response.getRefreshToken());
                });
    }

    private String getCachedRefreshToken() {
        String refreshToken = tokenCache.getIfPresent(refreshTokenCacheKey);
        checkState(refreshToken != null, "refresh token in cache is null");
        return refreshToken;
    }

    private String getCachedAccessToken() {
        String accessToken = tokenCache.getIfPresent(accessTokenCacheKey);
        checkState(accessToken != null, "access token in cache is null");
        return accessToken;
    }

    private CompletionStage<Void> refreshToken() {
        ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest =
                new ApiHiecmGatewayV3SessionsPostRequest()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .grantType(refreshTokenGrantType)
                        .refreshToken(getCachedRefreshToken());
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
                .thenAccept(response -> {
                    tokenCache.put(accessTokenCacheKey, response.getAccessToken());
                    tokenCache.put(refreshTokenCacheKey, response.getRefreshToken());
                });
    }

    public CompletionStage<SearchFacilitiesData> getHealthFacility(String facilityID) {
        return getAccessToken()
                .thenCompose(token -> healthFacilitySearchApi.v15SearchFacilitiesFuzzyPostUsingPOSTAsync(token, new SearchForFacilitiesRequest().facilityId(facilityID)))
                .thenApply(searchForFacilitiesResponse -> {
                    if(searchForFacilitiesResponse.getTotalFacilities() == null || searchForFacilitiesResponse.getTotalFacilities() == 0) {
                        throw new ApiException(404, "Facility " + facilityID + " Not Found");
                    }
                    return searchForFacilitiesResponse.getFacilities().get(0);
                });
    }

    public CompletionStage<Void> getHealthProfessionalExists(String healthProfessionalID) {
        return healthProfessionalSearchApi.searchUserByUseridAsync(new SearchByHprIdRequest().idType("hpr_id").domainName("@hpr.abdm").hprId(healthProfessionalID))
                .thenAccept(verdict -> {
                    if(verdict.equals(Boolean.FALSE)) {
                        throw new ApiException(404, "Health Professional ID " + healthProfessionalID + " Doesn't Exist");
                    }
                });
    }
}
