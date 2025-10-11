package in.docq.patient.client;

import in.docq.patient.auth.BackendKeyCloakRestClient;
import in.docq.patient.model.Appointment;
import in.docq.patient.model.AppointmentDetails;
import in.docq.patient.model.OPD;
import in.docq.patient.model.HealthProfessional;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import in.docq.patient.model.Prescription;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Component
public class HealthFacilityRestClient {

    private final RestTemplate restTemplate;
    private final BackendKeyCloakRestClient backendKeyCloakRestClient;

    @Value("${health-facility.service.url}")
    private String healthFacilityServiceUrl;

    public HealthFacilityRestClient(RestTemplate restTemplate, 
                                   BackendKeyCloakRestClient backendKeyCloakRestClient) {
        this.restTemplate = restTemplate;
        this.backendKeyCloakRestClient = backendKeyCloakRestClient;
    }

    public CompletionStage<Appointment> createAppointment(String healthFacilityId,
                                                          LocalDate opdDate,
                                                          String opdId,
                                                          CreateAppointmentRequestBody requestBody) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    String url = healthFacilityServiceUrl + "/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments";
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<CreateAppointmentRequestBody> entity = new HttpEntity<>(requestBody, headers);
                    
                    ResponseEntity<Appointment> response = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Appointment.class,
                            healthFacilityId,
                            opdDate,
                            opdId
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<List<Appointment>> getAppointments(LocalDate startOpdDate,
                                                            LocalDate endOpdDate,
                                                            String opdId,
                                                            String patientId,
                                                            List<Appointment.State> states,
                                                            Integer offset,
                                                            Integer limit) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(healthFacilityServiceUrl + "/appointments")
                            .queryParam("start-opd-date", startOpdDate)
                            .queryParam("end-opd-date", endOpdDate);

                    if (opdId != null) builder.queryParam("opd-id", opdId);
                    if (patientId != null) builder.queryParam("patient-id", patientId);
                    if (states != null && !states.isEmpty()) builder.queryParam("state", states);
                    if (offset != null) builder.queryParam("offset", offset);
                    if (limit != null) builder.queryParam("limit", limit);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<List<Appointment>> response = restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<Appointment>>() {}
                    );
                    return response.getBody();
                }));
        }

    public CompletionStage<Appointment> cancelAppointment(String healthFacilityId, 
                                                        LocalDate opdDate, 
                                                        String opdId, 
                                                        Integer appointmentId) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    String url = healthFacilityServiceUrl + "/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/cancel";
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);
                    
                    ResponseEntity<Appointment> response = restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            entity,
                            Appointment.class,
                            healthFacilityId,
                            opdDate,
                            opdId,
                            appointmentId
                    );
                    return response.getBody();
                }));
    }

    // New method to get AppointmentDetails with prescriptions
    public CompletionStage<List<AppointmentDetails>> getAppointmentDetails(
            LocalDate startOpdDate,
            LocalDate endOpdDate,
            String opdId,
            String patientId,
            Integer offset,
            Integer limit) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(healthFacilityServiceUrl + "/appointments/details")
                            .queryParam("start-opd-date", startOpdDate)
                            .queryParam("end-opd-date", endOpdDate);

                    if (opdId != null) builder.queryParam("opd-id", opdId);
                    if (patientId != null) builder.queryParam("patient-id", patientId);
                    if (offset != null) builder.queryParam("offset", offset);
                    if (limit != null) builder.queryParam("limit", limit);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<List<AppointmentDetails>> response = restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<AppointmentDetails>>() {}
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<List<OPD>> listOPDs(String healthFacilityId,
                                              String healthFacilityProfessionalId,
                                              LocalDate startDate,
                                              LocalDate endDate) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    UriComponentsBuilder builder = UriComponentsBuilder
                        .fromHttpUrl(healthFacilityServiceUrl + "/health-facilities/{health-facility-id}/health-facility-professionals/opds")
                        .queryParam("health-facility-professional-id", healthFacilityProfessionalId)
                        .queryParam("start-date", startDate)
                        .queryParam("end-date", endDate);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<List<OPD>> response = restTemplate.exchange(
                        builder.build().toUriString(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<OPD>>() {},
                        healthFacilityId
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<List<AppointmentDetails>> listPatientPrescriptions(String patientId,
                                                                              LocalDate startOpdDate,
                                                                              LocalDate endOpdDate,
                                                                              Integer limit) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    UriComponentsBuilder builder = UriComponentsBuilder
                            .fromHttpUrl(healthFacilityServiceUrl + "/patients/{patient-id}/prescriptions")
                            .queryParam("start-opd-date", startOpdDate)
                            .queryParam("end-opd-date", endOpdDate)
                            .queryParam("limit", Optional.ofNullable(limit).orElse(100));

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<List<AppointmentDetails>> response = restTemplate.exchange(
                            builder.build().toUriString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<AppointmentDetails>>() {},
                            patientId
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<Prescription> getOPDPrescription(LocalDate opdDate,
                                                            String opdId,
                                                            Integer appointmentId) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    String url = healthFacilityServiceUrl + "/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/prescriptions";

                    // No facility scoping enforced at patient side; pass a placeholder or empty string if required
                    String healthFacilityId = "_";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<Prescription> response = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            Prescription.class,
                            healthFacilityId,
                            opdDate,
                            opdId,
                            appointmentId
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<List<HealthProfessional>> listHealthProfessionals(int stateCode,
                                                                             int districtCode,
                                                                             String speciality) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    UriComponentsBuilder builder = UriComponentsBuilder
                            .fromHttpUrl(healthFacilityServiceUrl + "/health-facilities/_/health-facility-professionals/search")
                            .queryParam("state-code", stateCode)
                            .queryParam("district-code", districtCode);

                    if (speciality != null && !speciality.isBlank()) {
                        builder.queryParam("speciality", speciality);
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    HttpEntity<?> entity = new HttpEntity<>(headers);

                    ResponseEntity<List<HealthProfessional>> response = restTemplate.exchange(
                            builder.build().toUriString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<HealthProfessional>>() {}
                    );
                    return response.getBody();
                }));
    }

    public CompletionStage<Void> createPatientIfNotExists(String healthFacilityId,
                                                          CreatePatientRequestBody requestBody) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    String url = healthFacilityServiceUrl + "/health-facilities/{health-facility-id}/patients/create-if-not-exists";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(token);
                    headers.set("Content-Type", "application/json");
                    HttpEntity<CreatePatientRequestBody> entity = new HttpEntity<>(requestBody, headers);

                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Void.class,
                            healthFacilityId
                    );
                    return null;
                }));
    }

    @Builder
    @Getter
    public static class CreateAppointmentRequestBody {
        private final String patientID;
    }

    @Builder
    @Getter
    public static class CreatePatientRequestBody {
        private String abhaNo;
        private String abhaAddress;
        private String name;
        private String mobileNo;
        private LocalDate dob;
        private String gender;
    }
}
