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

public class ChildAbhaTheseApisAreIntendedForUseOnlyBySpecificGovernmentIntegratorsApprovedByNhaApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ChildAbhaTheseApisAreIntendedForUseOnlyBySpecificGovernmentIntegratorsApprovedByNhaApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ChildAbhaTheseApisAreIntendedForUseOnlyBySpecificGovernmentIntegratorsApprovedByNhaApi(ApiClient apiClient) {
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
     * Build call for abhaApiV3EnrollmentEnrolByAadhaarPost
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName **Applicable for user who is enrolling via Benefit Program.** (optional)
     * @param xToken **Applicable for child abha creation. X-token of Parent user, user can get X-token after login to the system** (optional)
     * @param abhaApiV3EnrollmentEnrolByAadhaarPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;CHILD ABHA - Create CHILD ABHA&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems:&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dayOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;14&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;monthOfBirth:&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;yearOfBirth:&lt;/strong&gt; The year of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;2000&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s last name. &lt;ul&gt;&lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Doe John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;27&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;parentConsent:&lt;/strong&gt; Consent of the parent should be true &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;true&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Demo Auth API&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;18-06-1995&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;districtCode:&lt;/strong&gt; The code of the user’s district. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;486&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John Doe&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;mobile:&lt;/strong&gt; The user’s mobile number. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;******1234&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA Via Biometrics&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by using users biometrics. In this flow user will be using Aadhaar authentication and biometrics verification(FingerPrint, Face and Iris). &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful generation and delivery of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Positive Flow:&lt;/strong&gt; his API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Positive Flow:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Account Already Exists.&lt;/strong&gt; This error occurs when an attempt is made to create a CHILD ABHA number for a minor who already has an existing ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - In Case of Existing ABHA Numbe:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match. If an ABHA number already exists, it will return the existing ABHA number&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - New ABHA Creation :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates a new ABHA number if the details match and no existing ABHA number is found.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) verification process.&lt;br&gt;&lt;br&gt; &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Transaction Id:&lt;/strong&gt;This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems..&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid authMethod:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Mobile Number:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;DemoAuth API - State District Not Matching - :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt; CHILD ABHA - Access Issue&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - X-token Expired&lt;/strong&gt;: This error occurs when the X-token provided for authorization has expired. The X-token is essential for authenticating the request, and an expired token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 response code Indicates an unprocessable entity due to errors such as invalid OTP value.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid OTP Value&lt;/strong&gt;: This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems ,This error occurs when the OTP value provided in the request is invalid. The OTP must be correctly formatted and match the one sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Certificate&lt;/strong&gt;: This error occurs when the certificate provided for Aadhaar biometric authentication is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - 6 ABHA Linked to Mobile&lt;/strong&gt;: This error occurs when the mobile number provided is already linked to 6 ABHA numbers. The mobile number must be unique or linked to fewer than 6 ABHA numbers.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;  DemoAuth API - Details Not Matches Against Aadhaar&lt;/strong&gt;: This error occurs when the information provided does not match the details on record with Aadhaar. The demographic details must be accurate and match the Aadhaar records.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - CHILD LIMIT&lt;/strong&gt;: This error occurs when the limit for creating or updating CHILD ABHA profiles has been exceeded. The request cannot be processed as the limit has been reached.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3EnrollmentEnrolByAadhaarPostCall(String TIMESTAMP, String REQUEST_ID, String benefitName, String xToken, AbhaApiV3EnrollmentEnrolByAadhaarPostRequest abhaApiV3EnrollmentEnrolByAadhaarPostRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = abhaApiV3EnrollmentEnrolByAadhaarPostRequest;

        // create path and map variables
        String localVarPath = "/abha/api/v3/enrollment/enrol/byAadhaar";

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


        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3EnrollmentEnrolByAadhaarPostValidateBeforeCall(String TIMESTAMP, String REQUEST_ID, String benefitName, String xToken, AbhaApiV3EnrollmentEnrolByAadhaarPostRequest abhaApiV3EnrollmentEnrolByAadhaarPostRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3EnrollmentEnrolByAadhaarPost(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3EnrollmentEnrolByAadhaarPost(Async)");
        }

        return abhaApiV3EnrollmentEnrolByAadhaarPostCall(TIMESTAMP, REQUEST_ID, benefitName, xToken, abhaApiV3EnrollmentEnrolByAadhaarPostRequest, _callback);

    }

    /**
     * UseCase : Create ABHA number Via Aadhaar by verifying Aadhaar OTP, using Biometrics, using demoAuth and Child ABHA Creation.
     * This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number for an individual using their Aadhaar number. The ABHA number is a unique identifier that helps in authenticating individuals and linking their health records across multiple systems and stakeholders, ensuring that medical records are issued to the correct individual and accessed by authorized Health Information Users with appropriate consent.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName **Applicable for user who is enrolling via Benefit Program.** (optional)
     * @param xToken **Applicable for child abha creation. X-token of Parent user, user can get X-token after login to the system** (optional)
     * @param abhaApiV3EnrollmentEnrolByAadhaarPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;CHILD ABHA - Create CHILD ABHA&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems:&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dayOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;14&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;monthOfBirth:&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;yearOfBirth:&lt;/strong&gt; The year of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;2000&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s last name. &lt;ul&gt;&lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Doe John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;27&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;parentConsent:&lt;/strong&gt; Consent of the parent should be true &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;true&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Demo Auth API&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;18-06-1995&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;districtCode:&lt;/strong&gt; The code of the user’s district. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;486&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John Doe&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;mobile:&lt;/strong&gt; The user’s mobile number. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;******1234&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA Via Biometrics&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by using users biometrics. In this flow user will be using Aadhaar authentication and biometrics verification(FingerPrint, Face and Iris). &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return AbhaApiV3EnrollmentEnrolByAadhaarPost200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful generation and delivery of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Positive Flow:&lt;/strong&gt; his API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Positive Flow:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Account Already Exists.&lt;/strong&gt; This error occurs when an attempt is made to create a CHILD ABHA number for a minor who already has an existing ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - In Case of Existing ABHA Numbe:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match. If an ABHA number already exists, it will return the existing ABHA number&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - New ABHA Creation :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates a new ABHA number if the details match and no existing ABHA number is found.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) verification process.&lt;br&gt;&lt;br&gt; &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Transaction Id:&lt;/strong&gt;This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems..&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid authMethod:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Mobile Number:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;DemoAuth API - State District Not Matching - :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt; CHILD ABHA - Access Issue&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - X-token Expired&lt;/strong&gt;: This error occurs when the X-token provided for authorization has expired. The X-token is essential for authenticating the request, and an expired token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 response code Indicates an unprocessable entity due to errors such as invalid OTP value.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid OTP Value&lt;/strong&gt;: This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems ,This error occurs when the OTP value provided in the request is invalid. The OTP must be correctly formatted and match the one sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Certificate&lt;/strong&gt;: This error occurs when the certificate provided for Aadhaar biometric authentication is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - 6 ABHA Linked to Mobile&lt;/strong&gt;: This error occurs when the mobile number provided is already linked to 6 ABHA numbers. The mobile number must be unique or linked to fewer than 6 ABHA numbers.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;  DemoAuth API - Details Not Matches Against Aadhaar&lt;/strong&gt;: This error occurs when the information provided does not match the details on record with Aadhaar. The demographic details must be accurate and match the Aadhaar records.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - CHILD LIMIT&lt;/strong&gt;: This error occurs when the limit for creating or updating CHILD ABHA profiles has been exceeded. The request cannot be processed as the limit has been reached.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3EnrollmentEnrolByAadhaarPost200Response abhaApiV3EnrollmentEnrolByAadhaarPost(String TIMESTAMP, String REQUEST_ID, String benefitName, String xToken, AbhaApiV3EnrollmentEnrolByAadhaarPostRequest abhaApiV3EnrollmentEnrolByAadhaarPostRequest) throws ApiException {
        ApiResponse<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response> localVarResp = abhaApiV3EnrollmentEnrolByAadhaarPostWithHttpInfo(TIMESTAMP, REQUEST_ID, benefitName, xToken, abhaApiV3EnrollmentEnrolByAadhaarPostRequest);
        return localVarResp.getData();
    }

    /**
     * UseCase : Create ABHA number Via Aadhaar by verifying Aadhaar OTP, using Biometrics, using demoAuth and Child ABHA Creation.
     * This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number for an individual using their Aadhaar number. The ABHA number is a unique identifier that helps in authenticating individuals and linking their health records across multiple systems and stakeholders, ensuring that medical records are issued to the correct individual and accessed by authorized Health Information Users with appropriate consent.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName **Applicable for user who is enrolling via Benefit Program.** (optional)
     * @param xToken **Applicable for child abha creation. X-token of Parent user, user can get X-token after login to the system** (optional)
     * @param abhaApiV3EnrollmentEnrolByAadhaarPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;CHILD ABHA - Create CHILD ABHA&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems:&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dayOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;14&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;monthOfBirth:&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;yearOfBirth:&lt;/strong&gt; The year of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;2000&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s last name. &lt;ul&gt;&lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Doe John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;27&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;parentConsent:&lt;/strong&gt; Consent of the parent should be true &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;true&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Demo Auth API&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;18-06-1995&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;districtCode:&lt;/strong&gt; The code of the user’s district. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;486&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John Doe&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;mobile:&lt;/strong&gt; The user’s mobile number. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;******1234&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA Via Biometrics&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by using users biometrics. In this flow user will be using Aadhaar authentication and biometrics verification(FingerPrint, Face and Iris). &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @return ApiResponse&lt;AbhaApiV3EnrollmentEnrolByAadhaarPost200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful generation and delivery of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Positive Flow:&lt;/strong&gt; his API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Positive Flow:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Account Already Exists.&lt;/strong&gt; This error occurs when an attempt is made to create a CHILD ABHA number for a minor who already has an existing ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - In Case of Existing ABHA Numbe:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match. If an ABHA number already exists, it will return the existing ABHA number&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - New ABHA Creation :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates a new ABHA number if the details match and no existing ABHA number is found.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) verification process.&lt;br&gt;&lt;br&gt; &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Transaction Id:&lt;/strong&gt;This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems..&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid authMethod:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Mobile Number:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;DemoAuth API - State District Not Matching - :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt; CHILD ABHA - Access Issue&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - X-token Expired&lt;/strong&gt;: This error occurs when the X-token provided for authorization has expired. The X-token is essential for authenticating the request, and an expired token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 response code Indicates an unprocessable entity due to errors such as invalid OTP value.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid OTP Value&lt;/strong&gt;: This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems ,This error occurs when the OTP value provided in the request is invalid. The OTP must be correctly formatted and match the one sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Certificate&lt;/strong&gt;: This error occurs when the certificate provided for Aadhaar biometric authentication is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - 6 ABHA Linked to Mobile&lt;/strong&gt;: This error occurs when the mobile number provided is already linked to 6 ABHA numbers. The mobile number must be unique or linked to fewer than 6 ABHA numbers.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;  DemoAuth API - Details Not Matches Against Aadhaar&lt;/strong&gt;: This error occurs when the information provided does not match the details on record with Aadhaar. The demographic details must be accurate and match the Aadhaar records.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - CHILD LIMIT&lt;/strong&gt;: This error occurs when the limit for creating or updating CHILD ABHA profiles has been exceeded. The request cannot be processed as the limit has been reached.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response> abhaApiV3EnrollmentEnrolByAadhaarPostWithHttpInfo(String TIMESTAMP, String REQUEST_ID, String benefitName, String xToken, AbhaApiV3EnrollmentEnrolByAadhaarPostRequest abhaApiV3EnrollmentEnrolByAadhaarPostRequest) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3EnrollmentEnrolByAadhaarPostValidateBeforeCall(TIMESTAMP, REQUEST_ID, benefitName, xToken, abhaApiV3EnrollmentEnrolByAadhaarPostRequest, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * UseCase : Create ABHA number Via Aadhaar by verifying Aadhaar OTP, using Biometrics, using demoAuth and Child ABHA Creation. (asynchronously)
     * This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number for an individual using their Aadhaar number. The ABHA number is a unique identifier that helps in authenticating individuals and linking their health records across multiple systems and stakeholders, ensuring that medical records are issued to the correct individual and accessed by authorized Health Information Users with appropriate consent.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param benefitName **Applicable for user who is enrolling via Benefit Program.** (optional)
     * @param xToken **Applicable for child abha creation. X-token of Parent user, user can get X-token after login to the system** (optional)
     * @param abhaApiV3EnrollmentEnrolByAadhaarPostRequest &lt;b&gt;Below is the Request Body description:&lt;/b&gt;&lt;br&gt;&lt;br&gt;  &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;CHILD ABHA - Create CHILD ABHA&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems:&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dayOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;14&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;monthOfBirth:&lt;/strong&gt; The month of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;yearOfBirth:&lt;/strong&gt; The year of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;2000&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s last name. &lt;ul&gt;&lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;Doe John&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt;  &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;27&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;parentConsent:&lt;/strong&gt; Consent of the parent should be true &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;true&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Demo Auth API&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/li&gt; &lt;li&gt;&lt;strong&gt;dateOfBirth:&lt;/strong&gt; The day of the user’s birth. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;18-06-1995&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;districtCode:&lt;/strong&gt; The code of the user’s district. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;486&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;stateCode:&lt;/strong&gt; The code of the user’s state. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;1&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;name:&lt;/strong&gt; The user’s name. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;John Doe&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;gender:&lt;/strong&gt; The user’s gender. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;M&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt;&lt;strong&gt;mobile:&lt;/strong&gt; The user’s mobile number. &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Type:&lt;/strong&gt; String&lt;/li&gt; &lt;li&gt;&lt;strong&gt;Example:&lt;/strong&gt; &lt;code&gt;******1234&lt;/code&gt;&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA Via Biometrics&lt;/strong&gt;&lt;/p&gt; &lt;ul&gt; &lt;li&gt;&lt;strong&gt;Description:&lt;/strong&gt; This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by using users biometrics. In this flow user will be using Aadhaar authentication and biometrics verification(FingerPrint, Face and Iris). &lt;/li&gt; &lt;/ul&gt; &lt;/li&gt; &lt;/ol&gt; (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The 200 response code indicates a successful request. In this context, it refers to the successful generation and delivery of an OTP (One-Time Password) for various services.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Responses:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Positive Flow:&lt;/strong&gt; his API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Positive Flow:&lt;/strong&gt; This API endpoint is used to create a CHILD ABHA number for a minor using their demographic details. The CHILD ABHA number helps in uniquely identifying minors and linking their health records across multiple systems.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Account Already Exists.&lt;/strong&gt; This error occurs when an attempt is made to create a CHILD ABHA number for a minor who already has an existing ABHA number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - In Case of Existing ABHA Numbe:&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match. If an ABHA number already exists, it will return the existing ABHA number&lt;/li&gt; &lt;/ol&gt; &lt;ol start &#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - New ABHA Creation :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates a new ABHA number if the details match and no existing ABHA number is found.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> The 400 response code indicates a bad request. In this context, it refers to various errors encountered during the OTP (One-Time Password) verification process.&lt;br&gt;&lt;br&gt; &lt;ol&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Transaction Id:&lt;/strong&gt;This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems..&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid authMethod:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid Mobile Number:&lt;/strong&gt; This API endpoint is used to create an ABHA number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt; &lt;p&gt;&lt;strong&gt;DemoAuth API - State District Not Matching - :&lt;/strong&gt; This API endpoint is used for the demo authentication of an individual using their Aadhaar number. It verifies the demographic details against the Aadhaar database and generates an ABHA number if the details match.&lt;/p&gt; &lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> The 401 response code indicates an unauthorized request. In this context, it refers to the lack of proper authentication during the operation of the Invalid Credentials.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity..&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - Invalid Benefit Name&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt; CHILD ABHA - Access Issue&lt;/strong&gt;: This error occurs when the access token provided for authorization is invalid. The access token is essential for authenticating the request, and an invalid token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - X-token Expired&lt;/strong&gt;: This error occurs when the X-token provided for authorization has expired. The X-token is essential for authenticating the request, and an expired token means the server cannot verify the user’s identity.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> The 422 response code Indicates an unprocessable entity due to errors such as invalid OTP value.&lt;br&gt;&lt;br&gt; &lt;strong&gt;Types of OTP Response Errors:&lt;/strong&gt; &lt;ol&gt; &lt;li&gt;&lt;strong&gt;Create ABHA by Verifying OTP - Invalid OTP Value&lt;/strong&gt;: This API endpoint is used to create an ABHA (Ayushman Bharat Health Account) number by verifying an OTP sent to the user’s registered mobile number. The ABHA number uniquely identifies individuals and helps in authenticating them and linking their health records across multiple systems ,This error occurs when the OTP value provided in the request is invalid. The OTP must be correctly formatted and match the one sent to the user’s registered mobile number.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;2\&quot;&gt; &lt;li&gt;&lt;strong&gt;Aadhaar Bio - Invalid Certificate&lt;/strong&gt;: This error occurs when the certificate provided for Aadhaar biometric authentication is invalid.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;3\&quot;&gt; &lt;li&gt;&lt;strong&gt;DemoAuth API - 6 ABHA Linked to Mobile&lt;/strong&gt;: This error occurs when the mobile number provided is already linked to 6 ABHA numbers. The mobile number must be unique or linked to fewer than 6 ABHA numbers.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;4\&quot;&gt; &lt;li&gt;&lt;strong&gt;  DemoAuth API - Details Not Matches Against Aadhaar&lt;/strong&gt;: This error occurs when the information provided does not match the details on record with Aadhaar. The demographic details must be accurate and match the Aadhaar records.&lt;/li&gt; &lt;/ol&gt; &lt;ol start&#x3D;\&quot;5\&quot;&gt; &lt;li&gt;&lt;strong&gt;CHILD ABHA - CHILD LIMIT&lt;/strong&gt;: This error occurs when the limit for creating or updating CHILD ABHA profiles has been exceeded. The request cannot be processed as the limit has been reached.&lt;/li&gt; &lt;/ol&gt; </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Internal Server Error </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3EnrollmentEnrolByAadhaarPostAsync(String TIMESTAMP, String REQUEST_ID, String benefitName, String xToken, AbhaApiV3EnrollmentEnrolByAadhaarPostRequest abhaApiV3EnrollmentEnrolByAadhaarPostRequest, final ApiCallback<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3EnrollmentEnrolByAadhaarPostValidateBeforeCall(TIMESTAMP, REQUEST_ID, benefitName, xToken, abhaApiV3EnrollmentEnrolByAadhaarPostRequest, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3EnrollmentEnrolByAadhaarPost200Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for abhaApiV3EnrollmentProfileChildrenGet
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param contentType  (required)
     * @param benefitName  (required)
     * @param xToken  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3EnrollmentProfileChildrenGetCall(String TIMESTAMP, String REQUEST_ID, String contentType, String benefitName, String xToken, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/abha/api/v3/enrollment/profile/children";

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

        if (TIMESTAMP != null) {
            localVarHeaderParams.put("TIMESTAMP", localVarApiClient.parameterToString(TIMESTAMP));
        }


        if (REQUEST_ID != null) {
            localVarHeaderParams.put("REQUEST-ID", localVarApiClient.parameterToString(REQUEST_ID));
        }


        if (contentType != null) {
            localVarHeaderParams.put("Content-Type", localVarApiClient.parameterToString(contentType));
        }


        if (benefitName != null) {
            localVarHeaderParams.put("Benefit-Name", localVarApiClient.parameterToString(benefitName));
        }


        if (xToken != null) {
            localVarHeaderParams.put("X-token", localVarApiClient.parameterToString(xToken));
        }


        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call abhaApiV3EnrollmentProfileChildrenGetValidateBeforeCall(String TIMESTAMP, String REQUEST_ID, String contentType, String benefitName, String xToken, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'TIMESTAMP' is set
        if (TIMESTAMP == null) {
            throw new ApiException("Missing the required parameter 'TIMESTAMP' when calling abhaApiV3EnrollmentProfileChildrenGet(Async)");
        }

        // verify the required parameter 'REQUEST_ID' is set
        if (REQUEST_ID == null) {
            throw new ApiException("Missing the required parameter 'REQUEST_ID' when calling abhaApiV3EnrollmentProfileChildrenGet(Async)");
        }

        // verify the required parameter 'contentType' is set
        if (contentType == null) {
            throw new ApiException("Missing the required parameter 'contentType' when calling abhaApiV3EnrollmentProfileChildrenGet(Async)");
        }

        // verify the required parameter 'benefitName' is set
        if (benefitName == null) {
            throw new ApiException("Missing the required parameter 'benefitName' when calling abhaApiV3EnrollmentProfileChildrenGet(Async)");
        }

        // verify the required parameter 'xToken' is set
        if (xToken == null) {
            throw new ApiException("Missing the required parameter 'xToken' when calling abhaApiV3EnrollmentProfileChildrenGet(Async)");
        }

        return abhaApiV3EnrollmentProfileChildrenGetCall(TIMESTAMP, REQUEST_ID, contentType, benefitName, xToken, _callback);

    }

    /**
     * UseCase: Get Child ABHA address
     * This API endpoint retrieves the ABHA (Ayushman Bharat Health Account) profile of a child, making it especially useful for managing minors’ health records and information. It provides essential details such as the child’s ABHA number, date of birth, name, and gender. Additionally, this endpoint allows users to access all child records linked to a parent’s ABHA number.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param contentType  (required)
     * @param benefitName  (required)
     * @param xToken  (required)
     * @return AbhaApiV3EnrollmentProfileChildrenGet200Response
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public AbhaApiV3EnrollmentProfileChildrenGet200Response abhaApiV3EnrollmentProfileChildrenGet(String TIMESTAMP, String REQUEST_ID, String contentType, String benefitName, String xToken) throws ApiException {
        ApiResponse<AbhaApiV3EnrollmentProfileChildrenGet200Response> localVarResp = abhaApiV3EnrollmentProfileChildrenGetWithHttpInfo(TIMESTAMP, REQUEST_ID, contentType, benefitName, xToken);
        return localVarResp.getData();
    }

    /**
     * UseCase: Get Child ABHA address
     * This API endpoint retrieves the ABHA (Ayushman Bharat Health Account) profile of a child, making it especially useful for managing minors’ health records and information. It provides essential details such as the child’s ABHA number, date of birth, name, and gender. Additionally, this endpoint allows users to access all child records linked to a parent’s ABHA number.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param contentType  (required)
     * @param benefitName  (required)
     * @param xToken  (required)
     * @return ApiResponse&lt;AbhaApiV3EnrollmentProfileChildrenGet200Response&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<AbhaApiV3EnrollmentProfileChildrenGet200Response> abhaApiV3EnrollmentProfileChildrenGetWithHttpInfo(String TIMESTAMP, String REQUEST_ID, String contentType, String benefitName, String xToken) throws ApiException {
        okhttp3.Call localVarCall = abhaApiV3EnrollmentProfileChildrenGetValidateBeforeCall(TIMESTAMP, REQUEST_ID, contentType, benefitName, xToken, null);
        Type localVarReturnType = new TypeToken<AbhaApiV3EnrollmentProfileChildrenGet200Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * UseCase: Get Child ABHA address (asynchronously)
     * This API endpoint retrieves the ABHA (Ayushman Bharat Health Account) profile of a child, making it especially useful for managing minors’ health records and information. It provides essential details such as the child’s ABHA number, date of birth, name, and gender. Additionally, this endpoint allows users to access all child records linked to a parent’s ABHA number.
     * @param TIMESTAMP  (required)
     * @param REQUEST_ID  (required)
     * @param contentType  (required)
     * @param benefitName  (required)
     * @param xToken  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Indicates a successful request. The response includes the updated child ABHA profile details </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Indicates an unauthorized request due to invalid credentials </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> &lt;b&gt;Internal Server Error&lt;/b&gt;&lt;br&gt;&lt;br&gt;  An Internal Server Error (500) indicates that the server encountered an unexpected condition that prevented it from fulfilling the request. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call abhaApiV3EnrollmentProfileChildrenGetAsync(String TIMESTAMP, String REQUEST_ID, String contentType, String benefitName, String xToken, final ApiCallback<AbhaApiV3EnrollmentProfileChildrenGet200Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = abhaApiV3EnrollmentProfileChildrenGetValidateBeforeCall(TIMESTAMP, REQUEST_ID, contentType, benefitName, xToken, _callback);
        Type localVarReturnType = new TypeToken<AbhaApiV3EnrollmentProfileChildrenGet200Response>(){}.getType();
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
}
