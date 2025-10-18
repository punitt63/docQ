package in.docq.patient.controller;

import in.docq.patient.auth.AbdmAuthorized;
import in.docq.patient.client.HealthFacilityRestClient;
import in.docq.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/health-facilities/{health-facility-id}/create-if-not-exists")
    @AbdmAuthorized(resource = "patient", validatePatientId = false)
    public CompletionStage<ResponseEntity<Void>> createPatientIfNotExists(@PathVariable("health-facility-id") String healthFacilityId,
                                                                          @RequestBody HealthFacilityRestClient.CreatePatientRequestBody requestBody) {
        return patientService.createPatientIfNotExists(healthFacilityId, requestBody)
                .thenApply(ResponseEntity::ok);
    }
}


