package in.docq.patient.controller;

import in.docq.patient.auth.AbdmAuthorized;
import in.docq.patient.model.AppointmentDetails;
import in.docq.patient.service.PrescriptionService;
import in.docq.patient.model.Prescription;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @AbdmAuthorized(resource = "prescription", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<AppointmentDetails>>> listPatientPrescriptions(@RequestParam("start-opd-date") LocalDate startOpdDate,
                                                                                              @RequestParam("end-opd-date") LocalDate endOpdDate,
                                                                                              @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit,
                                                                                              HttpServletRequest request) {
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        return prescriptionService.listPatientPrescriptions(authenticatedPatientId, startOpdDate, endOpdDate, limit)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}")
    @AbdmAuthorized(resource = "prescription", validatePatientId = false)
    public CompletionStage<ResponseEntity<Prescription>> getOPDPrescription(@PathVariable("opd-date") LocalDate opdDate,
                                                                            @PathVariable("opd-id") String opdId,
                                                                            @PathVariable("appointment-id") Integer appointmentId,
                                                                            HttpServletRequest request) {
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");

        // Optional: Could add validation by fetching appointments and ensuring ownership, similar to cancel logic
        return prescriptionService.getOPDPrescription(opdDate, opdId, appointmentId)
                .thenApply(ResponseEntity::ok);
    }
}


