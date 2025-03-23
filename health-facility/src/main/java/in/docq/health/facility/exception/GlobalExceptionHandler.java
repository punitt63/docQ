package in.docq.health.facility.exception;

import in.docq.keycloak.rest.client.ApiException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final KeyCloakErrorCodesMapper keyCloakErrorCodesMapper= new KeyCloakErrorCodesMapper();
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ApiException ex) {
        ErrorCodes errorCode = keyCloakErrorCodesMapper.getErrorCode(ex.getResponseBody().getErrorCode());
        return new ResponseEntity<>(new ErrorResponse(errorCode), HttpStatusCode.valueOf(ex.getCode()));
    }
}
