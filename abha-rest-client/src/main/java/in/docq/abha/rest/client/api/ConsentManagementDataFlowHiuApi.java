package in.docq.abha.rest.client.api;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.docq.abha.rest.client.model.*;
import okhttp3.Call;
import okhttp3.Call;
import in.docq.abha.rest.client.*;


public class ConsentManagementDataFlowHiuApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ConsentManagementDataFlowHiuApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ConsentManagementDataFlowHiuApi(ApiClient apiClient) {
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

    public Call abdmConsentManagement1Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement1Request abdmConsentManagement1Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement1Request;
        String localVarPath = "/api/hiecm/consent/v3/request/init";
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmConsentManagement1ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement1Request abdmConsentManagement1Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement1(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement1(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement1(Async)");
        } else if (abdmConsentManagement1Request == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement1Request' when calling abdmConsentManagement1(Async)");
        } else {
            return this.abdmConsentManagement1Call(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement1Request, _callback);
        }
    }

    public void abdmConsentManagement1(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement1Request abdmConsentManagement1Request) throws ApiException {
        this.abdmConsentManagement1WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement1Request);
    }

    public ApiResponse<Void> abdmConsentManagement1WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement1Request abdmConsentManagement1Request) throws ApiException {
        Call localVarCall = this.abdmConsentManagement1ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement1Request, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmConsentManagement1Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement1Request abdmConsentManagement1Request, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmConsentManagement1ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement1Request, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmConsentManagement3Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement3Request1 abdmConsentManagement3Request1, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement3Request1;
        String localVarPath = "/api/hiecm/consent/v3/request/status";
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmConsentManagement3ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement3Request1 abdmConsentManagement3Request1, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement3(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement3(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement3(Async)");
        } else if (X_HIU_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIU_ID' when calling abdmConsentManagement3(Async)");
        } else if (abdmConsentManagement3Request1 == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement3Request1' when calling abdmConsentManagement3(Async)");
        } else {
            return this.abdmConsentManagement3Call(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement3Request1, _callback);
        }
    }

    public void abdmConsentManagement3(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement3Request1 abdmConsentManagement3Request1) throws ApiException {
        this.abdmConsentManagement3WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement3Request1);
    }

    public ApiResponse<Void> abdmConsentManagement3WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement3Request1 abdmConsentManagement3Request1) throws ApiException {
        Call localVarCall = this.abdmConsentManagement3ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement3Request1, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmConsentManagement3Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement3Request1 abdmConsentManagement3Request1, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmConsentManagement3ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement3Request1, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmConsentManagement3_0Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement3Request2 abdmConsentManagement3Request2, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement3Request2;
        String localVarPath = "/api/hiecm/consent/v3/request/hiu/on-notify";
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmConsentManagement3_0ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement3Request2 abdmConsentManagement3Request2, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement3_0(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement3_0(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement3_0(Async)");
        } else if (abdmConsentManagement3Request2 == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement3Request2' when calling abdmConsentManagement3_0(Async)");
        } else {
            return this.abdmConsentManagement3_0Call(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement3Request2, _callback);
        }
    }

    public void abdmConsentManagement3_0(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement3Request2 abdmConsentManagement3Request2) throws ApiException {
        this.abdmConsentManagement3_0WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement3Request2);
    }

    public ApiResponse<Void> abdmConsentManagement3_0WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement3Request2 abdmConsentManagement3Request2) throws ApiException {
        Call localVarCall = this.abdmConsentManagement3_0ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement3Request2, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmConsentManagement3_0Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmConsentManagement3Request2 abdmConsentManagement3Request2, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmConsentManagement3_0ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmConsentManagement3Request2, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmConsentManagement5Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement5Request1 abdmConsentManagement5Request1, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmConsentManagement5Request1;
        String localVarPath = "/api/hiecm/consent/v3/fetch";
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmConsentManagement5ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement5Request1 abdmConsentManagement5Request1, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmConsentManagement5(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmConsentManagement5(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmConsentManagement5(Async)");
        } else if (X_HIU_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIU_ID' when calling abdmConsentManagement5(Async)");
        } else if (abdmConsentManagement5Request1 == null) {
            throw new ApiException("Missing the required parameter 'abdmConsentManagement5Request1' when calling abdmConsentManagement5(Async)");
        } else {
            return this.abdmConsentManagement5Call(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement5Request1, _callback);
        }
    }

    public void abdmConsentManagement5(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement5Request1 abdmConsentManagement5Request1) throws ApiException {
        this.abdmConsentManagement5WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement5Request1);
    }

    public ApiResponse<Void> abdmConsentManagement5WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement5Request1 abdmConsentManagement5Request1) throws ApiException {
        Call localVarCall = this.abdmConsentManagement5ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement5Request1, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmConsentManagement5Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmConsentManagement5Request1 abdmConsentManagement5Request1, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmConsentManagement5ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmConsentManagement5Request1, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmDataFlow7Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmDataFlow7Request abdmDataFlow7Request, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = abdmDataFlow7Request;
        String localVarPath = "/api/hiecm/data-flow/v3/health-information/request";
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmDataFlow7ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmDataFlow7Request abdmDataFlow7Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmDataFlow7(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmDataFlow7(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmDataFlow7(Async)");
        } else if (X_HIU_ID == null) {
            throw new ApiException("Missing the required parameter 'X_HIU_ID' when calling abdmDataFlow7(Async)");
        } else if (abdmDataFlow7Request == null) {
            throw new ApiException("Missing the required parameter 'abdmDataFlow7Request' when calling abdmDataFlow7(Async)");
        } else {
            return this.abdmDataFlow7Call(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmDataFlow7Request, _callback);
        }
    }

    public void abdmDataFlow7(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmDataFlow7Request abdmDataFlow7Request) throws ApiException {
        this.abdmDataFlow7WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmDataFlow7Request);
    }

    public ApiResponse<Void> abdmDataFlow7WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmDataFlow7Request abdmDataFlow7Request) throws ApiException {
        Call localVarCall = this.abdmDataFlow7ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmDataFlow7Request, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmDataFlow7Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String X_HIU_ID, AbdmDataFlow7Request abdmDataFlow7Request, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmDataFlow7ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, X_HIU_ID, abdmDataFlow7Request, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmDataFlow8Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request, ApiCallback _callback) throws ApiException {
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmDataFlow8ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmDataFlow8(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmDataFlow8(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmDataFlow8(Async)");
        } else if (abdmDataFlow8Request == null) {
            throw new ApiException("Missing the required parameter 'abdmDataFlow8Request' when calling abdmDataFlow8(Async)");
        } else {
            return this.abdmDataFlow8Call(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request, _callback);
        }
    }

    public void abdmDataFlow8(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request) throws ApiException {
        this.abdmDataFlow8WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request);
    }

    public ApiResponse<Void> abdmDataFlow8WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request) throws ApiException {
        Call localVarCall = this.abdmDataFlow8ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request, (ApiCallback)null);
        return this.localVarApiClient.execute(localVarCall);
    }

    public Call abdmDataFlow8Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, AbdmDataFlow8Request abdmDataFlow8Request, ApiCallback<Void> _callback) throws ApiException {
        Call localVarCall = this.abdmDataFlow8ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, abdmDataFlow8Request, _callback);
        this.localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public Call abdmDataFlow9Call(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String transactionId, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;
        String localVarPath = "/api/hiecm/data-flow/v3/health-information/request/status/{transaction-id}".replace("{transaction-id}", this.localVarApiClient.escapeString(transactionId.toString()));
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

        String[] localVarContentTypes = new String[0];
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

        String[] localVarAuthNames = new String[]{"bearerAuth"};
        return this.localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call abdmDataFlow9ValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String transactionId, ApiCallback _callback) throws ApiException {
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abdmDataFlow9(Async)");
        } else if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abdmDataFlow9(Async)");
        } else if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling abdmDataFlow9(Async)");
        } else if (transactionId == null) {
            throw new ApiException("Missing the required parameter 'transactionId' when calling abdmDataFlow9(Async)");
        } else {
            return this.abdmDataFlow9Call(REQUEST_ID, TIMESTAMP, X_CM_ID, transactionId, _callback);
        }
    }

    public AbdmDataFlow9200Response abdmDataFlow9(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String transactionId) throws ApiException {
        ApiResponse<AbdmDataFlow9200Response> localVarResp = this.abdmDataFlow9WithHttpInfo(REQUEST_ID, TIMESTAMP, X_CM_ID, transactionId);
        return (AbdmDataFlow9200Response)localVarResp.getData();
    }

    public ApiResponse<AbdmDataFlow9200Response> abdmDataFlow9WithHttpInfo(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String transactionId) throws ApiException {
        Call localVarCall = this.abdmDataFlow9ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, transactionId, (ApiCallback)null);
        Type localVarReturnType = (new TypeToken<AbdmDataFlow9200Response>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call abdmDataFlow9Async(String REQUEST_ID, String TIMESTAMP, String X_CM_ID, String transactionId, ApiCallback<AbdmDataFlow9200Response> _callback) throws ApiException {
        Call localVarCall = this.abdmDataFlow9ValidateBeforeCall(REQUEST_ID, TIMESTAMP, X_CM_ID, transactionId, _callback);
        Type localVarReturnType = (new TypeToken<AbdmDataFlow9200Response>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}

