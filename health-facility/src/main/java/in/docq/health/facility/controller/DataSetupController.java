package in.docq.health.facility.controller;

import in.docq.data.setup.service.DataSetupOrchestrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class DataSetupController {

    private final DataSetupOrchestrator dataSetupOrchestrator;

    @Autowired
    public DataSetupController(DataSetupOrchestrator dataSetupOrchestrator) {
        this.dataSetupOrchestrator = dataSetupOrchestrator;
    }

    /**
     * Execute the complete data setup flow
     * 
     * Invocation:
     * 1. Triggered by HTTP POST to /admin/data-setup endpoint
     * 2. Can be called via:
     *    - curl -X POST http://localhost:9097/admin/data-setup
     *    - docker-compose (data-setup service automatically calls this on startup)
     *    - Postman/any HTTP client
     * 
     * This endpoint will:
     * 1. Get admin token from Keycloak
     * 2. Onboard facility managers via REST API
     * 3. Login as facility managers and onboard doctors via REST API
     */
    @PostMapping("/data-setup")
    public ResponseEntity<String> executeDataSetup() {
        log.info("Data setup endpoint called");
        
        try {
            dataSetupOrchestrator.executeDataSetup();
            log.info("Data setup completed successfully");
            return ResponseEntity.ok("Data setup completed successfully");
        } catch (Exception e) {
            log.error("Data setup failed: ", e);
            return ResponseEntity.internalServerError()
                    .body("Data setup failed: " + e.getMessage());
        }
    }
}

