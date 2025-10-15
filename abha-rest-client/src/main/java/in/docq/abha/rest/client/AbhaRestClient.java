package in.docq.abha.rest.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.api.*;
import in.docq.abha.rest.client.api.phr.AbdmHiecmPatientSharePhrApi;
import in.docq.abha.rest.client.api.phr.EnrollmentApiCollectionApi;
import in.docq.abha.rest.client.model.*;
import in.docq.abha.rest.client.model.AbdmConsentManagement1Request;
import in.docq.abha.rest.client.model.AbdmConsentManagement2Request;
import in.docq.abha.rest.client.model.AbdmConsentManagement3Request2;
import in.docq.abha.rest.client.model.AbdmConsentManagement5Request;
import in.docq.abha.rest.client.model.AbdmConsentManagement5Request1;
import in.docq.abha.rest.client.model.AbdmConsentManagement6Request;
import in.docq.abha.rest.client.model.AbdmDataFlow7Request;
import in.docq.abha.rest.client.model.AbdmDataFlow8Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking2Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking4Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking6Request;
import in.docq.abha.rest.client.model.MultipleHRPRequest;
import in.docq.abha.rest.client.utils.RSAEncrypter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

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
    private final AbhaLoginApi abhaLoginApi;
    private final AbhaCardApi abhaCardApi;
    private final AbhaEnrollmentViaAadhaarApi abhaEnrollmentViaAadhaarApi;
    private final AbhaAddressVerificationApi abhaAddressVerificationApi;
    private final AbhaProfileApi abhaProfileApi;
    private final HIPInitiatedLinkingApi hipInitiatedLinkingApi;
    private final UserInitiatedLinkingHipApi userInitiatedLinkingHipApi;
    private final HealthFacilitySearchApi healthFacilitySearchApi;
    private final HealthProfessionalSearchApi healthProfessionalSearchApi;
    private final MultipleHrpApiApi multipleHrpApiApi;
    private final EnrollmentApiCollectionApi enrollmentApiCollectionApi;
    private final in.docq.abha.rest.client.api.phr.LoginApiCollectionApi loginApiCollectionApi;
    private final in.docq.abha.rest.client.api.phr.ProfileApiCollectionApi profileApiCollectionApi;
    private final AbdmHiecmPatientSharePhrApi abdmHiecmPatientSharePhrApi;

    private final Cache<String, String> tokenCache;
    private final Cache<String, AbdmSessions3200Response> gatewayPublicCertCache;
    private final GatewaySessionApi gatewaySessionApi;
    private final ConsentManagementDataFlowHipApi consentManagementDataFlowHipApi;
    private final ConsentManagementDataFlowHiuApi consentManagementDataFlowHiuApi;
    private final String xCmId;

    public AbhaRestClient(ApiClient apiClient, String clientId, String clientSecret, String xCmId) {
        this.apiClient = apiClient;
        this.abhaLoginApi = new AbhaLoginApi(apiClient);
        this.consentManagementDataFlowHipApi = new ConsentManagementDataFlowHipApi(apiClient);
        this.xCmId = xCmId;
        this.abhaLoginApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.abhaCardApi = new AbhaCardApi(apiClient);
        this.abhaEnrollmentViaAadhaarApi = new AbhaEnrollmentViaAadhaarApi(apiClient);
        this.abhaEnrollmentViaAadhaarApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.abhaAddressVerificationApi = new AbhaAddressVerificationApi(apiClient);
        this.abhaAddressVerificationApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.abhaProfileApi = new AbhaProfileApi(apiClient);
        this.abhaProfileApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.healthFacilitySearchApi = new HealthFacilitySearchApi(apiClient);
        this.healthProfessionalSearchApi = new HealthProfessionalSearchApi(apiClient);
        this.enrollmentApiCollectionApi = new EnrollmentApiCollectionApi(apiClient);
        this.enrollmentApiCollectionApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.loginApiCollectionApi = new in.docq.abha.rest.client.api.phr.LoginApiCollectionApi(apiClient);
        this.loginApiCollectionApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.profileApiCollectionApi = new in.docq.abha.rest.client.api.phr.ProfileApiCollectionApi(apiClient);
        this.profileApiCollectionApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");
        this.abdmHiecmPatientSharePhrApi = new AbdmHiecmPatientSharePhrApi(apiClient);
        this.abdmHiecmPatientSharePhrApi.setCustomBaseUrl("https://abhasbx.abdm.gov.in");

        this.gatewaySessionApi = new GatewaySessionApi(apiClient);
        this.multipleHrpApiApi = new MultipleHrpApiApi(apiClient);
        this.hipInitiatedLinkingApi = new HIPInitiatedLinkingApi(apiClient);
        this.userInitiatedLinkingHipApi = new UserInitiatedLinkingHipApi(apiClient);
        this.consentManagementDataFlowHiuApi = new ConsentManagementDataFlowHiuApi(apiClient);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(1000)
                .maximumSize(10000)
                .build();
        this.gatewayPublicCertCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
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
                        .grantType(accessTokenGrantType);
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
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
        //checkState(accessToken != null, "access token in cache is null");
        return accessToken;
    }

    private CompletionStage<Void> refreshToken() {
        ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest =
                new ApiHiecmGatewayV3SessionsPostRequest()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .grantType(refreshTokenGrantType)
                        .refreshToken(getCachedRefreshToken());
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
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

    public CompletionStage<AbhaApiV3EnrollmentRequestOtpPost200Response> abhaEnrollmentRequestOtp(String aadharNumber) {
        return getAccessToken()
                .thenCompose(token -> abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentRequestOtpPostAsync(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new AbhaApiV3EnrollmentRequestOtpPostRequest()
                                .scope(List.of("abha-enrol"))
                                .loginHint("aadhaar")
                                .loginId(RSAEncrypter.encrypt(aadharNumber))
                                .otpSystem("aadhaar")));
    }

    public CompletionStage<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response> abhaEnrollmentByAadhar(List<String> authMethods, String txnId, String otpValue, String mobile) {
        AbhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthData abhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthData = new AbhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthData();
        abhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthData.setActualInstance(new AbhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthDataAnyOf()
                .otp(new AbhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthDataAnyOfOtp()
                        .otpValue(RSAEncrypter.encrypt(otpValue))
                        .mobile(mobile)
                        .txnId(txnId)));
        return getAccessToken().thenCompose(token -> {
            try {
                return abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentEnrolByAadhaarPostAsyncCall(Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), UUID.randomUUID().toString(), null, token,
                        new AbhaApiV3EnrollmentEnrolByAadhaarPostRequest()
                                .authData(abhaApiV3EnrollmentEnrolByAadhaarPostRequestAuthData)
                                .consent(new AbhaApiV3EnrollmentEnrolByAadhaarPostRequestConsent().code("abha-enrollment").version("1.4")));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<AbhaApiV3EnrollmentEnrolSuggestionGet200Response> getAbhaAddressSuggestions(String txnId) {
        return getAccessToken()
                .thenCompose(token -> abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentEnrolSuggestionGetAsyncCall(token, txnId, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()));
    }

    public CompletionStage<AbhaApiV3EnrollmentEnrolAbhaAddressPost200Response> enrolAbhaAddress(String txnId, String abhaAddress, String preferred) {
        return getAccessToken().thenCompose(token -> {
            try {
                return abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentEnrolAbhaAddressPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new AbhaApiV3EnrollmentEnrolAbhaAddressPostRequest()
                                .txnId(txnId)
                                .abhaAddress(abhaAddress)
                                .preferred(preferred));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<AbhaApiV3ProfileLoginRequestOtpPost200Response> abhaLoginRequestOtp(List<String> scopes, String loginHint, String loginId, String otpSystem) {
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaLoginApi.abhaApiV3ProfileLoginRequestOtpPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3ProfileLoginRequestOtpPostRequest()
                                        .scope(scopes)
                                        .loginHint(loginHint)
                                        .loginId(RSAEncrypter.encrypt(loginId))
                                        .otpSystem(otpSystem));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3ProfileLoginVerifyPost200Response> abhaLoginVerifyOtp(List<String> scopes, List<String> authMethods, String txnId, String otp) {
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaLoginApi.abhaApiV3ProfileLoginVerifyPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3ProfileLoginVerifyPostRequest()
                                        .scope(scopes)
                                        .authData(new AbhaApiV3ProfileLoginVerifyPostRequestAuthData()
                                                .authMethods(authMethods)
                                                .otp(new AbhaApiV3ProfileLoginVerifyPostRequestAuthDataOtp()
                                                        .otpValue(RSAEncrypter.encrypt(otp))
                                                        .txnId(txnId))));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3ProfileLoginVerifyUserPost200Response> abhaLoginVerifyUser(String abhaNumber, String txnId, String tToken) {
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaLoginApi.abhaApiV3ProfileLoginVerifyUserPostAsyncCall(token, tToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3ProfileLoginVerifyUserPostRequest()
                                        .abHANumber(abhaNumber)
                                        .txnId(txnId));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> searchAbhaAddress(String abhaAddress) {
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaAddressVerificationApi.abhaApiV3PhrWebLoginAbhaSearchPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3PhrWebLoginAbhaSearchPostRequest()
                                        .abhaAddress(abhaAddress));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> abhaAddressLoginRequestOtp(List<String> scopes, String loginHint, String loginId, String otpSystem) {
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaAddressVerificationApi.abhaApiV3PhrWebLoginAbhaRequestOtpPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest()
                                        .scope(scopes)
                                        .loginHint(loginHint)
                                        .loginId(RSAEncrypter.encrypt(loginId))
                                        .otpSystem(otpSystem));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> abhaAddressLoginVerifyOtp(List<String> scopes, List<String> authMethods, String txnId, String otp) {
        AbhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthData abhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthData = new AbhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthData();
        abhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthData.setActualInstance(List.of(new AbhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthDataAnyOfInner()
                .otp(new AbhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthDataAnyOfInnerOtp()
                        .otpValue(RSAEncrypter.encrypt(otp))
                        .txnId(txnId))));
        return getAccessToken()
                .thenCompose(token -> {
                    try {
                        return abhaAddressVerificationApi.abhaApiV3PhrWebLoginAbhaVerifyPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                new AbhaApiV3PhrWebLoginAbhaVerifyPostRequest()
                                        .scope(scopes)
                                        .authData(abhaApiV3PhrWebLoginAbhaVerifyPostRequestAuthData));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletionStage<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> getAbhaAddressProfile(String xToken) {
        return getAccessToken()
                .thenCompose(token -> abhaAddressVerificationApi.abhaApiV3PhrWebLoginProfileAbhaProfileGetAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()));
    }

    public CompletionStage<String> getAbhaCard(String xToken) {
        return getAccessToken()
                .thenCompose(token -> abhaAddressVerificationApi.abhaApiV3PhrWebLoginProfileAbhaPhrCardGetAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()));
    }

    public CompletionStage<AbhaApiV3ProfileAccountRequestOtpPost200Response> abhaDeletionRequestOtp(String xToken, List<String> scopes, String loginHint, String loginId, String otpSystem) {
        return getAccessToken()
                .thenCompose(token -> abhaProfileApi.abhaApiV3ProfileAccountRequestOtpPostAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new AbhaApiV3ProfileAccountRequestOtpPostRequest()
                                .scope(scopes)
                                .loginHint(loginHint)
                                .loginId(RSAEncrypter.encrypt(loginId))
                                .otpSystem(otpSystem)));
    }

    public CompletionStage<AbhaApiV3ProfileAccountVerifyPost200Response> abhaDeletionVerifyOtp(String xToken, List<String> scopes, List<String> authMethods, String txnId, String otp, List<String> reasons) {
        return getAccessToken()
                .thenCompose(token -> abhaProfileApi.abhaApiV3ProfileAccountVerifyPostAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new AbhaApiV3ProfileAccountVerifyPostRequest()
                                .scope(scopes)
                                .authData(new AbhaApiV3ProfileAccountVerifyPostRequestAuthData()
                                        .authMethods(authMethods)
                                        .otp(new AbhaApiV3ProfileAccountVerifyPostRequestAuthDataOtp()
                                                .otpValue(RSAEncrypter.encrypt(otp))
                                                .txnId(txnId)))
                                .reasons(reasons)));
    }

    public CompletionStage<Void> registerHFRToBridge(String facilityId, String facilityName) {
        return getAccessToken()
                .thenCompose(token -> multipleHrpApiApi.v1MutipleHRPAddUpdateServicesUsingPOSTAsync(token,
                        new BridgeAddUpdate()
                                .facilityId(facilityId)
                                .facilityName(facilityName)
                                .HRP(List.of(new MultipleHRPRequest().active(true)
                                        .bridgeId(clientId)
                                        .hipName(facilityName)
                                        .type("HFR")
                                ))))
                .thenApply(response -> {
                    if(response.getError() != null) {
                        throw new ApiException(500, "Failed to register HFR to bridge");
                    }
                    return null;
                });
    }

    public CompletionStage<Void> generateLinkingToken(String requestId, String timestamp, String xHipId, HIPInitiatedGenerateTokenRequest hipInitiatedGenerateTokenRequest) {
        return getAccessToken()
                .thenCompose(token ->  hipInitiatedLinkingApi.generateTokenAsync(token, requestId, timestamp, xHipId, xCmId, hipInitiatedGenerateTokenRequest));
    }

    public  CompletionStage<Void> sendDeepLinkNotification(String requestId, String timestamp, SendSmsNotificationRequest sendSmsNotificationRequest) {
        return getAccessToken()
                .thenCompose(token -> hipInitiatedLinkingApi.sendSmsNotificationAsync(token, requestId, timestamp, xCmId, sendSmsNotificationRequest));
    }

    public CompletionStage<Void> linkHIPInitiatedCareContext(String requestId, String timestamp, String xHipId, String xLinkToken, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request) {
        return getAccessToken()
                .thenCompose(token -> hipInitiatedLinkingApi.linkCareContextAsync(token, requestId, timestamp, xCmId, xHipId, xLinkToken, abdmHipInitiatedLinkingHip1Request));
    }

    public CompletionStage<Void> linkUserInitiatedCareContext(String requestId, String timestamp, String xHiuId, AbdmUserInitiatedLinking2Request abdmUserInitiatedLinking2Request) {
        return getAccessToken()
                .thenCompose(token -> userInitiatedLinkingHipApi.userInitiatedLinkingAsync(token, requestId, timestamp, xCmId, xHiuId, abdmUserInitiatedLinking2Request));
    }

    public CompletionStage<Void> initiateUserLinking(String requestId, String timestamp, String xHiuId, AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request) {
        return getAccessToken()
                .thenCompose(token -> userInitiatedLinkingHipApi.abdmUserInitiatedLinking4Async(token, requestId, timestamp, xCmId, abdmUserInitiatedLinking4Request));
    }

    public CompletionStage<Void> confirmCareContextLinking(String requestId, String timestamp, AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request) {
        return getAccessToken()
                .thenCompose(token -> userInitiatedLinkingHipApi.abdmUserInitiatedLinking6Async(token, requestId, timestamp, xCmId, abdmUserInitiatedLinking6Request));
    }

    public CompletionStage<Void> sendConsentGrantAcknowledgement(String requestId, String timestamp, AbdmConsentManagement2Request abdmConsentManagement2Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHipApi.abdmConsentManagement2Async(token, requestId, timestamp, xCmId, abdmConsentManagement2Request));
    }

    public CompletionStage<Void> healthInfoRequestAcknowledgement(String requestId, String timestamp, AbdmConsentManagement5Request abdmConsentManagement5Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHipApi.abdmConsentManagement5Async(token, requestId, timestamp, xCmId, abdmConsentManagement5Request));
    }

    public CompletionStage<Void> healthRecordDataTransfer(String dataPushUrl, AbdmConsentManagement6Request abdmConsentManagement6Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHipApi.abdmConsentManagement6Async(token, dataPushUrl, abdmConsentManagement6Request));
    }

    public CompletionStage<Void> notifyDataTransfer(String requestId, String timestamp, AbdmDataFlow8Request abdmConsentManagement8Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHipApi.abdmDataFlow8Async(token, requestId, timestamp, xCmId, abdmConsentManagement8Request));
    }

    public CompletionStage<AbdmSessions3200Response> getGatewayPublicCerts() {
        return getAccessToken()
                .thenCompose(token -> {
                    AbdmSessions3200Response cachedCerts = gatewayPublicCertCache.getIfPresent("gatewayPublicCerts");
                    if(cachedCerts != null) {
                        return completedFuture(cachedCerts);
                    }
                    return gatewaySessionApi.abdmSessions3Async(token, UUID.randomUUID().toString(),  Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),xCmId)
                            .thenApply(response -> {
                                gatewayPublicCertCache.put("gatewayPublicCerts", response);
                                return response;
                            });
                });
    }

    public CompletionStage<Void> sendConsentRequest(String requestId, String timestamp, AbdmConsentManagement1Request abdmConsentManagement1Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHiuApi.abdmConsentManagement1Async(token, requestId, timestamp, xCmId, abdmConsentManagement1Request));
    }

    public CompletionStage<Void> notifyPatientActionForConsent(String requestId, String timestamp, AbdmConsentManagement3Request2 abdmConsentManagement3Request2) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHiuApi.abdmConsentManagement3_0Async(token, requestId, timestamp, xCmId, abdmConsentManagement3Request2));
    }

    public CompletionStage<Void> fetchConsentArtifact(String requestId, String timestamp, String hiuId, AbdmConsentManagement5Request1 abdmConsentManagement5Request1) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHiuApi.abdmConsentManagement5Async(token, requestId, timestamp, xCmId, hiuId, abdmConsentManagement5Request1));
    }

    public CompletionStage<Void> sendHealthInfoRequest(String requestId, String timestamp, String hiuId, AbdmDataFlow7Request dataFlow7Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHiuApi.abdmDataFlow7Async(token, requestId, timestamp, xCmId, hiuId, dataFlow7Request));
    }

    public CompletionStage<Void> notifyHealthInfoTransfer(String requestId, String timestamp, AbdmDataFlow8Request abdmDataFlow8Request) {
        return getAccessToken()
                .thenCompose(token -> consentManagementDataFlowHiuApi.abdmDataFlow8Async(token, requestId, timestamp, xCmId, abdmDataFlow8Request));
    }

    // phr abha enrol apis

    public CompletionStage<OtpRequestMobile200Response> otpRequestMobile(String aadharNumber) {
        return getAccessToken()
                .thenCompose(token -> enrollmentApiCollectionApi.otpRequestMobileAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new OtpRequestMobileRequest()
                                .scope(List.of("abha-login", "mobile-verify"))
                                .loginHint("mobile-number")
                                .loginId(RSAEncrypter.encrypt(aadharNumber))
                                .otpSystem("abdm")));
    }

    public CompletionStage<OtpVerifyMobile200Response> verifyMobileOtpPhr(List<String> scopes, List<String> authMethods, String txnId, String otp) {
        return getAccessToken().thenCompose(token -> {
            try {
                return enrollmentApiCollectionApi.otpVerifyMobileAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), new OtpVerifyMobileRequest()
                        .scope(scopes)
                        .authData(new OtpVerifyMobileRequestAuthData()
                                .authMethods(authMethods)
                                .otp(new OtpVerifyMobileRequestAuthDataOtp()
                                        .otpValue(RSAEncrypter.encrypt(otp))
                                        .txnId(txnId))));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<SuggestionApi200Response> getAbhaAddressSuggestionsPhr(String dayOfBirth, String firstName, String lastName, String monthOfBirth, String yearOfBirth, String txnId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return enrollmentApiCollectionApi.suggestionApiAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), new SuggestionApiRequest()
                        .dayOfBirth(dayOfBirth)
                        .firstName(firstName)
                        .lastName(lastName)
                        .monthOfBirth(monthOfBirth)
                        .yearOfBirth(yearOfBirth)
                        .txnId(txnId));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<Void> isAbhaAddressExists(String abhaAddress) {
        return getAccessToken().thenCompose(token -> {
            try {
                return enrollmentApiCollectionApi.isexistsApiAsyncCall(token, abhaAddress, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<EnrollAbhaAddress200Response> enrollAbhaAddressPhr(PhrDetails phrDetails, String txnId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return enrollmentApiCollectionApi.enrollAbhaAddressAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), new EnrollAbhaAddressRequest()
                        .phrDetails(new EnrollAbhaAddressRequestPhrDetails()
                                .abhaAddress(phrDetails.getAbhaAddress())
                                .address(phrDetails.getAddress())
                                .districtCode(phrDetails.getDistrictCode())
                                .districtName(phrDetails.getDistrictName())
                                .email(phrDetails.getEmail())
                                .firstName(phrDetails.getFirstName())
                                .gender(phrDetails.getGender())
                                .lastName(phrDetails.getLastName())
                                .middleName(phrDetails.getMiddleName())
                                .mobile(phrDetails.getMobile())
                                .monthOfBirth(phrDetails.getMonthOfBirth())
                                .password(phrDetails.getPassword())
                                .pinCode(phrDetails.getPinCode())
                                .stateCode(phrDetails.getStateCode())
                                .stateName(phrDetails.getStateName())
                                .yearOfBirth(phrDetails.getYearOfBirth()))
                        .txnId(txnId));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // phr login apis

    public CompletionStage<SearchAuthMethodsAbhaaddress200Response> searchAuthMethodsByAbhaAddress(String abhaAddress) {
        return getAccessToken().thenCompose(token -> {
            try {
                return loginApiCollectionApi.searchAuthMethodsAbhaaddressAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new SearchAuthMethodsAbhaaddressRequest().abhaAddress(abhaAddress));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<OtpRequestMobileLogin200Response> loginRequestOtpForLogin(List<String> scopes, String loginHint, String loginId, String otpSystem) {
        return getAccessToken().thenCompose(token -> {
            try {
                return loginApiCollectionApi.otpRequestMobileLoginAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new OtpRequestMobileLoginRequest()
                                .scope(scopes)
                                .loginHint(loginHint)
                                .loginId(RSAEncrypter.encrypt(loginId))
                                .otpSystem(otpSystem));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<LoginOtpVerifyMobile200Response> loginVerifyMobileOtp(List<String> scopes, List<String> authMethods, String txnId, String otp) {
        return getAccessToken().thenCompose(token -> {
            try {
                return loginApiCollectionApi.loginOtpVerifyMobileAsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new OtpVerifyMobileRequest()
                                .scope(scopes)
                                .authData(new OtpVerifyMobileRequestAuthData()
                                        .authMethods(authMethods)
                                        .otp(new OtpVerifyMobileRequestAuthDataOtp()
                                                .otpValue(RSAEncrypter.encrypt(otp))
                                                .txnId(txnId))));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<VerifyUser200Response> loginVerifyUser(String abhaAddress, String txnId, String tToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return loginApiCollectionApi.verifyUserAsyncCall(token, tToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new VerifyUserRequest()
                                .txnId(txnId)
                                .abhaAddress(abhaAddress));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // profile apis (phr)

    public CompletionStage<DeLinkRequest200Response> deLinkAbhaProfile(String xToken, String transactionId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.deLinkRequestAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new DeLinkRequestRequest().action("DE_LINK").transactionId(transactionId));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<byte[]> getPhrCard(String xToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.getPhrCardAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<GetProfile200Response> getProfile(String xToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.getProfileAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<byte[]> getQrCode(String xToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.getQrCodeAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<LinkRequest200Response> linkAbhaProfile(String xToken, String transactionId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.linkRequestAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new LinkRequestRequest().action("LINK").transactionId(transactionId));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<Logout200Response> logout(String xToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.logoutAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<RefreshToken200Response> refreshProfileToken(String rToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.refreshTokenAsyncCall(token, rToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<OtpRequestMobile200Response> sendUpdateEmailOtp(String xToken, String email, String txnId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.sendOtpUpdateEmailAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new SendOtpUpdateEmailRequest()
                                .scope(List.of("email-verify"))
                                .loginHint("email")
                                .loginId(RSAEncrypter.encrypt(email))
                                .otpSystem("abdm"));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<VerifyOtpUpdateEmail200Response> verifyUpdateEmailOtp(String xToken, String otp, String txnId) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.verifyOtpUpdateEmailAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new VerifyOtpUpdateEmailRequest()
                                .scope(List.of("email-verify"))
                                .authData(new OtpVerifyMobileRequestAuthData()
                                        .authMethods(List.of("OTP"))
                                        .otp(new OtpVerifyMobileRequestAuthDataOtp()
                                                .otpValue(RSAEncrypter.encrypt(otp))
                                                .txnId(txnId))));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<SwitchProfile200Response> switchProfile(String xToken) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.switchProfileAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<UpdateProfile200Response> updateProfile(String xToken, String email, String firstName, String lastName) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.updateProfileAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new UpdateProfileRequest()
                                .email(email)
                                .firstName(firstName)
                                .lastName(lastName));
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<OtpVerifyMobile200ResponseTokens> verifyUserForSwitch(String tToken, String txnId, String otp) {
        return getAccessToken().thenCompose(token -> {
            try {
                return profileApiCollectionApi.verifyUserSwitchProfileAsyncCall(token, tToken, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        new VerifyUserRequest().txnId(txnId) /* request shape per API */);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // AbdmHiecmPatientSharePhrApi wrapper methods

    public CompletionStage<Void> abdmPatientShareHip1(String xCmId, String xHiuId, String xAuthToken, AbdmPatientShareHip1Request request) {
        return getAccessToken().thenCompose(token -> {
            try {
                return abdmHiecmPatientSharePhrApi.abdmPatientShareHip1AsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        xCmId, xHiuId, xAuthToken, request);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<Void> profileShare2(String xHiuId, PatientShare2Request request) {
        return getAccessToken().thenCompose(token -> {
            try {
                return abdmHiecmPatientSharePhrApi.profileShare2AsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        xHiuId, request);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletionStage<ProfileShare3200Response> profileShare3(String xCmId, String xAuthToken, String limit) {
        return getAccessToken().thenCompose(token -> {
            try {
                return abdmHiecmPatientSharePhrApi.profileShare3AsyncCall(token, UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                        xCmId, xAuthToken, limit);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
