package in.docq.data.setup.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HealthFacilityRestClient {
    private final WebClient webClient;
    private final String healthFacilityBaseUrl;

    public HealthFacilityRestClient(@org.springframework.beans.factory.annotation.Value("${health.facility.base.url:http://localhost:9097}") String healthFacilityBaseUrl) {
        this.healthFacilityBaseUrl = healthFacilityBaseUrl;
        this.webClient = WebClient.builder()
                .baseUrl(healthFacilityBaseUrl)
                .build();
    }

    /**
     * Get admin token from Keycloak
     */
    public Mono<String> getAdminToken(String keycloakBaseUrl, String realm, String username, String password) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme(keycloakBaseUrl.startsWith("http") ? "" : "http")
                        .host(keycloakBaseUrl.replace("http://", "").replace("https://", ""))
                        .path("/realms/{realm}/protocol/openid-connect/token")
                        .build(realm))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=password&username=" + username + "&password=" + password + "&client_id=admin-cli")
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::getAccessToken);
    }

    /**
     * Onboard facility manager with admin token
     */
    public Mono<Void> onboardFacilityManager(String facilityId, String facilityManagerId, String password, String bearerToken) {
        OnBoardFacilityManagerRequest request = OnBoardFacilityManagerRequest.builder()
                .facilityManagerID(facilityManagerId)
                .password(password)
                .build();

        return webClient.post()
                .uri("/health-facilities/{facilityId}/health-facility-professionals/facility-manager/onboard", facilityId)
                .header("Authorization", "Bearer " + bearerToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Onboard facility manager without auth token (for testing/setup)
     * Note: This will fail if the endpoint requires authentication
     */
    public Mono<Void> onboardFacilityManagerWithoutToken(String facilityId, String facilityManagerId, String password) {
        OnBoardFacilityManagerRequest request = OnBoardFacilityManagerRequest.builder()
                .facilityManagerID(facilityManagerId)
                .password(password)
                .build();

        return webClient.post()
                .uri("/health-facilities/{facilityId}/health-facility-professionals/facility-manager/onboard", facilityId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Login as facility manager
     */
    public Mono<LoginResponse> loginFacilityManager(String facilityId, String facilityManagerId, String password) {
        LoginRequest request = LoginRequest.builder()
                .password(password)
                .build();

        return webClient.post()
                .uri("/health-facilities/{facilityId}/health-facility-professionals/{facilityManagerId}/login", facilityId, facilityManagerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class);
    }

    /**
     * Onboard doctor
     */
    public Mono<Void> onboardDoctor(String facilityId, String doctorId, String password, String facilityManagerId, String bearerToken) {
        OnBoardDoctorRequest request = OnBoardDoctorRequest.builder()
                .doctorID(doctorId)
                .password(password)
                .facilityManagerID(facilityManagerId)
                .build();

        return webClient.post()
                .uri("/health-facilities/{facilityId}/health-facility-professionals/doctor/onboard", facilityId)
                .header("Authorization", "Bearer " + bearerToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OnBoardFacilityManagerRequest {
        private String facilityManagerID;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OnBoardDoctorRequest {
        private String doctorID;
        private String password;
        private String facilityManagerID;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginRequest {
        private String password;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
    }
}

