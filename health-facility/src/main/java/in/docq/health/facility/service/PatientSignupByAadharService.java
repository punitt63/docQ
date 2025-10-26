package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.health.facility.controller.PatientSignupByAadharController;
import in.docq.abha.rest.client.model.AbhaProfile;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.health.facility.dao.PatientDao;
import in.docq.health.facility.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Service
public class PatientSignupByAadharService {

    private final AbhaRestClient abhaRestClient;
    private final PatientDao patientDao;

    @Autowired
    public PatientSignupByAadharService(AbhaRestClient abhaRestClient, PatientDao patientDao) {
        this.abhaRestClient = abhaRestClient;
        this.patientDao = patientDao;
    }

    public CompletionStage<PatientSignupByAadharController.RequestOtpResponseBody> abhaSignupRequestOtp(String aadharNumber) {
        return abhaRestClient.abhaEnrollmentRequestOtp(aadharNumber)
                .thenApply(response -> PatientSignupByAadharController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientSignupByAadharController.EnrolByAadharResponseBody> enrolByAadhaar(List<String> authMethods, String txnId, String otpValue, String mobile) {
        return abhaRestClient.abhaEnrollmentByAadhar(authMethods, txnId, otpValue, mobile)
                .thenCompose(response -> {
                    AbhaProfile abhaProfile = AbhaProfile.toAbhaProfile(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getAbHAProfile()));
                    Patient patient = Patient.builder()
                            .id(UUID.randomUUID().toString())
                            .gender(abhaProfile.getGender())
                            .name(abhaProfile.getFirstName())
                            .abhaNo(abhaProfile.getAbhaNumber())
                            .dob(LocalDate.parse(abhaProfile.getDob(), DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                            .mobileNo(abhaProfile.getMobile())
                            .abhaAddress(abhaProfile.getPreferredAbhaAddress())
                            .build();
                    return patientDao.insert(patient)
                            .thenApply(ignore  ->
                                PatientSignupByAadharController.EnrolByAadharResponseBody.builder()
                                        .patientId(patient.getId())
                                        .txnId(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                                        .message(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                                        .tokens(Tokens.toTokens(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getTokens())))
                                        .abhaProfile(abhaProfile)
                                        .isNew(Boolean.TRUE.equals(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getIsNew()))
                                        .build());
                });
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


