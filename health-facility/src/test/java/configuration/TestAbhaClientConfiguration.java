package configuration;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import in.docq.abha.rest.client.model.SearchFacilitiesData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Configuration
public class TestAbhaClientConfiguration {

    @Primary
    @Bean
    public AbhaRestClient getMockAbhaRestClient() {
        return new MockAbhaRestClient();
    }

    public static class MockAbhaRestClient extends AbhaRestClient {
        public static String testHealthFacilityID = "IN2310020040";
        public static String testHealthFacilityManagerID = "test-fm-id";
        public static String testDoctorID = "test-doctor-id";
        public static int testStateCode = 29;
        public static int testDistrictCode = 525;
        public static String testFMSpeciality = "Facility Manager";
        public static String testDoctorSpeciality = "General Physician";

        public MockAbhaRestClient() {
            super(null, null, null);
        }

        public MockAbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
            super(apiClient, clientId, clientSecret);
        }

        @Override
        public CompletionStage<SearchFacilitiesData> getHealthFacility(String facilityID) {
            return completedFuture(new SearchFacilitiesData().facilityId("IN2310020040"));
        }

        @Override
        public CompletionStage<Void> getHealthProfessionalExists(String healthProfessionalID) {
            return completedFuture(null);
        }
    }
}
