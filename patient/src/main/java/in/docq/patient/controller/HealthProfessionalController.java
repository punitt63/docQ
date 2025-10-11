package in.docq.patient.controller;

import in.docq.patient.auth.AbdmAuthorized;
import in.docq.patient.model.HealthProfessional;
import in.docq.patient.service.HealthProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-professionals")
public class HealthProfessionalController {

    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public HealthProfessionalController(HealthProfessionalService healthProfessionalService) {
        this.healthProfessionalService = healthProfessionalService;
    }

    @GetMapping
    @AbdmAuthorized(resource = "health-professionals", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<HealthProfessional>>> listHealthProfessionals(
            @RequestParam("state-code") int stateCode,
            @RequestParam("district-code") int districtCode,
            @RequestParam(value = "speciality", required = false) String speciality) {
        return healthProfessionalService.list(stateCode, districtCode, speciality)
                .thenApply(ResponseEntity::ok);
    }
}


