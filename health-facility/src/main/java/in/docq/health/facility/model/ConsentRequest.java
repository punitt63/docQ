package in.docq.health.facility.model;

import in.docq.abha.rest.client.model.AbdmConsentManagement1Request;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConsentRequest {
    private String requestId;
    private String consentRequestId;
    private String hiuId;
    private AbdmConsentManagement1Request request;
    private Status status;
    private String requesterId;


    public enum Status {
        REQUESTED,
        GRANTED,
        DENIED,
        REVOKED,
        EXPIRED
    }
}