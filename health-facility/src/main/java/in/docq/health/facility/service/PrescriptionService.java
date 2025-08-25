package in.docq.health.facility.service;

import in.docq.health.facility.dao.PrescriptionDAO;
import in.docq.health.facility.model.Prescription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

@Service
public class PrescriptionService {
    private final AppointmentService appointmentService;
    private final PrescriptionDAO opdPrescriptionDao;
    private final CareContextService careContextService;

    public PrescriptionService(AppointmentService appointmentService, PrescriptionDAO opdPrescriptionDao, CareContextService careContextService) {
        this.appointmentService = appointmentService;
        this.opdPrescriptionDao = opdPrescriptionDao;
        this.careContextService = careContextService;
    }

    public CompletionStage<Void> create(LocalDate opdDate, String opdID, Integer appointmentID, String content) {
        Prescription prescription = Prescription.builder()
                .opdID(opdID)
                .appointmentID(appointmentID)
                .date(opdDate)
                .content(content)
                .build();
        return appointmentService.get(opdDate, opdID, appointmentID)
                .thenCompose(appointment -> opdPrescriptionDao.insert(prescription)
                        .thenCompose(ignore -> careContextService.linkCareContext(appointment)));
    }

    public CompletionStage<Void> replace(LocalDate opdDate, String opdID, Integer appointmentID, String content) {
        Prescription prescription = Prescription.builder()
                .opdID(opdID)
                .appointmentID(appointmentID)
                .date(opdDate)
                .content(content)
                .build();
        return appointmentService.get(opdDate, opdID, appointmentID)
                .thenCompose(appointment -> opdPrescriptionDao.replace(prescription));
    }

    public CompletionStage<Prescription> get(LocalDate opdDate, String opdID, Integer appointmentID) {
        return appointmentService.get(opdDate, opdID, appointmentID)
                .thenCompose(appointment -> opdPrescriptionDao.get(opdID, appointmentID, java.sql.Date.valueOf(opdDate)));
    }
}
