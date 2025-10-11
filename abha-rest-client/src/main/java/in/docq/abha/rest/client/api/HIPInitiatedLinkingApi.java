package in.docq.abha.rest.client.api;

import com.google.gson.reflect.TypeToken;
import in.docq.abha.rest.client.*;
import in.docq.abha.rest.client.model.AbdmHipInitiatedLinkingHip1Request;
import in.docq.abha.rest.client.model.HIPInitiatedGenerateTokenRequest;
import in.docq.abha.rest.client.model.SearchForFacilitiesResponse;
import in.docq.abha.rest.client.model.SendSmsNotificationRequest;
import okhttp3.Call;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public class HIPInitiatedLinkingApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public HIPInitiatedLinkingApi() {
        this(Configuration.getDefaultApiClient());
    }

    public HIPInitiatedLinkingApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return this.localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return this.localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return this.localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    public Call generateTokenCall(String token, String REQUEST_ID, String TIMESTAMP, String X_HIP_ID, String X_CM_ID, HIPInitiatedGenerateTokenRequest HIPInitiatedGenerateTokenRequest, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = HIPInitiatedGenerateTokenRequest;
        String localVarPath = "/api/hiecm/v3/token/generate-token";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[]{"application/json"};
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + token);
        }

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", this.localVarApiClient.parameterToString(REQUEST_ID));
        }

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", this.localVarApiClient.parameterToString(TIMESTAMP));
        }

        if (X_HIP_ID != null) {
            localVarHeaderParams.put("X-HIP-ID", this.localVarApiClient.parameterToString(X_HIP_ID));
        }

        if (X_CM_ID != null) {
            localVarHeaderParams.put("X-CM-ID", this.localVarApiClient.parameterToString(X_CM_ID));
        }

        String[] localVarAuthNames = new String[0];
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call generateTokenValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_HIP_ID, String X_CM_ID, HIPInitiatedGenerateTokenRequest HIPInitiatedGenerateTokenRequest, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling generateToken(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling generateToken(Async)");
        } else if (X_HIP_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIP_ID' when calling generateToken(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling generateToken(Async)");
        } else if (HIPInitiatedGenerateTokenRequest == null) {
            throw new ApiException("Missing the required parameter 'HIPInitiatedGenerateTokenRequest' when calling generateToken(Async)");
        } else {
            return this.generateTokenCall(token, REQUEST_ID, TIMESTAMP, X_HIP_ID, X_CM_ID, HIPInitiatedGenerateTokenRequest, _callback);
        }
    }

    public CompletionStage<Void> generateTokenAsync(String token, String requestId, String timestamp, String xHipId, String xCmId, HIPInitiatedGenerateTokenRequest hipInitiatedGenerateTokenRequest) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = generateTokenValidateBeforeCall(token, requestId, timestamp, xHipId, xCmId, hipInitiatedGenerateTokenRequest, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call sendSmsNotificationCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, SendSmsNotificationRequest sendSmsNotificationRequest, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = sendSmsNotificationRequest;
        String localVarPath = "/api/hiecm/hip/v3/link/patient/links/sms/notify2";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[]{"application/json"};
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", this.localVarApiClient.parameterToString(REQUEST_ID));
        }

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", this.localVarApiClient.parameterToString(TIMESTAMP));
        }

        if (X_CM_ID != null) {
            localVarHeaderParams.put("X-CM-ID", this.localVarApiClient.parameterToString(X_CM_ID));
        }

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + token);
        }

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call sendSmsNotificationValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, SendSmsNotificationRequest sendSmsNotificationRequest, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling sendSmsNotification(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling sendSmsNotification(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling sendSmsNotification(Async)");
        } else if (sendSmsNotificationRequest == null) {
            throw new ApiException("Missing the required parameter 'sendSmsNotificationRequest' when calling sendSmsNotification(Async)");
        } else {
            return this.sendSmsNotificationCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, sendSmsNotificationRequest, _callback);
        }
    }

    public CompletionStage<Void> sendSmsNotificationAsync(String token, String requestId, String timestamp, String xCmId, SendSmsNotificationRequest sendSmsNotificationRequest) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = sendSmsNotificationValidateBeforeCall(token, requestId, timestamp, xCmId, sendSmsNotificationRequest, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmHipInitiatedLinkingHip1Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIP_ID, String X_LINK_TOKEN, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmHipInitiatedLinkingHip1Request;
        String localVarPath = "/api/hiecm/hip/v3/link/carecontext";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "text/plain"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[]{"application/json"};
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if(token != null){
            localVarHeaderParams.put("Authorization", "Bearer " + token);
        }

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", this.localVarApiClient.parameterToString(REQUEST_ID));
        }

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", this.localVarApiClient.parameterToString(TIMESTAMP));
        }

        if (X_CM_ID != null) {
            localVarHeaderParams.put("X-CM-ID", this.localVarApiClient.parameterToString(X_CM_ID));
        }

        if (X_HIP_ID != null) {
            localVarHeaderParams.put("X-HIP-ID", this.localVarApiClient.parameterToString(X_HIP_ID));
        }

        if (X_LINK_TOKEN != null) {
            localVarHeaderParams.put("X-LINK-TOKEN", this.localVarApiClient.parameterToString(X_LINK_TOKEN));
        }

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmHipInitiatedLinkingHip1ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIP_ID, String X_LINK_TOKEN, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else if (X_HIP_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIP_ID' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else if (X_LINK_TOKEN == null) {
            throw new ApiException("Missing the required parameter 'X_LINK_TOKEN' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else if (abdmHipInitiatedLinkingHip1Request == null) {
            throw new ApiException("Missing the required parameter 'abdmHipInitiatedLinkingHip1Request' when calling abdmHipInitiatedLinkingHip1(Async)");
        } else {
            return this.abdmHipInitiatedLinkingHip1Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIP_ID, X_LINK_TOKEN, abdmHipInitiatedLinkingHip1Request, _callback);
        }
    }

    public CompletionStage<Void> linkCareContextAsync(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIP_ID, String X_LINK_TOKEN, AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmHipInitiatedLinkingHip1ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIP_ID, X_LINK_TOKEN, abdmHipInitiatedLinkingHip1Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
