package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.Prescription;
import in.docq.health.facility.model.AppointmentDetails;
import in.docq.health.facility.service.AppointmentService;
import in.docq.health.facility.service.PrescriptionService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

@RestController
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;

    public PrescriptionController(PrescriptionService prescriptionService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/prescriptions")
    @Authorized(resource = "prescription", scope = "create")
    public CompletionStage<ResponseEntity<Void>> createOPDPrescription(@PathVariable("opd-date") LocalDate opdDate,
                                                                                  @PathVariable("opd-id") String opdId,
                                                                                  @PathVariable("appointment-id") Integer appointmentId,
                                                                                  @RequestBody CreateOrReplaceOPDPrescriptionRequestBody createOrReplaceOPDPrescriptionRequestBody) {
        return prescriptionService.create(opdDate, opdId, appointmentId, createOrReplaceOPDPrescriptionRequestBody.getContent())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/patients/{patient-id}/prescriptions")
    //@Authorized(resource = "prescription", scope = "read")
    public CompletionStage<ResponseEntity<java.util.List<AppointmentDetails>>> listPatientPrescriptions(@PathVariable("patient-id") String patientId,
                                                                                                        @RequestParam("start-opd-date") LocalDate startOpdDate,
                                                                                                        @RequestParam("end-opd-date") LocalDate endOpdDate,
                                                                                                        @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit) {
        return appointmentService.listCompleted(startOpdDate, endOpdDate, null, patientId, limit)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/prescriptions")
    @Authorized(resource = "prescription", scope = "read")
    public CompletionStage<ResponseEntity<Prescription>> getOPDPrescription(@PathVariable("opd-date") LocalDate opdDate,
                                                                            @PathVariable("opd-id") String opdId,
                                                                            @PathVariable("appointment-id") Integer appointmentId) {
        return prescriptionService.get(opdDate, opdId, appointmentId)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/health-facilities/{health-facility-id}/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}/prescriptions")
    @Authorized(resource = "prescription", scope = "update")
    public CompletionStage<ResponseEntity<Void>> getOPDPrescription(@PathVariable("opd-date") LocalDate opdDate,
                                                                               @PathVariable("opd-id") String opdId,
                                                                               @PathVariable("appointment-id") Integer appointmentId,
                                                                               @RequestBody CreateOrReplaceOPDPrescriptionRequestBody createOrReplaceOPDPrescriptionRequestBody
                                                                               ) {
        return prescriptionService.replace(opdDate, opdId, appointmentId, createOrReplaceOPDPrescriptionRequestBody.getContent())
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class CreateOrReplaceOPDPrescriptionRequestBody {
        private String content;
    }

}
