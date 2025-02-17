package in.docq.abha.rest.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.api.GatewaySessionApi;
import in.docq.abha.rest.client.api.HealthFacilitySearchApi;
import in.docq.abha.rest.client.api.HealthProfessionalSearchApi;
import in.docq.abha.rest.client.model.ApiHiecmGatewayV3SessionsPost200Response;
import in.docq.abha.rest.client.model.ApiHiecmGatewayV3SessionsPostRequest;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AbhaRestClient {

    private final String clientSecret;
    private final ApiClient apiClient;
    private final String clientId;
    private final String clientCredentials;
    private final HealthFacilitySearchApi healthFacilitySearchApi;
    private final HealthProfessionalSearchApi healthProfessionalSearchApi;
    private final Cache<String, String> tokenCache;
    private final GatewaySessionApi gatewaySessionApi;
    private final String envName;

    public AbhaRestClient(ApiClient apiClient, String clientId, String clientSecret, String clientCredentials, String envName) {
        this.apiClient = apiClient;
        this.healthFacilitySearchApi = new HealthFacilitySearchApi(apiClient);
        this.healthProfessionalSearchApi = new HealthProfessionalSearchApi(apiClient);
        this.gatewaySessionApi = new GatewaySessionApi(apiClient);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientCredentials = clientCredentials;
        this.envName = envName;
        this.tokenCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(1000)
                .maximumSize(10000)
                .build();;
    }

    private CompletionStage<String> getToken() throws ApiException {
        String requestId = UUID.randomUUID().toString();
        ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest = new ApiHiecmGatewayV3SessionsPostRequest();
        apiHiecmGatewayV3SessionsPostRequest.setClientId(clientId);
        apiHiecmGatewayV3SessionsPostRequest.setClientSecret(clientSecret);
        apiHiecmGatewayV3SessionsPostRequest.setGrantType(clientCredentials);
        if (tokenCache.getIfPresent("clientAccessToken") != null) {
            return CompletableFuture.completedFuture(tokenCache.getIfPresent("clientAccessToken"));
        }
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().toString(), requestId, envName, apiHiecmGatewayV3SessionsPostRequest)
                .thenApply(response -> {
                    tokenCache.put("clientAccessToken", response.getAccessToken());
                    return response.getAccessToken();
                });
    }
}
