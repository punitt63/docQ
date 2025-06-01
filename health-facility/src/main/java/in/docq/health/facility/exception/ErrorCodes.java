package in.docq.health.facility.exception;

import lombok.Getter;

public enum ErrorCodes {
    INVALID_USER_CREDENTIALS(401, "invalid user credentials"),
    APPOINTMENTS_LIMIT_BREACHED(400, "appointments limit breached"),
    ACTION_NOT_ALLOWED(400, "action not allowed"),
    APPOINTMENT_NOT_FOUND(404, "appointment not found"),
    STATE_CHANGE_NOT_ALLOWED(400, "state change not allowed"),
    INTERNAL_SERVER_ERROR(500, "internal server error");

    @Getter
    private String message;

    @Getter
    private int httpCode;

    ErrorCodes(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }
}
