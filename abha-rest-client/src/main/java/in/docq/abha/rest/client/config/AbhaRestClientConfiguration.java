package in.docq.abha.rest.client.config;

import in.docq.abha.rest.client.ApiClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbhaRestClientConfiguration {

    public ApiClient abhaApiClient() {
        return new ApiClient(new OkHttpClient());
    }

}
