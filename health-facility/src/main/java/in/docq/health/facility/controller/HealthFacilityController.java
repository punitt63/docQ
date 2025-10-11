package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.service.HealthFacilityService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities")
public class HealthFacilityController {
    private final HealthFacilityService healthFacilityService;

    public HealthFacilityController(HealthFacilityService healthFacilityService) {
        this.healthFacilityService = healthFacilityService;
    }

    @PostMapping("/onboard")
    @Authorized(resource = "health-facility", scope = "onboarding")
    public CompletionStage<ResponseEntity<Void>> onboardHealthFacility(@RequestBody onBoardHealthFacilityRequestBody requestBody) {
        return healthFacilityService.onboardHealthFacility(requestBody.getFacilityName(), requestBody.getFacilityId())
                .thenApply(response -> ResponseEntity.ok().build());
    }

    @Builder
    @Getter
    public static class onBoardHealthFacilityRequestBody {
        private String facilityName;
        private String facilityId;
    }
}
