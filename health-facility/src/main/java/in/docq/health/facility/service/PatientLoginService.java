package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.health.facility.controller.PatientLoginController;
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

    public CompletionStage<PatientLoginController.RequestOtpResponseBody> requestOtp(PatientLoginController.RequestOtpRequestBody requestOtpRequestBody) {
        return abhaRestClient.abhaLoginRequestOtp(requestOtpRequestBody.getScopes(), requestOtpRequestBody.getLoginHint(), requestOtpRequestBody.getLoginId() , requestOtpRequestBody.getOtpSystem())
                .thenApply(response -> PatientLoginController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientLoginController.VerifyOtpResponseBody> verifyOtp(PatientLoginController.VerifyOtpRequestBody verifyOtpRequestBody) {
        return abhaRestClient.abhaLoginVerifyOtp(verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), verifyOtpRequestBody.getOtpValue())
                .thenApply(response -> PatientLoginController.VerifyOtpResponseBody.builder()
                        .authResult(response.getAuthResult())
                        .message(response.getMessage())
                        .tokens(Tokens.toTokens(response.getToken(), response.getExpiresIn(), response.getRefreshToken(), response.getRefreshExpiresIn()))
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