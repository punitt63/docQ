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

import in.docq.abha.rest.client.model.*;
import com.google.gson.reflect.TypeToken;
import in.docq.abha.rest.client.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenefitApisApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public BenefitApisApi() {
        this(Configuration.getDefaultApiClient());
    }

    public BenefitApisApi(ApiClient apiClient) {
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
     * Build call for abhaApiV3ProfileBenefitAbhaAbhanumberGet
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple benefit programs. Each benefit program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant benefit programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaAbhanumberGetCall(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/profile/benefit/abha/{abhanumber}"
            .replace("{" + "abhanumber" + "}", localVarApiClient.escapeString(abhanumber.toString()));

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

        if (BENEFIT_NAME != null) {
            localVarHeaderParams.put("BENEFIT NAME", localVarApiClient.parameterToString(BENEFIT_NAME));
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
    private okhttp3.Call abhaApiV3ProfileBenefitAbhaAbhanumberGetValidateBeforeCall(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'abhanumber' is set
        if (abhanumber == null) {
            throw new ApiException("Missing the required parameter 'abhanumber' when calling abhaApiV3ProfileBenefitAbhaAbhanumberGet(Async)");
        }

        // verify the required parameter 'BENEFIT_NAME' is set
        if (BENEFIT_NAME == null) {
            throw new ApiException("Missing the required parameter 'BENEFIT_NAME' when calling abhaApiV3ProfileBenefitAbhaAbhanumberGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileBenefitAbhaAbhanumberGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileBenefitAbhaAbhanumberGet(Async)");
        }

        return abhaApiV3ProfileBenefitAbhaAbhanumberGetCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Retrieve the benefit details associated with a specific ABHA number
     * This API endpoint is used to retrieve the benefit details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the benefits linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple benefit programs. Each benefit program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant benefit programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response abhaApiV3ProfileBenefitAbhaAbhanumberGet(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response> localVarResp = abhaApiV3ProfileBenefitAbhaAbhanumberGetWithHttpInfo(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Retrieve the benefit details associated with a specific ABHA number
     * This API endpoint is used to retrieve the benefit details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the benefits linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple benefit programs. Each benefit program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant benefit programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response> abhaApiV3ProfileBenefitAbhaAbhanumberGetWithHttpInfo(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaAbhanumberGetValidateBeforeCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Retrieve the benefit details associated with a specific ABHA number (asynchronously)
     * This API endpoint is used to retrieve the benefit details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the benefits linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple benefit programs. Each benefit program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant benefit programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaAbhanumberGetAsync(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback<AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaAbhanumberGetValidateBeforeCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaAbhanumberGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet
     * @param abhanumber  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple Insurance programs. Each Insurance program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant Insurance programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetCall(String abhanumber, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/profile/benefit/abha/search/insurance/{abhanumber}"
            .replace("{" + "abhanumber" + "}", localVarApiClient.escapeString(abhanumber.toString()));

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


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetValidateBeforeCall(String abhanumber, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'abhanumber' is set
        if (abhanumber == null) {
            throw new ApiException("Missing the required parameter 'abhanumber' when calling abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet(Async)");
        }

        return abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetCall(abhanumber, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Retrieve the Insurance details associated with a specific ABHA number
     * This API endpoint is used to retrieve the Insurance details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the Insurance Programs linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Note: &lt;/strong&gt; Only integrators with &lt;code&gt;HidInsuranceProgram &lt;/code&gt; role can access this API
     * @param abhanumber  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple Insurance programs. Each Insurance program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant Insurance programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet(String abhanumber, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response> localVarResp = abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetWithHttpInfo(abhanumber, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Retrieve the Insurance details associated with a specific ABHA number
     * This API endpoint is used to retrieve the Insurance details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the Insurance Programs linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Note: &lt;/strong&gt; Only integrators with &lt;code&gt;HidInsuranceProgram &lt;/code&gt; role can access this API
     * @param abhanumber  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple Insurance programs. Each Insurance program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant Insurance programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response> abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetWithHttpInfo(String abhanumber, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetValidateBeforeCall(abhanumber, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Retrieve the Insurance details associated with a specific ABHA number (asynchronously)
     * This API endpoint is used to retrieve the Insurance details associated with a specific ABHA (Ayushman Bharat Health Account) number. By providing the ABHA number in the request, the API returns information about the Insurance Programs linked to that account. This includes details such as the type of benefits, eligibility, and any other relevant information associated with the ABHA number. This endpoint ensures that users can access and manage their health benefits securely and efficiently. &lt;br&gt;&lt;br&gt; &lt;strong&gt;Note: &lt;/strong&gt; Only integrators with &lt;code&gt;HidInsuranceProgram &lt;/code&gt; role can access this API
     * @param abhanumber  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This scenario occurs when an ABHA (Ayushman Bharat Health Account) is linked to one or multiple Insurance programs. Each Insurance program may offer different types of health services, financial assistance, or other healthcare-related benefits. The system ensures that the ABHA number is correctly associated with all the relevant Insurance programs, allowing the user to access and manage their benefits efficiently </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of searching for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetAsync(String abhanumber, String REQUEST_ID, String TIMESTAMP, final ApiCallback<AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGetValidateBeforeCall(abhanumber, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaSearchInsuranceAbhanumberGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This API is used to fetch state code and district code of an user by ABHA Number </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This response code indicates that the provided param is invalid. Here in this case if user provides invalid ABHA Number, then the API will return 400 response code </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of finding state code and district code for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetCall(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/profile/benefit/abha/statedistrict/{abhanumber}"
            .replace("{" + "abhanumber" + "}", localVarApiClient.escapeString(abhanumber.toString()));

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

        if (BENEFIT_NAME != null) {
            localVarHeaderParams.put("BENEFIT NAME", localVarApiClient.parameterToString(BENEFIT_NAME));
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
    private okhttp3.Call abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetValidateBeforeCall(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'abhanumber' is set
        if (abhanumber == null) {
            throw new ApiException("Missing the required parameter 'abhanumber' when calling abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet(Async)");
        }

        // verify the required parameter 'BENEFIT_NAME' is set
        if (BENEFIT_NAME == null) {
            throw new ApiException("Missing the required parameter 'BENEFIT_NAME' when calling abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet(Async)");
        }

        return abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Search State Code and District Code by ABHA Number
     * This API endpoint is used to retrieve the state code and district code of an ABHA user by providing ABHA Number. The ABHA number will be provided as a param.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This API is used to fetch state code and district code of an user by ABHA Number </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This response code indicates that the provided param is invalid. Here in this case if user provides invalid ABHA Number, then the API will return 400 response code </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of finding state code and district code for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response> localVarResp = abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetWithHttpInfo(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Search State Code and District Code by ABHA Number
     * This API endpoint is used to retrieve the state code and district code of an ABHA user by providing ABHA Number. The ABHA number will be provided as a param.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This API is used to fetch state code and district code of an user by ABHA Number </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This response code indicates that the provided param is invalid. Here in this case if user provides invalid ABHA Number, then the API will return 400 response code </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of finding state code and district code for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response> abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetWithHttpInfo(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetValidateBeforeCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Search State Code and District Code by ABHA Number (asynchronously)
     * This API endpoint is used to retrieve the state code and district code of an ABHA user by providing ABHA Number. The ABHA number will be provided as a param.
     * @param abhanumber  (required)
     * @param BENEFIT_NAME  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> This API is used to fetch state code and district code of an user by ABHA Number </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> This response code indicates that the provided param is invalid. Here in this case if user provides invalid ABHA Number, then the API will return 400 response code </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> The 404 response code indicates that the requested resource could not be found. In the context of finding state code and district code for an ABHA (Ayushman Bharat Health Account) number using the endpoint API, this error occurs when the specified ABHA number does not exist or cannot be found in the system. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetAsync(String abhanumber, String BENEFIT_NAME, String REQUEST_ID, String TIMESTAMP, final ApiCallback<AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGetValidateBeforeCall(abhanumber, BENEFIT_NAME, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitAbhaStatedistrictAbhanumberGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileBenefitLinkAndDelinkPost
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitLinkAndDelinkPostRequest &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;de-link\&quot;, \&quot;link\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-link&lt;/code&gt;, &lt;code&gt;link&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;xmlUid&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;xmlUid&lt;/code&gt;, &lt;code&gt;abha-number&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted xmlUid}},&lt;code&gt;{{encrypted abha-number}}&lt;/code&gt;&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt; Indicates that the request was processed correctly and the expected result was returned.  this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;  Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitLinkAndDelinkPostCall(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitLinkAndDelinkPostRequest abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileBenefitLinkAndDelinkPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/benefit/linkAndDelink";

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


        if (BENEFIT_NAME != null) {
            localVarHeaderParams.put("BENEFIT_NAME", localVarApiClient.parameterToString(BENEFIT_NAME));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileBenefitLinkAndDelinkPostValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitLinkAndDelinkPostRequest abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileBenefitLinkAndDelinkPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileBenefitLinkAndDelinkPost(Async)");
        }

        // verify the required parameter 'BENEFIT_NAME' is set
        if (BENEFIT_NAME == null) {
            throw new ApiException("Missing the required parameter 'BENEFIT_NAME' when calling abhaApiV3ProfileBenefitLinkAndDelinkPost(Async)");
        }

        return abhaApiV3ProfileBenefitLinkAndDelinkPostCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, _callback);

    }

    /**
     * Usecase : Benefit LINK or DELINK
     * This API is used for benefit linking and delinking.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitLinkAndDelinkPostRequest &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;de-link\&quot;, \&quot;link\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-link&lt;/code&gt;, &lt;code&gt;link&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;xmlUid&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;xmlUid&lt;/code&gt;, &lt;code&gt;abha-number&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted xmlUid}},&lt;code&gt;{{encrypted abha-number}}&lt;/code&gt;&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @return AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt; Indicates that the request was processed correctly and the expected result was returned.  this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;  Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response abhaApiV3ProfileBenefitLinkAndDelinkPost(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitLinkAndDelinkPostRequest abhaApiV3ProfileBenefitLinkAndDelinkPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response> localVarResp = abhaApiV3ProfileBenefitLinkAndDelinkPostWithHttpInfo(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitLinkAndDelinkPostRequest);
        return localVarResp.getData();
    }

    /**
     * Usecase : Benefit LINK or DELINK
     * This API is used for benefit linking and delinking.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitLinkAndDelinkPostRequest &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;de-link\&quot;, \&quot;link\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-link&lt;/code&gt;, &lt;code&gt;link&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;xmlUid&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;xmlUid&lt;/code&gt;, &lt;code&gt;abha-number&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted xmlUid}},&lt;code&gt;{{encrypted abha-number}}&lt;/code&gt;&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt; Indicates that the request was processed correctly and the expected result was returned.  this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;  Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response> abhaApiV3ProfileBenefitLinkAndDelinkPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitLinkAndDelinkPostRequest abhaApiV3ProfileBenefitLinkAndDelinkPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitLinkAndDelinkPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Usecase : Benefit LINK or DELINK (asynchronously)
     * This API is used for benefit linking and delinking.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitLinkAndDelinkPostRequest &lt;!--&lt;div&gt; &lt;table&gt; &lt;thead&gt; &lt;tr&gt; &lt;th&gt;Attributes&lt;/th&gt; &lt;th&gt;Description&lt;/th&gt; &lt;/tr&gt; &lt;/thead&gt; &lt;tbody&gt; &lt;tr&gt; &lt;td&gt;scope &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;Aadhaar/Abha/mobile&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginHint &lt;sup&gt; * required&lt;/td&gt; &lt;td&gt;Aadhaar,Abha And Mobile Number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;loginId &lt;sup&gt;* required &lt;/sup&gt;&lt;/td&gt; &lt;td&gt;encrypted mobile-number&lt;/td&gt; &lt;/tr&gt; &lt;tr&gt; &lt;td&gt;otpSystem &lt;sup&gt;* required&lt;/td&gt; &lt;td&gt;abdm/aadhaar&lt;/td&gt; &lt;/tr&gt; &lt;/tbody&gt; &lt;/table&gt; &lt;/div&gt; &lt;hr&gt; --&gt; &lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.. &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;de-link\&quot;, \&quot;link\&quot;]&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;de-link&lt;/code&gt;, &lt;code&gt;link&lt;/code&gt; etc.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;xmlUid&lt;/code&gt;&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt;&lt;code&gt;xmlUid&lt;/code&gt;, &lt;code&gt;abha-number&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; The encrypted identifier (ABHA Number, or Mobile Number) for which the OTP is being requested.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encrypted xmlUid}},&lt;code&gt;{{encrypted abha-number}}&lt;/code&gt;&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt; Indicates that the request was processed correctly and the expected result was returned.  this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;  Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitLinkAndDelinkPostAsync(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitLinkAndDelinkPostRequest abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, final ApiCallback<AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitLinkAndDelinkPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitLinkAndDelinkPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3ProfileBenefitLinkAndDelinkPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3ProfileBenefitSearchPost
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitSearchPostRequest  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt;Indicates that the request was processed correctly and the expected result was returned. this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt;Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> &lt;strong&gt;Not Found&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot find the requested resource. The server returns a 404 status code when the resource is not available at the given URL. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;strong&gt;Internal Server Error&lt;/strong&gt;&lt;br&gt;An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitSearchPostCall(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitSearchPostRequest abhaApiV3ProfileBenefitSearchPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3ProfileBenefitSearchPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/profile/benefit/search";

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


        if (BENEFIT_NAME != null) {
            localVarHeaderParams.put("BENEFIT_NAME", localVarApiClient.parameterToString(BENEFIT_NAME));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3ProfileBenefitSearchPostValidateBeforeCall(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitSearchPostRequest abhaApiV3ProfileBenefitSearchPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3ProfileBenefitSearchPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3ProfileBenefitSearchPost(Async)");
        }

        // verify the required parameter 'BENEFIT_NAME' is set
        if (BENEFIT_NAME == null) {
            throw new ApiException("Missing the required parameter 'BENEFIT_NAME' when calling abhaApiV3ProfileBenefitSearchPost(Async)");
        }

        return abhaApiV3ProfileBenefitSearchPostCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitSearchPostRequest, _callback);

    }

    /**
     * Use Case: Search for benefits associated with a user’s profile
     * This API endpoint is used to search for benefits associated with a user’s profile. It allows users to retrieve information about various benefits they are eligible for or currently enrolled in, based on their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitSearchPostRequest  (optional)
     * @return List&lt;AbhaApiV3ProfileBenefitSearchPost200ResponseInner&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt;Indicates that the request was processed correctly and the expected result was returned. this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt;Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> &lt;strong&gt;Not Found&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot find the requested resource. The server returns a 404 status code when the resource is not available at the given URL. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;strong&gt;Internal Server Error&lt;/strong&gt;&lt;br&gt;An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner> abhaApiV3ProfileBenefitSearchPost(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitSearchPostRequest abhaApiV3ProfileBenefitSearchPostRequest) throws ApiException {
        ApiResponse<List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner>> localVarResp = abhaApiV3ProfileBenefitSearchPostWithHttpInfo(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitSearchPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Search for benefits associated with a user’s profile
     * This API endpoint is used to search for benefits associated with a user’s profile. It allows users to retrieve information about various benefits they are eligible for or currently enrolled in, based on their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitSearchPostRequest  (optional)
     * @return ApiResponse&lt;List&lt;AbhaApiV3ProfileBenefitSearchPost200ResponseInner&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt;Indicates that the request was processed correctly and the expected result was returned. this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt;Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> &lt;strong&gt;Not Found&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot find the requested resource. The server returns a 404 status code when the resource is not available at the given URL. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;strong&gt;Internal Server Error&lt;/strong&gt;&lt;br&gt;An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner>> abhaApiV3ProfileBenefitSearchPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitSearchPostRequest abhaApiV3ProfileBenefitSearchPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitSearchPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitSearchPostRequest, null);
        Type localVarReturnType = new TypeToken<List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Search for benefits associated with a user’s profile (asynchronously)
     * This API endpoint is used to search for benefits associated with a user’s profile. It allows users to retrieve information about various benefits they are eligible for or currently enrolled in, based on their ABHA (Ayushman Bharat Health Account) profile.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param BENEFIT_NAME  (required)
     * @param abhaApiV3ProfileBenefitSearchPostRequest  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> &lt;strong&gt;Successful Response &lt;/strong&gt;&lt;br&gt;Indicates that the request was processed correctly and the expected result was returned. this is represented by a 200 status code, meaning the operation was successful. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> &lt;strong&gt;Bad Request&lt;/strong&gt;&lt;br&gt;Indicates that the server cannot process the request due to a client error. The server returns a 400 status code when the request is malformed, such as missing required parameters or having invalid syntax </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> &lt;strong&gt;Unauthorized&lt;/strong&gt;&lt;br&gt;Indicates that the request requires user authentication. The server returns a 401 status code when the client has not provided valid authentication credentials. </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> &lt;strong&gt;Not Found&lt;/strong&gt;&lt;br&gt; Indicates that the server cannot find the requested resource. The server returns a 404 status code when the resource is not available at the given URL. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;strong&gt;Internal Server Error&lt;/strong&gt;&lt;br&gt;An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3ProfileBenefitSearchPostAsync(String REQUEST_ID, String TIMESTAMP, String BENEFIT_NAME, AbhaApiV3ProfileBenefitSearchPostRequest abhaApiV3ProfileBenefitSearchPostRequest, final ApiCallback<List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner>> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3ProfileBenefitSearchPostValidateBeforeCall(REQUEST_ID, TIMESTAMP, BENEFIT_NAME, abhaApiV3ProfileBenefitSearchPostRequest, _callback);
        Type localVarReturnType = new TypeToken<List<AbhaApiV3ProfileBenefitSearchPost200ResponseInner>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
