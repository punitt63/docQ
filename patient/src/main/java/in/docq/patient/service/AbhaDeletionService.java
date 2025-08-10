package in.docq.patient.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.patient.controller.AbhaDeletionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component
public class AbhaDeletionService {

    private final AbhaRestClient abhaRestClient;

    @Autowired
    public AbhaDeletionService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<AbhaDeletionController.RequestOtpResponseBody> requestOtp(String xToken, AbhaDeletionController.RequestOtpRequestBody requestOtpRequestBody) {
        return abhaRestClient.abhaDeletionRequestOtp(xToken, requestOtpRequestBody.getScopes(), requestOtpRequestBody.getLoginHint(), requestOtpRequestBody.getLoginId() , requestOtpRequestBody.getOtpSystem())
                .thenApply(response -> AbhaDeletionController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    // TODO: return list of all abha numbers deleted
    public CompletionStage<AbhaDeletionController.VerifyOtpResponseBody> verifyOtp(String xToken, AbhaDeletionController.VerifyOtpRequestBody verifyOtpRequestBody) {
        return abhaRestClient.abhaDeletionVerifyOtp(xToken, verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), verifyOtpRequestBody.getOtpValue(), verifyOtpRequestBody.getReasons())
                .thenApply(response -> {
                    assert response.getAccounts() != null;
                    return AbhaDeletionController.VerifyOtpResponseBody.builder()
                            .authResult(response.getAuthResult())
                            .message(response.getMessage())
                            .abhaNumbers(List.of(response.getAccounts().get(0).getAbHANumber()))
                            .build();
                });
    }
}
