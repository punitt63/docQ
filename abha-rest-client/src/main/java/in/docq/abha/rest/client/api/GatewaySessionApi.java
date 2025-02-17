/*
 * ABHA
 * It is important to standardize the process of identification of an individual across healthcare providers, to ensure that the created medical records are issued to the right individual or accessed by a Health Information User through appropriate consent. <br><br><b>API Security</b> <br> You need Authorization Token and X-HIP-ID to consume APIs. <br><br> <b>Notes:</b> <ol> <li><b>In order to have access to HealthID APIs, your clientId must have hid role in gateway. So if you want access to these APIs then please request it in your ABDM on-boarding request.</b></li> <li><b>In order to have access to Integrated Programs HealthID APIs, your clientId must have integrated_program role in gateway. So if you want access to these APIs then please request it in your ABDM on-boarding request. Also, you will need to share integrated program benefit name to be used in this case.</b></li> <li><b>When calling APIs, please ensure that Authorization header must have format as <i>Bearer {Token_Value}</i>. Please note the prefix Bearer followed by space before token value.</b></li> <li><b>Check the state and district codes from LGD directory <a href=\"https://lgdirectory.gov.in/\">here.</a></b></li> <li><b>Highlighted Changes in the API Version 3:</b> <ul> <li>Sensitive data (Data like OTP, Aadhaar Number, Password, Username etc.) have to be encrypted.</li> <li>Data is encrypted by the public certificate. The certificate can be downloaded from the <code>/v3/auth/cert</code> API under the <b>Authentication</b> tag in version 3.</li> <li>RSA Encryption to encrypt the data. Cipher Type - <b>RSA/ECB/PKCS1Padding</b>. An online tool to encrypt data is available <a href=\"https://www.devglan.com/online-tools/rsa-encryption-decryption\">here.</a></li> </ul> </li> </ol> <br> <b> <font size=\"3\">Validations Regex Patterns </font> </b> <ol><li> Mobile Number  Validation :<code>  <b>(\\\\+91|0)?[1-9][0-9]{9}</b></code> </li> <li>Date of Birth Validation : <code> <b>\\d{4}\\-(0[0-9]|1[012])\\-(0[0-9]|[12][0-9]|3[01])$</b> </code> </li> <li>Abha Address Validation: <code><b>(^[a-zA-Z0-9]+[.]?[a-zA-Z0-9]*[_]?[a-zA-Z0-9]+$)|(^[a-zA-Z0-9]+[_]?[a-zA-Z0-9]*[.]?[a-zA-Z0-9]+$)</b></code></li> <li> ABHA Number Validation: <code><b>\\d{2}-\\d{4}-\\d{4}-\\d{4}</b></code> <li> OTP Validation: <b><code>[0-9]{6}</code></b></li> <li>Password Validation: <b><code>^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$^*_-])[A-Za-z\\d!@#$%^&*_-]{8,}$</b></code></li> <li>UUID Validation: <code>^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$</code></li> <li>Email  Validation: <code>^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$ </code></li> <li>Driving License Validation: <code>^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$</code></li> <li> Transaction Id Validation: <code>^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$</code></li><br><br> </ol><b> <font size=\"3\"> Brief Description about Regex Patterns </b> <ol><li><b>Mobile Number Validation</b><ol type=\"a\"><li>Optional Country Code or Leading Zero: Matches either +91 (country code for India) or 0, or neither, at the beginning of the string.</li><li>First Digit Non-Zero: Ensures the first digit after the country code or zero is a digit from 1 to 9 (non-zero).</li><li>Phone Number Length: Validates that the phone number part is exactly 10 digits long.</li><li>For example: <code>9876543210,+911234567890 </code></li></ol></li><li><b>Date of Birth Validation</b><ol type=\"a\"><li>Year Validation: Matches exactly 4 digits. Ensures the year is a 4-digit number.</li><li>Month Validation: Matches a hyphen followed by either a digit from 0-9 (for months 01 to 09), a digit from 10 to 12 (for months 10 to 12). Ensures the month is between 01 and 12.</li><li>Day Validation: Matches a hyphen followed by either a digit from 0-9 (for days 01 to 09), a digit from 10 to 29 (for days 10 to 29), a digit from 30 to 31 (for days 30 and 31). Ensures the day is between 01 and 31.</li><li>For Example: <code>2023-04-15</code></li></ol></li></ol><ol start=\"3\"><li><b>Abha Address Validation</b><ol type=\"a\"><li>Start and End with Alphanumeric Characters: Ensures the address starts with one or more alphanumeric characters. Ensures the address ends with one or more alphanumeric characters.</li><li>Optional Period (.): Allows for an optional period (.) anywhere within the address.</li><li>Optional Underscore: Allows for an optional underscore (_) in between the alphanumeric characters.</li><li>Length Validation: Ensures the ABHA address is between 8 to 18 characters long.</li><li>For Example: <code>john.doe_123, alice_smith.456, user.name_1</code></li></ol></li></ol><ol start=\"4\"><li><b>ABHA Number Validation</b><ol type=\"a\"><li>Two Digit Prefix: Matches exactly 2 digits at the beginning.</li><li>Four Digit Groups: Matches exactly 4 digits, separated by hyphens.</li><li>Hyphen Separation: Each group of digits is separated by a hyphen.</li><li>Complete Format: Ensures the ABHA number is in the format 11-XXXX-XXXX-XXXX.</li></ol></li></ol> <ol start=\"5\"><li><b>OTP Validation</b><ol type=\"a\"><li>Digit Only: Ensures that only numeric digits (0-9) are used.</li><li>Exact Length: Ensures the OTP is exactly 6 digits long.</li><li>Complete Format: The OTP must match the format [0-9]{6}.</li><li>For Example: <code>123456, 654321, 000123</code></li></ol> <li><b>Password Validation</b><ol type=\"a\"><li>Uppercase Letter: Ensures the password contains at least one uppercase letter (A-Z).</li><li>Digit: Ensures the password contains at least one digit (0-9).</li><li>Special Character: Ensures the password contains at least one special character from <code>!@#$%^&*-</code>.</li><li>Length: Ensures the password is at least 8 characters long.</li><li>Allowed Characters: The password can contain uppercase letters, lowercase letters, digits, and the specified special characters.</li><li>Complete Format: The password must match the pattern ^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*-])[A-Za-z\\d!@#$%^&*-]{8,}$.</li><li>For Example: <code>Password1!, Secure#123, My_Pass4$ </code></li></ol></li> <li><b>UUID Validation</b><ol type=\"a\"><li><b>8</b> Hexadecimal Characters: Ensures the UUID starts with exactly 8 hexadecimal characters (0-9, a-f).</li><li><b>4</b> Hexadecimal Characters: Ensures the next segment contains exactly 4 hexadecimal characters.</li><li>Version Indicator: Ensures the next segment starts with a digit between 1 and 5, followed by 3 hexadecimal characters.</li><li>Variant Indicator: Ensures the next segment starts with a digit from 8, 9, a, or b, followed by 3 hexadecimal characters.</li><li><b>12</b> Hexadecimal Characters: Ensures the UUID ends with exactly 12 hexadecimal characters.</li><li>Hyphen Separation: Each segment is separated by a hyphen (-).</li><li>Complete Format: The UUID must match the pattern ^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$.</li><li>For Example: <code>123e4567-e89b-12d3-a456-426614174000, 550e8400-e29b-41d4-a716-446655440000</code></li></ol></li><li><b>Email Validation</b><ol type = \"a\"><li>Alphanumeric Characters: Allows letters (a-z, A-Z) and numbers (0-9), as well as underscores (_) and hyphens (-).</li><li>Dot Separator: Allows dots (.) within the local part of the email.</li><li>At Symbol: Requires an @ symbol separating the local part and the domain part.</li><li>Domain: Allows letters (a-z, A-Z), numbers (0-9), and hyphens (-).</li><li>Domain Extension: Requires a domain extension with only letters, ranging from 2 to 7 characters.</li><li>Complete Format: The email must match the pattern ^[a-zA-Z0-9_-]+(?:\\\\.[a-zA-Z0-9_-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$.</li><li>For Example: <code>user.name@example.com, username_123@domain.org</code></li></ol></li><li><b>Driving License Validation</b><ol type = \"a\"><li>Alphanumeric Characters: The license must start with letters (a-z, A-Z) or numbers (0-9).</li><li>Optional Separator: Allows for an optional hyphen (-) or space ( ) as a separator, but only one.</li><li>Continuation: After the optional separator, the license continues with letters (a-z, A-Z) or numbers (0-9).</li><li>Complete Format: The license must match the pattern ^[a-zA-Z0-9]+([-\\s]{0,1})[a-zA-Z0-9]+$.</li><li>For Example: <code>ABC123, ABC-123, ABC 123</code></li></ol></li><li><b>Transaction Id Validation</b><ol type=\"a\"><li>8 Hexadecimal Characters: Ensures the Txn Id starts with exactly 8 hexadecimal characters (0-9, a-f).</li><li>4 Hexadecimal Characters: Ensures the next segment contains exactly 4 hexadecimal characters.</li><li>Version Indicator: Ensures the next segment starts with a digit between 1 and 5, followed by 3 hexadecimal characters.</li><li>Variant Indicator: Ensures the next segment starts with a digit from 8, 9, a, or b, followed by 3 hexadecimal characters.</li><li>12 Hexadecimal Characters: Ensures the Txn Id ends with exactly 12 hexadecimal characters.</li><li>Hyphen Separation: Each segment is separated by a hyphen (-).</li><li>Complete Format: The Txn Id must match the pattern ^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$.</li><li>For Example: <code>123e4567-e89b-12d3-a456-426614174000, 550e8400-e29b-41d4-a716-446655440000</code></li></ol></li>
 *
 * The version of the OpenAPI document: 3.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package in.docq.abha.rest.client.api;

import com.google.gson.reflect.TypeToken;
import in.docq.abha.rest.client.ApiClient;
import in.docq.abha.rest.client.Configuration;

import in.docq.abha.rest.client.*;
import in.docq.abha.rest.client.model.ApiHiecmGatewayV3SessionsPost200Response;
import in.docq.abha.rest.client.model.ApiHiecmGatewayV3SessionsPostRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class GatewaySessionApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public GatewaySessionApi() {
        this(Configuration.getDefaultApiClient());
    }

    public GatewaySessionApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    /**
     * Build call for apiHiecmGatewayV3SessionsPost
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param X_CM_ID  (required)
     * @param apiHiecmGatewayV3SessionsPostRequest  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Upon successful verification of the client_id and client_secret, the session API generates a unique session token for the client. This token is used for authenticated communication in subsequent API requests. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request- Invalid Credentials: &lt;br&gt; The request failed because the provided client_id or client_secret is incorrect. Please verify your credentials and try again. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call apiHiecmGatewayV3SessionsPostCall(String TIMESTAMP, String REQUEST_ID, String X_CM_ID, ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = apiHiecmGatewayV3SessionsPostRequest;

        // create path and map variables
        String localVarPath = "/api/hiecm/gateway/v3/sessions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (X_CM_ID != null) {
            localVarHeaderParams.put("X-CM-ID", localVarApiClient.parameterToString(X_CM_ID));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call apiHiecmGatewayV3SessionsPostValidateBeforeCall(String TIMESTAMP, String REQUEST_ID, String X_CM_ID, ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling apiHiecmGatewayV3SessionsPost(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling apiHiecmGatewayV3SessionsPost(Async)");
        }

        // verify the required parameter 'X_CM_ID' is set
        if (X_CM_ID == null) {
            throw new ApiException("Missing the required parameter 'X_CM_ID' when calling apiHiecmGatewayV3SessionsPost(Async)");
        }

        return apiHiecmGatewayV3SessionsPostCall(TIMESTAMP, REQUEST_ID, X_CM_ID, apiHiecmGatewayV3SessionsPostRequest, _callback);

    }

    /**
     * Use Case: This API is used to generate access token 
     * This API endpoint is used to create an access token. The access token is essential for authenticating and authorizing subsequent API requests. The token is generated based on the provided client credentials and is used to ensure secure access to the system.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param X_CM_ID  (required)
     * @param apiHiecmGatewayV3SessionsPostRequest  (optional)
     * @return ApiHiecmGatewayV3SessionsPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Upon successful verification of the client_id and client_secret, the session API generates a unique session token for the client. This token is used for authenticated communication in subsequent API requests. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request- Invalid Credentials: &lt;br&gt; The request failed because the provided client_id or client_secret is incorrect. Please verify your credentials and try again. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiHiecmGatewayV3SessionsPost200Response apiHiecmGatewayV3SessionsPost(String TIMESTAMP, String REQUEST_ID, String X_CM_ID, ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest) throws ApiException {
        ApiResponse<ApiHiecmGatewayV3SessionsPost200Response> localVarResp = apiHiecmGatewayV3SessionsPostWithHttpInfo(TIMESTAMP, REQUEST_ID, X_CM_ID, apiHiecmGatewayV3SessionsPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: This API is used to generate access token 
     * This API endpoint is used to create an access token. The access token is essential for authenticating and authorizing subsequent API requests. The token is generated based on the provided client credentials and is used to ensure secure access to the system.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param X_CM_ID  (required)
     * @param apiHiecmGatewayV3SessionsPostRequest  (optional)
     * @return ApiResponse&lt;ApiHiecmGatewayV3SessionsPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Upon successful verification of the client_id and client_secret, the session API generates a unique session token for the client. This token is used for authenticated communication in subsequent API requests. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request- Invalid Credentials: &lt;br&gt; The request failed because the provided client_id or client_secret is incorrect. Please verify your credentials and try again. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<ApiHiecmGatewayV3SessionsPost200Response> apiHiecmGatewayV3SessionsPostWithHttpInfo(String TIMESTAMP, String REQUEST_ID, String X_CM_ID, ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest) throws ApiException {
        okhttp3.Call localVarCall = apiHiecmGatewayV3SessionsPostValidateBeforeCall(TIMESTAMP, REQUEST_ID, X_CM_ID, apiHiecmGatewayV3SessionsPostRequest, null);
        Type localVarReturnType = new TypeToken<ApiHiecmGatewayV3SessionsPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: This API is used to generate access token  (asynchronously)
     * This API endpoint is used to create an access token. The access token is essential for authenticating and authorizing subsequent API requests. The token is generated based on the provided client credentials and is used to ensure secure access to the system.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param X_CM_ID  (required)
     * @param apiHiecmGatewayV3SessionsPostRequest  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Upon successful verification of the client_id and client_secret, the session API generates a unique session token for the client. This token is used for authenticated communication in subsequent API requests. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request- Invalid Credentials: &lt;br&gt; The request failed because the provided client_id or client_secret is incorrect. Please verify your credentials and try again. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public CompletionStage<ApiHiecmGatewayV3SessionsPost200Response> apiHiecmGatewayV3SessionsPostAsync(String TIMESTAMP, String REQUEST_ID, String X_CM_ID, ApiHiecmGatewayV3SessionsPostRequest apiHiecmGatewayV3SessionsPostRequest) throws ApiException {

        okhttp3.Call localVarCall = apiHiecmGatewayV3SessionsPostValidateBeforeCall(TIMESTAMP, REQUEST_ID, X_CM_ID, apiHiecmGatewayV3SessionsPostRequest, null);
        Type localVarReturnType = new TypeToken<ApiHiecmGatewayV3SessionsPost200Response>(){}.getType();
        return localVarApiClient.executeAsync(localVarCall, localVarReturnType);
    }
}
