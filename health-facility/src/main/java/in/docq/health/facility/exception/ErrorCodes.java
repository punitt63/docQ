package in.docq.health.facility.exception;

import lombok.Getter;

public enum ErrorCodes {
    INVALID_USER_CREDENTIALS("invalid user credentials");
    @Getter
    private String message;

    ErrorCodes(String message) {
        this.message = message;
    }
}
