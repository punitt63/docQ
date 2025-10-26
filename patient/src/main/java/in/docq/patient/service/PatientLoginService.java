package in.docq.patient.service;

import com.google.common.cache.Cache;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.patient.controller.PatientLoginController;
import in.docq.abha.rest.client.model.SearchAuthMethodsAbhaaddress200Response;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.abha.rest.client.model.LoginOtpVerifyMobile200ResponseUsersInner;
import in.docq.abha.rest.client.model.PhrUser;
import in.docq.abha.rest.client.model.DeLinkRequest200Response;
import in.docq.abha.rest.client.model.GetProfile200Response;
import in.docq.abha.rest.client.model.LinkRequest200Response;
import in.docq.abha.rest.client.model.Logout200Response;
import in.docq.abha.rest.client.model.RefreshToken200Response;
import in.docq.abha.rest.client.model.OtpRequestMobile200Response;
import in.docq.abha.rest.client.model.VerifyOtpUpdateEmail200Response;
import in.docq.abha.rest.client.model.SwitchProfile200Response;
import in.docq.abha.rest.client.model.UpdateProfile200Response;
import in.docq.abha.rest.client.model.OtpVerifyMobile200ResponseTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletionStage;

@Component
public class PatientLoginService {

    private final AbhaRestClient abhaRestClient;

    @Autowired
    public PatientLoginService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<PatientLoginController.RequestOtpResponseBody> requestOtp(PatientLoginController.RequestOtpRequestBody requestOtpRequestBody) {
        return abhaRestClient.loginRequestOtpForLogin(requestOtpRequestBody.getScopes(), requestOtpRequestBody.getLoginHint(), requestOtpRequestBody.getLoginId() , requestOtpRequestBody.getOtpSystem())
                .thenApply(response -> PatientLoginController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientLoginController.VerifyOtpResponseBody> verifyOtp(PatientLoginController.VerifyOtpRequestBody verifyOtpRequestBody) {
        return abhaRestClient.loginVerifyMobileOtp(verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), verifyOtpRequestBody.getOtpValue())
                .thenApply(response -> PatientLoginController.VerifyOtpResponseBody.builder()
                        .authResult(response.getAuthResult())
                        .message(response.getMessage())
                        .tokens(response.getTokens() == null ? null :
                                Tokens.toTokens(
                                        response.getTokens().getToken(),
                                        response.getTokens().getExpiresIn(),
                                        response.getTokens().getRefreshToken(),
                                        response.getTokens().getRefreshExpiresIn()
                                ))
                        .users(mapToPhrUsers(response.getUsers()))
                        .preferredAbhaAddress(response.getPreferredAbhaAddress())
                        .build());
    }

    public CompletionStage<PatientLoginController.VerifyUserResponseBody> verifyUser(PatientLoginController.VerifyUserRequestBody verifyUserRequestBody, String tToken) {
        return abhaRestClient.loginVerifyUser(verifyUserRequestBody.getAbhaAddress(), verifyUserRequestBody.getTxnId(), tToken)
                .thenApply(response -> PatientLoginController.VerifyUserResponseBody.builder()
                        .tokens(Tokens.toTokens(response.getToken(), response.getExpiresIn(), response.getRefreshToken(), response.getRefreshExpiresIn()))
                        .build());
    }

    public CompletionStage<PatientLoginController.SearchAuthMethodsResponseBody> searchAuthMethodsByAbhaAddress(String abhaAddress) {
        return abhaRestClient.searchAuthMethodsByAbhaAddress(abhaAddress)
                .thenApply((SearchAuthMethodsAbhaaddress200Response response) -> PatientLoginController.SearchAuthMethodsResponseBody.builder()
                        .abhaAddress(response.getAbhaAddress())
                        .authMethods(response.getAuthMethods())
                        .fullName(response.getFullName())
                        .healthIdNumber(response.getHealthIdNumber())
                        .mobile(response.getMobile())
                        .status(response.getStatus())
                        .build());
    }

    // ===== PHR Profile methods =====

    public CompletionStage<DeLinkRequest200Response> deLinkAbhaProfile(String xToken, String transactionId) {
        return abhaRestClient.deLinkAbhaProfile(xToken, transactionId)
                .thenApply(response -> response);
    }

    public CompletionStage<byte[]> getPhrCard(String xToken) {
        return abhaRestClient.getPhrCard(xToken)
                .thenApply(response -> response);
    }

    public CompletionStage<GetProfile200Response> getProfile(String xToken) {
        return abhaRestClient.getProfile(xToken)
                .thenApply(response -> response);
    }

    public CompletionStage<byte[]> getQrCode(String xToken) {
        return abhaRestClient.getQrCode(xToken)
                .thenApply(response -> response);
    }

    public CompletionStage<LinkRequest200Response> linkAbhaProfile(String xToken, String transactionId) {
        return abhaRestClient.linkAbhaProfile(xToken, transactionId)
                .thenApply(response -> response);
    }

    public CompletionStage<Logout200Response> logout(String xToken) {
        return abhaRestClient.logout(xToken)
                .thenApply(response -> response);
    }

    public CompletionStage<RefreshToken200Response> refreshProfileToken(String rToken) {
        return abhaRestClient.refreshProfileToken(rToken)
                .thenApply(response -> response);
    }

    public CompletionStage<OtpRequestMobile200Response> sendUpdateEmailOtp(String xToken, String email, String txnId) {
        return abhaRestClient.sendUpdateEmailOtp(xToken, email, txnId)
                .thenApply(response -> response);
    }

    public CompletionStage<VerifyOtpUpdateEmail200Response> verifyUpdateEmailOtp(String xToken, String otp, String txnId) {
        return abhaRestClient.verifyUpdateEmailOtp(xToken, otp, txnId)
                .thenApply(response -> response);
    }

    public CompletionStage<SwitchProfile200Response> switchProfile(String xToken) {
        return abhaRestClient.switchProfile(xToken)
                .thenApply(response -> response);
    }

    public CompletionStage<UpdateProfile200Response> updateProfile(String xToken, String email, String firstName, String lastName) {
        return abhaRestClient.updateProfile(xToken, email, firstName, lastName)
                .thenApply(response -> response);
    }

    public CompletionStage<OtpVerifyMobile200ResponseTokens> verifyUserForSwitch(String tToken, String txnId, String otp) {
        return abhaRestClient.verifyUserForSwitch(tToken, txnId, otp)
                .thenApply(response -> response);
    }

    private static List<PhrUser> mapToPhrUsers(List<LoginOtpVerifyMobile200ResponseUsersInner> users) {
        if (users == null) {
            return null;
        }
        return users.stream().map(u -> new PhrUser()
                .abhaAddress(u.getAbhaAddress())
                .abhaNumber(u.getAbhaNumber())
                .fullName(u.getFullName())
                .kycStatus(u.getKycStatus())
                .status(u.getStatus())
        ).collect(Collectors.toList());
    }
}
