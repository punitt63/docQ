package in.docq.patient.service;

import in.docq.patient.client.HealthFacilityRestClient;
import in.docq.patient.model.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Service
public class AppointmentService {

    private final HealthFacilityRestClient healthFacilityRestClient;

    public AppointmentService(HealthFacilityRestClient healthFacilityRestClient) {
        this.healthFacilityRestClient = healthFacilityRestClient;
    }

    public CompletionStage<Appointment> createAppointment(String healthFacilityId,
                                                          LocalDate opdDate,
                                                          String opdId,
                                                          String patientId) {
        HealthFacilityRestClient.CreateAppointmentRequestBody requestBody =
                HealthFacilityRestClient.CreateAppointmentRequestBody.builder()
                    .patientID(patientId)
                    .build();
            
        return healthFacilityRestClient.createAppointment(healthFacilityId, opdDate, opdId, requestBody);
    }

    public CompletionStage<List<Appointment>> getAppointments(LocalDate startOpdDate,
                                                            LocalDate endOpdDate,
                                                            String opdId,
                                                            String patientId,
                                                            List<Appointment.State> states,
                                                            Integer offset,
                                                            Integer limit) {
        return healthFacilityRestClient.getAppointments(startOpdDate, endOpdDate, opdId, patientId, states, offset, limit);
    }

    public CompletionStage<Appointment> cancelAppointment(String healthFacilityId, 
                                                        LocalDate opdDate, 
                                                        String opdId, 
                                                        Integer appointmentId) {
        Objects.requireNonNull(healthFacilityId, "healthFacilityId");
        Objects.requireNonNull(opdDate, "opdDate");
        Objects.requireNonNull(opdId, "opdId");
        Objects.requireNonNull(appointmentId, "appointmentId");

        return healthFacilityRestClient.cancelAppointment(healthFacilityId, opdDate, opdId, appointmentId);
    }

    // Get appointment details with prescriptions based on state
    public CompletionStage<List<Appointment>> getAppointmentByState(
            String patientId,
            Appointment.State state,
            LocalDate startDate,
            LocalDate endDate,
            Integer offset,
            Integer limit) {

        // Set reasonable defaults for date ranges based on state
        LocalDate effectiveStartDate, effectiveEndDate;
        
        switch (state) {
            case WAITING -> {
                // Upcoming: today to next 30 days (max window 30 days)
                effectiveStartDate = startDate != null ? startDate : LocalDate.now();
                effectiveEndDate = endDate != null ? endDate : LocalDate.now().plusDays(30);
            }
            case IN_PROGRESS -> {
                // Current appointments: today only (typically)
                effectiveStartDate = startDate != null ? startDate : LocalDate.now();
                effectiveEndDate = endDate != null ? endDate : LocalDate.now();
            }
            case COMPLETED -> {
                // Past: last 30 days to yesterday (max window 30 days)
                effectiveStartDate = startDate != null ? startDate : LocalDate.now().minusDays(30);
                effectiveEndDate = endDate != null ? endDate : LocalDate.now().minusDays(1);
            }
            default -> {
                effectiveStartDate = startDate != null ? startDate : LocalDate.now().minusMonths(1);
                effectiveEndDate = endDate != null ? endDate : LocalDate.now().plusMonths(1);
            }
        }

        return healthFacilityRestClient.getAppointment(
                effectiveStartDate, effectiveEndDate, null, patientId, 
                offset != null ? offset : 0, 
                limit != null ? limit : 50);
    }
}
