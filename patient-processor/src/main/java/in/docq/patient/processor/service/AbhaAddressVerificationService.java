package in.docq.patient.processor.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.patient.processor.controller.AbhaAddressVerificationController;
import in.docq.patient.processor.model.AbhaUser;
import in.docq.patient.processor.model.Tokens;
import in.docq.patient.processor.utils.RSAEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Component
public class AbhaAddressVerificationService {

    private AbhaRestClient abhaRestClient;

    @Autowired
    public AbhaAddressVerificationService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<AbhaAddressVerificationController.AbhaAddressSearchResponseBody> searchAbhaAddress(String abhaAddress) {
        return abhaRestClient.searchAbhaAddress(abhaAddress)
                .thenApply(response -> AbhaAddressVerificationController.AbhaAddressSearchResponseBody.builder()
                        .authMethods(response.getAuthMethods())
                        .mobile(response.getMobile())
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .fullName(response.getFullName())
                        .healthIdNumber(response.getHealthIdNumber())
                        .blockedAuthMethods(response.getBlockedAuthMethods())
                        .abhaAddress(response.getAbhaAddress())
                        .build());
    }

    public CompletionStage<AbhaAddressVerificationController.RequestOtpResponseBody> abhaAddressLoginRequestOtp(AbhaAddressVerificationController.RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        String encryptedLoginId = RSAEncrypter.encrypt(requestOtpRequestBody.getLoginId());
        return abhaRestClient.abhaAddressLoginRequestOtp(requestOtpRequestBody.getScopes(), requestOtpRequestBody.getLoginHint(), encryptedLoginId, requestOtpRequestBody.getOtpSystem())
                .thenApply(response ->
                    AbhaAddressVerificationController.RequestOtpResponseBody
                            .builder()
                            .txnId(response.getTxnId())
                            .message(response.getMessage())
                            .build()
                );
    }

    public CompletionStage<AbhaAddressVerificationController.VerifyOtpResponseBody> abhaAddressLoginVerifyOtp(AbhaAddressVerificationController.VerifyOtpRequestBody verifyOtpRequestBody) throws Exception {
        String encryptedOtpValue = RSAEncrypter.encrypt(verifyOtpRequestBody.getOtpValue());
        return abhaRestClient.abhaAddressLoginVerifyOtp(verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), encryptedOtpValue)
                .thenApply(response -> AbhaAddressVerificationController.VerifyOtpResponseBody
                        .builder()
                        .users(AbhaUser.toUsers(Objects.requireNonNull(response.getUsers())))
                        .tokens(Tokens.toTokens(Objects.requireNonNull(response.getTokens())))
                        .authResult(response.getAuthResult())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<AbhaAddressVerificationController.AbhaAddressProfileResponseBody> getAbhaAddressProfile(String xToken) {
        return abhaRestClient.getAbhaAddressProfile(xToken)
                .thenApply(response -> AbhaAddressVerificationController.AbhaAddressProfileResponseBody
                        .builder()
                        .abhaNumber(response.getAbhaNumber())
                        .profilePhoto(response.getProfilePhoto())
                        .abhaAddress(response.getAbhaAddress())
                        .mobile(response.getMobile())
                        .status(response.getStatus())
                        .authMethods(response.getAuthMethods())
                        .build());
    }

    public CompletionStage<String> getAbhaCard(String xToken) {
        return abhaRestClient.getAbhaCard(xToken);
    }
}
