package in.docq.patient.controller;

import in.docq.patient.model.Doctor;
import in.docq.patient.model.HealthProfessional;
import in.docq.patient.service.HealthProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
public class HealthProfessionalController {

    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public HealthProfessionalController(HealthProfessionalService healthProfessionalService) {
        this.healthProfessionalService = healthProfessionalService;
    }

    @GetMapping("/doctors")
    public CompletionStage<ResponseEntity<List<Doctor>>> listDoctors(
            @RequestParam("state-code") int stateCode,
            @RequestParam("district-code") int districtCode) {
        return healthProfessionalService.list(stateCode, districtCode)
                .thenApply(ResponseEntity::ok);
    }
}


