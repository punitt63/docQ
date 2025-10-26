package in.docq.patient.controller;

import in.docq.patient.model.Prescription;
import in.docq.patient.service.PrescriptionService;
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
    public CompletionStage<ResponseEntity<List<Prescription>>> listPatientPrescriptions(@RequestParam("start-opd-date") LocalDate startOpdDate,
                                                                                              @RequestParam("end-opd-date") LocalDate endOpdDate,
                                                                                              @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit,
                                                                                              HttpServletRequest request) {
        String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
        return prescriptionService.listPatientPrescriptions(authenticatedPatientId, startOpdDate, endOpdDate, limit)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/opd-dates/{opd-date}/opds/{opd-id}/appointments/{appointment-id}")
    public CompletionStage<ResponseEntity<Prescription>> getOPDPrescription(@PathVariable("opd-date") LocalDate opdDate,
                                                                            @PathVariable("opd-id") String opdId,
                                                                            @PathVariable("appointment-id") Integer appointmentId,
                                                                            HttpServletRequest request) {
        return prescriptionService.getOPDPrescription(opdDate, opdId, appointmentId)
                .thenApply(ResponseEntity::ok);
    }
}


