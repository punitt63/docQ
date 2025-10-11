package in.docq.health.facility.model;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ConsentHealthRecord {
    private String consentId;
    private String consentRequestId;
    private JsonObject healthRecord;
    private Status status;
    private JsonObject keyMaterial;
    private String transactionId;
    private String healthDataRequestId;
    private String hipId;
    private String hiuId;

    public enum Status {
        AWAITING_FETCH,
        REQUESTED,
        TRANSFERRED,
        FAILED
    }
}