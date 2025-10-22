package in.docq.patient.auth;

public class AbdmAuthenticationException extends RuntimeException {
    public AbdmAuthenticationException(String message) {
        super(message);
    }
    
    public AbdmAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
