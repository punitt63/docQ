package in.docq.health.facility.exception;

import lombok.Getter;

@Getter
public class HealthFacilityException extends RuntimeException {
    private final ErrorCodes errorCode;
    public HealthFacilityException(ErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
