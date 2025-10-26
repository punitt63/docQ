package in.docq.patient.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.docq.patient.auth.BackendKeyCloakRestClient;
import in.docq.patient.model.Appointment;
import in.docq.patient.model.Doctor;
import in.docq.patient.model.OPD;
import in.docq.patient.model.Prescription;
import lombok.Builder;
import lombok.Getter;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class HealthFacilityRestClient {

    private final OkHttpClient httpClient;
    private final BackendKeyCloakRestClient backendKeyCloakRestClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService = new ThreadPoolExecutor(20, 50,
            60L, java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>());

    @Value("${health-facility.service.url}")
    private String healthFacilityServiceUrl;

    public HealthFacilityRestClient(BackendKeyCloakRestClient backendKeyCloakRestClient) {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.backendKeyCloakRestClient = backendKeyCloakRestClient;
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper to handle Java 8 date/time types
        this.objectMapper.findAndRegisterModules();
    }

    public CompletionStage<Appointment> createAppointment(String healthFacilityId,
                                                          LocalDate opdDate,
                                                          String opdId,
                                                          CreateAppointmentRequestBody requestBody) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String url = healthFacilityServiceUrl + "/health-facilities/" + healthFacilityId + 
                                   "/opd-dates/" + opdDate + "/opds/" + opdId + "/appointments";
                        
                        String jsonBody = objectMapper.writeValueAsString(requestBody);
                        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
                        
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("Authorization", "Bearer " + token)
                                .addHeader("Content-Type", "application/json")
                                .build();
                        
                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, Appointment.class);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create appointment", e);
                    }
                }, executorService));
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
                    try {
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(healthFacilityServiceUrl + "/appointments").newBuilder()
                                .addQueryParameter("start-opd-date", startOpdDate.toString())
                                .addQueryParameter("end-opd-date", endOpdDate.toString());

                        if (opdId != null) urlBuilder.addQueryParameter("opd-id", opdId);
                        if (patientId != null) urlBuilder.addQueryParameter("patient-id", patientId);
                        if (states != null && !states.isEmpty()) {
                            for (Appointment.State state : states) {
                                urlBuilder.addQueryParameter("state", state.toString());
                            }
                        }
                        if (offset != null) urlBuilder.addQueryParameter("offset", offset.toString());
                        if (limit != null) urlBuilder.addQueryParameter("limit", limit.toString());

                        Request request = new Request.Builder()
                                .url(urlBuilder.build())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, new TypeReference<List<Appointment>>() {});
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get appointments", e);
                    }
                }, executorService));
    }

    public CompletionStage<Appointment> cancelAppointment(String healthFacilityId, 
                                                        LocalDate opdDate, 
                                                        String opdId, 
                                                        Integer appointmentId) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String url = healthFacilityServiceUrl + "/health-facilities/" + healthFacilityId + 
                                   "/opd-dates/" + opdDate + "/opds/" + opdId + 
                                   "/appointments/" + appointmentId + "/cancel";
                        
                        Request request = new Request.Builder()
                                .url(url)
                                .patch(RequestBody.create("", MediaType.get("application/json")))
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        
                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, Appointment.class);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to cancel appointment", e);
                    }
                }, executorService));
    }

    public CompletionStage<List<Appointment>> getAppointment(
            LocalDate startOpdDate,
            LocalDate endOpdDate,
            String opdId,
            String patientId,
            Integer offset,
            Integer limit) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(healthFacilityServiceUrl + "/appointments").newBuilder()
                                .addQueryParameter("start-opd-date", startOpdDate.toString())
                                .addQueryParameter("end-opd-date", endOpdDate.toString());

                        if (opdId != null) urlBuilder.addQueryParameter("opd-id", opdId);
                        if (patientId != null) urlBuilder.addQueryParameter("patient-id", patientId);
                        if (offset != null) urlBuilder.addQueryParameter("offset", offset.toString());
                        if (limit != null) urlBuilder.addQueryParameter("limit", limit.toString());

                        Request request = new Request.Builder()
                                .url(urlBuilder.build())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, new TypeReference<List<Appointment>>() {});
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get appointment details", e);
                    }
                }, executorService));
    }

    public CompletionStage<List<OPD>> listOPDs(String healthFacilityId,
                                              String healthFacilityProfessionalId,
                                              LocalDate startDate,
                                              LocalDate endDate) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(healthFacilityServiceUrl + "/health-facilities/" + healthFacilityId + "/health-facility-professionals/opds").newBuilder()
                                .addQueryParameter("health-facility-professional-id", healthFacilityProfessionalId)
                                .addQueryParameter("start-date", startDate.toString())
                                .addQueryParameter("end-date", endDate.toString());

                        Request request = new Request.Builder()
                                .url(urlBuilder.build())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, new TypeReference<List<OPD>>() {});
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to list OPDs", e);
                    }
                }, executorService));
    }

    public CompletionStage<List<Prescription>> listPatientPrescriptions(String patientId,
                                                                      LocalDate startOpdDate,
                                                                      LocalDate endOpdDate,
                                                                      Integer limit) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(healthFacilityServiceUrl + "/patients/" + patientId + "/prescriptions").newBuilder()
                                .addQueryParameter("start-opd-date", startOpdDate.toString())
                                .addQueryParameter("end-opd-date", endOpdDate.toString())
                                .addQueryParameter("limit", Optional.ofNullable(limit).orElse(100).toString());

                        Request request = new Request.Builder()
                                .url(urlBuilder.build())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, new TypeReference<List<Prescription>>() {});
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to list patient prescriptions", e);
                    }
                }, executorService));
    }

    public CompletionStage<Prescription> getOPDPrescription(LocalDate opdDate,
                                                            String opdId,
                                                            Integer appointmentId) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String healthFacilityId = "_"; // Placeholder as per original logic
                        String url = healthFacilityServiceUrl + "/health-facilities/" + healthFacilityId + 
                                   "/opd-dates/" + opdDate + "/opds/" + opdId + 
                                   "/appointments/" + appointmentId + "/prescriptions";

                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, Prescription.class);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get OPD prescription", e);
                    }
                }, executorService));
    }

    public CompletionStage<List<Doctor>> listDoctors(int stateCode,
                                                     int districtCode) {
        return backendKeyCloakRestClient.getAccessToken()
                .thenCompose(token -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(healthFacilityServiceUrl + "/doctors").newBuilder()
                                .addQueryParameter("state-code", String.valueOf(stateCode))
                                .addQueryParameter("district-code", String.valueOf(districtCode));

                        Request request = new Request.Builder()
                                .url(urlBuilder.build())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        try (Response response = httpClient.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                throw new RuntimeException("HTTP error: " + response.code() + " " + response.message());
                            }
                            String responseBody = response.body().string();
                            if (responseBody == null || responseBody.trim().isEmpty()) {
                                throw new RuntimeException("Empty response body from server");
                            }
                            return objectMapper.readValue(responseBody, new TypeReference<List<Doctor>>() {});
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to list health professionals", e);
                    }
                }, executorService));
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