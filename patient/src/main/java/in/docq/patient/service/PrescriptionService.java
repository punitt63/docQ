package in.docq.patient.service;

import in.docq.patient.client.HealthFacilityRestClient;
import in.docq.patient.model.Prescription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Service
public class PrescriptionService {

    private final HealthFacilityRestClient healthFacilityRestClient;

    public PrescriptionService(HealthFacilityRestClient healthFacilityRestClient) {
        this.healthFacilityRestClient = healthFacilityRestClient;
    }

    public CompletionStage<List<Prescription>> listPatientPrescriptions(String patientId,
                                                                              LocalDate startOpdDate,
                                                                              LocalDate endOpdDate,
                                                                              Integer limit) {
        return healthFacilityRestClient.listPatientPrescriptions(patientId, startOpdDate, endOpdDate, limit);
    }

    public CompletionStage<Prescription> getOPDPrescription(LocalDate opdDate,
                                                            String opdId,
                                                            Integer appointmentId) {
        return healthFacilityRestClient.getOPDPrescription(opdDate, opdId, appointmentId);
    }
}


