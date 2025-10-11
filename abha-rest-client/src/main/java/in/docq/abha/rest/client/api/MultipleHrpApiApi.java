//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package in.docq.abha.rest.client.api;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import in.docq.abha.rest.client.model.*;
import okhttp3.Call;
import in.docq.abha.rest.client.*;

public class MultipleHrpApiApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public MultipleHrpApiApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MultipleHrpApiApi(ApiClient apiClient) {
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

    public Call v1BridgesSearchFacilitiesPostUsingPOSTCall(String authorization, SearchFacilitiesRequest body, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = body;
        String localVarPath = "/v1.0/facility/search-bridges-facilities";
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

        if (authorization != null) {
            localVarHeaderParams.put("Authorization", this.localVarApiClient.parameterToString(authorization));
        }

        String[] localVarAuthNames = new String[0];
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call v1BridgesSearchFacilitiesPostUsingPOSTValidateBeforeCall(String authorization, SearchFacilitiesRequest body, ApiCallback _callback) throws ApiException {
        if (authorization == null) {
            throw new ApiException("Missing the required parameter 'authorization' when calling v1BridgesSearchFacilitiesPostUsingPOST(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling v1BridgesSearchFacilitiesPostUsingPOST(Async)");
        } else {
            return this.v1BridgesSearchFacilitiesPostUsingPOSTCall(authorization, body, _callback);
        }
    }

    public FacilitiesAndBridgesSearchedResponse v1BridgesSearchFacilitiesPostUsingPOST(String authorization, SearchFacilitiesRequest body) throws ApiException {
        ApiResponse<FacilitiesAndBridgesSearchedResponse> localVarResp = this.v1BridgesSearchFacilitiesPostUsingPOSTWithHttpInfo(authorization, body);
        return (FacilitiesAndBridgesSearchedResponse)localVarResp.getData();
    }

    public ApiResponse<FacilitiesAndBridgesSearchedResponse> v1BridgesSearchFacilitiesPostUsingPOSTWithHttpInfo(String authorization, SearchFacilitiesRequest body) throws ApiException {
        Call localVarCall = this.v1BridgesSearchFacilitiesPostUsingPOSTValidateBeforeCall(authorization, body, (ApiCallback)null);
        Type localVarReturnType = (new TypeToken<FacilitiesAndBridgesSearchedResponse>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call v1BridgesSearchFacilitiesPostUsingPOSTAsync(String authorization, SearchFacilitiesRequest body, ApiCallback<FacilitiesAndBridgesSearchedResponse> _callback) throws ApiException {
        Call localVarCall = this.v1BridgesSearchFacilitiesPostUsingPOSTValidateBeforeCall(authorization, body, _callback);
        Type localVarReturnType = (new TypeToken<FacilitiesAndBridgesSearchedResponse>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public Call v1MutipleHRPAddUpdateServicesUsingPOSTCall(String authorization, BridgeAddUpdate body, ApiCallback _callback) throws ApiException {
        String basePath = null;
        String[] localBasePaths = new String[0];
        if (this.localCustomBaseUrl != null) {
            basePath = this.localCustomBaseUrl;
        } else if (localBasePaths.length > 0) {
            basePath = localBasePaths[this.localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = body;
        String localVarPath = "/v1/bridges/MutipleHRPAddUpdateServices";
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

        if (authorization != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + this.localVarApiClient.parameterToString(authorization));
        }

        String[] localVarAuthNames = new String[0];
        return this.localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call v1MutipleHRPAddUpdateServicesUsingPOSTValidateBeforeCall(String authorization, BridgeAddUpdate body, ApiCallback _callback) throws ApiException {
        if (authorization == null) {
            throw new ApiException("Missing the required parameter 'authorization' when calling v1MutipleHRPAddUpdateServicesUsingPOST(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling v1MutipleHRPAddUpdateServicesUsingPOST(Async)");
        } else {
            return this.v1MutipleHRPAddUpdateServicesUsingPOSTCall(authorization, body, _callback);
        }
    }

    public List<MultipleBridgeResponse> v1MutipleHRPAddUpdateServicesUsingPOST(String authorization, BridgeAddUpdate body) throws ApiException {
        ApiResponse<List<MultipleBridgeResponse>> localVarResp = this.v1MutipleHRPAddUpdateServicesUsingPOSTWithHttpInfo(authorization, body);
        return (List)localVarResp.getData();
    }

    public ApiResponse<List<MultipleBridgeResponse>> v1MutipleHRPAddUpdateServicesUsingPOSTWithHttpInfo(String authorization, BridgeAddUpdate body) throws ApiException {
        Call localVarCall = this.v1MutipleHRPAddUpdateServicesUsingPOSTValidateBeforeCall(authorization, body, (ApiCallback)null);
        Type localVarReturnType = (new TypeToken<List<MultipleBridgeResponse>>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public CompletionStage<MultipleBridgeResponse> v1MutipleHRPAddUpdateServicesUsingPOSTAsync(String authorization, BridgeAddUpdate body) {
        try {
            FutureApiCallBack<MultipleBridgeResponse> callBack = FutureApiCallBack.newCallback();
            Call localVarCall = this.v1MutipleHRPAddUpdateServicesUsingPOSTValidateBeforeCall(authorization, body, callBack);
            Type localVarReturnType = (new TypeToken<List<MultipleBridgeResponse>>() {
            }).getType();
            this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, callBack);
            return callBack.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
