package configuration;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import in.docq.abha.rest.client.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
        public int sendDeepLinkNotificationCount;
        public int generateLinkingTokenCount;
        public int linkCareContextCount;
        public int linkUserInitiatedCareContextCount = 0;
        public AbdmUserInitiatedLinking2Request lastLinkUserInitiatedCareContextRequest;

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

        @Override
        public CompletionStage<Void> sendDeepLinkNotification(String requestId, String timestamp, String xCmId, SendSmsNotificationRequest sendSmsNotificationRequest) {
            sendDeepLinkNotificationCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> generateLinkingToken(String requestId, String timestamp, String xHipId, String xCmId, HIPInitiatedGenerateTokenRequest hipInitiatedGenerateTokenRequest) {
            generateLinkingTokenCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> linkHIPInitiatedCareContext(String requestId, String timestamp, String xCmId, String xHipId, String xLinkToken, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request) {
            linkCareContextCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> linkUserInitiatedCareContext(String requestId, String timestamp, String xCmId, String xHiuId, AbdmUserInitiatedLinking2Request request) {
            linkUserInitiatedCareContextCount++;
            lastLinkUserInitiatedCareContextRequest = request;
            return CompletableFuture.completedFuture(null);
        }
    }
}
