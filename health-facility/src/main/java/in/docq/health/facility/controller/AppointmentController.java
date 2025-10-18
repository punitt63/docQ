package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.service.AppointmentService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments")
    @Authorized(resource = "appointment", scope = "create")
    public CompletionStage<ResponseEntity<Appointment>> createAppointment(@PathVariable("health-facility-id") String healthFacilityID,
                                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String id,
                                                                          @RequestBody CreateAppointmentRequestBody createAppointmentRequestBody) {
        return appointmentService.createAppointment(opdDate, id, createAppointmentRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}/appointments")
    @Authorized(resource = "appointment", scope = "read")
    public CompletionStage<ResponseEntity<List<Appointment>>> listAppointmentsByHealthFacilityAndDoctor(@PathVariable("health-facility-id") String healthFacilityID,
                                                                                                        @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                                                               @RequestParam(value = "start-opd-date") LocalDate startOpdDate,
                                                                                               @RequestParam(value = "end-opd-date") LocalDate endOpdDate,
                                                                                               @RequestParam(value = "opd-id", required = false) String opdID,
                                                                                               @RequestParam(value = "patient-id", required = false) String patientID,
                                                                                               @RequestParam(value = "state", required = false) List<Appointment.State> states,
                                                                                               @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                                                               @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        checkState(endOpdDate.isBefore(startOpdDate.plusDays(31)), "start date and end date should have at max 30 days of difference");
        checkState(limit <= 5, "Limit should be less than 5");
        return appointmentService.list(healthFacilityID, healthFacilityProfessionalID, startOpdDate, endOpdDate, opdID, patientID, states, limit)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/appointments")
    @Authorized(resource = "appointment", scope = "read")
    public CompletionStage<ResponseEntity<List<Appointment>>> getAppointment(@RequestParam(value = "start-opd-date") LocalDate startOpdDate,
                                                                             @RequestParam(value = "end-opd-date") LocalDate endOpdDate,
                                                                             @RequestParam(value = "opd-id", required = false) String opdID,
                                                                             @RequestParam(value = "patient-id", required = false) String patientID,
                                                                             @RequestParam(value = "state", required = false) List<Appointment.State> states,
                                                                             @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                                             @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        checkState(endOpdDate.isBefore(startOpdDate.plusDays(31)), "start date and end date should have at max 30 days of difference");
        checkState(limit <= 5, "Limit should be less than 5");
        return appointmentService.list(startOpdDate, endOpdDate, opdID, patientID, states, limit)
                .thenApply(ResponseEntity::ok);
    }


    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/start")
    @Authorized(resource = "appointment", scope = "start")
    public CompletionStage<ResponseEntity<Appointment>> startAppointment(@PathVariable("health-facility-id") String healthFacilityID,
                                                                         @PathVariable("opd-date") LocalDate opdDate,
                                                                         @PathVariable("opd-id") String opdID,
                                                                         @PathVariable("appointment-id") Integer appointmentId,
                                                                         @RequestAttribute("type") HealthProfessionalType healthProfessionalType) {
        return appointmentService.startAppointment(healthProfessionalType, opdDate, opdID, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/complete")
    @Authorized(resource = "appointment", scope = "complete")
    public CompletionStage<ResponseEntity<Appointment>> completeAppointment(@PathVariable("opd-date") LocalDate opdDate,
                                                                           @PathVariable("opd-id") String opdID,
                                                                           @PathVariable("appointment-id") Integer appointmentId) {
        return appointmentService.completeAppointment(opdDate, opdID, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/cancel")
    @Authorized(resource = "appointment", scope = "cancel")
    public CompletionStage<ResponseEntity<Appointment>> cancelAppointment(@PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdID,
                                                                          @PathVariable("appointment-id") Integer appointmentId) {
        return appointmentService.cancelAppointment(opdDate, opdID, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/no-show")
    @Authorized(resource = "appointment", scope = "no-show")
    public CompletionStage<ResponseEntity<Appointment>> noShowAppointment(@PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdID,
                                                                          @PathVariable("appointment-id") Integer appointmentId) {
        return appointmentService.noShow(opdDate, opdID, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/no-show-to-waiting")
    @Authorized(resource = "appointment", scope = "no-show-to-waiting")
    public CompletionStage<ResponseEntity<Appointment>> noShowToWaitingAppointment(@PathVariable("opd-date") LocalDate opdDate,
                                                                          @PathVariable("opd-id") String opdID,
                                                                          @PathVariable("appointment-id") Integer appointmentId) {
        return appointmentService.noShowToWaiting(opdDate, opdID, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class CreateAppointmentRequestBody {
        private final String patientID;
    }

    @Builder
    @Getter
    public static class UpdateAppointmentRequestBody {
        private final Appointment.State state;
        private final String cancellationReason;
        private final Long startTime;
        private final Long endTime;
        private final int priority;
    }

}
