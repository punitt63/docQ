package in.docq.health.facility.auth;

import in.docq.keycloak.rest.client.ApiClient;
import in.docq.keycloak.rest.client.api.AuthenticationApi;
import in.docq.keycloak.rest.client.api.UsersApi;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

@Component
public class DesktopKeycloakRestClient {
    private final String realm;
    private final String clientID;
    private final String clientSecret;
    private final ApiClient apiClient;
    private final AuthenticationApi authenticationApi;
    private final UsersApi usersApi;

    public DesktopKeycloakRestClient(@Value("${keycloak.base.url}") String baseUrl,
                                     @Value("${keycloak.realm}") String realm,
                                     @Value("${keycloak.desktop.client.id}") String clientID,
                                     @Value("${keycloak.desktop.client.secret}") String clientSecret,
                                     @Qualifier("KeyCloakOkHttpClient") OkHttpClient okHttpClient) {
        this.realm = realm;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.apiClient = new ApiClient(baseUrl, okHttpClient);
        this.authenticationApi = new AuthenticationApi(apiClient);
        this.usersApi = new UsersApi(apiClient);
    }

    public CompletionStage<GetAccessToken200Response> getUserAccessToken(String userName, String password) {
        return authenticationApi.getUserAccessTokenAsync(realm, userName, password, clientID, clientSecret);
    }

    public CompletionStage<GetAccessToken200Response> refreshUserAccessToken(String refreshToken) {
        return authenticationApi.refreshUserAccessTokenAsync(realm, refreshToken, clientID, clientSecret);
    }

    public CompletionStage<Void> logoutUser(String bearerToken, String refreshToken) {
        return usersApi.realmsRealmProtocolOpenIdConnectLogoutPostAsync(realm, clientID, clientSecret, bearerToken, refreshToken);
    }


}
