package in.docq.health.facility.model;

import com.auth0.jwt.JWT;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Optional;

@Builder
@Getter
public class HIPLinkingToken {
    private final String healthFacilityId;
    private final String patientId;
    private final String lastTokenRequestAppointmentId;
    private final String lastTokenRequestId;
    private final String lastToken;

    public boolean isHipLinkTokenExpired() {
        return Optional.ofNullable(lastToken).map(token -> JWT.decode(token).getExpiresAt().before(new Date())).orElse(true);
    }
}
