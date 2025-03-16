package in.docq.health.facility.controller;

import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.service.HealthProfessionalService;
import in.docq.keycloak.rest.client.model.Permission;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals")
public class HealthProfessionalController {
    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public HealthProfessionalController(HealthProfessionalService healthProfessionalService) {
        this.healthProfessionalService = healthProfessionalService;
    }

    @PostMapping("/{health-facility-professional-id}/login")
    public CompletionStage<ResponseEntity<LoginResponse>> loginFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                           @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                                           @RequestBody LoginHealthProfessionalRequestBody loginHealthProfessionalRequestBody) {
        return healthProfessionalService.login(healthFacilityID, healthFacilityProfessionalID, loginHealthProfessionalRequestBody.getPassword())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/onboard")
    public CompletionStage<ResponseEntity<Void>> onBoardHealthFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                                  @RequestBody OnBoardHealthProfessionalRequestBody createHealthProfessionalRequestBody) {
        return healthProfessionalService.onBoard(healthFacilityID, createHealthProfessionalRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{health-facility-professional-id}")
    public CompletionStage<ResponseEntity<HealthProfessional>> getHealthFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                                             @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID) {
        return healthProfessionalService.get(healthFacilityID, healthFacilityProfessionalID)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class OnBoardHealthProfessionalRequestBody {
        private final String healthProfessionalID;
        private final HealthProfessionalType type;
        private final String password;
    }

    @Builder
    @Getter
    public static class LoginHealthProfessionalRequestBody {
        private final String password;
    }

    @Builder
    @Getter
    public static class LoginResponse {
        private final String accessToken;
        private final String refreshToken;
        private final List<Permission> permissions;
    }


}
