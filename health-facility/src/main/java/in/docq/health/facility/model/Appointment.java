package in.docq.health.facility.model;

import in.docq.health.facility.controller.AppointmentController;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
@Getter
public class Appointment {
    private final int id;
    private final LocalDate opdDate;
    private final String opdId;
    private final String patientId;
    private final State state;
    private final String cancellationReason;
    private final Long startTime;
    private final Long endTime;
    private final int priority;
    private static final List<Pair<State, State>> allowedStateChange = List.of(
            ImmutablePair.of(State.WAITING, State.NO_SHOW),
            ImmutablePair.of(State.NO_SHOW, State.WAITING),
            ImmutablePair.of(State.IN_PROGRESS, State.COMPLETED),
            ImmutablePair.of(State.WAITING, State.CANCELLED)
    );

    public enum State {
        WAITING(false),
        IN_PROGRESS(false),
        COMPLETED(true),
        CANCELLED(true),
        NO_SHOW(false);

        @Getter
        private boolean isTerminal;

        State(boolean isTerminal) {
            this.isTerminal = isTerminal;
        }
    }

    public boolean isStateChangeAllowed(State finalState) {
        return allowedStateChange
                .stream()
                .anyMatch(allowedStateChange -> state.equals(allowedStateChange.getKey()) && finalState.equals(allowedStateChange.getValue()));
    }

    public static class AppointmentBuilder {
        public AppointmentBuilder from(LocalDate opdDate, String opdId, AppointmentController.CreateAppointmentRequestBody createAppointmentRequestBody) {
            return this
                    .opdDate(opdDate)
                    .opdId(opdId)
                    .patientId(createAppointmentRequestBody.getPatientID())
                    .state(State.WAITING);
        }

        public AppointmentBuilder from(LocalDate opdDate, String opdId, Integer appointmentID, AppointmentController.UpdateAppointmentRequestBody updateAppointmentRequestBody) {
            return this
                    .opdDate(opdDate)
                    .opdId(opdId)
                    .id(appointmentID)
                    .state(updateAppointmentRequestBody.getState())
                    .cancellationReason(updateAppointmentRequestBody.getCancellationReason());
        }
    }
}
