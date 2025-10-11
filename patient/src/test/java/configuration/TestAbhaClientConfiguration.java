package configuration;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.ApiClient;
import in.docq.abha.rest.client.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
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

        public MockAbhaRestClient() {
            super(null, null, null, null);
        }

        public MockAbhaRestClient(ApiClient apiClient, String clientId, String clientSecret) {
            super(apiClient, clientId, clientSecret, null);
        }

        @Override
        public CompletionStage<AbhaApiV3EnrollmentRequestOtpPost200Response> abhaEnrollmentRequestOtp(String aadharNumber) {
            return completedFuture(new AbhaApiV3EnrollmentRequestOtpPost200Response()
                    .message("OTP sent to Aadhaar registered mobile number ending with ******9568")
                    .txnId("6f0eeab7-1309-411c-bf02-4c83320ada67"));
        }

        @Override
        public CompletionStage<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response> abhaEnrollmentByAadhar(List<String> authMethods, String txnId, String otpValue, String mobile) {
            AbhaApiV3EnrollmentEnrolByAadhaarPost200Response abhaApiV3EnrollmentEnrolByAadhaarPost200Response = new AbhaApiV3EnrollmentEnrolByAadhaarPost200Response();
            abhaApiV3EnrollmentEnrolByAadhaarPost200Response.setActualInstance(new AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf()
                    .message("Account created successfully")
                    .tokens(new AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens()
                            .token("dummy-token")
                            .refreshToken("dummy-refresh-token")
                            .expiresIn(1800)
                            .refreshExpiresIn(1296000))
                    .abHAProfile(new AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfABHAProfile()
                            .abHANumber("91-1534-3113-3740")
                            .abhaStatus("ACTIVE")
                            .phrAddress(List.of("91153431133740@sbx")))
            );
            return completedFuture(abhaApiV3EnrollmentEnrolByAadhaarPost200Response);
        }

        @Override
        public CompletionStage<AbhaApiV3EnrollmentEnrolSuggestionGet200Response> getAbhaAddressSuggestions(String txnId) {
            return completedFuture(new AbhaApiV3EnrollmentEnrolSuggestionGet200Response()
                    .txnId("6f0eeab7-1309-411c-bf02-4c83320ada67")
                    .abhaAddressList(List.of("punitpichholia06","punitpichholia0306")));
        }

        @Override
        public CompletionStage<AbhaApiV3EnrollmentEnrolAbhaAddressPost200Response> enrolAbhaAddress(String txnId, String abhaAddress, String preferred) {
            return completedFuture(new AbhaApiV3EnrollmentEnrolAbhaAddressPost200Response()
                    .txnId("6f0eeab7-1309-411c-bf02-4c83320ada67")
                    .abhaAddress("punitpichholia06")
                    .preferred("punitpichholia06"));
        }

        @Override
        public CompletionStage<AbhaApiV3ProfileLoginRequestOtpPost200Response> abhaLoginRequestOtp(List<String> scopes, String loginHint, String loginId, String otpSystem) {
            return completedFuture(new AbhaApiV3ProfileLoginRequestOtpPost200Response()
                    .txnId("6f0eeab7-1309-411c-bf02-4c83320ada67")
                    .message("OTP sent to Aadhaar registered mobile number ending with ******9568"));
        }

        @Override
        public CompletionStage<AbhaApiV3ProfileLoginVerifyPost200Response> abhaLoginVerifyOtp(List<String> scopes, List<String> authMethods, String txnId, String otp) {
            return completedFuture(new AbhaApiV3ProfileLoginVerifyPost200Response()
                    .addAccountsItem(new AbhaApiV3ProfileLoginVerifyPost200ResponseAccountsInner()
                                    .abHANumber("91-1534-3113-3740")
                                    .name("Punit Pichholia"))
                    .message("OTP verified successfully")
                    .authResult("success")
                    .token("dummy-token"));
        }

        @Override
        public CompletionStage<AbhaApiV3ProfileLoginVerifyUserPost200Response> abhaLoginVerifyUser(String abhaNumber, String txnId, String tToken) {
            return completedFuture(new AbhaApiV3ProfileLoginVerifyUserPost200Response()
                    .refreshToken("")
                    .token(""));
        }

        @Override
        public CompletionStage<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> abhaAddressLoginRequestOtp(List<String> scopes, String loginHint, String loginId, String otpSystem) {
            return completedFuture(new AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response()
                    .txnId("")
                    .message(""));
        }

        @Override
        public CompletionStage<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> abhaAddressLoginVerifyOtp(List<String> scopes, List<String> authMethods, String txnId, String otp) {
            return completedFuture(new AbhaApiV3PhrWebLoginAbhaVerifyPost200Response()
                    .authResult("")
                    .message(""));
        }

        @Override
        public CompletionStage<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> getAbhaAddressProfile(String xToken) {
            return completedFuture(new AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response()
                    .abhaNumber("")
                    .address("")
                    .dateOfBirth(""));
        }

        @Override
        public CompletionStage<String> getAbhaCard(String xToken) {
            return completedFuture("");
        }
    }
}
