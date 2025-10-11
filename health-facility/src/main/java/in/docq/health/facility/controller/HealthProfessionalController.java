package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.service.HealthProfessionalService;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import in.docq.keycloak.rest.client.model.Permission;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/refresh-token")
    public CompletionStage<ResponseEntity<GetAccessToken200Response>> refreshUserAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return healthProfessionalService.refreshUserAccessToken(refreshToken)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/logout")
    public CompletionStage<ResponseEntity<Void>> logoutFacilityProfessional(@RequestHeader("Authorization") String bearerToken,
                                                                            @RequestBody LogoutHealthProfessionalRequestBody logoutHealthProfessionalRequestBody) {
        return healthProfessionalService.logout(bearerToken, logoutHealthProfessionalRequestBody.getRefreshToken())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/onboard")
    @Authorized(resource = "health-facility", scope = "onboarding")
    public CompletionStage<ResponseEntity<Void>> onBoardHealthFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                                  @RequestBody OnBoardHealthProfessionalRequestBody createHealthProfessionalRequestBody) {
        return healthProfessionalService.onBoard(healthFacilityID, createHealthProfessionalRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{health-facility-professional-id}")
    public CompletionStage<ResponseEntity<HealthProfessional>> getHealthFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                                             @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID) {
        return healthProfessionalService.get(healthFacilityID, healthFacilityProfessionalID)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    Throwable cause = throwable.getCause();
                    if (cause instanceof HealthProfessionalNotFound) {
                        return ResponseEntity.notFound().build();
                    }
                    return ResponseEntity.internalServerError().build();
                });
    }

    @GetMapping("/search")
    public CompletionStage<ResponseEntity<List<HealthProfessional>>> listHealthProfessionals(
            @RequestParam("state-code") int stateCode,
            @RequestParam("district-code") int districtCode,
            @RequestParam(value = "speciality", required = false) String speciality) {
        return healthProfessionalService
                .listByStateDistrictAndSpeciality(stateCode, districtCode, Optional.ofNullable(speciality))
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class OnBoardHealthProfessionalRequestBody {
        private final String healthProfessionalID;
        private final String healthProfessionalName;
        private final HealthProfessionalType type;
        private final int stateCode;
        private final int districtCode;
        private final String healthFacilityName;
        private final String address;
        private final String pincode;
        private final Double latitude;
        private final Double longitude;
        private final String speciality;
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

    @Builder
    @Getter
    public static class LogoutHealthProfessionalRequestBody {
        private final String refreshToken;
    }


}
