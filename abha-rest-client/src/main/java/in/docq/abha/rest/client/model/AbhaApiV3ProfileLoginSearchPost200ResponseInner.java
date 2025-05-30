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


package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.*;

/**
 * AbhaApiV3ProfileLoginSearchPost200ResponseInner
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-21T15:03:26.931725+05:30[Asia/Kolkata]", comments = "Generator version: 7.11.0")
public class AbhaApiV3ProfileLoginSearchPost200ResponseInner {
  public static final String SERIALIZED_NAME_PREFERRED_ABHA_ADDRESS = "preferredAbhaAddress";
  @SerializedName(SERIALIZED_NAME_PREFERRED_ABHA_ADDRESS)
  @javax.annotation.Nullable
  private String preferredAbhaAddress;

  public static final String SERIALIZED_NAME_AB_H_A_NUMBER = "ABHANumber";
  @SerializedName(SERIALIZED_NAME_AB_H_A_NUMBER)
  @javax.annotation.Nullable
  private String abHANumber;

  public static final String SERIALIZED_NAME_AUTH_METHODS = "authMethods";
  @SerializedName(SERIALIZED_NAME_AUTH_METHODS)
  @javax.annotation.Nullable
  private String authMethods;

  public static final String SERIALIZED_NAME_TAGS = "tags";
  @SerializedName(SERIALIZED_NAME_TAGS)
  @javax.annotation.Nullable
  private Object tags;

  public static final String SERIALIZED_NAME_BLOCKED_AUTH_METHODS = "blockedAuthMethods";
  @SerializedName(SERIALIZED_NAME_BLOCKED_AUTH_METHODS)
  @javax.annotation.Nullable
  private List<String> blockedAuthMethods = new ArrayList<>();

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  @javax.annotation.Nullable
  private String status;

  public static final String SERIALIZED_NAME_VERIFICATION_STATUS = "verificationStatus";
  @SerializedName(SERIALIZED_NAME_VERIFICATION_STATUS)
  @javax.annotation.Nullable
  private String verificationStatus;

  public static final String SERIALIZED_NAME_MOBILE = "mobile";
  @SerializedName(SERIALIZED_NAME_MOBILE)
  @javax.annotation.Nullable
  private String mobile;

  public static final String SERIALIZED_NAME_VERIFICATION_TYPE = "verificationType";
  @SerializedName(SERIALIZED_NAME_VERIFICATION_TYPE)
  @javax.annotation.Nullable
  private String verificationType;

  public static final String SERIALIZED_NAME_IS_DOCUMENT_ACCOUNT = "isDocumentAccount";
  @SerializedName(SERIALIZED_NAME_IS_DOCUMENT_ACCOUNT)
  @javax.annotation.Nullable
  private String isDocumentAccount;

  public AbhaApiV3ProfileLoginSearchPost200ResponseInner() {
  }

  public AbhaApiV3ProfileLoginSearchPost200ResponseInner preferredAbhaAddress(@javax.annotation.Nullable String preferredAbhaAddress) {
    this.preferredAbhaAddress = preferredAbhaAddress;
    return this;
  }

  /**
   * Get preferredAbhaAddress
   * @return preferredAbhaAddress
   */
  @javax.annotation.Nullable
  public String getPreferredAbhaAddress() {
    return preferredAbhaAddress;
  }

  public void setPreferredAbhaAddress(@javax.annotation.Nullable String preferredAbhaAddress) {
    this.preferredAbhaAddress = preferredAbhaAddress;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner abHANumber(@javax.annotation.Nullable String abHANumber) {
    this.abHANumber = abHANumber;
    return this;
  }

  /**
   * Get abHANumber
   * @return abHANumber
   */
  @javax.annotation.Nullable
  public String getAbHANumber() {
    return abHANumber;
  }

  public void setAbHANumber(@javax.annotation.Nullable String abHANumber) {
    this.abHANumber = abHANumber;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner authMethods(@javax.annotation.Nullable String authMethods) {
    this.authMethods = authMethods;
    return this;
  }

  /**
   * Get authMethods
   * @return authMethods
   */
  @javax.annotation.Nullable
  public String getAuthMethods() {
    return authMethods;
  }

  public void setAuthMethods(@javax.annotation.Nullable String authMethods) {
    this.authMethods = authMethods;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner tags(@javax.annotation.Nullable Object tags) {
    this.tags = tags;
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  @javax.annotation.Nullable
  public Object getTags() {
    return tags;
  }

  public void setTags(@javax.annotation.Nullable Object tags) {
    this.tags = tags;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner blockedAuthMethods(@javax.annotation.Nullable List<String> blockedAuthMethods) {
    this.blockedAuthMethods = blockedAuthMethods;
    return this;
  }

  public AbhaApiV3ProfileLoginSearchPost200ResponseInner addBlockedAuthMethodsItem(String blockedAuthMethodsItem) {
    if (this.blockedAuthMethods == null) {
      this.blockedAuthMethods = new ArrayList<>();
    }
    this.blockedAuthMethods.add(blockedAuthMethodsItem);
    return this;
  }

  /**
   * Get blockedAuthMethods
   * @return blockedAuthMethods
   */
  @javax.annotation.Nullable
  public List<String> getBlockedAuthMethods() {
    return blockedAuthMethods;
  }

  public void setBlockedAuthMethods(@javax.annotation.Nullable List<String> blockedAuthMethods) {
    this.blockedAuthMethods = blockedAuthMethods;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner status(@javax.annotation.Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @javax.annotation.Nullable
  public String getStatus() {
    return status;
  }

  public void setStatus(@javax.annotation.Nullable String status) {
    this.status = status;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner verificationStatus(@javax.annotation.Nullable String verificationStatus) {
    this.verificationStatus = verificationStatus;
    return this;
  }

  /**
   * Get verificationStatus
   * @return verificationStatus
   */
  @javax.annotation.Nullable
  public String getVerificationStatus() {
    return verificationStatus;
  }

  public void setVerificationStatus(@javax.annotation.Nullable String verificationStatus) {
    this.verificationStatus = verificationStatus;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner mobile(@javax.annotation.Nullable String mobile) {
    this.mobile = mobile;
    return this;
  }

  /**
   * Get mobile
   * @return mobile
   */
  @javax.annotation.Nullable
  public String getMobile() {
    return mobile;
  }

  public void setMobile(@javax.annotation.Nullable String mobile) {
    this.mobile = mobile;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner verificationType(@javax.annotation.Nullable String verificationType) {
    this.verificationType = verificationType;
    return this;
  }

  /**
   * Get verificationType
   * @return verificationType
   */
  @javax.annotation.Nullable
  public String getVerificationType() {
    return verificationType;
  }

  public void setVerificationType(@javax.annotation.Nullable String verificationType) {
    this.verificationType = verificationType;
  }


  public AbhaApiV3ProfileLoginSearchPost200ResponseInner isDocumentAccount(@javax.annotation.Nullable String isDocumentAccount) {
    this.isDocumentAccount = isDocumentAccount;
    return this;
  }

  /**
   * Get isDocumentAccount
   * @return isDocumentAccount
   */
  @javax.annotation.Nullable
  public String getIsDocumentAccount() {
    return isDocumentAccount;
  }

  public void setIsDocumentAccount(@javax.annotation.Nullable String isDocumentAccount) {
    this.isDocumentAccount = isDocumentAccount;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbhaApiV3ProfileLoginSearchPost200ResponseInner abhaApiV3ProfileLoginSearchPost200ResponseInner = (AbhaApiV3ProfileLoginSearchPost200ResponseInner) o;
    return Objects.equals(this.preferredAbhaAddress, abhaApiV3ProfileLoginSearchPost200ResponseInner.preferredAbhaAddress) &&
        Objects.equals(this.abHANumber, abhaApiV3ProfileLoginSearchPost200ResponseInner.abHANumber) &&
        Objects.equals(this.authMethods, abhaApiV3ProfileLoginSearchPost200ResponseInner.authMethods) &&
        Objects.equals(this.tags, abhaApiV3ProfileLoginSearchPost200ResponseInner.tags) &&
        Objects.equals(this.blockedAuthMethods, abhaApiV3ProfileLoginSearchPost200ResponseInner.blockedAuthMethods) &&
        Objects.equals(this.status, abhaApiV3ProfileLoginSearchPost200ResponseInner.status) &&
        Objects.equals(this.verificationStatus, abhaApiV3ProfileLoginSearchPost200ResponseInner.verificationStatus) &&
        Objects.equals(this.mobile, abhaApiV3ProfileLoginSearchPost200ResponseInner.mobile) &&
        Objects.equals(this.verificationType, abhaApiV3ProfileLoginSearchPost200ResponseInner.verificationType) &&
        Objects.equals(this.isDocumentAccount, abhaApiV3ProfileLoginSearchPost200ResponseInner.isDocumentAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferredAbhaAddress, abHANumber, authMethods, tags, blockedAuthMethods, status, verificationStatus, mobile, verificationType, isDocumentAccount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbhaApiV3ProfileLoginSearchPost200ResponseInner {\n");
    sb.append("    preferredAbhaAddress: ").append(toIndentedString(preferredAbhaAddress)).append("\n");
    sb.append("    abHANumber: ").append(toIndentedString(abHANumber)).append("\n");
    sb.append("    authMethods: ").append(toIndentedString(authMethods)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    blockedAuthMethods: ").append(toIndentedString(blockedAuthMethods)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    verificationStatus: ").append(toIndentedString(verificationStatus)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    verificationType: ").append(toIndentedString(verificationType)).append("\n");
    sb.append("    isDocumentAccount: ").append(toIndentedString(isDocumentAccount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("preferredAbhaAddress");
    openapiFields.add("ABHANumber");
    openapiFields.add("authMethods");
    openapiFields.add("tags");
    openapiFields.add("blockedAuthMethods");
    openapiFields.add("status");
    openapiFields.add("verificationStatus");
    openapiFields.add("mobile");
    openapiFields.add("verificationType");
    openapiFields.add("isDocumentAccount");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AbhaApiV3ProfileLoginSearchPost200ResponseInner
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AbhaApiV3ProfileLoginSearchPost200ResponseInner.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AbhaApiV3ProfileLoginSearchPost200ResponseInner is not found in the empty JSON string", AbhaApiV3ProfileLoginSearchPost200ResponseInner.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AbhaApiV3ProfileLoginSearchPost200ResponseInner.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbhaApiV3ProfileLoginSearchPost200ResponseInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("preferredAbhaAddress") != null && !jsonObj.get("preferredAbhaAddress").isJsonNull()) && !jsonObj.get("preferredAbhaAddress").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `preferredAbhaAddress` to be a primitive type in the JSON string but got `%s`", jsonObj.get("preferredAbhaAddress").toString()));
      }
      if ((jsonObj.get("ABHANumber") != null && !jsonObj.get("ABHANumber").isJsonNull()) && !jsonObj.get("ABHANumber").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ABHANumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ABHANumber").toString()));
      }
      if ((jsonObj.get("authMethods") != null && !jsonObj.get("authMethods").isJsonNull()) && !jsonObj.get("authMethods").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `authMethods` to be a primitive type in the JSON string but got `%s`", jsonObj.get("authMethods").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("blockedAuthMethods") != null && !jsonObj.get("blockedAuthMethods").isJsonNull() && !jsonObj.get("blockedAuthMethods").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `blockedAuthMethods` to be an array in the JSON string but got `%s`", jsonObj.get("blockedAuthMethods").toString()));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("verificationStatus") != null && !jsonObj.get("verificationStatus").isJsonNull()) && !jsonObj.get("verificationStatus").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `verificationStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("verificationStatus").toString()));
      }
      if ((jsonObj.get("mobile") != null && !jsonObj.get("mobile").isJsonNull()) && !jsonObj.get("mobile").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `mobile` to be a primitive type in the JSON string but got `%s`", jsonObj.get("mobile").toString()));
      }
      if ((jsonObj.get("verificationType") != null && !jsonObj.get("verificationType").isJsonNull()) && !jsonObj.get("verificationType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `verificationType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("verificationType").toString()));
      }
      if ((jsonObj.get("isDocumentAccount") != null && !jsonObj.get("isDocumentAccount").isJsonNull()) && !jsonObj.get("isDocumentAccount").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `isDocumentAccount` to be a primitive type in the JSON string but got `%s`", jsonObj.get("isDocumentAccount").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AbhaApiV3ProfileLoginSearchPost200ResponseInner.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AbhaApiV3ProfileLoginSearchPost200ResponseInner' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AbhaApiV3ProfileLoginSearchPost200ResponseInner> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3ProfileLoginSearchPost200ResponseInner.class));

       return (TypeAdapter<T>) new TypeAdapter<AbhaApiV3ProfileLoginSearchPost200ResponseInner>() {
           @Override
           public void write(JsonWriter out, AbhaApiV3ProfileLoginSearchPost200ResponseInner value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AbhaApiV3ProfileLoginSearchPost200ResponseInner read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AbhaApiV3ProfileLoginSearchPost200ResponseInner given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AbhaApiV3ProfileLoginSearchPost200ResponseInner
   * @throws IOException if the JSON string is invalid with respect to AbhaApiV3ProfileLoginSearchPost200ResponseInner
   */
  public static AbhaApiV3ProfileLoginSearchPost200ResponseInner fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AbhaApiV3ProfileLoginSearchPost200ResponseInner.class);
  }

  /**
   * Convert an instance of AbhaApiV3ProfileLoginSearchPost200ResponseInner to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

