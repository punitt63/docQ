package in.docq.health.facility.controller;

import in.docq.health.facility.service.DataSetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@Slf4j
@RestController
@RequestMapping("/admin")
public class DataSetupController {

    private final DataSetupService dataSetupService;

    @Autowired
    public DataSetupController(DataSetupService dataSetupService) {
        this.dataSetupService = dataSetupService;
    }

    /**
     * Execute the complete data setup flow
     * This endpoint will:
     * 1. Create admin token
     * 2. Onboard health facility
     * 3. Onboard facility managers
     * 4. Login as facility managers and onboard doctors
     */
    @PostMapping("/data-setup")
    public CompletionStage<ResponseEntity<String>> executeDataSetup() {
        log.info("Data setup endpoint called");
        
        return dataSetupService.executeDataSetup()
                .thenApply(ignore -> {
                    log.info("Data setup completed successfully");
                    return ResponseEntity.ok("Data setup completed successfully");
                })
                .exceptionally(throwable -> {
                    log.error("Data setup failed: ", throwable);
                    return ResponseEntity.internalServerError()
                            .body("Data setup failed: " + throwable.getMessage());
                });
    }
}
