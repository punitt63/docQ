package in.docq.abha.rest.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.docq.abha.rest.client.api.*;
import in.docq.abha.rest.client.model.*;
import in.docq.abha.rest.client.utils.RSAEncrypter;
import in.docq.abha.rest.client.api.GatewaySessionApi;
import in.docq.abha.rest.client.api.HealthFacilitySearchApi;
import in.docq.abha.rest.client.api.HealthProfessionalSearchApi;
import in.docq.abha.rest.client.api.MultipleHrpApiApi;
import in.docq.abha.rest.client.model.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

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

    private final HealthFacilitySearchApi healthFacilitySearchApi;
    private final HealthProfessionalSearchApi healthProfessionalSearchApi;
    private final MultipleHrpApiApi multipleHrpApiApi;
    private final Cache<String, String> tokenCache;
    private final GatewaySessionApi gatewaySessionApi;

    public AbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
        this.apiClient = apiClient;
        this.abhaLoginApi = new AbhaLoginApi(apiClient);
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
        this.gatewaySessionApi = new GatewaySessionApi(apiClient);
        this.multipleHrpApiApi = new MultipleHrpApiApi(apiClient);
        this.hipInitiatedLinkingApi = new HIPInitiatedLinkingApi(apiClient);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(1000)
                .maximumSize(10000)
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
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
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
        return gatewaySessionApi.apiHiecmGatewayV3SessionsPostAsync(Instant.now().toString(), UUID.randomUUID().toString(), "sbx", apiHiecmGatewayV3SessionsPostRequest)
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
                .thenCompose(token -> abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentRequestOtpPostAsync(token, UUID.randomUUID().toString(), Instant.now().toString(),
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
                return abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentEnrolByAadhaarPostAsyncCall(Instant.now().toString(), UUID.randomUUID().toString(), null, token,
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
                return abhaEnrollmentViaAadhaarApi.abhaApiV3EnrollmentEnrolAbhaAddressPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().toString(),
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
                        return abhaLoginApi.abhaApiV3ProfileLoginRequestOtpPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().toString(),
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
                        return abhaLoginApi.abhaApiV3ProfileLoginVerifyPostAsyncCall(token, UUID.randomUUID().toString(), Instant.now().toString(),
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
                        return abhaLoginApi.abhaApiV3ProfileLoginVerifyUserPostAsyncCall(token, tToken, UUID.randomUUID().toString(), Instant.now().toString(),
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
                .thenCompose(token -> abhaAddressVerificationApi.abhaApiV3PhrWebLoginProfileAbhaProfileGetAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().toString()));
    }

    public CompletionStage<String> getAbhaCard(String xToken) {
        return getAccessToken()
                .thenCompose(token -> abhaAddressVerificationApi.abhaApiV3PhrWebLoginProfileAbhaPhrCardGetAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().toString()));
    }

    public CompletionStage<AbhaApiV3ProfileAccountRequestOtpPost200Response> abhaDeletionRequestOtp(String xToken, List<String> scopes, String loginHint, String loginId, String otpSystem) {
        return getAccessToken()
                .thenCompose(token -> abhaProfileApi.abhaApiV3ProfileAccountRequestOtpPostAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().toString(),
                        new AbhaApiV3ProfileAccountRequestOtpPostRequest()
                                .scope(scopes)
                                .loginHint(loginHint)
                                .loginId(RSAEncrypter.encrypt(loginId))
                                .otpSystem(otpSystem)));
    }

    public CompletionStage<AbhaApiV3ProfileAccountVerifyPost200Response> abhaDeletionVerifyOtp(String xToken, List<String> scopes, List<String> authMethods, String txnId, String otp, List<String> reasons) {
        return getAccessToken()
                .thenCompose(token -> abhaProfileApi.abhaApiV3ProfileAccountVerifyPostAsyncCall(token, xToken, UUID.randomUUID().toString(), Instant.now().toString(),
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

    public CompletionStage<Void> generateLinkingToken(String requestId, String timestamp, String xHipId, String xCmId, HIPInitiatedGenerateTokenRequest hipInitiatedGenerateTokenRequest) {
        return getAccessToken()
                .thenCompose(token ->  hipInitiatedLinkingApi.generateTokenAsync(token, requestId, timestamp, xHipId, xCmId, hipInitiatedGenerateTokenRequest));
    }

    public  CompletionStage<Void> sendDeepLinkNotification(String requestId, String timestamp, String xCmId, SendSmsNotificationRequest sendSmsNotificationRequest) {
        return getAccessToken()
                .thenCompose(token -> hipInitiatedLinkingApi.sendSmsNotificationAsync(token, requestId, timestamp, xCmId, sendSmsNotificationRequest));
    }

    public CompletionStage<Void> linkHIPInitiatedCareContext(String requestId, String timestamp, String xCmId, String xHipId, String xLinkToken, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request) {
        return getAccessToken()
                .thenCompose(token -> hipInitiatedLinkingApi.linkCareContextAsync(token, requestId, timestamp, xCmId, xHipId, xLinkToken, abdmHipInitiatedLinkingHip1Request));
    }
}
