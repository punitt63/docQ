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
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public class AbhaAddressVerificationApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public AbhaAddressVerificationApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AbhaAddressVerificationApi(ApiClient apiClient) {
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
     * Build call for abhaApiV3PhrWebLoginAbhaRequestOtpPost
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt;,&lt;code&gt;aadhaar-verify&lt;/code&gt; &lt;code&gt;aadhaar-face-verify&lt;/code&gt; &lt;code&gt;aadhaar-bio-verify&lt;/code&gt; &lt;code&gt;aadhaar-iris-verify&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted abha address&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Otp system &lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates that the OTP has been successfully sent to the  mobile number. This response confirms that the authentication process has been initiated, ensuring that only authorized users can access their accounts. The OTP must be entered correctly to proceed with the authentication. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;User not found:&lt;/strong&gt;failure to find user .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid abha number:&lt;/strong&gt; The provided abha address is Invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaRequestOtpPostCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/phr/web/login/abha/request/otp";

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
    private okhttp3.Call abhaApiV3PhrWebLoginAbhaRequestOtpPostValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginAbhaRequestOtpPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginAbhaRequestOtpPost(Async)");
        }

        return abhaApiV3PhrWebLoginAbhaRequestOtpPostCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, _callback);

    }

    /**
     * Use Case: Sends an OTP to the Mobile Number, Aadhaar Number, Request Biometric Authentication
     * This API is designed to facilitate secure user authentication by sending a One-Time Password (OTP) to the user’s mobile number. This ensures that only authorized users can access their accounts  &lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Mobile OTP:** Sends an OTP to the mobile number used for ABHA enrollment.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Aadhaar OTP:** Sends an OTP to the mobile number linked to the user’s Aadhaar number.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Fingerprint Authentication):** Sends Fingerprint Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Face Authentication):** Sends Face Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Iris Authentication):** Sends an Iris Authentication request.&lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt;,&lt;code&gt;aadhaar-verify&lt;/code&gt; &lt;code&gt;aadhaar-face-verify&lt;/code&gt; &lt;code&gt;aadhaar-bio-verify&lt;/code&gt; &lt;code&gt;aadhaar-iris-verify&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted abha address&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Otp system &lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt; (optional)
     * @return AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates that the OTP has been successfully sent to the  mobile number. This response confirms that the authentication process has been initiated, ensuring that only authorized users can access their accounts. The OTP must be entered correctly to proceed with the authentication. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;User not found:&lt;/strong&gt;failure to find user .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid abha number:&lt;/strong&gt; The provided abha address is Invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response abhaApiV3PhrWebLoginAbhaRequestOtpPost(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> localVarResp = abhaApiV3PhrWebLoginAbhaRequestOtpPostWithHttpInfo(REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Sends an OTP to the Mobile Number, Aadhaar Number, Request Biometric Authentication
     * This API is designed to facilitate secure user authentication by sending a One-Time Password (OTP) to the user’s mobile number. This ensures that only authorized users can access their accounts  &lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Mobile OTP:** Sends an OTP to the mobile number used for ABHA enrollment.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Aadhaar OTP:** Sends an OTP to the mobile number linked to the user’s Aadhaar number.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Fingerprint Authentication):** Sends Fingerprint Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Face Authentication):** Sends Face Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Iris Authentication):** Sends an Iris Authentication request.&lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt;,&lt;code&gt;aadhaar-verify&lt;/code&gt; &lt;code&gt;aadhaar-face-verify&lt;/code&gt; &lt;code&gt;aadhaar-bio-verify&lt;/code&gt; &lt;code&gt;aadhaar-iris-verify&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted abha address&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Otp system &lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates that the OTP has been successfully sent to the  mobile number. This response confirms that the authentication process has been initiated, ensuring that only authorized users can access their accounts. The OTP must be entered correctly to proceed with the authentication. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;User not found:&lt;/strong&gt;failure to find user .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid abha number:&lt;/strong&gt; The provided abha address is Invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> abhaApiV3PhrWebLoginAbhaRequestOtpPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaRequestOtpPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Sends an OTP to the Mobile Number, Aadhaar Number, Request Biometric Authentication (asynchronously)
     * This API is designed to facilitate secure user authentication by sending a One-Time Password (OTP) to the user’s mobile number. This ensures that only authorized users can access their accounts  &lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Mobile OTP:** Sends an OTP to the mobile number used for ABHA enrollment.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Aadhaar OTP:** Sends an OTP to the mobile number linked to the user’s Aadhaar number.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Fingerprint Authentication):** Sends Fingerprint Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Face Authentication):** Sends Face Authentication request.&lt;br&gt;&lt;br&gt; **ABHA ADDRESS Verification via Biometric (Iris Authentication):** Sends an Iris Authentication request.&lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;&lt;b&gt;Note:&lt;/b&gt; Mandatory fields can&#39;t be null.&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login&lt;/code&gt;,&lt;code&gt;mobile-verify&lt;/code&gt;,&lt;code&gt;aadhaar-verify&lt;/code&gt; &lt;code&gt;aadhaar-face-verify&lt;/code&gt; &lt;code&gt;aadhaar-bio-verify&lt;/code&gt; &lt;code&gt;aadhaar-iris-verify&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginHint&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Indicates the type of identifier being used for the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;loginId&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted abha address&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedAbhaAddress}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;otpSystem&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Otp system &lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abdm,aadhaar&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates that the OTP has been successfully sent to the  mobile number. This response confirms that the authentication process has been initiated, ensuring that only authorized users can access their accounts. The OTP must be entered correctly to proceed with the authentication. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;User not found:&lt;/strong&gt;failure to find user .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid abha number:&lt;/strong&gt; The provided abha address is Invalid.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaRequestOtpPostAsync(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, final ApiCallback<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> _callback) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaRequestOtpPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> abhaApiV3PhrWebLoginAbhaRequestOtpPostAsyncCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaRequestOtpPostRequest abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest) throws ApiException {
        try {
            FutureApiCallBack<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaRequestOtpPostValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaRequestOtpPostRequest, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaRequestOtpPost200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
    /**
     * Build call for abhaApiV3PhrWebLoginAbhaSearchPost
     *
     * @param token
     * @param REQUEST_ID                                (required)
     * @param TIMESTAMP                                 (required)
     * @param abhaApiV3PhrWebLoginAbhaSearchPostRequest (optional)
     * @param _callback                                 Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details <table border="1">
     * <caption>Response Details</caption>
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful retrieve of user details based on the ABHA address&lt;br&gt; </td><td>  -  </td></tr>
     * <tr><td> 400 </td><td> The 400 response code signifies a bad request. In this context, it means that no user was found for the provided ABHA address. </td><td>  -  </td></tr>
     * <tr><td> 401 </td><td> A 401 Unauthorized error occurs when a server receives a request without valid authentication credentials or with incorrect credentials. This error indicates that the server cannot authenticate the user, </td><td>  -  </td></tr>
     * <tr><td> 404 </td><td> A 404 Not Found error occurs when a server cannot find the requested resource. This error indicates that the server is reachable, but the specific page or resource is not available </td><td>  -  </td></tr>
     * <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     * </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaSearchPostCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3PhrWebLoginAbhaSearchPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/phr/web/login/abha/search";

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
    private okhttp3.Call abhaApiV3PhrWebLoginAbhaSearchPostValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginAbhaSearchPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginAbhaSearchPost(Async)");
        }

        return abhaApiV3PhrWebLoginAbhaSearchPostCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaSearchPostRequest, _callback);

    }

    /**
     * Use Case: Search ABHA Profile using ABHA address
     * This API endpoint is used to search for ABHA (Ayushman Bharat Health Account) profiles. It allows users to retrieve information about their ABHA profiles using  identifier ABHA address. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile &lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaSearchPostRequest  (optional)
     * @return AbhaApiV3PhrWebLoginAbhaSearchPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful retrieve of user details based on the ABHA address&lt;br&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code signifies a bad request. In this context, it means that no user was found for the provided ABHA address. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> A 401 Unauthorized error occurs when a server receives a request without valid authentication credentials or with incorrect credentials. This error indicates that the server cannot authenticate the user, </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> A 404 Not Found error occurs when a server cannot find the requested resource. This error indicates that the server is reachable, but the specific page or resource is not available </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3PhrWebLoginAbhaSearchPost200Response abhaApiV3PhrWebLoginAbhaSearchPost(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> localVarResp = abhaApiV3PhrWebLoginAbhaSearchPostWithHttpInfo(REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaSearchPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Search ABHA Profile using ABHA address
     * This API endpoint is used to search for ABHA (Ayushman Bharat Health Account) profiles. It allows users to retrieve information about their ABHA profiles using  identifier ABHA address. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile &lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaSearchPostRequest  (optional)
     * @return ApiResponse&lt;AbhaApiV3PhrWebLoginAbhaSearchPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful retrieve of user details based on the ABHA address&lt;br&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code signifies a bad request. In this context, it means that no user was found for the provided ABHA address. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> A 401 Unauthorized error occurs when a server receives a request without valid authentication credentials or with incorrect credentials. This error indicates that the server cannot authenticate the user, </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> A 404 Not Found error occurs when a server cannot find the requested resource. This error indicates that the server is reachable, but the specific page or resource is not available </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> abhaApiV3PhrWebLoginAbhaSearchPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaSearchPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaSearchPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaSearchPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Search ABHA Profile using ABHA address (asynchronously)
     * This API endpoint is used to search for ABHA (Ayushman Bharat Health Account) profiles. It allows users to retrieve information about their ABHA profiles using  identifier ABHA address. This is essential for verifying the user’s identity and ensuring secure access to their ABHA profile &lt;br&gt;
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaSearchPostRequest  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful retrieve of user details based on the ABHA address&lt;br&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code signifies a bad request. In this context, it means that no user was found for the provided ABHA address. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> A 401 Unauthorized error occurs when a server receives a request without valid authentication credentials or with incorrect credentials. This error indicates that the server cannot authenticate the user, </td><td>  -  </td></tr>
        <tr><td> 404 </td><td> A 404 Not Found error occurs when a server cannot find the requested resource. This error indicates that the server is reachable, but the specific page or resource is not available </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaSearchPostAsync(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest, final ApiCallback<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> _callback) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaSearchPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaSearchPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaSearchPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> abhaApiV3PhrWebLoginAbhaSearchPostAsyncCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaSearchPostRequest abhaApiV3PhrWebLoginAbhaSearchPostRequest) throws ApiException {
        try {
            FutureApiCallBack<AbhaApiV3PhrWebLoginAbhaSearchPost200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaSearchPostValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaSearchPostRequest, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaSearchPost200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
    /**
     * Build call for abhaApiV3PhrWebLoginAbhaVerifyPost
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login,mobile-verify,aadhaar-verify,aadhaar-bio-verify,aadhaar-face-verify,aadhaar-iris-verify&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt;&lt;ol start&#x3D;\&quot;2\&quot;&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;authData&lt;/strong&gt; (required):&lt;/p&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;authMethods:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the auth method of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;otp\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;bio\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;face\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;iris\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;otp&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is Otp:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;otpValue:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted otp value.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; &lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is face:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;faceAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be faceAuthPid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;fingerprintAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be finger print auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/li&gt; &lt;/ol&gt; &lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;irisAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be iris  auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, the entered OTP was verified, confirming the user’s identity and  the account was  accessed by verified user.&lt;br&gt;&lt;br&gt; &lt;li&gt;&lt;strong&gt;ABHA Address Verification via Mobile OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Abha address. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Aadhaar OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Aadhaar. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Biometric :&lt;/strong&gt; This action allows users to verify their ABHA address using Biometric Authentication modes(Fingerprint, Face, Iris) . This ensures that the user’s ABHA address is securely verified.&lt;/li&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Invalid Scope::&lt;/strong&gt;The scope of the OTP response is invalid .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Auth Methods:&lt;/strong&gt; The authentication methods used for OTP verification are invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Transaction Id:&lt;/strong&gt; The transaction ID provided for OTP verification is invalid.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Biometric Data did not match:&lt;/strong&gt; The Biometric data entered in the form of pid did not match with your profile.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaVerifyPostCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3PhrWebLoginAbhaVerifyPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/phr/web/login/abha/verify";

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

        if (token != null) {
            localVarHeaderParams.put("Authorization", "Bearer " + localVarApiClient.parameterToString(token));
        }

        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3PhrWebLoginAbhaVerifyPostValidateBeforeCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginAbhaVerifyPost(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginAbhaVerifyPost(Async)");
        }

        return abhaApiV3PhrWebLoginAbhaVerifyPostCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaVerifyPostRequest, _callback);

    }

    /**
     * Use Case: Verify OTP - Aadhaar Number, Mobile Number, Verify via Biometric
     * This API endpoint is designed to verify a One-Time Password (OTP) for user authentication, enabling secure access to user profiles. The OTP verification process ensures that only authorized users can log into their profiles, thereby enhancing security.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login,mobile-verify,aadhaar-verify,aadhaar-bio-verify,aadhaar-face-verify,aadhaar-iris-verify&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt;&lt;ol start&#x3D;\&quot;2\&quot;&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;authData&lt;/strong&gt; (required):&lt;/p&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;authMethods:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the auth method of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;otp\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;bio\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;face\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;iris\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;otp&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is Otp:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;otpValue:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted otp value.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; &lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is face:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;faceAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be faceAuthPid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;fingerprintAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be finger print auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/li&gt; &lt;/ol&gt; &lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;irisAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be iris  auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; (optional)
     * @return AbhaApiV3PhrWebLoginAbhaVerifyPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, the entered OTP was verified, confirming the user’s identity and  the account was  accessed by verified user.&lt;br&gt;&lt;br&gt; &lt;li&gt;&lt;strong&gt;ABHA Address Verification via Mobile OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Abha address. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Aadhaar OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Aadhaar. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Biometric :&lt;/strong&gt; This action allows users to verify their ABHA address using Biometric Authentication modes(Fingerprint, Face, Iris) . This ensures that the user’s ABHA address is securely verified.&lt;/li&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Invalid Scope::&lt;/strong&gt;The scope of the OTP response is invalid .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Auth Methods:&lt;/strong&gt; The authentication methods used for OTP verification are invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Transaction Id:&lt;/strong&gt; The transaction ID provided for OTP verification is invalid.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Biometric Data did not match:&lt;/strong&gt; The Biometric data entered in the form of pid did not match with your profile.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3PhrWebLoginAbhaVerifyPost200Response abhaApiV3PhrWebLoginAbhaVerifyPost(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> localVarResp = abhaApiV3PhrWebLoginAbhaVerifyPostWithHttpInfo(REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaVerifyPostRequest);
        return localVarResp.getData();
    }

    /**
     * Use Case: Verify OTP - Aadhaar Number, Mobile Number, Verify via Biometric
     * This API endpoint is designed to verify a One-Time Password (OTP) for user authentication, enabling secure access to user profiles. The OTP verification process ensures that only authorized users can log into their profiles, thereby enhancing security.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login,mobile-verify,aadhaar-verify,aadhaar-bio-verify,aadhaar-face-verify,aadhaar-iris-verify&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt;&lt;ol start&#x3D;\&quot;2\&quot;&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;authData&lt;/strong&gt; (required):&lt;/p&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;authMethods:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the auth method of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;otp\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;bio\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;face\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;iris\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;otp&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is Otp:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;otpValue:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted otp value.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; &lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is face:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;faceAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be faceAuthPid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;fingerprintAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be finger print auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/li&gt; &lt;/ol&gt; &lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;irisAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be iris  auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3PhrWebLoginAbhaVerifyPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, the entered OTP was verified, confirming the user’s identity and  the account was  accessed by verified user.&lt;br&gt;&lt;br&gt; &lt;li&gt;&lt;strong&gt;ABHA Address Verification via Mobile OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Abha address. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Aadhaar OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Aadhaar. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Biometric :&lt;/strong&gt; This action allows users to verify their ABHA address using Biometric Authentication modes(Fingerprint, Face, Iris) . This ensures that the user’s ABHA address is securely verified.&lt;/li&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Invalid Scope::&lt;/strong&gt;The scope of the OTP response is invalid .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Auth Methods:&lt;/strong&gt; The authentication methods used for OTP verification are invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Transaction Id:&lt;/strong&gt; The transaction ID provided for OTP verification is invalid.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Biometric Data did not match:&lt;/strong&gt; The Biometric data entered in the form of pid did not match with your profile.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> abhaApiV3PhrWebLoginAbhaVerifyPostWithHttpInfo(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaVerifyPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaVerifyPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Verify OTP - Aadhaar Number, Mobile Number, Verify via Biometric (asynchronously)
     * This API endpoint is designed to verify a One-Time Password (OTP) for user authentication, enabling secure access to user profiles. The OTP verification process ensures that only authorized users can log into their profiles, thereby enhancing security.
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @param abhaApiV3PhrWebLoginAbhaVerifyPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;ol&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;scope&lt;/strong&gt; (required):&lt;/p&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the scope of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;abha-address-login\&quot;,\&quot;mobile-verify\&quot;,,\&quot;aadhaar-verify\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;abha-address-login,mobile-verify,aadhaar-verify,aadhaar-bio-verify,aadhaar-face-verify,aadhaar-iris-verify&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ol&gt;&lt;ol start&#x3D;\&quot;2\&quot;&gt;&lt;li&gt;&lt;p&gt;&lt;strong&gt;authData&lt;/strong&gt; (required):&lt;/p&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;authMethods:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Specifies the auth method of the OTP request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;[\&quot;otp\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;bio\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;face\&quot;]&lt;/code&gt;,&lt;code&gt;[\&quot;iris\&quot;]&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;otp&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is Otp:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;otpValue:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Encrypted otp value.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{encryptedOtpValue}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; &lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is face:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;faceAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be faceAuthPid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{faceAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/ol&gt;&lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;fingerprintAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be finger print auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{fingerprintAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/li&gt;&lt;/li&gt; &lt;/ol&gt; &lt;li&gt;&lt;strong&gt;Incase authMethod is bio:&lt;/strong&gt;&lt;ol&gt;&lt;li&gt;&lt;strong&gt;txnId:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Transaction id of the request.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{txnId}}&lt;/code&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;irisAuthPid:&lt;/strong&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; Value for this would be iris  auth pid.&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;Possible Values:&lt;/strong&gt; &lt;code&gt;{{irisAuthPid}}&lt;/code&gt;.&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, the entered OTP was verified, confirming the user’s identity and  the account was  accessed by verified user.&lt;br&gt;&lt;br&gt; &lt;li&gt;&lt;strong&gt;ABHA Address Verification via Mobile OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Abha address. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Aadhaar OTP:&lt;/strong&gt; This action allows users to verify their ABHA address using an OTP sent to the mobile number registered with their Aadhaar. This ensures that the user’s ABHA address is securely verified.&lt;/li&gt;&lt;br&gt;&lt;li&gt;&lt;strong&gt;ABHA Address Verification via Biometric :&lt;/strong&gt; This action allows users to verify their ABHA address using Biometric Authentication modes(Fingerprint, Face, Iris) . This ensures that the user’s ABHA address is securely verified.&lt;/li&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a client error. In this context.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Invalid Scope::&lt;/strong&gt;The scope of the OTP response is invalid .&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Auth Methods:&lt;/strong&gt; The authentication methods used for OTP verification are invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;Invalid Transaction Id:&lt;/strong&gt; The transaction ID provided for OTP verification is invalid.&lt;/li&gt; &lt;/ol&gt;&lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;Biometric Data did not match:&lt;/strong&gt; The Biometric data entered in the form of pid did not match with your profile.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates a access denial. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginAbhaVerifyPostAsync(String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest, final ApiCallback<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaVerifyPostValidateBeforeCall(null, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaVerifyPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> abhaApiV3PhrWebLoginAbhaVerifyPostAsyncCall(String token, String REQUEST_ID, String TIMESTAMP, AbhaApiV3PhrWebLoginAbhaVerifyPostRequest abhaApiV3PhrWebLoginAbhaVerifyPostRequest) throws ApiException {
        try {
            FutureApiCallBack<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3PhrWebLoginAbhaVerifyPostValidateBeforeCall(token, REQUEST_ID, TIMESTAMP, abhaApiV3PhrWebLoginAbhaVerifyPostRequest, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginAbhaVerifyPost200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
    /**
     * Build call for abhaApiV3PhrWebLoginProfileAbhaPhrCardGet
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
        <tr><td> 202 </td><td> Indicates a successful generation of PHR card </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A 400 Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials error occurs when a server receives a request but cannot authorize it due to incorrect or missing authentication </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaPhrCardGetCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/phr/web/login/profile/abha/phr-card";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "IMAGE/PNG",
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
    private okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaPhrCardGetValidateBeforeCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3PhrWebLoginProfileAbhaPhrCardGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginProfileAbhaPhrCardGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginProfileAbhaPhrCardGet(Async)");
        }

        return abhaApiV3PhrWebLoginProfileAbhaPhrCardGetCall(token, xToken, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Generate a PHR Card Profile
     * This API endpoint is used to generate a PHR Card profile. It requires valid credentials and headers for authentication.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> Indicates a successful generation of PHR card </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A 400 Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials error occurs when a server receives a request but cannot authorize it due to incorrect or missing authentication </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public String abhaApiV3PhrWebLoginProfileAbhaPhrCardGet(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<String> localVarResp = abhaApiV3PhrWebLoginProfileAbhaPhrCardGetWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Generate a PHR Card Profile
     * This API endpoint is used to generate a PHR Card profile. It requires valid credentials and headers for authentication.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> Indicates a successful generation of PHR card </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A 400 Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials error occurs when a server receives a request but cannot authorize it due to incorrect or missing authentication </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<String> abhaApiV3PhrWebLoginProfileAbhaPhrCardGetWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaPhrCardGetValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Generate a PHR Card Profile (asynchronously)
     * This API endpoint is used to generate a PHR Card profile. It requires valid credentials and headers for authentication.
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
        <tr><td> 202 </td><td> Indicates a successful generation of PHR card </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A 400 Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Invalid Credentials error occurs when a server receives a request but cannot authorize it due to incorrect or missing authentication </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaPhrCardGetAsync(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback<String> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaPhrCardGetValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    public CompletionStage<String> abhaApiV3PhrWebLoginProfileAbhaPhrCardGetAsyncCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP) {
        try {
            FutureApiCallBack<String> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaPhrCardGetValidateBeforeCall(token, xToken, REQUEST_ID, TIMESTAMP, callback);
            Type localVarReturnType = new TypeToken<String>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
    /**
     * Build call for abhaApiV3PhrWebLoginProfileAbhaProfileGet
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
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it signifies that the user was successfully verified with the OTP in the previous API call. A valid JWT token was provided, which authenticated the user and allowed access to their profile. As a result, the user profile was successfully retrieved, including all ABHA profile details. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A  Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates that the request was not authorized due to invalid credentials. Ensure that your API invocation includes the appropriate authorization header. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaProfileGetCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/phr/web/login/profile/abha-profile";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaProfileGetValidateBeforeCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3PhrWebLoginProfileAbhaProfileGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginProfileAbhaProfileGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginProfileAbhaProfileGet(Async)");
        }

        return abhaApiV3PhrWebLoginProfileAbhaProfileGetCall(token, xToken, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Retrieves the user’s ABHA Profile
     * This API endpoint retrieves the user’s ABHA (Ayushman Bharat Health Account) profile. Access to this profile is granted after the successful verification of the OTP, ensuring the user’s profile information is secure.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it signifies that the user was successfully verified with the OTP in the previous API call. A valid JWT token was provided, which authenticated the user and allowed access to their profile. As a result, the user profile was successfully retrieved, including all ABHA profile details. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A  Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates that the request was not authorized due to invalid credentials. Ensure that your API invocation includes the appropriate authorization header. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response abhaApiV3PhrWebLoginProfileAbhaProfileGet(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        ApiResponse<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> localVarResp = abhaApiV3PhrWebLoginProfileAbhaProfileGetWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP);
        return localVarResp.getData();
    }

    /**
     * Use Case: Retrieves the user’s ABHA Profile
     * This API endpoint retrieves the user’s ABHA (Ayushman Bharat Health Account) profile. Access to this profile is granted after the successful verification of the OTP, ensuring the user’s profile information is secure.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it signifies that the user was successfully verified with the OTP in the previous API call. A valid JWT token was provided, which authenticated the user and allowed access to their profile. As a result, the user profile was successfully retrieved, including all ABHA profile details. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A  Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates that the request was not authorized due to invalid credentials. Ensure that your API invocation includes the appropriate authorization header. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> abhaApiV3PhrWebLoginProfileAbhaProfileGetWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaProfileGetValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Use Case: Retrieves the user’s ABHA Profile (asynchronously)
     * This API endpoint retrieves the user’s ABHA (Ayushman Bharat Health Account) profile. Access to this profile is granted after the successful verification of the OTP, ensuring the user’s profile information is secure.
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
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it signifies that the user was successfully verified with the OTP in the previous API call. A valid JWT token was provided, which authenticated the user and allowed access to their profile. As a result, the user profile was successfully retrieved, including all ABHA profile details. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> A  Bad Request error with the description “invalid X-token”  indicates that the server received a request with an invalid or missing authentication token. </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates that the request was not authorized due to invalid credentials. Ensure that your API invocation includes the appropriate authorization header. </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaProfileGetAsync(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaProfileGetValidateBeforeCall(null, xToken, REQUEST_ID, TIMESTAMP, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public CompletionStage<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> abhaApiV3PhrWebLoginProfileAbhaProfileGetAsyncCall(String token, String xToken, String REQUEST_ID, String TIMESTAMP) {
        try {
            FutureApiCallBack<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> callback = FutureApiCallBack.newCallback();
            okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaProfileGetValidateBeforeCall(token, xToken, REQUEST_ID, TIMESTAMP, callback);
            Type localVarReturnType = new TypeToken<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response>() {
            }.getType();
            localVarApiClient.executeAsync(localVarCall, localVarReturnType, callback);
            return callback.getFuture();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
    /**
     * Build call for abhaApiV3PhrWebLoginProfileAbhaQrCodeGet
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
        <tr><td> 200 </td><td> Receives a QR code as a response which can be used to quickly access and share the user’s ABHA profile information. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaQrCodeGetCall(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/phr/web/login/profile/abha/qrCode";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "Image/PNG",
            "application/json:"
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
    private okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaQrCodeGetValidateBeforeCall(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3PhrWebLoginProfileAbhaQrCodeGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3PhrWebLoginProfileAbhaQrCodeGet(Async)");
        }

        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3PhrWebLoginProfileAbhaQrCodeGet(Async)");
        }

        return abhaApiV3PhrWebLoginProfileAbhaQrCodeGetCall(xToken, REQUEST_ID, TIMESTAMP, _callback);

    }

    /**
     * Use Case: Generate QR Code By Passing X-token to share user ABHA address Profile Information.
     * This API endpoint is used to generate a QR code for a complete ABHA address profile. The QR code can be used to quickly access and share the user’s ABHA address profile information. This is particularly useful for healthcare providers to retrieve patient information efficiently and securely.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Receives a QR code as a response which can be used to quickly access and share the user’s ABHA profile information. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public void abhaApiV3PhrWebLoginProfileAbhaQrCodeGet(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        abhaApiV3PhrWebLoginProfileAbhaQrCodeGetWithHttpInfo(xToken, REQUEST_ID, TIMESTAMP);
    }

    /**
     * Use Case: Generate QR Code By Passing X-token to share user ABHA address Profile Information.
     * This API endpoint is used to generate a QR code for a complete ABHA address profile. The QR code can be used to quickly access and share the user’s ABHA address profile information. This is particularly useful for healthcare providers to retrieve patient information efficiently and securely.
     * @param xToken  (required)
     * @param REQUEST_ID  (required)
     * @param TIMESTAMP  (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Receives a QR code as a response which can be used to quickly access and share the user’s ABHA profile information. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<Void> abhaApiV3PhrWebLoginProfileAbhaQrCodeGetWithHttpInfo(String xToken, String REQUEST_ID, String TIMESTAMP) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaQrCodeGetValidateBeforeCall(xToken, REQUEST_ID, TIMESTAMP, null);
        return localVarApiClient.execute(localVarCall);
    }

    /**
     * Use Case: Generate QR Code By Passing X-token to share user ABHA address Profile Information. (asynchronously)
     * This API endpoint is used to generate a QR code for a complete ABHA address profile. The QR code can be used to quickly access and share the user’s ABHA address profile information. This is particularly useful for healthcare providers to retrieve patient information efficiently and securely.
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
        <tr><td> 200 </td><td> Receives a QR code as a response which can be used to quickly access and share the user’s ABHA profile information. </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Bad Request </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    /*public okhttp3.Call abhaApiV3PhrWebLoginProfileAbhaQrCodeGetAsync(String xToken, String REQUEST_ID, String TIMESTAMP, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3PhrWebLoginProfileAbhaQrCodeGetValidateBeforeCall(xToken, REQUEST_ID, TIMESTAMP, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }*/
}
