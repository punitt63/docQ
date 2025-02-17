package in.docq.abha.rest.client.config;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbhaRestClientConfiguration {

    @Bean
    public AbhaRestClient abhaRestClient(@Value("${abha.base.url}") String abhaBaseURL,
                                         @Value("${abha.client.id}") String abhaClientID,
                                         @Value("${abha.client.secret}") String abhaClientSecret) {
        ApiClient apiClient = new ApiClient(new OkHttpClient());
        apiClient.setBasePath(abhaBaseURL);
        return new AbhaRestClient(apiClient, abhaClientID, abhaClientSecret);
    }
}
