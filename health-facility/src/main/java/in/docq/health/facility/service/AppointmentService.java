package in.docq.health.facility.service;

import in.docq.health.facility.controller.AppointmentController;
import in.docq.health.facility.dao.AppointmentDao;
import in.docq.health.facility.exception.ErrorCodes;
import in.docq.health.facility.exception.HealthFacilityException;
import in.docq.health.facility.model.Appointment;
import in.docq.health.facility.model.HealthProfessional;
import in.docq.health.facility.model.HealthProfessionalType;
import in.docq.health.facility.model.OPD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static com.google.common.base.Preconditions.checkState;

@Service
public class AppointmentService {
    private final OPDService opdService;
    private final AppointmentDao appointmentDao;
    private final HealthProfessionalService healthProfessionalService;

    @Autowired
    public AppointmentService(OPDService opdService, AppointmentDao appointmentDao, HealthProfessionalService healthProfessionalService) {
        this.opdService = opdService;
        this.appointmentDao = appointmentDao;
        this.healthProfessionalService = healthProfessionalService;
    }

    public CompletionStage<Appointment> createAppointment(LocalDate opdDate, String opdId, AppointmentController.CreateAppointmentRequestBody createAppointmentRequestBody) {
        Appointment appointment = Appointment.builder().from(opdDate, opdId, createAppointmentRequestBody).build();
        return opdService.get(opdDate, opdId)
                .thenCompose(opd -> appointmentDao.insert(appointment));
    }

    public CompletionStage<List<Appointment>> list(LocalDate startOpdDate, LocalDate endOpdDate, String opdId, String patientId, List<Appointment.State> states, int limit) {
        return appointmentDao.list(startOpdDate, endOpdDate, List.of(opdId), patientId, states, limit);
    }

    public CompletionStage<List<Appointment>> list(String healthFacilityID, String healthFacilityProfessionalID, LocalDate startOpdDate, LocalDate endOpdDate, String opdId, String patientId, List<Appointment.State> states, int limit) {
        return opdService.list(healthFacilityID, healthFacilityProfessionalID, startOpdDate, endOpdDate)
                        .thenCompose(opds -> appointmentDao.list(startOpdDate, endOpdDate, opds.stream().map(OPD::getId).toList(), patientId, states, limit));
    }

    public CompletionStage<Appointment> get(LocalDate opdDate, String opdID, Integer id) {
        return opdService.get(opdDate, opdID)
                .thenCompose(opd -> appointmentDao.get(opdDate, opdID, id).thenApply(appointment -> appointment.toBuilder().opd(opd).build()));
    }

    private CompletionStage<Appointment> updateAppointment(LocalDate opdDate, String opdID, Integer id, AppointmentController.UpdateAppointmentRequestBody updateAppointmentRequestBody) {
        return appointmentDao.update(opdDate, opdID, id, updateAppointmentRequestBody);
    }

    public CompletionStage<Appointment> startAppointment(HealthProfessionalType originatorType, LocalDate opdDate, String opdID, Integer id) {
        return get(opdDate, opdID, id)
                .thenCompose(appointment -> getTopAppointment(opdDate, opdID, List.of(Appointment.State.WAITING, Appointment.State.IN_PROGRESS))
                        .thenCompose(topAppointmentOptional -> {
                            if (topAppointmentOptional.isEmpty()) {
                                throw new HealthFacilityException(ErrorCodes.ACTION_NOT_ALLOWED);
                            }
                            Appointment topAppointment = topAppointmentOptional.get();
                            if (topAppointment.getId() != id) {
                                throw new HealthFacilityException(ErrorCodes.ACTION_NOT_ALLOWED);
                            }
                            return updateAppointment(opdDate, opdID, id, AppointmentController.UpdateAppointmentRequestBody.builder()
                                    .state(Appointment.State.IN_PROGRESS)
                                    .startTime(Instant.now().toEpochMilli())
                                    .priority(1000000)
                                    .build())
                                    .thenApply(updatedAppointment -> {
                                        healthProfessionalService.sendMessageToCounterPart(
                                                originatorType,
                                                appointment.getDoctor(),
                                                WsConnectionHandler.StateChangeMessage.builder()
                                                        .objectId(updatedAppointment.getUniqueId())
                                                        .objectType("APPOINTMENT")
                                                        .fromState(Appointment.State.WAITING.name())
                                                        .toState(Appointment.State.IN_PROGRESS.name())
                                                        .build());
                                        return updatedAppointment;
                                    });
                        }));
    }

    public CompletionStage<Appointment> completeAppointment(LocalDate opdDate, String opdID, Integer id) {
        return get(opdDate, opdID, id)
                .thenCompose(currentAppointment -> {
                    if(!currentAppointment.isStateChangeAllowed(Appointment.State.COMPLETED)) {
                        throw new HealthFacilityException(ErrorCodes.STATE_CHANGE_NOT_ALLOWED);
                    }
                    return updateAppointment(opdDate, opdID, id, AppointmentController.UpdateAppointmentRequestBody.builder()
                            .state(Appointment.State.COMPLETED)
                            .endTime(Instant.now().toEpochMilli())
                            .build());
                });
    }

    public CompletionStage<Appointment> cancelAppointment(LocalDate opdDate, String opdID, Integer id) {
        return get(opdDate, opdID, id)
                .thenCompose(currentAppointment -> {
                    if (!currentAppointment.isStateChangeAllowed(Appointment.State.CANCELLED)) {
                        throw new HealthFacilityException(ErrorCodes.STATE_CHANGE_NOT_ALLOWED);
                    }
                    return updateAppointment(opdDate, opdID, id, AppointmentController.UpdateAppointmentRequestBody.builder()
                            .state(Appointment.State.CANCELLED)
                            .build());
                });
    }

    public CompletionStage<Appointment> noShow(LocalDate opdDate, String opdID, Integer id) {
        return get(opdDate, opdID, id)
                .thenCompose(currentAppointment -> {
                    if(!currentAppointment.isStateChangeAllowed(Appointment.State.NO_SHOW)) {
                        throw new HealthFacilityException(ErrorCodes.STATE_CHANGE_NOT_ALLOWED);
                    }
                    return updateAppointment(opdDate, opdID, id, AppointmentController.UpdateAppointmentRequestBody.builder()
                            .state(Appointment.State.NO_SHOW)
                            .build());
                });
    }

    public CompletionStage<Appointment> noShowToWaiting(LocalDate opdDate, String opdID, Integer id) {
        return get(opdDate, opdID, id)
                .thenCompose(currentAppointment -> {
                    if(!currentAppointment.isStateChangeAllowed(Appointment.State.WAITING)) {
                        throw new HealthFacilityException(ErrorCodes.STATE_CHANGE_NOT_ALLOWED);
                    }
                    return getTopAppointment(opdDate, opdID, List.of(Appointment.State.WAITING))
                            .thenCompose(topWaitingAppointmentOptional -> updateAppointment(opdDate, opdID, id, AppointmentController.UpdateAppointmentRequestBody.builder()
                            .state(Appointment.State.WAITING)
                            .priority(topWaitingAppointmentOptional.map(topWaitingAppointment -> topWaitingAppointment.getPriority() + 1).orElse(0))
                            .build()));
                });
    }

    private CompletionStage<Optional<Appointment>> getTopAppointment(LocalDate opdDate, String opdID, List<Appointment.State> states) {
        return appointmentDao.list(opdDate, opdDate, List.of(opdID), null, states, 1)
                .thenApply(appointments -> {
                    if(!appointments.isEmpty()) {
                        return Optional.of(appointments.get(0));
                    }
                    return Optional.empty();
                });
    }
}
