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
import in.docq.abha.rest.client.*;
import in.docq.abha.rest.client.model.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public class AbhaProfileApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public AbhaProfileApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AbhaProfileApi(ApiClient apiClient) {
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
     * Build call for abhaApiV3AccountRequestLogoutGet
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successfully logged out. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3AccountRequestLogoutGetCall(String REQUEST_ID, String TIMESTAMP, String xToken, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/abha/api/v3/account/request/logout";

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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3AccountRequestLogoutGetValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String xToken, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3AccountRequestLogoutGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3AccountRequestLogoutGet(Async)");
        }

        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3AccountRequestLogoutGet(Async)");
        }

        return abhaApiV3AccountRequestLogoutGetCall(REQUEST_ID, TIMESTAMP, xToken, _callback);

    }

    /**
     * Use Case: Logout user from their ABHA Profile
     * This API endpoint is used to log out a user from their ABHA profile. It requires valid credentials and headers for authentication. Upon successful logout, a confirmation message along with a timestamp is returned.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @return AbhaApiV3AccountRequestLogoutGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successfully logged out. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3AccountRequestLogoutGet200Response abhaApiV3AccountRequestLogoutGet(String REQUEST_ID, String TIMESTAMP, String xToken) throws ApiException {
        ApiResponse<AbhaApiV3AccountRequestLogoutGet200Response> localVarResp = abhaApiV3AccountRequestLogoutGetWithHttpInfo(REQUEST_ID, TIMESTAMP, xToken);
        return localVarResp.getData();
    }

    /**
     * Use Case: Logout user from their ABHA Profile
     * This API endpoint is used to log out a user from their ABHA profile. It requires valid credentials and headers for authentication. Upon successful logout, a confirmation message along with a timestamp is returned.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @return ApiResponse&lt;AbhaApiV3AccountRequestLogoutGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successfully logged out. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3AccountRequestLogoutGet200Response> abhaApiV3AccountRequestLogoutGetWithHttpInfo(String REQUEST_ID, String TIMESTAMP, String xToken) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3AccountRequestLogoutGetValidateBeforeCall(REQUEST_ID, TIMESTAMP, xToken, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3AccountRequestLogoutGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Logout user from their ABHA Profile (asynchronously)
     * This API endpoint is used to log out a user from their ABHA profile. It requires valid credentials and headers for authentication. Upon successful logout, a confirmation message along with a timestamp is returned.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successfully logged out. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3AccountRequestLogoutGetAsync(String REQUEST_ID, String TIMESTAMP, String xToken, final ApiCallback<AbhaApiV3AccountRequestLogoutGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3AccountRequestLogoutGetValidateBeforeCall(REQUEST_ID, TIMESTAMP, xToken, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3AccountRequestLogoutGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileAccountGet
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The account information was successfully retrieved or updated. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Indicates various errors encountered during the account management process, such as invalid identifiers or missing parameters. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Not Found </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountGetCall(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/account";

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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileAccountGetValidateBeforeCall(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3ProfileAccountGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileAccountGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileAccountGet(Async)");
        }

        return abhaApiV3ProfileAccountGetCall(xToken, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Get User Profile Details
     * This API endpoint is used to manage ABHA (Ayushman Bharat Health Account) profiles. It allows users to fetch the user profile, ensuring that their details are accurate and up-to-date. This is essential for maintaining the integrity and security of the user’s health records.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return AbhaApiV3ProfileAccountGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The account information was successfully retrieved or updated. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Indicates various errors encountered during the account management process, such as invalid identifiers or missing parameters. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Not Found </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileAccountGet200Response abhaApiV3ProfileAccountGet(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<AbhaApiV3ProfileAccountGet200Response> localVarResp = abhaApiV3ProfileAccountGetWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Get User Profile Details
     * This API endpoint is used to manage ABHA (Ayushman Bharat Health Account) profiles. It allows users to fetch the user profile, ensuring that their details are accurate and up-to-date. This is essential for maintaining the integrity and security of the user’s health records.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;AbhaApiV3ProfileAccountGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The account information was successfully retrieved or updated. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Indicates various errors encountered during the account management process, such as invalid identifiers or missing parameters. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Not Found </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileAccountGet200Response> abhaApiV3ProfileAccountGetWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountGetValidateBeforeCall(xToken, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Get User Profile Details (asynchronously)
     * This API endpoint is used to manage ABHA (Ayushman Bharat Health Account) profiles. It allows users to fetch the user profile, ensuring that their details are accurate and up-to-date. This is essential for maintaining the integrity and security of the user’s health records.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The account information was successfully retrieved or updated. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Indicates various errors encountered during the account management process, such as invalid identifiers or missing parameters. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> Not Found </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountGetAsync(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback<AbhaApiV3ProfileAccountGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileAccountGetValidateBeforeCall(xToken, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileAccountPatch
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName  (required)
     * @param abhaApiV3ProfileAccountPatchRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;div&gt; &lt;table&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Update ABHA&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt;  This API endpoint is used to update a ABHA number using their demographic details and linking their health records across multiple systems:&lt;/strong&gt; &lt;ul&gt; &lt;br&gt;&lt;strong&gt;Note:&lt;/strong&gt;  Child ABHA can also be updated using this API  &lt;br&gt;&lt;br&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;abhaNumber:&lt;/strong&gt;   The ABHA number of the child&lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;91-5553-4126-XXXX&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:(required)&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;21-2-2020&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;Name:(required)&lt;/strong&gt; The user’s  name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Mohite&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:(required)&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This error indicates that the request contains a field that is not valid for updating. In the context of updating a resource, certain fields may be restricted or immutable </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> This error indicates that the system could not find a user account associated with the provided ABHA (Ayushman Bharat Health Account) number or other identifying information. This can happen if the ABHA number is incorrect, the user does not exist in the system, or there is a mismatch in the provided details </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> This error indicates that a non-KYC (Know Your Customer) verified CHILD ABHA (Ayushman Bharat Health Account) user is permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in this error. This restriction is likely in place to ensure data integrity and prevent unauthorized changes. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountPatchCall(String TIMESTAMP, String REQUEST_ID, String benefitName, AbhaApiV3ProfileAccountPatchRequest abhaApiV3ProfileAccountPatchRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileAccountPatchRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/account";

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


        if (benefitName != null) {
            localVarHeaderParams.put("Benefit-Name", localVarApiClient.parameterToString(benefitName));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileAccountPatchValidateBeforeCall(String TIMESTAMP, String REQUEST_ID, String benefitName, AbhaApiV3ProfileAccountPatchRequest abhaApiV3ProfileAccountPatchRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileAccountPatch(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileAccountPatch(Async)");
        }

        // verify the required parameter 'benefitName' is set
        if (benefitName == null) {
            throw new ApiException("Missing the required parameter 'benefitName' when calling abhaApiV3ProfileAccountPatch(Async)");
        }

        return abhaApiV3ProfileAccountPatchCall(TIMESTAMP, REQUEST_ID, benefitName, abhaApiV3ProfileAccountPatchRequest, _callback);

    }

    /**
     * Use Case: Update the user ABHA Profile Photo , Update the Child ABHA Profile
     * This API endpoint is used to update the ABHA (Ayushman Bharat Health Account) profile. It is particularly useful for updating the profile and information of individuals. The endpoint allows users to update essential details such as the ABHA number, date of birth, name, and gender.&lt;br&gt;&lt;br&gt;Note: Non-KYC (Know Your Customer) verified ABHA users are permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in an error. This restriction ensures data integrity and prevents unauthorized changes.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName  (required)
     * @param abhaApiV3ProfileAccountPatchRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;div&gt; &lt;table&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Update ABHA&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt;  This API endpoint is used to update a ABHA number using their demographic details and linking their health records across multiple systems:&lt;/strong&gt; &lt;ul&gt; &lt;br&gt;&lt;strong&gt;Note:&lt;/strong&gt;  Child ABHA can also be updated using this API  &lt;br&gt;&lt;br&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;abhaNumber:&lt;/strong&gt;   The ABHA number of the child&lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;91-5553-4126-XXXX&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:(required)&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;21-2-2020&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;Name:(required)&lt;/strong&gt; The user’s  name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Mohite&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:(required)&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return AbhaApiV3ProfileAccountPatch200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This error indicates that the request contains a field that is not valid for updating. In the context of updating a resource, certain fields may be restricted or immutable </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> This error indicates that the system could not find a user account associated with the provided ABHA (Ayushman Bharat Health Account) number or other identifying information. This can happen if the ABHA number is incorrect, the user does not exist in the system, or there is a mismatch in the provided details </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> This error indicates that a non-KYC (Know Your Customer) verified CHILD ABHA (Ayushman Bharat Health Account) user is permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in this error. This restriction is likely in place to ensure data integrity and prevent unauthorized changes. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileAccountPatch200Response abhaApiV3ProfileAccountPatch(String TIMESTAMP, String REQUEST_ID, String benefitName, AbhaApiV3ProfileAccountPatchRequest abhaApiV3ProfileAccountPatchRequest) throws ApiException {
        ApiResponse<AbhaApiV3ProfileAccountPatch200Response> localVarResp = abhaApiV3ProfileAccountPatchWithHttpInfo(TIMESTAMP, REQUEST_ID, benefitName, abhaApiV3ProfileAccountPatchRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Update the user ABHA Profile Photo , Update the Child ABHA Profile
     * This API endpoint is used to update the ABHA (Ayushman Bharat Health Account) profile. It is particularly useful for updating the profile and information of individuals. The endpoint allows users to update essential details such as the ABHA number, date of birth, name, and gender.&lt;br&gt;&lt;br&gt;Note: Non-KYC (Know Your Customer) verified ABHA users are permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in an error. This restriction ensures data integrity and prevents unauthorized changes.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName  (required)
     * @param abhaApiV3ProfileAccountPatchRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;div&gt; &lt;table&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Update ABHA&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt;  This API endpoint is used to update a ABHA number using their demographic details and linking their health records across multiple systems:&lt;/strong&gt; &lt;ul&gt; &lt;br&gt;&lt;strong&gt;Note:&lt;/strong&gt;  Child ABHA can also be updated using this API  &lt;br&gt;&lt;br&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;abhaNumber:&lt;/strong&gt;   The ABHA number of the child&lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;91-5553-4126-XXXX&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:(required)&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;21-2-2020&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;Name:(required)&lt;/strong&gt; The user’s  name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Mohite&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:(required)&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3ProfileAccountPatch200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This error indicates that the request contains a field that is not valid for updating. In the context of updating a resource, certain fields may be restricted or immutable </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> This error indicates that the system could not find a user account associated with the provided ABHA (Ayushman Bharat Health Account) number or other identifying information. This can happen if the ABHA number is incorrect, the user does not exist in the system, or there is a mismatch in the provided details </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> This error indicates that a non-KYC (Know Your Customer) verified CHILD ABHA (Ayushman Bharat Health Account) user is permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in this error. This restriction is likely in place to ensure data integrity and prevent unauthorized changes. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileAccountPatch200Response> abhaApiV3ProfileAccountPatchWithHttpInfo(String TIMESTAMP, String REQUEST_ID, String benefitName, AbhaApiV3ProfileAccountPatchRequest abhaApiV3ProfileAccountPatchRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountPatchValidateBeforeCall(TIMESTAMP, REQUEST_ID, benefitName, abhaApiV3ProfileAccountPatchRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountPatch200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Update the user ABHA Profile Photo , Update the Child ABHA Profile (asynchronously)
     * This API endpoint is used to update the ABHA (Ayushman Bharat Health Account) profile. It is particularly useful for updating the profile and information of individuals. The endpoint allows users to update essential details such as the ABHA number, date of birth, name, and gender.&lt;br&gt;&lt;br&gt;Note: Non-KYC (Know Your Customer) verified ABHA users are permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in an error. This restriction ensures data integrity and prevents unauthorized changes.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName  (required)
     * @param abhaApiV3ProfileAccountPatchRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;div&gt; &lt;table&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Update ABHA&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt;  This API endpoint is used to update a ABHA number using their demographic details and linking their health records across multiple systems:&lt;/strong&gt; &lt;ul&gt; &lt;br&gt;&lt;strong&gt;Note:&lt;/strong&gt;  Child ABHA can also be updated using this API  &lt;br&gt;&lt;br&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;abhaNumber:&lt;/strong&gt;   The ABHA number of the child&lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;91-5553-4126-XXXX&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:(required)&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;21-2-2020&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;Name:(required)&lt;/strong&gt; The user’s  name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Mohite&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:(required)&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This error indicates that the request contains a field that is not valid for updating. In the context of updating a resource, certain fields may be restricted or immutable </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> This error indicates that the system could not find a user account associated with the provided ABHA (Ayushman Bharat Health Account) number or other identifying information. This can happen if the ABHA number is incorrect, the user does not exist in the system, or there is a mismatch in the provided details </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> This error indicates that a non-KYC (Know Your Customer) verified CHILD ABHA (Ayushman Bharat Health Account) user is permitted to update their profile only once. After the initial update, any further attempts to modify the profile will result in this error. This restriction is likely in place to ensure data integrity and prevent unauthorized changes. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountPatchAsync(String TIMESTAMP, String REQUEST_ID, String benefitName, AbhaApiV3ProfileAccountPatchRequest abhaApiV3ProfileAccountPatchRequest, final ApiCallback<AbhaApiV3ProfileAccountPatch200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileAccountPatchValidateBeforeCall(TIMESTAMP, REQUEST_ID, benefitName, abhaApiV3ProfileAccountPatchRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountPatch200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileAccountRequestEmailVerificationLinkPost
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;     &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-profile\&quot;, \&quot;email-link-verify\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;email-link-verify&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;email&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;email&lt;/code&gt;,&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted email}}&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the system used for OTP generation.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;, &lt;code&gt;aadhaar&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successful response </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountRequestEmailVerificationLinkPostCall(String REQUEST_ID, String TIMESTAMP, String xToken, AbhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/account/request/emailVerificationLink";

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

        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileAccountRequestEmailVerificationLinkPostValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String xToken, AbhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileAccountRequestEmailVerificationLinkPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileAccountRequestEmailVerificationLinkPost(Async)");
        }

        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3ProfileAccountRequestEmailVerificationLinkPost(Async)");
        }

        return abhaApiV3ProfileAccountRequestEmailVerificationLinkPostCall(REQUEST_ID, TIMESTAMP, xToken, abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, _callback);

    }

    /**
     * Use Case: This API is used to generate and send an email verification link to the user’s registered email address
     * This API endpoint is used to generate and send an email verification link to the user’s registered email address. The email verification link is essential for verifying the user’s email address and ensuring secure access to their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;     &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-profile\&quot;, \&quot;email-link-verify\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;email-link-verify&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;email&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;email&lt;/code&gt;,&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted email}}&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the system used for OTP generation.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;, &lt;code&gt;aadhaar&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successful response </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response abhaApiV3ProfileAccountRequestEmailVerificationLinkPost(String REQUEST_ID, String TIMESTAMP, String xToken, AbhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response> localVarResp = abhaApiV3ProfileAccountRequestEmailVerificationLinkPostWithHttpInfo(REQUEST_ID, TIMESTAMP, xToken, abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: This API is used to generate and send an email verification link to the user’s registered email address
     * This API endpoint is used to generate and send an email verification link to the user’s registered email address. The email verification link is essential for verifying the user’s email address and ensuring secure access to their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;     &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-profile\&quot;, \&quot;email-link-verify\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;email-link-verify&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;email&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;email&lt;/code&gt;,&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted email}}&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the system used for OTP generation.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;, &lt;code&gt;aadhaar&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successful response </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response> abhaApiV3ProfileAccountRequestEmailVerificationLinkPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, String xToken, AbhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountRequestEmailVerificationLinkPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, xToken, abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: This API is used to generate and send an email verification link to the user’s registered email address (asynchronously)
     * This API endpoint is used to generate and send an email verification link to the user’s registered email address. The email verification link is essential for verifying the user’s email address and ensuring secure access to their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param xToken  (required)
     * @param abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;     &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-profile\&quot;, \&quot;email-link-verify\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;email-link-verify&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;email&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;email&lt;/code&gt;,&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted email}}&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the system used for OTP generation.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm&lt;/code&gt;, &lt;code&gt;aadhaar&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Successful response </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountRequestEmailVerificationLinkPostAsync(String REQUEST_ID, String TIMESTAMP, String xToken, AbhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, final ApiCallback<AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileAccountRequestEmailVerificationLinkPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, xToken, abhaApiV3ProfileAccountRequestEmailVerificationLinkPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountRequestEmailVerificationLinkPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileAccountRequestOtpPost
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountRequestOtpPostRequest  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The OTP was successfully sent to the user’s registered contact method.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Positive Flow:&lt;/strong&gt; This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow:&lt;/strong&gt; This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Positive flow.&lt;/strong&gt; This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhaar OTP-Positive flow:&lt;/strong&gt; This scenario describes the successful deletion process of an ABHA (Ayushman Bharat Health Account) using an Aadhaar OTP. The user provides their Aadhaar number and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via ABHA OTP- Positive Flow.&lt;/strong&gt;This scenario describes the successful deletion process of an ABHA number using ABHA OTP and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their ABHA number. The OTP is sent to the mobile number registered with the ABHA account. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via ABHA OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their ABHA (Ayushman Bharat Health Account) number. The OTP is sent to the mobile number registered with the ABHA number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via Aadhaar OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar Number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The request was invalid. This can occur due to various reasons such as invalid login ID, invalid scope, etc. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses &lt;/strong&gt;&lt;ol start&#x3D;\&quot;1\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the email update OTP is invalid. The login ID should be the encrypted email address.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Login Hint:&lt;/strong&gt;This error occurs when the login hint provided for the email update OTP is invalid. The login hint should indicate the type of identifier being used, such as email.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid X-token.&lt;/strong&gt; This error occurs when the X-token provided for the email update OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid LoginId:&lt;/strong&gt; This error occurs when the login ID provided for the mobile update OTP is invalid. The login ID should be the encrypted mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid Login Hint.&lt;/strong&gt; This error occurs when the login hint provided for the mobile update OTP is invalid. The login hint should indicate the type of identifier being used, such as mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Already verified mobile number :&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL,the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for mobile update is invalid. The scope specifies the purpose of the OTP request, such as abha-enrol, mobile-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Already Verified Mobile Number:&lt;/strong&gt; This error occurs when the mobile number provided for the update is already verified. The system recognizes that the mobile number is already linked and verified with the user’s profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for re-KYC is invalid. The scope specifies the purpose of the OTP request, such as re-kyc, kyc-update, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid X-token:&lt;/strong&gt; This error occurs when the X-token provided for the re-KYC OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc- Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the re-KYC OTP is invalid. The login ID should be the encrypted identifier, such as Aadhaar number or mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Login Hint:&lt;/strong&gt; This error occurs when the login hint provided for the re-KYC OTP is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar or mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for updating the password using Aadhaar is invalid. The scope specifies the purpose of the OTP request, such as password-update, aadhaar-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;14\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginId:&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL, the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;15\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginHint.&lt;/strong&gt; This error occurs when the login ID provided for the password update OTP using Aadhaar is invalid. The login ID should be the encrypted Aadhaar number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;16\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to delete the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;17\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to delete the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;18\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request for deleting the ABHA using ABHA OTP is invalid. The scope specifies the purpose of the OTP request, such as abha-delete, abha-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;19\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to delete the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;20\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;21\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;22\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request to deactivate the ABHA using ABHA OTP is invalid.The scope specifies the purpose of the OTP request.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;23\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to deactivate the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error               . </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountRequestOtpPostCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileAccountRequestOtpPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/account/request/otp";

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

        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + localVarApiClient.parameterToString(token));
        }
        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileAccountRequestOtpPostValidateBeforeCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3ProfileAccountRequestOtpPost(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileAccountRequestOtpPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileAccountRequestOtpPost(Async)");
        }

        return abhaApiV3ProfileAccountRequestOtpPostCall(token, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountRequestOtpPostRequest, _callback);

    }

    /**
     * Use Case: Send OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile
     * This API endpoint is used to request an OTP (One-Time Password) for verifying an ABHA (Ayushman Bharat Health Account) profile. The OTP is sent to the user’s registered mobile number or email address, ensuring secure access to their ABHA profile. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile.&lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Update Email :** This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.  &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Update Mobile :** This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Re-KYC - Send OTP:** This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Deactivate ABHA via Aadhaar OTP - Send OTP:** This action allows users to deactivate their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Deactivate ABHA via ABHA OTP - Send OTP:**This action allows users to deactivate their ABHA by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.  &lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via ABHA OTP - Send OTP:**This action allows users to delete their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deletion of their profile. &lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **PASSWORD_REQUEST_OTP_ABDM:** This action allows users to request an OTP for password-related actions using their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **PASSWORD_UPDATE_REQUEST_OTP - AADHAAR:** This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is essential for verifying the user’s identity and ensuring secure password updates. \&quot;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.**OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountRequestOtpPostRequest  (optional)
     * @return AbhaApiV3ProfileAccountRequestOtpPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The OTP was successfully sent to the user’s registered contact method.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Positive Flow:&lt;/strong&gt; This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow:&lt;/strong&gt; This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Positive flow.&lt;/strong&gt; This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhaar OTP-Positive flow:&lt;/strong&gt; This scenario describes the successful deletion process of an ABHA (Ayushman Bharat Health Account) using an Aadhaar OTP. The user provides their Aadhaar number and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via ABHA OTP- Positive Flow.&lt;/strong&gt;This scenario describes the successful deletion process of an ABHA number using ABHA OTP and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their ABHA number. The OTP is sent to the mobile number registered with the ABHA account. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via ABHA OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their ABHA (Ayushman Bharat Health Account) number. The OTP is sent to the mobile number registered with the ABHA number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via Aadhaar OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar Number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The request was invalid. This can occur due to various reasons such as invalid login ID, invalid scope, etc. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses &lt;/strong&gt;&lt;ol start&#x3D;\&quot;1\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the email update OTP is invalid. The login ID should be the encrypted email address.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Login Hint:&lt;/strong&gt;This error occurs when the login hint provided for the email update OTP is invalid. The login hint should indicate the type of identifier being used, such as email.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid X-token.&lt;/strong&gt; This error occurs when the X-token provided for the email update OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid LoginId:&lt;/strong&gt; This error occurs when the login ID provided for the mobile update OTP is invalid. The login ID should be the encrypted mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid Login Hint.&lt;/strong&gt; This error occurs when the login hint provided for the mobile update OTP is invalid. The login hint should indicate the type of identifier being used, such as mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Already verified mobile number :&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL,the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for mobile update is invalid. The scope specifies the purpose of the OTP request, such as abha-enrol, mobile-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Already Verified Mobile Number:&lt;/strong&gt; This error occurs when the mobile number provided for the update is already verified. The system recognizes that the mobile number is already linked and verified with the user’s profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for re-KYC is invalid. The scope specifies the purpose of the OTP request, such as re-kyc, kyc-update, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid X-token:&lt;/strong&gt; This error occurs when the X-token provided for the re-KYC OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc- Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the re-KYC OTP is invalid. The login ID should be the encrypted identifier, such as Aadhaar number or mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Login Hint:&lt;/strong&gt; This error occurs when the login hint provided for the re-KYC OTP is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar or mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for updating the password using Aadhaar is invalid. The scope specifies the purpose of the OTP request, such as password-update, aadhaar-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;14\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginId:&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL, the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;15\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginHint.&lt;/strong&gt; This error occurs when the login ID provided for the password update OTP using Aadhaar is invalid. The login ID should be the encrypted Aadhaar number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;16\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to delete the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;17\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to delete the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;18\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request for deleting the ABHA using ABHA OTP is invalid. The scope specifies the purpose of the OTP request, such as abha-delete, abha-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;19\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to delete the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;20\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;21\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;22\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request to deactivate the ABHA using ABHA OTP is invalid.The scope specifies the purpose of the OTP request.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;23\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to deactivate the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error               . </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileAccountRequestOtpPost200Response abhaApiV3ProfileAccountRequestOtpPost(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3ProfileAccountRequestOtpPost200Response> localVarResp = abhaApiV3ProfileAccountRequestOtpPostWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountRequestOtpPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Send OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile
     * This API endpoint is used to request an OTP (One-Time Password) for verifying an ABHA (Ayushman Bharat Health Account) profile. The OTP is sent to the user’s registered mobile number or email address, ensuring secure access to their ABHA profile. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile.&lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Update Email :** This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.  &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Update Mobile :** This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Re-KYC - Send OTP:** This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Deactivate ABHA via Aadhaar OTP - Send OTP:** This action allows users to deactivate their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Deactivate ABHA via ABHA OTP - Send OTP:**This action allows users to deactivate their ABHA by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.  &lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via ABHA OTP - Send OTP:**This action allows users to delete their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deletion of their profile. &lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **PASSWORD_REQUEST_OTP_ABDM:** This action allows users to request an OTP for password-related actions using their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **PASSWORD_UPDATE_REQUEST_OTP - AADHAAR:** This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is essential for verifying the user’s identity and ensuring secure password updates. \&quot;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.**OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountRequestOtpPostRequest  (optional)
     * @return ApiResponse&lt;AbhaApiV3ProfileAccountRequestOtpPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The OTP was successfully sent to the user’s registered contact method.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Positive Flow:&lt;/strong&gt; This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow:&lt;/strong&gt; This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Positive flow.&lt;/strong&gt; This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhaar OTP-Positive flow:&lt;/strong&gt; This scenario describes the successful deletion process of an ABHA (Ayushman Bharat Health Account) using an Aadhaar OTP. The user provides their Aadhaar number and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via ABHA OTP- Positive Flow.&lt;/strong&gt;This scenario describes the successful deletion process of an ABHA number using ABHA OTP and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their ABHA number. The OTP is sent to the mobile number registered with the ABHA account. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via ABHA OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their ABHA (Ayushman Bharat Health Account) number. The OTP is sent to the mobile number registered with the ABHA number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via Aadhaar OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar Number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The request was invalid. This can occur due to various reasons such as invalid login ID, invalid scope, etc. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses &lt;/strong&gt;&lt;ol start&#x3D;\&quot;1\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the email update OTP is invalid. The login ID should be the encrypted email address.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Login Hint:&lt;/strong&gt;This error occurs when the login hint provided for the email update OTP is invalid. The login hint should indicate the type of identifier being used, such as email.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid X-token.&lt;/strong&gt; This error occurs when the X-token provided for the email update OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid LoginId:&lt;/strong&gt; This error occurs when the login ID provided for the mobile update OTP is invalid. The login ID should be the encrypted mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid Login Hint.&lt;/strong&gt; This error occurs when the login hint provided for the mobile update OTP is invalid. The login hint should indicate the type of identifier being used, such as mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Already verified mobile number :&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL,the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for mobile update is invalid. The scope specifies the purpose of the OTP request, such as abha-enrol, mobile-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Already Verified Mobile Number:&lt;/strong&gt; This error occurs when the mobile number provided for the update is already verified. The system recognizes that the mobile number is already linked and verified with the user’s profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for re-KYC is invalid. The scope specifies the purpose of the OTP request, such as re-kyc, kyc-update, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid X-token:&lt;/strong&gt; This error occurs when the X-token provided for the re-KYC OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc- Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the re-KYC OTP is invalid. The login ID should be the encrypted identifier, such as Aadhaar number or mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Login Hint:&lt;/strong&gt; This error occurs when the login hint provided for the re-KYC OTP is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar or mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for updating the password using Aadhaar is invalid. The scope specifies the purpose of the OTP request, such as password-update, aadhaar-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;14\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginId:&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL, the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;15\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginHint.&lt;/strong&gt; This error occurs when the login ID provided for the password update OTP using Aadhaar is invalid. The login ID should be the encrypted Aadhaar number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;16\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to delete the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;17\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to delete the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;18\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request for deleting the ABHA using ABHA OTP is invalid. The scope specifies the purpose of the OTP request, such as abha-delete, abha-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;19\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to delete the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;20\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;21\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;22\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request to deactivate the ABHA using ABHA OTP is invalid.The scope specifies the purpose of the OTP request.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;23\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to deactivate the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error               . </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileAccountRequestOtpPost200Response> abhaApiV3ProfileAccountRequestOtpPostWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountRequestOtpPostValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountRequestOtpPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountRequestOtpPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Send OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile (asynchronously)
     * This API endpoint is used to request an OTP (One-Time Password) for verifying an ABHA (Ayushman Bharat Health Account) profile. The OTP is sent to the user’s registered mobile number or email address, ensuring secure access to their ABHA profile. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile.&lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Update Email :** This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.  &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Update Mobile :** This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Re-KYC - Send OTP:** This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Deactivate ABHA via Aadhaar OTP - Send OTP:** This action allows users to deactivate their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Deactivate ABHA via ABHA OTP - Send OTP:**This action allows users to deactivate their ABHA by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deactivation of their profile.  &lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via ABHA OTP - Send OTP:**This action allows users to delete their ABHA (Ayushman Bharat Health Account) by sending an OTP to the mobile number linked with their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure deletion of their profile. &lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **PASSWORD_REQUEST_OTP_ABDM:** This action allows users to request an OTP for password-related actions using their ABHA number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **PASSWORD_UPDATE_REQUEST_OTP - AADHAAR:** This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is essential for verifying the user’s identity and ensuring secure password updates. \&quot;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.**OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountRequestOtpPostRequest  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The OTP was successfully sent to the user’s registered contact method.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Positive Flow:&lt;/strong&gt; This action allows users to update their mobile number by sending an OTP to the new mobile number. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow:&lt;/strong&gt; This action allows users to re-verify their KYC (Know Your Customer) details by sending an OTP to the registered mobile number linked with their Aadhaar. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Positive flow.&lt;/strong&gt; This action allows users to update their email address by sending an OTP to the new email address. The OTP is essential for verifying the user’s identity and ensuring secure access to their profile&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhaar OTP-Positive flow:&lt;/strong&gt; This scenario describes the successful deletion process of an ABHA (Ayushman Bharat Health Account) using an Aadhaar OTP. The user provides their Aadhaar number and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via ABHA OTP- Positive Flow.&lt;/strong&gt;This scenario describes the successful deletion process of an ABHA number using ABHA OTP and the correct OTP received on their registered mobile number. Upon successful verification, the ABHA account is deleted.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP - Positive Flow:&lt;/strong&gt; This action allows users to request an OTP for de-activating their account using their ABHA number. The OTP is sent to the mobile number registered with the ABHA account. Upon successful verification of the OTP, the user can proceed to de-activate his account.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via ABHA OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their ABHA (Ayushman Bharat Health Account) number. The OTP is sent to the mobile number registered with the ABHA number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt; Change Password via Aadhaar OTP- Positive Flow:&lt;/strong&gt;This action allows users to request an OTP for updating their password using their Aadhaar number. The OTP is sent to the mobile number registered with the Aadhaar Number. Upon successful verification of the OTP, the user can proceed to update their password.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The request was invalid. This can occur due to various reasons such as invalid login ID, invalid scope, etc. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses &lt;/strong&gt;&lt;ol start&#x3D;\&quot;1\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the email update OTP is invalid. The login ID should be the encrypted email address.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Login Hint:&lt;/strong&gt;This error occurs when the login hint provided for the email update OTP is invalid. The login hint should indicate the type of identifier being used, such as email.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid X-token.&lt;/strong&gt; This error occurs when the X-token provided for the email update OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid LoginId:&lt;/strong&gt; This error occurs when the login ID provided for the mobile update OTP is invalid. The login ID should be the encrypted mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Invalid Login Hint.&lt;/strong&gt; This error occurs when the login hint provided for the mobile update OTP is invalid. The login hint should indicate the type of identifier being used, such as mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile- Already verified mobile number :&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL,the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for mobile update is invalid. The scope specifies the purpose of the OTP request, such as abha-enrol, mobile-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Already Verified Mobile Number:&lt;/strong&gt; This error occurs when the mobile number provided for the update is already verified. The system recognizes that the mobile number is already linked and verified with the user’s profile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for re-KYC is invalid. The scope specifies the purpose of the OTP request, such as re-kyc, kyc-update, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid X-token:&lt;/strong&gt; This error occurs when the X-token provided for the re-KYC OTP is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc- Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the re-KYC OTP is invalid. The login ID should be the encrypted identifier, such as Aadhaar number or mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;ReKyc - Invalid Login Hint:&lt;/strong&gt; This error occurs when the login hint provided for the re-KYC OTP is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar or mobile.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid Scope.&lt;/strong&gt; This error occurs when the scope provided in the OTP request for updating the password using Aadhaar is invalid. The scope specifies the purpose of the OTP request, such as password-update, aadhaar-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;14\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginId:&lt;/strong&gt; This OTP is generated for authentication or verification purposes related to DL, the unique identification number issued by the Indian government&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;15\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Update_Request_OTP_Aadhaar-Invalid LoginHint.&lt;/strong&gt; This error occurs when the login ID provided for the password update OTP using Aadhaar is invalid. The login ID should be the encrypted Aadhaar number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;16\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to delete the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;17\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to delete the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;18\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request for deleting the ABHA using ABHA OTP is invalid. The scope specifies the purpose of the OTP request, such as abha-delete, abha-verify, etc.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;19\&quot;&gt; &lt;li&gt;&lt;strong&gt;Delete ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to delete the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;20\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate Abha via Aadhar OTP-Invalid LoginHint:&lt;/strong&gt; This error occurs when the login hint provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The login hint should indicate the type of identifier being used, such as Aadhaar.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;21\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid x-token.&lt;/strong&gt; This error occurs when the X-token provided for the OTP request to deactivate the ABHA using Aadhaar is invalid. The X-token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;22\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid Scope:&lt;/strong&gt; This error occurs when the scope provided in the OTP request to deactivate the ABHA using ABHA OTP is invalid.The scope specifies the purpose of the OTP request.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;23\&quot;&gt; &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhar OTP-Invalid LoginId.&lt;/strong&gt; This error occurs when the login ID provided for the OTP request to deactivate the ABHA using ABHA OTP is invalid. The login ID should be the encrypted ABHA number.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The request was unauthorized. This can occur due to invalid credentials or token. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error               . </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountRequestOtpPostAsync(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest, final ApiCallback<AbhaApiV3ProfileAccountRequestOtpPost200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileAccountRequestOtpPostValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountRequestOtpPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountRequestOtpPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3ProfileAccountRequestOtpPost200Response> abhaApiV3ProfileAccountRequestOtpPostAsyncCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountRequestOtpPostRequest abhaApiV3ProfileAccountRequestOtpPostRequest) {
        try {
            FutureApiCallBack<AbhaApiV3ProfileAccountRequestOtpPost200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3ProfileAccountRequestOtpPostValidateBeforeCall(token, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountRequestOtpPostRequest, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountRequestOtpPost200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    /**
     * Build call for abhaApiV3ProfileAccountVerifyPost
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt; &lt;br&gt;&lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\\\&quot;abha-profile\&quot;, \\\&quot;mobile-verify\\\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-activate&lt;/code&gt;,&lt;code&gt;delete&lt;/code&gt;, &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;change-password&lt;/code&gt;,&lt;code&gt;re-kyc&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt; ,&lt;code&gt;email-verify&lt;/code&gt;etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful verification of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt;   &lt;strong&gt;Types of OTP Verifications:&lt;/strong&gt;  &lt;ol&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD_VERIFY_OTP- Positive Flow : &lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful verification of the OTP (One-Time Password) for password verification purposes.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;2\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA - Verify Password:&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful verification of the password to deactivate the account.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;3\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP- Positive flow :&lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via ABHA OTP.&lt;/li&gt;  &lt;/ol&gt;  &lt;ol start &#x3D;\&quot;4\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP- Positive flow:&lt;/strong&gt; The 200  response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via Aadhaar OTP.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;5\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow :&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful completion of the Re-KYC process.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;6\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Mobile- Positive Flow: &lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the mobile number.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;7\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Email- Positive Flow:&lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the email address.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;8\&quot;&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD UPDATE-OLD PASSWORD- Positive Flow:&lt;/strong&gt;The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the password when the old password is verified.&lt;/li&gt;  &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) generation or validation process.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Auth Methods&lt;/strong&gt;: The authentication method provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Auth Methods:&lt;/strong&gt;: The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -  Invalid X-token:&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC  - Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt;  &lt;ol start&#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Auth Method&lt;/strong&gt;:  The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email-X-token expired&lt;/strong&gt;: The X-token provided for updating the email address has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid access token&lt;/strong&gt;: The access token provided for updating the mobile number is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-X-token expired&lt;/strong&gt;: The X-token provided for updating the mobile number has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid access token:&lt;/strong&gt;: The access token provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-X-token expired&lt;/strong&gt;: The X-token provided for Re-KYC has expired..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-Invalid access token&lt;/strong&gt;:  The access token provided for setting the password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-X-token expired&lt;/strong&gt;: The ABHA number provided for ABHA OTP is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-Invalid access token&lt;/strong&gt;: The access token provided for updating the password using the old password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-X-token expired&lt;/strong&gt;: The X-token provided for updating the password using the old password has expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 Unprocessable Entity response for this endpoint indicates that the server understands the content type of the request entity, and the syntax of the request entity is correct, but it was unable to process the contained instructions.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-Invalid OTP&lt;/strong&gt;: The OTP provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD VERIFY OTP-Invalid OTP-AADHAAR&lt;/strong&gt;: The OTP provided for Aadhaar verification during password verification is invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountVerifyPostCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileAccountVerifyPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/account/verify";

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

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + localVarApiClient.parameterToString(token));
        }

        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileAccountVerifyPostValidateBeforeCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3ProfileAccountVerifyPost(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileAccountVerifyPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileAccountVerifyPost(Async)");
        }

        return abhaApiV3ProfileAccountVerifyPostCall(token, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountVerifyPostRequest, _callback);

    }

    /**
     * Use Case: Verify OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile
     * This API endpoint is used to verify an OTP (One-Time Password) for various purposes such as delete ABHA account, De-activate ABHA account, ReKyc, Update Mobile,Update Email,Set Password etc. &lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Verify password to Deactivate:** This scenario involves verifying the user’s password to deactivate their ABHA account.   &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Deactivate ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to deactivate the ABHA account. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Delete ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Verify Password:** This scenario involves verifying an OTP sent to the user’s registered contact method to verify their password.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Re-KYC:** This scenario involves verifying an OTP sent to the user’s registered contact method to perform re-KYC (Know Your Customer) verification.&lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via Aadhaar OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number registered with their Aadhaar number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **Update Mobile-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new mobile number to update their mobile number in the ABHA profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **Update Email-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new email address to update their email address in the ABHA profile.&lt;br&gt; &lt;br&gt;Example9:&lt;br&gt; **Set Password:** This scenario involves setting a new password for the user’s ABHA account.&lt;br&gt; &lt;br&gt;Example10:&lt;br&gt; **Update Old Password:** This scenario involves updating the user’s password using their old password.&lt;br&gt; &lt;br&gt;Example11:&lt;br&gt; **Deactivate ABHA via Password:** This scenario involves verifying the user’s password to deactivate their ABHA account.&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.** OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt; &lt;br&gt;&lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\\\&quot;abha-profile\&quot;, \\\&quot;mobile-verify\\\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-activate&lt;/code&gt;,&lt;code&gt;delete&lt;/code&gt;, &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;change-password&lt;/code&gt;,&lt;code&gt;re-kyc&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt; ,&lt;code&gt;email-verify&lt;/code&gt;etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @return AbhaApiV3ProfileAccountVerifyPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful verification of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt;   &lt;strong&gt;Types of OTP Verifications:&lt;/strong&gt;  &lt;ol&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD_VERIFY_OTP- Positive Flow : &lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful verification of the OTP (One-Time Password) for password verification purposes.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;2\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA - Verify Password:&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful verification of the password to deactivate the account.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;3\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP- Positive flow :&lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via ABHA OTP.&lt;/li&gt;  &lt;/ol&gt;  &lt;ol start &#x3D;\&quot;4\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP- Positive flow:&lt;/strong&gt; The 200  response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via Aadhaar OTP.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;5\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow :&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful completion of the Re-KYC process.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;6\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Mobile- Positive Flow: &lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the mobile number.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;7\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Email- Positive Flow:&lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the email address.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;8\&quot;&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD UPDATE-OLD PASSWORD- Positive Flow:&lt;/strong&gt;The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the password when the old password is verified.&lt;/li&gt;  &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) generation or validation process.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Auth Methods&lt;/strong&gt;: The authentication method provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Auth Methods:&lt;/strong&gt;: The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -  Invalid X-token:&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC  - Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt;  &lt;ol start&#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Auth Method&lt;/strong&gt;:  The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email-X-token expired&lt;/strong&gt;: The X-token provided for updating the email address has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid access token&lt;/strong&gt;: The access token provided for updating the mobile number is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-X-token expired&lt;/strong&gt;: The X-token provided for updating the mobile number has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid access token:&lt;/strong&gt;: The access token provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-X-token expired&lt;/strong&gt;: The X-token provided for Re-KYC has expired..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-Invalid access token&lt;/strong&gt;:  The access token provided for setting the password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-X-token expired&lt;/strong&gt;: The ABHA number provided for ABHA OTP is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-Invalid access token&lt;/strong&gt;: The access token provided for updating the password using the old password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-X-token expired&lt;/strong&gt;: The X-token provided for updating the password using the old password has expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 Unprocessable Entity response for this endpoint indicates that the server understands the content type of the request entity, and the syntax of the request entity is correct, but it was unable to process the contained instructions.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-Invalid OTP&lt;/strong&gt;: The OTP provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD VERIFY OTP-Invalid OTP-AADHAAR&lt;/strong&gt;: The OTP provided for Aadhaar verification during password verification is invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileAccountVerifyPost200Response abhaApiV3ProfileAccountVerifyPost(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3ProfileAccountVerifyPost200Response> localVarResp = abhaApiV3ProfileAccountVerifyPostWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountVerifyPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Verify OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile
     * This API endpoint is used to verify an OTP (One-Time Password) for various purposes such as delete ABHA account, De-activate ABHA account, ReKyc, Update Mobile,Update Email,Set Password etc. &lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Verify password to Deactivate:** This scenario involves verifying the user’s password to deactivate their ABHA account.   &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Deactivate ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to deactivate the ABHA account. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Delete ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Verify Password:** This scenario involves verifying an OTP sent to the user’s registered contact method to verify their password.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Re-KYC:** This scenario involves verifying an OTP sent to the user’s registered contact method to perform re-KYC (Know Your Customer) verification.&lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via Aadhaar OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number registered with their Aadhaar number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **Update Mobile-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new mobile number to update their mobile number in the ABHA profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **Update Email-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new email address to update their email address in the ABHA profile.&lt;br&gt; &lt;br&gt;Example9:&lt;br&gt; **Set Password:** This scenario involves setting a new password for the user’s ABHA account.&lt;br&gt; &lt;br&gt;Example10:&lt;br&gt; **Update Old Password:** This scenario involves updating the user’s password using their old password.&lt;br&gt; &lt;br&gt;Example11:&lt;br&gt; **Deactivate ABHA via Password:** This scenario involves verifying the user’s password to deactivate their ABHA account.&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.** OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt; &lt;br&gt;&lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\\\&quot;abha-profile\&quot;, \\\&quot;mobile-verify\\\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-activate&lt;/code&gt;,&lt;code&gt;delete&lt;/code&gt;, &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;change-password&lt;/code&gt;,&lt;code&gt;re-kyc&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt; ,&lt;code&gt;email-verify&lt;/code&gt;etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3ProfileAccountVerifyPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful verification of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt;   &lt;strong&gt;Types of OTP Verifications:&lt;/strong&gt;  &lt;ol&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD_VERIFY_OTP- Positive Flow : &lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful verification of the OTP (One-Time Password) for password verification purposes.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;2\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA - Verify Password:&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful verification of the password to deactivate the account.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;3\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP- Positive flow :&lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via ABHA OTP.&lt;/li&gt;  &lt;/ol&gt;  &lt;ol start &#x3D;\&quot;4\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP- Positive flow:&lt;/strong&gt; The 200  response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via Aadhaar OTP.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;5\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow :&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful completion of the Re-KYC process.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;6\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Mobile- Positive Flow: &lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the mobile number.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;7\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Email- Positive Flow:&lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the email address.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;8\&quot;&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD UPDATE-OLD PASSWORD- Positive Flow:&lt;/strong&gt;The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the password when the old password is verified.&lt;/li&gt;  &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) generation or validation process.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Auth Methods&lt;/strong&gt;: The authentication method provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Auth Methods:&lt;/strong&gt;: The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -  Invalid X-token:&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC  - Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt;  &lt;ol start&#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Auth Method&lt;/strong&gt;:  The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email-X-token expired&lt;/strong&gt;: The X-token provided for updating the email address has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid access token&lt;/strong&gt;: The access token provided for updating the mobile number is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-X-token expired&lt;/strong&gt;: The X-token provided for updating the mobile number has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid access token:&lt;/strong&gt;: The access token provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-X-token expired&lt;/strong&gt;: The X-token provided for Re-KYC has expired..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-Invalid access token&lt;/strong&gt;:  The access token provided for setting the password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-X-token expired&lt;/strong&gt;: The ABHA number provided for ABHA OTP is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-Invalid access token&lt;/strong&gt;: The access token provided for updating the password using the old password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-X-token expired&lt;/strong&gt;: The X-token provided for updating the password using the old password has expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 Unprocessable Entity response for this endpoint indicates that the server understands the content type of the request entity, and the syntax of the request entity is correct, but it was unable to process the contained instructions.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-Invalid OTP&lt;/strong&gt;: The OTP provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD VERIFY OTP-Invalid OTP-AADHAAR&lt;/strong&gt;: The OTP provided for Aadhaar verification during password verification is invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileAccountVerifyPost200Response> abhaApiV3ProfileAccountVerifyPostWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountVerifyPostValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountVerifyPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountVerifyPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Verify OTP - Delete ABHA, Deactivate ABHA, ReKyc, Change Password, Update Email, Update Mobile (asynchronously)
     * This API endpoint is used to verify an OTP (One-Time Password) for various purposes such as delete ABHA account, De-activate ABHA account, ReKyc, Update Mobile,Update Email,Set Password etc. &lt;br&gt; &lt;br&gt;**Example of OTP Request**&lt;br&gt; &lt;br&gt;Example1:&lt;br&gt; **Verify password to Deactivate:** This scenario involves verifying the user’s password to deactivate their ABHA account.   &lt;br&gt; &lt;br&gt;Example2:&lt;br&gt; **Deactivate ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to deactivate the ABHA account. &lt;br&gt; &lt;br&gt;Example3:&lt;br&gt; **Delete ABHA via ABHA OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number linked to their ABHA number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example4:&lt;br&gt; **Verify Password:** This scenario involves verifying an OTP sent to the user’s registered contact method to verify their password.&lt;br&gt; &lt;br&gt;Example5:&lt;br&gt; **Re-KYC:** This scenario involves verifying an OTP sent to the user’s registered contact method to perform re-KYC (Know Your Customer) verification.&lt;br&gt; &lt;br&gt;Example6:&lt;br&gt; **Delete ABHA via Aadhaar OTP- Verify OTP:** This scenario involves verifying an OTP sent to the user’s mobile number registered with their Aadhaar number to delete the ABHA account.&lt;br&gt; &lt;br&gt;Example7:&lt;br&gt; **Update Mobile-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new mobile number to update their mobile number in the ABHA profile.&lt;br&gt; &lt;br&gt;Example8:&lt;br&gt; **Update Email-Verify OTP:** This scenario involves verifying an OTP sent to the user’s new email address to update their email address in the ABHA profile.&lt;br&gt; &lt;br&gt;Example9:&lt;br&gt; **Set Password:** This scenario involves setting a new password for the user’s ABHA account.&lt;br&gt; &lt;br&gt;Example10:&lt;br&gt; **Update Old Password:** This scenario involves updating the user’s password using their old password.&lt;br&gt; &lt;br&gt;Example11:&lt;br&gt; **Deactivate ABHA via Password:** This scenario involves verifying the user’s password to deactivate their ABHA account.&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt;&lt;br&gt; **1.** OTP will be valid for 10 minute only &lt;br&gt;&lt;br&gt;
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3ProfileAccountVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt; &lt;br&gt;&lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\\\&quot;abha-profile\&quot;, \\\&quot;mobile-verify\\\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-activate&lt;/code&gt;,&lt;code&gt;delete&lt;/code&gt;, &lt;code&gt;abha-profile&lt;/code&gt;, &lt;code&gt;change-password&lt;/code&gt;,&lt;code&gt;re-kyc&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt; ,&lt;code&gt;email-verify&lt;/code&gt;etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful verification of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt;   &lt;strong&gt;Types of OTP Verifications:&lt;/strong&gt;  &lt;ol&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD_VERIFY_OTP- Positive Flow : &lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful verification of the OTP (One-Time Password) for password verification purposes.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;2\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA - Verify Password:&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful verification of the password to deactivate the account.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;3\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via ABHA OTP- Positive flow :&lt;/strong&gt;The 200 response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via ABHA OTP.&lt;/li&gt;  &lt;/ol&gt;  &lt;ol start &#x3D;\&quot;4\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Deactivate ABHA via Aadhaar OTP- Positive flow:&lt;/strong&gt; The 200  response code indicates a successful request. In this context, it refers to the successful deactivation of the ABHA account via Aadhaar OTP.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;5\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Re-KYC- Positive Flow :&lt;/strong&gt; The 200 response code indicates a successful request. In this context, it refers to the successful completion of the Re-KYC process.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;6\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Mobile- Positive Flow: &lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the mobile number.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start &#x3D;\&quot;7\&quot;&gt;  &lt;li&gt;&lt;strong&gt;Update Email- Positive Flow:&lt;/strong&gt; The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the email address.&lt;/li&gt;  &lt;/ol&gt;   &lt;ol start&#x3D;\&quot;8\&quot;&gt;  &lt;li&gt;&lt;strong&gt;PASSWORD UPDATE-OLD PASSWORD- Positive Flow:&lt;/strong&gt;The 200 OK response code indicates a successful request. In this context, it refers to the successful update of the password when the old password is verified.&lt;/li&gt;  &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) generation or validation process.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email- Invalid Auth Methods&lt;/strong&gt;: The authentication method provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Email -Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Scope&lt;/strong&gt;: The scope of the OTP response is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile - Invalid Auth Methods:&lt;/strong&gt;: The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile -  Invalid X-token:&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid OTP Value&lt;/strong&gt;: The OTP value provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;10\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;11\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC  - Invalid Transaction Id&lt;/strong&gt;: The transaction ID provided is invalid.&lt;/li&gt; &lt;/ol&gt;  &lt;ol start&#x3D;\&quot;12\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC- Invalid Auth Method&lt;/strong&gt;:  The authentication method provided is invalid..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;13\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid X-token&lt;/strong&gt;: The X-token provided is invalid or expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Update Email-X-token expired&lt;/strong&gt;: The X-token provided for updating the email address has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-Invalid access token&lt;/strong&gt;: The access token provided for updating the mobile number is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Update Mobile-X-token expired&lt;/strong&gt;: The X-token provided for updating the mobile number has expired.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC - Invalid access token:&lt;/strong&gt;: The access token provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-X-token expired&lt;/strong&gt;: The X-token provided for Re-KYC has expired..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;6\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-Invalid access token&lt;/strong&gt;:  The access token provided for setting the password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;7\&quot;&gt; &lt;li&gt;&lt;strong&gt;Password_Set-X-token expired&lt;/strong&gt;: The ABHA number provided for ABHA OTP is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;8\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-Invalid access token&lt;/strong&gt;: The access token provided for updating the password using the old password is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;9\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD_UPDATE-OLD PASSWORD-X-token expired&lt;/strong&gt;: The X-token provided for updating the password using the old password has expired.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 Unprocessable Entity response for this endpoint indicates that the server understands the content type of the request entity, and the syntax of the request entity is correct, but it was unable to process the contained instructions.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Re-KYC-Invalid OTP&lt;/strong&gt;: The OTP provided for Re-KYC is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;PASSWORD VERIFY OTP-Invalid OTP-AADHAAR&lt;/strong&gt;: The OTP provided for Aadhaar verification during password verification is invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileAccountVerifyPostAsync(String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest, final ApiCallback<AbhaApiV3ProfileAccountVerifyPost200Response> _callback) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileAccountVerifyPostValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountVerifyPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountVerifyPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3ProfileAccountVerifyPost200Response> abhaApiV3ProfileAccountVerifyPostAsyncCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, AbhaApiV3ProfileAccountVerifyPostRequest abhaApiV3ProfileAccountVerifyPostRequest) {
        try {
            FutureApiCallBack<AbhaApiV3ProfileAccountVerifyPost200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3ProfileAccountVerifyPostValidateBeforeCall(token, xToken, REQUEST_ID, TIMESTAMP, abhaApiV3ProfileAccountVerifyPostRequest, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3ProfileAccountVerifyPost200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
