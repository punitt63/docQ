//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package in.docq.abha.rest.client.api;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import com.google.gson.reflect.TypeToken;
import in.docq.abha.rest.client.model.*;
import okhttp3.Call;
import in.docq.abha.rest.client.*;

public class ConsentManagementDataFlowHipApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ConsentManagementDataFlowHipApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ConsentManagementDataFlowHipApi(ApiClient apiClient) {
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

    public Call abdmConsentManagement2Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement2Request abdmConsentManagement2Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement2Request;
        String localVarPath = "/api/hiecm/consent/v3/request/hip/on-notify";
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

    private Call abdmConsentManagement2ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement2Request abdmConsentManagement2Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement2(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement2(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement2(Async)");
        } else if (abdmConsentManagement2Request == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement2Request' when calling abdmConsentManagement2(Async)");
        } else {
            return this.abdmConsentManagement2Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement2Request, _callback);
        }
    }

    public CompletionStage<Void> abdmConsentManagement2Async(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement2Request abdmConsentManagement2Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmConsentManagement2ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement2Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmConsentManagement5Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement5Request abdmConsentManagement5Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement5Request;
        String localVarPath = "/api/hiecm/data-flow/v3/health-information/hip/on-request";
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

    private Call abdmConsentManagement5ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement5Request abdmConsentManagement5Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement5(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement5(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement5(Async)");
        } else if (abdmConsentManagement5Request == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement5Request' when calling abdmConsentManagement5(Async)");
        } else {
            return this.abdmConsentManagement5Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement5Request, _callback);
        }
    }

    public CompletionStage<Void> abdmConsentManagement5Async(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement5Request abdmConsentManagement5Request)  {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmConsentManagement5ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement5Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmDataFlow8Call(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmDataFlow8Request;
        String localVarPath = "/api/hiecm/data-flow/v3/health-information/notify";
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

    private Call abdmDataFlow8ValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmDataFlow8(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmDataFlow8(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmDataFlow8(Async)");
        } else if (abdmDataFlow8Request == null) {
            throw new ApiException("Missing the required parameter 'abdmDataFlow8Request' when calling abdmDataFlow8(Async)");
        } else {
            return this.abdmDataFlow8Call(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request, _callback);
        }
    }

    public CompletionStage<Void> abdmDataFlow8Async(String token, String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmDataFlow8ValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public Call abdmConsentManagement6Call(String token, String dataPushUrl, AbdmConsentManagement6Request abdmConsentManagement6Request, ApiCallback _callback) {
        String basePath = null;
        String localVarPath = null;
        try {
            URL url = new URL(dataPushUrl);
            basePath = url.getProtocol() + "://" + url.getHost() + (url.getPort() != -1 ? ":" + url.getPort() : "");
            localVarPath = url.getPath();
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL: " + dataPushUrl, e);
        }

        Object localVarPostBody = abdmConsentManagement6Request;
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmConsentManagement6ValidateBeforeCall(String token, String dataPushUrl, AbdmConsentManagement6Request abdmConsentManagement6Request, ApiCallback _callback) throws ApiException {
        if (abdmConsentManagement6Request == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement6Request' when calling abdmConsentManagement6(Async)");
        } else {
            return this.abdmConsentManagement6Call(token, dataPushUrl, abdmConsentManagement6Request, _callback);
        }
    }

    public CompletionStage<Void> abdmConsentManagement6Async(String token, String dataPushUrl, AbdmConsentManagement6Request abdmConsentManagement6Request) {
        try {
            FutureApiCallBack<Void> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abdmConsentManagement6ValidateBeforeCall(token, dataPushUrl, abdmConsentManagement6Request, callback);
            Type localVarReturnType = new TypeToken<Void>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
