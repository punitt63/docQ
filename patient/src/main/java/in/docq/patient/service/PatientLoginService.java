package in.docq.patient.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.patient.controller.PatientLoginController;
import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.patient.utils.RSAEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Component
public class PatientLoginService {

    private final AbhaRestClient abhaRestClient;

    @Autowired
    public PatientLoginService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<PatientLoginController.RequestOtpResponseBody> requestOtp(PatientLoginController.RequestOtpRequestBody requestOtpRequestBody) throws Exception {
        String encryptedLoginId = RSAEncrypter.encrypt(requestOtpRequestBody.getLoginId());
        return abhaRestClient.abhaLoginRequestOtp(requestOtpRequestBody.getScopes(), requestOtpRequestBody.getLoginHint(), encryptedLoginId , requestOtpRequestBody.getOtpSystem())
                .thenApply(response -> PatientLoginController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientLoginController.VerifyOtpResponseBody> verifyOtp(PatientLoginController.VerifyOtpRequestBody verifyOtpRequestBody) throws Exception {
        String encryptedOtpValue = RSAEncrypter.encrypt(verifyOtpRequestBody.getOtpValue());
        return abhaRestClient.abhaLoginVerifyOtp(verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), encryptedOtpValue)
                .thenApply(response -> PatientLoginController.VerifyOtpResponseBody.builder()
                        .authResult(response.getAuthResult())
                        .message(response.getMessage())
                        .tokens(Tokens.toTokens(response.getToken(), Integer.valueOf(Objects.requireNonNull(response.getExpiresIn())), response.getRefreshToken(), Integer.valueOf(Objects.requireNonNull(response.getRefreshExpiresIn()))))
                        .accounts(AbhaAccount.toAccounts(Objects.requireNonNull(response.getAccounts())))
                        .build());
    }

    public CompletionStage<PatientLoginController.VerifyUserResponseBody> verifyUser(PatientLoginController.VerifyUserRequestBody verifyUserRequestBody, String tToken) {
        return abhaRestClient.abhaLoginVerifyUser(verifyUserRequestBody.getAbhaNumber(), verifyUserRequestBody.getTxnId(), tToken)
                .thenApply(response -> PatientLoginController.VerifyUserResponseBody.builder()
                        .tokens(Tokens.toTokens(response.getToken(), response.getExpiresIn(), response.getRefreshToken(), response.getRefreshExpiresIn()))
                        .build());
    }
}
