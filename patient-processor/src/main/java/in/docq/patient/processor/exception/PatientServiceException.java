package in.docq.patient.processor.exception;

import lombok.Getter;

@Getter
public class PatientServiceException extends RuntimeException {
    private final ErrorCodes errorCode;
    public PatientServiceException(ErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
