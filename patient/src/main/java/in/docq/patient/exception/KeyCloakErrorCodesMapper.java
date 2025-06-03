package in.docq.patient.exception;

import in.docq.keycloak.rest.client.ErrorCodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KeyCloakErrorCodesMapper {
    private final Map<ErrorCodes, in.docq.patient.exception.ErrorCodes> keyCloakErrorCodeMap;

    public KeyCloakErrorCodesMapper() {
        this.keyCloakErrorCodeMap = new HashMap<>();
        this.keyCloakErrorCodeMap.put(ErrorCodes.invalid_grant, in.docq.patient.exception.ErrorCodes.INVALID_USER_CREDENTIALS);
    }

    public in.docq.patient.exception.ErrorCodes getErrorCode(ErrorCodes keyCloakErrorCode) {
        in.docq.patient.exception.ErrorCodes errorCode = keyCloakErrorCodeMap.get(keyCloakErrorCode);
        return Optional.ofNullable(errorCode)
                .orElseThrow(() -> new RuntimeException("Key Cloak Error Code to App Error Code Mapping Absent"));
    }
}
