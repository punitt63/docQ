package in.docq.patient.exception;

import lombok.Getter;

public enum ErrorCodes {
    INVALID_USER_CREDENTIALS(400, "invalid user credentials");

    @Getter
    private String message;

    @Getter
    private int httpCode;

    ErrorCodes(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }
}
