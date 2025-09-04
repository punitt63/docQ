package in.docq.abha.rest.client.api;

import com.google.gson.reflect.TypeToken;
import in.docq.abha.rest.client.*;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking2Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking4Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking6Request;
import in.docq.abha.rest.client.model.AbdmUserInitiatedLinking6RequestResponse;
import okhttp3.Call;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public class UserInitiatedLinkingHipApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public UserInitiatedLinkingHipApi() {
        this(Configuration.getDefaultApiClient());
    }

    public UserInitiatedLinkingHipApi(ApiClient apiClient) {
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

    public Call abdmUserInitiatedLinking2Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmUserInitiatedLinking2Request abdmUserInitiatedLinking2Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmUserInitiatedLinking2Request;
        String localVarPath = "/api/hiecm/user-initiated-linking/v3/patient/care-context/on-discover";
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

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", this.localVarApiClient.parameterToString(REQUEST_ID));
        }

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", this.localVarApiClient.parameterToString(TIMESTAMP));
        }

        if (X_CM_ID != null) {
            localVarHeaderParams.put("X-CM-ID", this.localVarApiClient.parameterToString(X_CM_ID));
        }

        if (X_HIU_ID != null) {
            localVarHeaderParams.put("X-HIU-ID", this.localVarApiClient.parameterToString(X_HIU_ID));
        }

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + token);
        }

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmUserInitiatedLinking2ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmUserInitiatedLinking2Request abdmUserInitiatedLinking2Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmUserInitiatedLinking2(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmUserInitiatedLinking2(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmUserInitiatedLinking2(Async)");
        } else if (X_HIU_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIU_ID' when calling abdmUserInitiatedLinking2(Async)");
        } else if (abdmUserInitiatedLinking2Request == null) {
            throw new ApiException("Missing the required parameter 'abdmUserInitiatedLinking2Request' when calling abdmUserInitiatedLinking2(Async)");
        } else {
            return this.abdmUserInitiatedLinking2Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmUserInitiatedLinking2Request, _callback);
        }
    }

    public CompletionStage<Void> userInitiatedLinkingAsync(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmUserInitiatedLinking2Request abdmUserInitiatedLinking2Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmUserInitiatedLinking2ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmUserInitiatedLinking2Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmUserInitiatedLinking4Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmUserInitiatedLinking4Request;
        String localVarPath = "/api/hiecm/user-initiated-linking/v3/link/care-context/on-init";
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

    private Call abdmUserInitiatedLinking4ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmUserInitiatedLinking4(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmUserInitiatedLinking4(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmUserInitiatedLinking4(Async)");
        } else if (abdmUserInitiatedLinking4Request == null) {
            throw new ApiException("Missing the required parameter 'abdmUserInitiatedLinking4Request' when calling abdmUserInitiatedLinking4(Async)");
        } else {
            return this.abdmUserInitiatedLinking4Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmUserInitiatedLinking4Request, _callback);
        }
    }

    public CompletionStage<Void> abdmUserInitiatedLinking4Async(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmUserInitiatedLinking4ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmUserInitiatedLinking4Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmUserInitiatedLinking6Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmUserInitiatedLinking6Request;
        String localVarPath = "/api/hiecm/user-initiated-linking/v3/link/care-context/on-confirm";
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

    private Call abdmUserInitiatedLinking6ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmUserInitiatedLinking6(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmUserInitiatedLinking6(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmUserInitiatedLinking6(Async)");
        } else if (abdmUserInitiatedLinking6Request == null) {
            throw new ApiException("Missing the required parameter 'abdmUserInitiatedLinking6Request' when calling abdmUserInitiatedLinking6(Async)");
        } else {
            return this.abdmUserInitiatedLinking6Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmUserInitiatedLinking6Request, _callback);
        }
    }

    public CompletionStage<Void> abdmUserInitiatedLinking6Async(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmUserInitiatedLinking6ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmUserInitiatedLinking6Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

}
