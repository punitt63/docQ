package in.docq.keycloak.rest.client.configuration;

import in.docq.keycloak.rest.client.ApiClient;
import in.docq.keycloak.rest.client.KeyCloakRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakClientConfiguration {

    @Bean
    public KeyCloakRestClient getKeyCloakRestClient(@Value("${keycloak.base.url}") String keyCloakBaseURL,
                               @Value("${keycloak.realm}") String keyCloakRealm,
                               @Value("${keycloak.client.id}") String keyCloakClientID,
                               @Value("${keycloak.client.secret}") String keyCloakClientSecret) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(keyCloakBaseURL);
        return new KeyCloakRestClient(keyCloakRealm, keyCloakClientID, keyCloakClientSecret, apiClient);
    }
}
