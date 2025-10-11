package in.docq.patient.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AbdmTokenInfo {
    private final String patientId;
    private final String abhaAddress;
    private final String abhaNumber;
    private final long expirationTime;
    private final String originalToken;
    
    public boolean isExpired() {
        return System.currentTimeMillis() / 1000 > expirationTime;
    }
}
