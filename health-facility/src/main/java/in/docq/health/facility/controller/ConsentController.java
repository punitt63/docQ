package in.docq.health.facility.controller;

import com.google.gson.JsonObject;
import in.docq.abha.rest.client.model.AbdmConsentManagement1Request;
import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.ConsentHealthRecord;
import in.docq.health.facility.model.ConsentRequest;
import in.docq.health.facility.service.HiuConsentService;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}")
public class ConsentController {
    private final HiuConsentService hiuConsentService;

    public ConsentController(HiuConsentService hiuConsentService) {
        this.hiuConsentService = hiuConsentService;
    }

    @PostMapping("/consent-request")
    @Authorized(resource = "consent", scope = "create")
    public CompletionStage<ResponseEntity<Void>> createConsentRequest(
            @PathVariable("health-facility-id") String healthFacilityId,
            @PathVariable("health-facility-professional-id") String healthFacilityProfessionalId,
            @RequestBody AbdmConsentManagement1Request consentRequest) {

            return hiuConsentService.processConsentRequest(healthFacilityId, healthFacilityProfessionalId, consentRequest)
                    .thenApply(ignore -> ResponseEntity.ok().build());

    }

    @GetMapping("/consent-request")
    @Authorized(resource = "consent", scope = "read")
    public CompletionStage<ResponseEntity<List<ConsentRequest>>> getConsentRequests(
            @PathVariable("health-facility-id") String healthFacilityId,
            @PathVariable("health-facility-professional-id") String healthFacilityProfessionalId) {

        return hiuConsentService.listConsentRequests(healthFacilityId, healthFacilityProfessionalId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/consent-request/{consent-request-id}/health-records")
    @Authorized(resource = "consent", scope = "read")
    public CompletionStage<ResponseEntity<ConsentHealthRecordsResponse>> getConsentHealthRecords(
            @PathVariable("health-facility-id") String healthFacilityId,
            @PathVariable("health-facility-professional-id") String healthFacilityProfessionalId,
            @PathVariable("consent-request-id") String consentRequestId) {

        return hiuConsentService.listConsentHealthRecords(healthFacilityId, healthFacilityProfessionalId, consentRequestId)
                .thenApply(consentHealthRecords -> ResponseEntity.ok(ConsentHealthRecordsResponse.builder()
                        .healthRecords(consentHealthRecords.stream().map(ConsentHealthRecord::getHealthRecord).toList())
                        .build()));
    }

    @Builder
    public static class ConsentHealthRecordsResponse {
        private List<JsonObject> healthRecords;
    }
}
