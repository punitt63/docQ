package in.docq.patient.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AbdmTokenInfo {
    private final String abhaAddress;       // patient identifier
    private final String abhaNumber;        // from healthIdNumber
    private final String fullName;          // from fullName
    private final String gender;            // from gender
    private final String mobile;            // from mobile or phrMobile
    private final long expirationTime;      // from exp
    private final long issuedAt;            // from iat
    private final String originalToken;     // raw JWT
}
