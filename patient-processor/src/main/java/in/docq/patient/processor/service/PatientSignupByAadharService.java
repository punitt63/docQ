package in.docq.patient.processor.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1;
import in.docq.patient.processor.controller.PatientSignupByAadharController;
import in.docq.patient.processor.model.AbhaProfile;
import in.docq.patient.processor.model.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Service
public class PatientSignupByAadharService {

    private final AbhaRestClient abhaRestClient;

    @Autowired
    public PatientSignupByAadharService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<PatientSignupByAadharController.RequestOtpResponseBody> requestOtp(String encryptedAadharNumber) {
        return abhaRestClient.abhaEnrollmentRequestOtp(encryptedAadharNumber)
                .thenApply(response -> PatientSignupByAadharController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientSignupByAadharController.EnrolByAadharResponseBody> enrolByAadhaar(List<String> authMethods, String txnId, String otpValue, String mobile) {
        return abhaRestClient.abhaEnrollmentByAadhar(authMethods, txnId, otpValue, mobile)
                .thenApply(response -> PatientSignupByAadharController.EnrolByAadharResponseBody.builder()
                        .txnId(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                        .message(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                        .tokens(Tokens.toTokens(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getTokens())))
                        .abhaProfile(AbhaProfile.toAbhaProfile(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getAbHAProfile())))
                        .isNew(Boolean.TRUE.equals(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getIsNew()))
                        .build());
    }

    public CompletionStage<PatientSignupByAadharController.AbhaAddressSuggestionsResponseBody> getAbhaAddressSuggestions(String txnId) {
        return abhaRestClient.getAbhaAddressSuggestions(txnId)
                .thenApply(response ->
                    PatientSignupByAadharController.AbhaAddressSuggestionsResponseBody.builder()
                            .txnId(response.getTxnId())
                            .abhaAddressSuggestions(response.getAbhaAddressList())
                            .build()
                );
    }

    public CompletionStage<PatientSignupByAadharController.EnrolAbhaAddressResponseBody> enrolAbhaAddress(String txnId, String abhaAddress, String preferred) {
        return abhaRestClient.enrolAbhaAddress(txnId, abhaAddress, preferred)
                .thenApply(response -> PatientSignupByAadharController.EnrolAbhaAddressResponseBody.builder()
                        .txnId(response.getTxnId())
                        .abhaAddress(response.getAbhaAddress())
                        .preferredAbhaAddress(response.getPreferred())
                        .build());
    }
}
