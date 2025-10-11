package in.docq.patient.controller;

import in.docq.patient.auth.AbdmAuthorized;
import in.docq.patient.model.Appointment;
import in.docq.patient.model.AppointmentDetails;
import in.docq.patient.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments")
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<Appointment>> createAppointment(@PathVariable("health-facility-id") String healthFacilityId,
                                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdId,
                                                                          @RequestBody CreateAppointmentRequestBody requestBody,
                                                                          HttpServletRequest request) {
        // Get authenticated patient ID from ABDM token
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        // Ensure the patient can only create appointments for themselves
        if (!authenticatedPatientId.equals(requestBody.getPatientId())) {
            return CompletableFuture.completedFuture(ResponseEntity.status(403).build());
        }
        
        return appointmentService.createAppointment(healthFacilityId, opdDate, opdId, requestBody.getPatientId())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<Appointment>>> getAppointments(@RequestParam(value = "start-opd-date") LocalDate startOpdDate,
                                                                             @RequestParam(value = "end-opd-date") LocalDate endOpdDate,
                                                                             @RequestParam(value = "opd-id", required = false) String opdId,
                                                                             @RequestParam(value = "patient-id", required = false) String patientId,
                                                                             @RequestParam(value = "state", required = false) List<Appointment.State> states,
                                                                             @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                                             @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
                                                                             HttpServletRequest request) {
        checkState(endOpdDate.isBefore(startOpdDate.plusDays(31)), "start date and end date should have at max 30 days of difference");
        checkState(limit <= 5, "Limit should be less than 5");
        
        // Get authenticated patient ID from ABDM token
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        // Patient can only see their own appointments
        // If patientId is provided in query param, it must match authenticated patient
        if (patientId != null && !authenticatedPatientId.equals(patientId)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(403).build());
        }
        
        // Always filter by authenticated patient ID
        return appointmentService.getAppointments(startOpdDate, endOpdDate, opdId, authenticatedPatientId, states, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/cancel")
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<Appointment>> cancelAppointment(@PathVariable("health-facility-id") String healthFacilityId,
                                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdId,
                                                                          @PathVariable("appointment-id") Integer appointmentId,
                                                                          HttpServletRequest request) {
        // Get authenticated patient ID from ABDM token
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        // First, get the appointment to verify it belongs to the authenticated patient
        return appointmentService.getAppointments(opdDate, opdDate, opdId, authenticatedPatientId, null, 0, 100)
                .thenCompose(appointments -> {
                    // Find the specific appointment
                    boolean appointmentBelongsToPatient = appointments.stream()
                            .anyMatch(appointment -> 
                                appointment.getId() == appointmentId && 
                                authenticatedPatientId.equals(appointment.getPatientId()));
                    
                    if (!appointmentBelongsToPatient) {
                        return CompletableFuture.<ResponseEntity<Appointment>>completedFuture(ResponseEntity.status(403).build());
                    }
                    
                    // If appointment belongs to patient, proceed with cancellation
                    return appointmentService.cancelAppointment(healthFacilityId, opdDate, opdId, appointmentId)
                            .thenApply(ResponseEntity::ok);
                })
                .exceptionally(throwable -> {
                    // Handle any errors (appointment not found, etc.)
                    return ResponseEntity.status(500).build();
                });
    }

    // Get upcoming appointments (WAITING state)
    @GetMapping("/upcoming")
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<AppointmentDetails>>> getUpcomingAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentDetailsByState(
                authenticatedPatientId, AppointmentDetails.State.WAITING, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    // Get current appointments (IN_PROGRESS state)
    @GetMapping("/current")
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<AppointmentDetails>>> getCurrentAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentDetailsByState(
                authenticatedPatientId, AppointmentDetails.State.IN_PROGRESS, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    // Get completed appointments (COMPLETED state)
    @GetMapping("/completed")
    @AbdmAuthorized(resource = "appointment", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<AppointmentDetails>>> getCompletedAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "50") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentDetailsByState(
                authenticatedPatientId, AppointmentDetails.State.COMPLETED, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class CreateAppointmentRequestBody {
        private final String patientId;
    }
}
