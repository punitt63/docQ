package in.docq.health.facility.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String errorCode;
    private String message;

    public ErrorResponse(ErrorCodes errorCodes) {
        this.errorCode = errorCodes.name();
        this.message = errorCodes.getMessage();
    }
}
