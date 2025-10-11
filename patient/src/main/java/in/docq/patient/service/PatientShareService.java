package in.docq.patient.service;

import in.docq.abha.rest.client.AbhaRestClient;
import in.docq.abha.rest.client.model.phr.AbdmPatientShareHip1Request;
import in.docq.abha.rest.client.model.phr.PatientShare2Request;
import in.docq.abha.rest.client.model.phr.ProfileShare3200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

@Component
public class PatientShareService {

    private final AbhaRestClient abhaRestClient;

    @Autowired
    public PatientShareService(AbhaRestClient abhaRestClient) {
        this.abhaRestClient = abhaRestClient;
    }

    public CompletionStage<Void> abdmPatientShareHip1(String xCmId, String xHiuId, String xAuthToken, AbdmPatientShareHip1Request request) {
        return abhaRestClient.abdmPatientShareHip1(xCmId, xHiuId, xAuthToken, request)
                .thenApply(response -> response);
    }

    public CompletionStage<Void> profileShare2(String xHiuId, PatientShare2Request request) {
        return abhaRestClient.profileShare2(xHiuId, request)
                .thenApply(response -> response);
    }

    public CompletionStage<ProfileShare3200Response> profileShare3(String xCmId, String xAuthToken, String limit) {
        return abhaRestClient.profileShare3(xCmId, xAuthToken, limit)
                .thenApply(response -> response);
    }

}
