package in.docq.health.facility.service;

import com.google.common.collect.ImmutableList;
import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.*;
import in.docq.health.facility.controller.HipWebhookController;
import in.docq.health.facility.dao.UserInitiatedLinkingDao;
import in.docq.health.facility.model.CareContext;
import in.docq.health.facility.model.Patient;
import in.docq.health.facility.model.UserInitiatedLinking;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class UserInitiatedLinkingService {
    private final PatientService patientService;
    private final CareContextService careContextService;
    private final UserInitiatedLinkingDao userInitiatedLinkingDao;
    private final AbhaRestClient abhaRestClient;
    private final OTPService otpService;

    public UserInitiatedLinkingService(PatientService patientService,
                                       CareContextService careContextService,
                                       UserInitiatedLinkingDao userInitiatedLinkingDao,
                                       AbhaRestClient abhaRestClient,
                                       OTPService otpService) {
        this.patientService = patientService;
        this.careContextService = careContextService;
        this.userInitiatedLinkingDao = userInitiatedLinkingDao;
        this.abhaRestClient = abhaRestClient;
        this.otpService = otpService;
    }

    public CompletionStage<Void> discoverCareContext(String requestId, String healthFacilityId, HipWebhookController.UserInitiatedLinkingRequest request) {
        return getUnlinkedCareContexts(healthFacilityId, request)
                .thenCompose(careContexts -> {
                    if(careContexts.getValue().isEmpty()) {
                        return completedFuture(null);
                    }
                    return userInitiatedLinkingDao.insert(UserInitiatedLinking.builder()
                            .transactionId(request.getTransactionId())
                            .patientId(careContexts.getValue().get(0).getPatientId())
                            .status("AWAITING_LINKING_INITIATION")
                            .build())
                            .thenCompose(ignore -> abhaRestClient.linkUserInitiatedCareContext(UUID.randomUUID().toString(), Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(), healthFacilityId,
                            new AbdmUserInitiatedLinking2Request()
                                    .transactionId(request.getTransactionId())
                                    .patient(ImmutableList.of(new AbdmUserInitiatedLinking2RequestPatientInner()
                                            .referenceNumber(request.getPatient().getId())
                                            .display(request.getPatient().getName())
                                            .hiType(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.PRESCRIPTION)
                                            .count(careContexts.getValue().size())
                                            .careContexts(careContexts.getValue().stream().map(careContext ->
                                                    new AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner()
                                                            .referenceNumber(careContext.getAppointmentID())
                                                            .display("Prescription from " + healthFacilityId)
                                            ).toList())))
                                    .matchedBy(List.of(careContexts.getKey()))
                                    .response(new AbdmUserInitiatedLinking2RequestResponse().requestId(UUID.fromString(requestId))
                                    )));
                });
    }

    private CompletionStage<Pair<AbdmUserInitiatedLinking2Request.MatchedByEnum, List<CareContext>>> getUnlinkedCareContexts(String healthFacilityId, HipWebhookController.UserInitiatedLinkingRequest request) {
        String abhaAddress = request.getPatient().getId();
        // Step 1: Search by ABHA address
        return patientService.getPatientByAbhaAddressOptional(abhaAddress)
                .thenCompose(patientOpt -> {
                    if (patientOpt.isPresent()) {
                        return completedFuture(Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.ABHA_ADDRESS, patientOpt.get()));
                    }

                    // Step 2: Search by mobile number
                    String mobileNumber = extractMobileNumber(request.getPatient().getVerifiedIdentifiers(),
                            request.getPatient().getUnverifiedIdentifiers());
                    if (mobileNumber == null) {
                        return completedFuture(Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, null));
                    }

                    return patientService.list(null, mobileNumber, null, request.getPatient().getGender())
                            .thenApply(patientsByMobileNoAndGender -> {
                                if (patientsByMobileNoAndGender.isEmpty()) {
                                    return Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, null);
                                }

                                // Step 2: Match age (+/- 5 years)
                                List<Patient> matchedAgePatients = patientsByMobileNoAndGender.stream()
                                        .filter(patient -> matchesAge(patient.getDob(), request.getPatient().getYearOfBirth()))
                                        .toList();
                                if (matchedAgePatients.isEmpty()) {
                                    return Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, null);
                                }

                                // Step 3: Match name phonetically
                                List<Patient> matchedNamePatients = matchedAgePatients.stream()
                                        .filter(patient -> matchesNamePhonetically(patient.getName(), request.getPatient().getName()))
                                        .toList();
                                if (matchedNamePatients.isEmpty()) {
                                    return Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, null);
                                }
                                return Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, matchedNamePatients.get(0));

                            });
                })
                .thenCompose(matchedPatient -> {
                    if(Objects.isNull(matchedPatient.getValue())) {
                        return completedFuture(Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, new ArrayList<>()));
                    }
                    return careContextService.getUnlinkedCareContexts(((Patient) matchedPatient.getRight()).getId(), healthFacilityId)
                            .thenApply(careContexts -> Pair.of(AbdmUserInitiatedLinking2Request.MatchedByEnum.MOBILE, careContexts));
                });
    }

    private String extractMobileNumber(List<HipWebhookController.UserInitiatedLinkingRequest.Identifier> verifiedIdentifiers,
                                       List<HipWebhookController.UserInitiatedLinkingRequest.Identifier> unverifiedIdentifiers) {
        // Check verified identifiers first
        if (verifiedIdentifiers != null) {
            for (HipWebhookController.UserInitiatedLinkingRequest.Identifier identifier : verifiedIdentifiers) {
                if ("MOBILE".equals(identifier.getType().getValue()) || "MR".equals(identifier.getType().getValue())) {
                    return cleanMobileNumber(identifier.getValue());
                }
            }
        }

        // Check unverified identifiers
        if (unverifiedIdentifiers != null) {
            for (HipWebhookController.UserInitiatedLinkingRequest.Identifier identifier : unverifiedIdentifiers) {
                if ("MOBILE".equals(identifier.getType().getValue())) {
                    return cleanMobileNumber(identifier.getValue());
                }
            }
        }

        return null;
    }

    private String cleanMobileNumber(String mobile) {
        if (mobile == null) return null;
        // Remove country code, spaces, and special characters, keep only digits
        return mobile.replaceAll("[^0-9]", "").replaceFirst("^91", "");
    }

    private boolean matchesAge(LocalDate patientDob, Integer requestYearOfBirth) {
        if (patientDob == null || requestYearOfBirth == null) return false;

        int patientBirthYear = patientDob.getYear();
        int ageDifference = Math.abs(patientBirthYear - requestYearOfBirth);

        return ageDifference <= 5;
    }

    private boolean matchesNamePhonetically(String patientName, String requestName) {
        if (patientName == null || requestName == null) return false;

        // Simple phonetic matching using Soundex algorithm
        return getSoundex(patientName).equals(getSoundex(requestName));
    }

    private String getSoundex(String name) {
        if (name == null || name.isEmpty()) return "";

        name = name.toUpperCase().replaceAll("[^A-Z]", "");
        if (name.isEmpty()) return "";

        StringBuilder soundex = new StringBuilder();
        soundex.append(name.charAt(0));

        String mapping = "01230120022455012623010202";
        char prevCode = mapping.charAt(name.charAt(0) - 'A');

        for (int i = 1; i < name.length() && soundex.length() < 4; i++) {
            char code = mapping.charAt(name.charAt(i) - 'A');
            if (code != '0' && code != prevCode) {
                soundex.append(code);
            }
            prevCode = code;
        }

        // Pad with zeros if necessary
        while (soundex.length() < 4) {
            soundex.append('0');
        }

        return soundex.toString();
    }

    public CompletionStage<Void> initCareContextLink(String requestId, String healthFacilityId,
                                                     HipWebhookController.InitCareContextLinkRequest request) {
        // Step 1: Get patient by ABHA address
        return patientService.getPatientByAbhaAddress(request.getAbhaAddress())
                .thenCompose(patient ->
                        // Step 2: Send OTP
                        otpService.sendOtp(patient.getMobileNo())
                                .thenCompose(otp -> {
                                    // Step 3: Generate link reference number
                                    String linkReferenceNumber = UUID.randomUUID().toString();
                                    Long otpExpiryTime = Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli();

                                    // Step 4: Update user initiated linking record
                                    return userInitiatedLinkingDao.getByTransactionId(request.getTransactionId())
                                            .thenCompose(userInitiatedLinking -> userInitiatedLinkingDao.updateUserInitiatedLinking(userInitiatedLinking.toBuilder()
                                                    .otp(otp)
                                                    .otpExpiryTime(otpExpiryTime)
                                                    .linkReferenceNumber(linkReferenceNumber)
                                                    .initLinkRequest(request)
                                                    .status("AWAITING_OTP_CONFIRMATION")
                                                    .build()))
                                            .thenCompose(ignore ->
                                                    // Step 5: Call ABHA rest client
                                                    abhaRestClient.initiateUserLinking(
                                                            UUID.randomUUID().toString(),
                                                            Instant.now().truncatedTo(ChronoUnit.MILLIS).toString(),
                                                            healthFacilityId,
                                                            new AbdmUserInitiatedLinking4Request()
                                                                    .transactionId(request.getTransactionId())
                                                                    .link(new AbdmUserInitiatedLinking4RequestLink()
                                                                            .referenceNumber(linkReferenceNumber)
                                                                            .authenticationType(AbdmUserInitiatedLinking4RequestLink.AuthenticationTypeEnum.DIRECT)
                                                                            .meta(new AbdmUserInitiatedLinking4RequestLinkMeta()
                                                                                    .communicationMedium(AbdmUserInitiatedLinking4RequestLinkMeta.CommunicationMediumEnum.MOBILE)
                                                                                    .communicationHint("OTP")
                                                                                    .communicationExpiry(otpExpiryTime.toString())))
                                                                    .error(request.getError() != null ?
                                                                            new AbdmUserInitiatedLinking4RequestError()
                                                                                    .code(request.getError().getCode())
                                                                                    .message(request.getError().getMessage()) : null)
                                                                    .response(new AbdmUserInitiatedLinking4RequestResponse()
                                                                            .requestId(UUID.fromString(requestId)))
                                                    )
                                            );
                                })
                );
    }

    public CompletionStage<Void> confirmCareContextLink(String requestId, String timestamp, String healthFacilityId,
                                                        HipWebhookController.ConfirmCareContextLinkRequest request) {
        String linkRefNumber = request.getConfirmation().getLinkRefNumber();
        String token = request.getConfirmation().getToken();

        return userInitiatedLinkingDao.getByLinkReferenceNumber(linkRefNumber)
                .thenCompose(userInitiatedLinking -> {
                    // Check if OTP has expired
                    if (userInitiatedLinking.getOtpExpiryTime() != null &&
                            Instant.now().toEpochMilli() > userInitiatedLinking.getOtpExpiryTime()) {
                        // OTP has expired - send error
                        return abhaRestClient.confirmCareContextLinking(
                                requestId,
                                timestamp,
                                new AbdmUserInitiatedLinking6Request()
                                        .error(new AbdmUserInitiatedLinking3RequestError()
                                                .code("900902")
                                                .message("OTP has expired. Please request a new OTP"))
                                        .response(new AbdmUserInitiatedLinking6RequestResponse()
                                                .requestId(UUID.fromString(requestId)))
                        );
                    }

                    // Check if OTP matches
                    if (userInitiatedLinking.getOtp() != null && userInitiatedLinking.getOtp().equals(token)) {
                        // OTP matches - proceed with confirmation
                        HipWebhookController.InitCareContextLinkRequest initLinkRequest = userInitiatedLinking.getInitLinkRequest();

                        return abhaRestClient.confirmCareContextLinking(
                                requestId,
                                timestamp,
                                new AbdmUserInitiatedLinking6Request()
                                        .patient(initLinkRequest.getPatient().stream()
                                                .map(patientInfo -> new AbdmUserInitiatedLinking6RequestPatientInner()
                                                        .referenceNumber(patientInfo.getReferenceNumber())
                                                        .display(patientInfo.getDisplay())
                                                        .careContexts(patientInfo.getCareContexts().stream()
                                                                .map(careContext -> new AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner()
                                                                        .referenceNumber(careContext.getReferenceNumber())
                                                                        .display(careContext.getDisplay()))
                                                                .toList())
                                                        .hiType(AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.valueOf(patientInfo.getHiType()))
                                                        .count(patientInfo.getCount()))
                                                .toList())
                                        .response(new AbdmUserInitiatedLinking6RequestResponse()
                                                .requestId(UUID.fromString(requestId)))
                        );
                    } else {
                        // OTP doesn't match - send error
                        return abhaRestClient.confirmCareContextLinking(
                                requestId,
                                timestamp,
                                new AbdmUserInitiatedLinking6Request()
                                        .error(new AbdmUserInitiatedLinking3RequestError()
                                                .code("900901")
                                                .message("Invalid Credentials. Make sure you have provided the correct security credentials"))
                                        .response(new AbdmUserInitiatedLinking6RequestResponse()
                                                .requestId(UUID.fromString(requestId)))
                        );
                    }
                });
    }
}
