package configuration;

import com.sun.xml.xsom.impl.scd.Iterators;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.service.OTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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

    @Primary
    @Bean
    public OTPService getMockOtpService() {
        return new MockOtpService();
    }

    public static class MockOtpService implements OTPService {
        public int sendOtpCount = 0;
        public String lastMobileNumber;
        public String otpToReturn;

        @Override
        public CompletionStage<String> sendOtp(String mobileNumber) {
            sendOtpCount++;
            lastMobileNumber = mobileNumber;
            return CompletableFuture.completedFuture(otpToReturn);
        }
    }

    public static class MockAbhaRestClient extends AbhaRestClient {
        public static String testHealthFacilityID = "IN2310020040";
        public static String testSecondHealthFacilityID = "IN2310020041";
        public static String testThirdHealthFacilityID = "IN2310020019";
        public static String testHealthFacilityManagerID = "test-fm-id";
        public static String testSecondHealthFacilityManagerID = "test-second-fm-id";
        public static String testThirdHealthFacilityManagerID = "test-third-fm-id";
        public static String testDoctorID = "test-doctor-id";
        public static String testSecondDoctorID = "test-doctor-id-2";
        public static String testThirdDoctorID = "test-doctor-id-3";
        public int sendDeepLinkNotificationCount;
        public int generateLinkingTokenCount;
        public int linkCareContextCount;
        public int linkUserInitiatedCareContextCount = 0;
        public int initiateUserLinking = 0;
        public int confirmCareContextLinking = 0;
        public AbdmUserInitiatedLinking6Request lastConfirmCareContextLinkingRequest;
        public AbdmUserInitiatedLinking2Request lastLinkUserInitiatedCareContextRequest;
        public int sendConsentGrantAcknowledgementCount = 0;
        public int healthInfoRequestAcknowledgementCount = 0;
        public int getGatewayPublicCertsCount = 0;
        public int sendConsentRequestCount = 0;
        public int fetchConsentRequestCount = 0;
        public int sendHealthInfoRequestCount = 0;
        public Map<String, AbdmDataFlow7Request> sendHealthInfoRequestMap = new HashMap<>();
        public AbdmSessions3200Response gatewayPublicKeysResponse;

        public MockAbhaRestClient() {
            super(null, null, null, null);
        }

        public MockAbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
            super(apiClient, clientId, clientSecret, null);
        }

        @Override
        public CompletionStage<SearchFacilitiesData> getHealthFacility(String facilityID) {
            if(facilityID.equals(testHealthFacilityID) || facilityID.equals(testSecondHealthFacilityID) || facilityID.equals(testThirdHealthFacilityID)) {
                return completedFuture(new SearchFacilitiesData().facilityId(facilityID));
            }
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> getHealthProfessionalExists(String healthProfessionalID) {
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> sendDeepLinkNotification(String requestId, String timestamp, SendSmsNotificationRequest sendSmsNotificationRequest) {
            sendDeepLinkNotificationCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> generateLinkingToken(String requestId, String timestamp, String xHipId, HIPInitiatedGenerateTokenRequest hipInitiatedGenerateTokenRequest) {
            generateLinkingTokenCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> linkHIPInitiatedCareContext(String requestId, String timestamp, String xHipId, String xLinkToken, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request) {
            linkCareContextCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> linkUserInitiatedCareContext(String requestId, String timestamp, String xHiuId, AbdmUserInitiatedLinking2Request request) {
            linkUserInitiatedCareContextCount++;
            lastLinkUserInitiatedCareContextRequest = request;
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<Void> initiateUserLinking(String requestId, String timestamp, String xHiuId, AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request) {
            initiateUserLinking++;
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<Void> confirmCareContextLinking(String requestId, String timestamp, AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request) {
            lastConfirmCareContextLinkingRequest = abdmUserInitiatedLinking6Request;
            confirmCareContextLinking++;
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<Void> sendConsentGrantAcknowledgement(String requestId, String timestamp, AbdmConsentManagement2Request request) {
            sendConsentGrantAcknowledgementCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> healthInfoRequestAcknowledgement(String requestId, String timestamp, AbdmConsentManagement5Request request) {
            healthInfoRequestAcknowledgementCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<AbdmSessions3200Response> getGatewayPublicCerts() {
            getGatewayPublicCertsCount++;
            return completedFuture(gatewayPublicKeysResponse);
        }

        @Override
        public CompletionStage<Void> sendConsentRequest(String requestId, String timestamp, AbdmConsentManagement1Request request) {
            sendConsentRequestCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> fetchConsentArtifact(String requestId, String timestamp, String hiuId, AbdmConsentManagement5Request1 abdmConsentManagement5Request1) {
            fetchConsentRequestCount++;
            return completedFuture(null);
        }

        @Override
        public CompletionStage<Void> sendHealthInfoRequest(String requestId, String timestamp, String hiuId, AbdmDataFlow7Request dataFlow7Request) {
            sendHealthInfoRequestCount++;
            sendHealthInfoRequestMap.put(dataFlow7Request.getHiRequest().getConsent().getId(), dataFlow7Request);
            return completedFuture(null);
        }

        public AbdmDataFlow7Request getStoredHealthInfoRequest(String consentArtifactId) {
            return sendHealthInfoRequestMap.get(consentArtifactId);
        }
    }
}
