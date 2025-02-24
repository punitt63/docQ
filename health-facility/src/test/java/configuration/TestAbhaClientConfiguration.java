package configuration;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestAbhaClientConfiguration {

    @Bean
    @Primary
    public AbhaRestClient getMockAbhaRestClient() {
        return new MockAbhaRestClient();
    }

    public static class MockAbhaRestClient extends AbhaRestClient {
        public static String testHealthFacilityID = "test-health-facility-id";

        public MockAbhaRestClient() {
            super(null, null, null);
        }

        public MockAbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
            super(apiClient, clientId, clientSecret);
        }
    }
}
