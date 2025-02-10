package in.docq.health.facility.controller;

import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.service.HealthProfessionalService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals")
public class HealthProfessionalController {
    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public HealthProfessionalController(HealthProfessionalService healthProfessionalService) {
        this.healthProfessionalService = healthProfessionalService;
    }

    @PostMapping("/onboard")
    public CompletionStage<ResponseEntity<Void>> onBoardHealthFacilityProfessional(@PathVariable String healthFacilityID,
                                                                                  @RequestBody OnBoardHealthProfessionalRequestBody createHealthProfessionalRequestBody) {
        return healthProfessionalService.onBoard(healthFacilityID, createHealthProfessionalRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class OnBoardHealthProfessionalRequestBody {
        private final String healthProfessionalID;
        private final HealthProfessionalType type;
        private final String password;
    }


}
