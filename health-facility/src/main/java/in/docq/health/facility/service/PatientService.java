package in.docq.health.facility.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.AbhaAccount;
import in.docq.abha.rest.client.model.AbhaProfile;
import in.docq.abha.rest.client.model.Tokens;
import in.docq.health.facility.controller.PatientController;
import in.docq.health.facility.dao.PatientDao;
import in.docq.health.facility.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Service
public class PatientService {
    private final AbhaRestClient abhaRestClient;
    private final PatientDao patientDao;

    @Autowired
    public PatientService(AbhaRestClient abhaRestClient, PatientDao patientDao) {
        this.abhaRestClient = abhaRestClient;
        this.patientDao = patientDao;
    }

    public CompletionStage<List<Patient>> list(String abhaAddress, String mobileNo, java.time.LocalDate dob, String gender) {
        return patientDao.list(abhaAddress, mobileNo, dob, gender);
    }

    public CompletionStage<Patient> getPatient(String patientId) {
        return patientDao.get(patientId);
    }

    public CompletionStage<Patient> getPatientByAbhaAddress(String abhaAddress) {
        return patientDao.getByAbhaAddress(abhaAddress);
    }

    public CompletionStage<Optional<Patient>> getPatientByAbhaAddressOptional(String abhaAddress) {
        return patientDao.getByAbhaAddressOptional(abhaAddress);
    }

    public CompletionStage<Patient> createPatient(Patient patient) {
        return patientDao.insert(patient)
                .thenApply(ignored -> patient);
    }

    public CompletionStage<Void> replacePatient(String id, Patient newPatient) {
        return patientDao.get(id)
                .thenCompose(oldPatient -> patientDao.update(oldPatient.getId(), newPatient));
    }

    public CompletionStage<PatientController.RequestOtpResponseBody> requestAbhaLoginOtp(PatientController.RequestAbhaLoginOtpRequestBody requestAbhaLoginOtpRequestBody) {
        return abhaRestClient.abhaLoginRequestOtp(requestAbhaLoginOtpRequestBody.getScopes(), requestAbhaLoginOtpRequestBody.getLoginHint(), requestAbhaLoginOtpRequestBody.getLoginId() , requestAbhaLoginOtpRequestBody.getOtpSystem())
                .thenApply(response -> PatientController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientController.VerifyOtpResponseBody> verifyAbhaLoginOtp(PatientController.VerifyOtpRequestBody verifyOtpRequestBody) {
        return abhaRestClient.abhaLoginVerifyOtp(verifyOtpRequestBody.getScopes(), verifyOtpRequestBody.getAuthMethods(), verifyOtpRequestBody.getTxnId(), verifyOtpRequestBody.getOtpValue())
                .thenApply(response -> PatientController.VerifyOtpResponseBody.builder()
                        .authResult(response.getAuthResult())
                        .message(response.getMessage())
                        .tokens(Tokens.toTokens(response.getToken(), response.getExpiresIn(), response.getRefreshToken(), response.getRefreshExpiresIn()))
                        .accounts(AbhaAccount.toAccounts(Objects.requireNonNull(response.getAccounts())))
                        .build());
    }

    public CompletionStage<PatientController.RequestOtpResponseBody> abhaSignupRequestOtp(String aadharNumber) {
        return abhaRestClient.abhaEnrollmentRequestOtp(aadharNumber)
                .thenApply(response -> PatientController.RequestOtpResponseBody.builder()
                        .txnId(response.getTxnId())
                        .message(response.getMessage())
                        .build());
    }

    public CompletionStage<PatientController.EnrolByAadharResponseBody> enrolByAadhaar(List<String> authMethods, String txnId, String otpValue, String mobile) {
        return abhaRestClient.abhaEnrollmentByAadhar(authMethods, txnId, otpValue, mobile)
                .thenApply(response -> PatientController.EnrolByAadharResponseBody.builder()
                        .txnId(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                        .message(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getMessage())
                        .tokens(Tokens.toTokens(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getTokens())))
                        .abhaProfile(AbhaProfile.toAbhaProfile(Objects.requireNonNull(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getAbHAProfile())))
                        .isNew(Boolean.TRUE.equals(response.getAbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf().getIsNew()))
                        .build());
    }
}
