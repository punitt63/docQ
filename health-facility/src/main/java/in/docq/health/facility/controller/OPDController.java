package in.docq.health.facility.controller;

import in.docq.health.facility.auth.Authorized;
import in.docq.health.facility.model.OPD;
import in.docq.health.facility.service.OPDService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;

@RestController
@RequestMapping("/health-facilities/{health-facility-id}/health-facility-professionals")
public class OPDController {
    private final OPDService opdService;

    @Autowired
    public OPDController(OPDService opdService) {
        this.opdService = opdService;
    }

    @PostMapping("/{health-facility-professional-id}/opds")
    @Authorized(resource = "opd", scope = "create")
    public CompletionStage<ResponseEntity<Void>> createOPD(@PathVariable("health-facility-id") String healthFacilityID,
                                                           @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                           @RequestBody CreateOPDRequestBody createOPDRequestBody) {
        createOPDRequestBody.validate();
        return opdService.insert(healthFacilityID, healthFacilityProfessionalID, createOPDRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/{health-facility-professional-id}/opd-dates/{opd-date}/opds/{opd-id}")
    public CompletionStage<ResponseEntity<OPD>> updateOPD(@PathVariable("health-facility-id") String healthFacilityID,
                                                          @PathVariable("health-facility-professional-id") String healthFacilityProfessionalID,
                                                          @PathVariable("opd-date") LocalDate opdDate,
                                                          @PathVariable("opd-id") String id,
                                                          @RequestBody UpdateOPDRequestBody updateOPDRequestBody) {
        return opdService.update(healthFacilityID, healthFacilityProfessionalID, opdDate, id, updateOPDRequestBody)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/opds")
    public CompletionStage<ResponseEntity<List<OPD>>> listOPDs(@PathVariable("health-facility-id") String healthFacilityID,
                                                               @RequestParam("health-facility-professional-id") String healthFacilityProfessionalID,
                                                               @RequestParam("start-date") LocalDate startDate,
                                                               @RequestParam("end-date") LocalDate endDate) {
        checkState(endDate.isBefore(startDate.plusDays(31)), "start date and end date should have at max 30 days of difference");
        return opdService.list(healthFacilityID, healthFacilityProfessionalID, startDate, endDate)
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

        private String startDate;
        private String endDate;

        private OPD.ScheduleType scheduleType;
        private List<Boolean> weeklyTemplate;
        private int maxSlots;
        private int minutesPerSlot;

        @Min(value = 60)
        @Max(value = 7 * 24 * 60)
        private int minutesToActivate;

        public void validate() {
            LocalDate currentDate = LocalDate.now();
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            checkState(start.isAfter(currentDate), "start Date should be At least Tomorrow");
            checkState(!end.isBefore(start.plusYears(1)), "End Date should be Maximum One Year From Start");
        }

        public LocalDate getStartDateAsLocalDate() {
            return LocalDate.parse(startDate);
        }

        public LocalDate getEndDateAsLocalDate() {
            return LocalDate.parse(endDate);
        }
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
        private int maxSlots;
        private int minutesPerSlot;
        @Min(value = 60)
        @Max(value = 7 * 24 * 60)
        private Integer minutesToActivate;
        private OPD.State state;
        private Long actualStartTime;
        private Long actualEndTime;
    }
}
