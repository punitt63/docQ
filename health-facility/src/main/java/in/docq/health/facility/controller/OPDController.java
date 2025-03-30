package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.service.OPDService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}/opds")
public class OPDController {
    private final OPDService opdService;

    public OPDController(OPDService opdService) {
        this.opdService = opdService;
    }

    @PostMapping("")
    @Authorized(resource = "opd", scope = "create")
    public CompletionStage<ResponseEntity<Void>> createOPD(@PathVariable("health-facility-id") String healthFacilityID,
                                                           @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                           @RequestBody CreateOPDRequestBody createOPDRequestBody) {
        return opdService.insert(healthFacilityID, healthFacilityProfessionalID, createOPDRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/{opd-name}")
    public CompletionStage<ResponseEntity<OPD>> updateOPD(@PathVariable("health-facility-id") String healthFacilityID,
                                                          @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                          @PathVariable("opd-name") String name,
                                                          @RequestBody UpdateOPDRequestBody updateOPDRequestBody) {
        return opdService.update(healthFacilityID, healthFacilityProfessionalID, name, updateOPDRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{opd-name}")
    public CompletionStage<ResponseEntity<OPD>> getOPD(@PathVariable("health-facility-id") String healthFacilityID,
                                                       @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                       @PathVariable("opd-name") String name) {
        return opdService.get(healthFacilityID, healthFacilityProfessionalID, name)
                .thenApply(ResponseEntity::ok);
    }

    @Getter
    @Builder
    public static class CreateOPDRequestBody {
        @NotBlank(message = "The name is required.")
        private String name;

        @Min(value = 0)
        @Max(value = 23)
        private int startHour;

        @Min(value = 0)
        @Max(value = 23)
        private int endHour;

        @Min(value = 0)
        @Max(value = 59)
        private int startMinute;

        @Min(value = 0)
        @Max(value = 59)
        private int endMinute;

        private boolean recurring;
        private String startDate;
        private OPD.ScheduleType scheduleType;
        private List<Boolean> weeklyTemplate;
        private int maxSlots;
        private int minutesPerSlot;

        @Min(value = 60)
        @Max(value = 7 * 24 * 60)
        private int instanceCreationMinutesBeforeStart;
    }

    @Getter
    @Builder
    public static class UpdateOPDRequestBody {
        @Min(value = 0)
        @Max(value = 23)
        private int startHour;

        @Min(value = 0)
        @Max(value = 23)
        private int endHour;

        @Min(value = 0)
        @Max(value = 59)
        private int startMinute;
        @Min(value = 0)
        @Max(value = 59)
        private int endMinute;
        private boolean recurring;
        private OPD.ScheduleType scheduleType;
        private List<Boolean> weeklyTemplate;
        private int maxSlots;
        private int minutesPerSlot;
        @Min(value = 60)
        @Max(value = 7 * 24 * 60)
        private int instanceCreationMinutesBeforeStart;
        private OPD.State state;
    }
}
