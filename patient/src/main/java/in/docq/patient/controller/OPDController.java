package in.docq.patient.controller;

import in.docq.patient.auth.AbdmAuthorized;
import in.docq.patient.model.OPD;
import in.docq.patient.service.OPDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;

@RestController
@RequestMapping("/opds")
public class OPDController {

    private final OPDService opdService;

    @Autowired
    public OPDController(OPDService opdService) {
        this.opdService = opdService;
    }

    @GetMapping("/health-facilities/{health-facility-id}/health-facility-professionals/{health-facility-professional-id}")
    @AbdmAuthorized(resource = "opd", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<OPD>>> listOPDs(@PathVariable("health-facility-id") String healthFacilityId,
                                                              @PathVariable("health-facility-professional-id") String healthFacilityProfessionalId,
                                                              @RequestParam("start-date") LocalDate startDate,
                                                              @RequestParam("end-date") LocalDate endDate) {
        checkState(endDate.isBefore(startDate.plusDays(31)), "start date and end date should have at max 30 days of difference");
        return opdService.listOPDs(healthFacilityId, healthFacilityProfessionalId, startDate, endDate)
                .thenApply(ResponseEntity::ok);
    }
}
