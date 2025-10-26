package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.exception.HealthProfessionalNotFound;
import in.docq.health.facility.model.Doctor;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.service.HealthProfessionalService;
import in.docq.keycloak.rest.client.model.GetAccessToken200Response;
import in.docq.keycloak.rest.client.model.Permission;
import lombok.Builder;
import lombok.Getter;
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

    @PostMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}/login")
    public CompletionStage<ResponseEntity<LoginResponse>> loginFacilityProfessional(@PathVariable("health-facility-id") String healthFacilityID,
                                                                           @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                                           @RequestBody LoginHealthProfessionalRequestBody loginHealthProfessionalRequestBody) {
        return healthProfessionalService.login(healthFacilityID, healthFacilityProfessionalID, loginHealthProfessionalRequestBody.getPassword())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/health-facilities/{health-facility-id}/health-facility-professionals/refresh-token")
    public CompletionStage<ResponseEntity<GetAccessToken200Response>> refreshUserAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return healthProfessionalService.refreshUserAccessToken(refreshToken)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/health-facility-professionals/logout")
    public CompletionStage<ResponseEntity<Void>> logoutFacilityProfessional(@RequestHeader("Authorization") String bearerToken,
                                                                            @RequestBody LogoutHealthProfessionalRequestBody logoutHealthProfessionalRequestBody) {
        return healthProfessionalService.logout(bearerToken, logoutHealthProfessionalRequestBody.getRefreshToken())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/health-facility-professionals/facility-manager/onboard")
    @Authorized(resource = "health-facility", scope = "onboarding")
    public CompletionStage<ResponseEntity<Void>> onBoardFacilityManager(@PathVariable("health-facility-id") String healthFacilityID,
                                                                        @RequestBody OnBoardFacilityManagerRequestBody createHealthProfessionalRequestBody) {
        return healthProfessionalService.onBoardFacilityManager(healthFacilityID, createHealthProfessionalRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/health-facilities/{health-facility-id}/health-facility-professionals/doctor/onboard")
    @Authorized(resource = "health-facility", scope = "onboarding")
    public CompletionStage<ResponseEntity<Void>> onBoardDoctor(@PathVariable("health-facility-id") String healthFacilityID,
                                                                @RequestBody OnBoardDoctorRequestBody onBoardDoctorRequestBody) {
        return healthProfessionalService.onBoardDoctor(healthFacilityID, onBoardDoctorRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}")
    @Authorized(resource = "health-professional", scope = "read")
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

    @GetMapping("/health-facilities/{health-facility-id}/health-facility-professionals/doctors")
    @Authorized(resource = "health-professional", scope = "read")
    public CompletionStage<ResponseEntity<List<Doctor>>> listByHealthFacility(
            @PathVariable("health-facility-id") String healthFacilityID,
            @RequestParam(value = "facility-manager-id", required = false) String facilityManagerID) {
        return healthProfessionalService
                .listDoctorsByHealthFacility(healthFacilityID, facilityManagerID)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/doctors")
    @Authorized(resource = "health-professional", scope = "read")
    public CompletionStage<ResponseEntity<List<Doctor>>> listHealthProfessionals(
            @RequestParam(value = "state-code") int stateCode,
            @RequestParam(value = "district-code") int districtCode) {
        return healthProfessionalService
                .listDoctorsByStateDistrict(stateCode, districtCode)
                .thenApply(ResponseEntity::ok);
    }

    @Builder
    @Getter
    public static class OnBoardFacilityManagerRequestBody {
        private final String facilityManagerID;
        private final String password;
    }

    @Builder
    @Getter
    public static class OnBoardDoctorRequestBody {
        private final String doctorID;
        private final String password;
        private final String facilityManagerID;
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
