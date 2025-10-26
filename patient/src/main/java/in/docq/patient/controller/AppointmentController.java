package in.docq.patient.controller;

import in.docq.patient.model.Appointment;
import in.docq.patient.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
    public CompletionStage<ResponseEntity<Appointment>> createAppointment(@PathVariable("health-facility-id") String healthFacilityId,
                                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdId,
                                                                          @RequestBody CreateAppointmentRequestBody requestBody,
                                                                          HttpServletRequest request) {
        return appointmentService.createAppointment(healthFacilityId, opdDate, opdId, requestBody.getPatientId())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
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

        // Use authenticated patientId for scoping
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");

        // Always filter by authenticated patient ID
        return appointmentService.getAppointments(startOpdDate, endOpdDate, opdId, authenticatedPatientId, states, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/cancel")
    public CompletionStage<ResponseEntity<Appointment>> cancelAppointment(@PathVariable("health-facility-id") String healthFacilityId,
                                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdId,
                                                                          @PathVariable("appointment-id") Integer appointmentId,
                                                                          HttpServletRequest request) {
        return appointmentService.cancelAppointment(healthFacilityId, opdDate, opdId, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    // Get upcoming appointments (WAITING state)
    @GetMapping("/upcoming")
    public CompletionStage<ResponseEntity<List<Appointment>>> getUpcomingAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentByState(
                authenticatedPatientId, Appointment.State.WAITING, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    // Get current appointments (IN_PROGRESS state)
    @GetMapping("/current")
    public CompletionStage<ResponseEntity<List<Appointment>>> getCurrentAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentByState(
                authenticatedPatientId, Appointment.State.IN_PROGRESS, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    // Get completed appointments (COMPLETED state)
    @GetMapping("/completed")
    public CompletionStage<ResponseEntity<List<Appointment>>> getCompletedAppointments(
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "50") Integer limit,
            HttpServletRequest request) {

        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        
        return appointmentService.getAppointmentByState(
                authenticatedPatientId, Appointment.State.COMPLETED, startDate, endDate, offset, limit)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class CreateAppointmentRequestBody {
        private final String patientId;
    }
}
