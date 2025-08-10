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
 * AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-21T15:03:26.931725+05:30[Asia/Kolkata]", comments = "Generator version: 7.11.0")
public class AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response {
  public static final String SERIALIZED_NAME_ABHA_ADDRESS = "abhaAddress";
  @SerializedName(SERIALIZED_NAME_ABHA_ADDRESS)
  @javax.annotation.Nullable
  private String abhaAddress;

  public static final String SERIALIZED_NAME_FULL_NAME = "fullName";
  @SerializedName(SERIALIZED_NAME_FULL_NAME)
  @javax.annotation.Nullable
  private String fullName;

  public static final String SERIALIZED_NAME_PROFILE_PHOTO = "profilePhoto";
  @SerializedName(SERIALIZED_NAME_PROFILE_PHOTO)
  @javax.annotation.Nullable
  private String profilePhoto;

  public static final String SERIALIZED_NAME_FIRST_NAME = "firstName";
  @SerializedName(SERIALIZED_NAME_FIRST_NAME)
  @javax.annotation.Nullable
  private String firstName;

  public static final String SERIALIZED_NAME_MIDDLE_NAME = "middleName";
  @SerializedName(SERIALIZED_NAME_MIDDLE_NAME)
  @javax.annotation.Nullable
  private String middleName;

  public static final String SERIALIZED_NAME_LAST_NAME = "lastName";
  @SerializedName(SERIALIZED_NAME_LAST_NAME)
  @javax.annotation.Nullable
  private String lastName;

  public static final String SERIALIZED_NAME_DAY_OF_BIRTH = "dayOfBirth";
  @SerializedName(SERIALIZED_NAME_DAY_OF_BIRTH)
  @javax.annotation.Nullable
  private String dayOfBirth;

  public static final String SERIALIZED_NAME_MONTH_OF_BIRTH = "monthOfBirth";
  @SerializedName(SERIALIZED_NAME_MONTH_OF_BIRTH)
  @javax.annotation.Nullable
  private String monthOfBirth;

  public static final String SERIALIZED_NAME_YEAR_OF_BIRTH = "yearOfBirth";
  @SerializedName(SERIALIZED_NAME_YEAR_OF_BIRTH)
  @javax.annotation.Nullable
  private String yearOfBirth;

  public static final String SERIALIZED_NAME_DATE_OF_BIRTH = "dateOfBirth";
  @SerializedName(SERIALIZED_NAME_DATE_OF_BIRTH)
  @javax.annotation.Nullable
  private String dateOfBirth;

  public static final String SERIALIZED_NAME_GENDER = "gender";
  @SerializedName(SERIALIZED_NAME_GENDER)
  @javax.annotation.Nullable
  private String gender;

  public static final String SERIALIZED_NAME_EMAIL = "email";
  @SerializedName(SERIALIZED_NAME_EMAIL)
  @javax.annotation.Nullable
  private String email;

  public static final String SERIALIZED_NAME_MOBILE = "mobile";
  @SerializedName(SERIALIZED_NAME_MOBILE)
  @javax.annotation.Nullable
  private String mobile;

  public static final String SERIALIZED_NAME_ABHA_NUMBER = "abhaNumber";
  @SerializedName(SERIALIZED_NAME_ABHA_NUMBER)
  @javax.annotation.Nullable
  private String abhaNumber;

  public static final String SERIALIZED_NAME_ADDRESS = "address";
  @SerializedName(SERIALIZED_NAME_ADDRESS)
  @javax.annotation.Nullable
  private String address;

  public static final String SERIALIZED_NAME_STATE_NAME = "stateName";
  @SerializedName(SERIALIZED_NAME_STATE_NAME)
  @javax.annotation.Nullable
  private String stateName;

  public static final String SERIALIZED_NAME_PIN_CODE = "pinCode";
  @SerializedName(SERIALIZED_NAME_PIN_CODE)
  @javax.annotation.Nullable
  private String pinCode;

  public static final String SERIALIZED_NAME_STATE_CODE = "stateCode";
  @SerializedName(SERIALIZED_NAME_STATE_CODE)
  @javax.annotation.Nullable
  private String stateCode;

  public static final String SERIALIZED_NAME_DISTRICT_CODE = "districtCode";
  @SerializedName(SERIALIZED_NAME_DISTRICT_CODE)
  @javax.annotation.Nullable
  private String districtCode;

  public static final String SERIALIZED_NAME_AUTH_METHODS = "authMethods";
  @SerializedName(SERIALIZED_NAME_AUTH_METHODS)
  @javax.annotation.Nullable
  private List<String> authMethods = new ArrayList<>();

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  @javax.annotation.Nullable
  private String status;

  public static final String SERIALIZED_NAME_SUB_DISTRICT_CODE = "subDistrictCode";
  @SerializedName(SERIALIZED_NAME_SUB_DISTRICT_CODE)
  @javax.annotation.Nullable
  private String subDistrictCode;

  public static final String SERIALIZED_NAME_SUB_DISTRICT_NAME = "subDistrictName";
  @SerializedName(SERIALIZED_NAME_SUB_DISTRICT_NAME)
  @javax.annotation.Nullable
  private String subDistrictName;

  public static final String SERIALIZED_NAME_EMAIL_VERIFIED = "emailVerified";
  @SerializedName(SERIALIZED_NAME_EMAIL_VERIFIED)
  @javax.annotation.Nullable
  private String emailVerified;

  public static final String SERIALIZED_NAME_MOBILE_VERIFIED = "mobileVerified";
  @SerializedName(SERIALIZED_NAME_MOBILE_VERIFIED)
  @javax.annotation.Nullable
  private String mobileVerified;

  public static final String SERIALIZED_NAME_KYC_STATUS = "kycStatus";
  @SerializedName(SERIALIZED_NAME_KYC_STATUS)
  @javax.annotation.Nullable
  private String kycStatus;

  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response() {
  }

  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response abhaAddress(@javax.annotation.Nullable String abhaAddress) {
    this.abhaAddress = abhaAddress;
    return this;
  }

  /**
   * Get abhaAddress
   * @return abhaAddress
   */
  @javax.annotation.Nullable
  public String getAbhaAddress() {
    return abhaAddress;
  }

  public void setAbhaAddress(@javax.annotation.Nullable String abhaAddress) {
    this.abhaAddress = abhaAddress;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response fullName(@javax.annotation.Nullable String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
   */
  @javax.annotation.Nullable
  public String getFullName() {
    return fullName;
  }

  public void setFullName(@javax.annotation.Nullable String fullName) {
    this.fullName = fullName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response profilePhoto(@javax.annotation.Nullable String profilePhoto) {
    this.profilePhoto = profilePhoto;
    return this;
  }

  /**
   * Get profilePhoto
   * @return profilePhoto
   */
  @javax.annotation.Nullable
  public String getProfilePhoto() {
    return profilePhoto;
  }

  public void setProfilePhoto(@javax.annotation.Nullable String profilePhoto) {
    this.profilePhoto = profilePhoto;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response firstName(@javax.annotation.Nullable String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  @javax.annotation.Nullable
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(@javax.annotation.Nullable String firstName) {
    this.firstName = firstName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response middleName(@javax.annotation.Nullable String middleName) {
    this.middleName = middleName;
    return this;
  }

  /**
   * Get middleName
   * @return middleName
   */
  @javax.annotation.Nullable
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(@javax.annotation.Nullable String middleName) {
    this.middleName = middleName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response lastName(@javax.annotation.Nullable String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  @javax.annotation.Nullable
  public String getLastName() {
    return lastName;
  }

  public void setLastName(@javax.annotation.Nullable String lastName) {
    this.lastName = lastName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response dayOfBirth(@javax.annotation.Nullable String dayOfBirth) {
    this.dayOfBirth = dayOfBirth;
    return this;
  }

  /**
   * Get dayOfBirth
   * @return dayOfBirth
   */
  @javax.annotation.Nullable
  public String getDayOfBirth() {
    return dayOfBirth;
  }

  public void setDayOfBirth(@javax.annotation.Nullable String dayOfBirth) {
    this.dayOfBirth = dayOfBirth;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response monthOfBirth(@javax.annotation.Nullable String monthOfBirth) {
    this.monthOfBirth = monthOfBirth;
    return this;
  }

  /**
   * Get monthOfBirth
   * @return monthOfBirth
   */
  @javax.annotation.Nullable
  public String getMonthOfBirth() {
    return monthOfBirth;
  }

  public void setMonthOfBirth(@javax.annotation.Nullable String monthOfBirth) {
    this.monthOfBirth = monthOfBirth;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response yearOfBirth(@javax.annotation.Nullable String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
    return this;
  }

  /**
   * Get yearOfBirth
   * @return yearOfBirth
   */
  @javax.annotation.Nullable
  public String getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(@javax.annotation.Nullable String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response dateOfBirth(@javax.annotation.Nullable String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  /**
   * Get dateOfBirth
   * @return dateOfBirth
   */
  @javax.annotation.Nullable
  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(@javax.annotation.Nullable String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response gender(@javax.annotation.Nullable String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
   */
  @javax.annotation.Nullable
  public String getGender() {
    return gender;
  }

  public void setGender(@javax.annotation.Nullable String gender) {
    this.gender = gender;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response email(@javax.annotation.Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @javax.annotation.Nullable
  public String getEmail() {
    return email;
  }

  public void setEmail(@javax.annotation.Nullable String email) {
    this.email = email;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response mobile(@javax.annotation.Nullable String mobile) {
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


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response abhaNumber(@javax.annotation.Nullable String abhaNumber) {
    this.abhaNumber = abhaNumber;
    return this;
  }

  /**
   * Get abhaNumber
   * @return abhaNumber
   */
  @javax.annotation.Nullable
  public String getAbhaNumber() {
    return abhaNumber;
  }

  public void setAbhaNumber(@javax.annotation.Nullable String abhaNumber) {
    this.abhaNumber = abhaNumber;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response address(@javax.annotation.Nullable String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
   */
  @javax.annotation.Nullable
  public String getAddress() {
    return address;
  }

  public void setAddress(@javax.annotation.Nullable String address) {
    this.address = address;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response stateName(@javax.annotation.Nullable String stateName) {
    this.stateName = stateName;
    return this;
  }

  /**
   * Get stateName
   * @return stateName
   */
  @javax.annotation.Nullable
  public String getStateName() {
    return stateName;
  }

  public void setStateName(@javax.annotation.Nullable String stateName) {
    this.stateName = stateName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response pinCode(@javax.annotation.Nullable String pinCode) {
    this.pinCode = pinCode;
    return this;
  }

  /**
   * Get pinCode
   * @return pinCode
   */
  @javax.annotation.Nullable
  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(@javax.annotation.Nullable String pinCode) {
    this.pinCode = pinCode;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response stateCode(@javax.annotation.Nullable String stateCode) {
    this.stateCode = stateCode;
    return this;
  }

  /**
   * Get stateCode
   * @return stateCode
   */
  @javax.annotation.Nullable
  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(@javax.annotation.Nullable String stateCode) {
    this.stateCode = stateCode;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response districtCode(@javax.annotation.Nullable String districtCode) {
    this.districtCode = districtCode;
    return this;
  }

  /**
   * Get districtCode
   * @return districtCode
   */
  @javax.annotation.Nullable
  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(@javax.annotation.Nullable String districtCode) {
    this.districtCode = districtCode;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response authMethods(@javax.annotation.Nullable List<String> authMethods) {
    this.authMethods = authMethods;
    return this;
  }

  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response addAuthMethodsItem(String authMethodsItem) {
    if (this.authMethods == null) {
      this.authMethods = new ArrayList<>();
    }
    this.authMethods.add(authMethodsItem);
    return this;
  }

  /**
   * Get authMethods
   * @return authMethods
   */
  @javax.annotation.Nullable
  public List<String> getAuthMethods() {
    return authMethods;
  }

  public void setAuthMethods(@javax.annotation.Nullable List<String> authMethods) {
    this.authMethods = authMethods;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response status(@javax.annotation.Nullable String status) {
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


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response subDistrictCode(@javax.annotation.Nullable String subDistrictCode) {
    this.subDistrictCode = subDistrictCode;
    return this;
  }

  /**
   * Get subDistrictCode
   * @return subDistrictCode
   */
  @javax.annotation.Nullable
  public String getSubDistrictCode() {
    return subDistrictCode;
  }

  public void setSubDistrictCode(@javax.annotation.Nullable String subDistrictCode) {
    this.subDistrictCode = subDistrictCode;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response subDistrictName(@javax.annotation.Nullable String subDistrictName) {
    this.subDistrictName = subDistrictName;
    return this;
  }

  /**
   * Get subDistrictName
   * @return subDistrictName
   */
  @javax.annotation.Nullable
  public String getSubDistrictName() {
    return subDistrictName;
  }

  public void setSubDistrictName(@javax.annotation.Nullable String subDistrictName) {
    this.subDistrictName = subDistrictName;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response emailVerified(@javax.annotation.Nullable String emailVerified) {
    this.emailVerified = emailVerified;
    return this;
  }

  /**
   * Get emailVerified
   * @return emailVerified
   */
  @javax.annotation.Nullable
  public String getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(@javax.annotation.Nullable String emailVerified) {
    this.emailVerified = emailVerified;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response mobileVerified(@javax.annotation.Nullable String mobileVerified) {
    this.mobileVerified = mobileVerified;
    return this;
  }

  /**
   * Get mobileVerified
   * @return mobileVerified
   */
  @javax.annotation.Nullable
  public String getMobileVerified() {
    return mobileVerified;
  }

  public void setMobileVerified(@javax.annotation.Nullable String mobileVerified) {
    this.mobileVerified = mobileVerified;
  }


  public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response kycStatus(@javax.annotation.Nullable String kycStatus) {
    this.kycStatus = kycStatus;
    return this;
  }

  /**
   * Get kycStatus
   * @return kycStatus
   */
  @javax.annotation.Nullable
  public String getKycStatus() {
    return kycStatus;
  }

  public void setKycStatus(@javax.annotation.Nullable String kycStatus) {
    this.kycStatus = kycStatus;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response = (AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response) o;
    return Objects.equals(this.abhaAddress, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.abhaAddress) &&
        Objects.equals(this.fullName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.fullName) &&
        Objects.equals(this.profilePhoto, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.profilePhoto) &&
        Objects.equals(this.firstName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.firstName) &&
        Objects.equals(this.middleName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.middleName) &&
        Objects.equals(this.lastName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.lastName) &&
        Objects.equals(this.dayOfBirth, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.dayOfBirth) &&
        Objects.equals(this.monthOfBirth, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.monthOfBirth) &&
        Objects.equals(this.yearOfBirth, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.yearOfBirth) &&
        Objects.equals(this.dateOfBirth, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.dateOfBirth) &&
        Objects.equals(this.gender, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.gender) &&
        Objects.equals(this.email, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.email) &&
        Objects.equals(this.mobile, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.mobile) &&
        Objects.equals(this.abhaNumber, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.abhaNumber) &&
        Objects.equals(this.address, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.address) &&
        Objects.equals(this.stateName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.stateName) &&
        Objects.equals(this.pinCode, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.pinCode) &&
        Objects.equals(this.stateCode, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.stateCode) &&
        Objects.equals(this.districtCode, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.districtCode) &&
        Objects.equals(this.authMethods, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.authMethods) &&
        Objects.equals(this.status, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.status) &&
        Objects.equals(this.subDistrictCode, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.subDistrictCode) &&
        Objects.equals(this.subDistrictName, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.subDistrictName) &&
        Objects.equals(this.emailVerified, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.emailVerified) &&
        Objects.equals(this.mobileVerified, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.mobileVerified) &&
        Objects.equals(this.kycStatus, abhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.kycStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abhaAddress, fullName, profilePhoto, firstName, middleName, lastName, dayOfBirth, monthOfBirth, yearOfBirth, dateOfBirth, gender, email, mobile, abhaNumber, address, stateName, pinCode, stateCode, districtCode, authMethods, status, subDistrictCode, subDistrictName, emailVerified, mobileVerified, kycStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response {\n");
    sb.append("    abhaAddress: ").append(toIndentedString(abhaAddress)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    profilePhoto: ").append(toIndentedString(profilePhoto)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    dayOfBirth: ").append(toIndentedString(dayOfBirth)).append("\n");
    sb.append("    monthOfBirth: ").append(toIndentedString(monthOfBirth)).append("\n");
    sb.append("    yearOfBirth: ").append(toIndentedString(yearOfBirth)).append("\n");
    sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    abhaNumber: ").append(toIndentedString(abhaNumber)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    stateName: ").append(toIndentedString(stateName)).append("\n");
    sb.append("    pinCode: ").append(toIndentedString(pinCode)).append("\n");
    sb.append("    stateCode: ").append(toIndentedString(stateCode)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    authMethods: ").append(toIndentedString(authMethods)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    subDistrictCode: ").append(toIndentedString(subDistrictCode)).append("\n");
    sb.append("    subDistrictName: ").append(toIndentedString(subDistrictName)).append("\n");
    sb.append("    emailVerified: ").append(toIndentedString(emailVerified)).append("\n");
    sb.append("    mobileVerified: ").append(toIndentedString(mobileVerified)).append("\n");
    sb.append("    kycStatus: ").append(toIndentedString(kycStatus)).append("\n");
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
    openapiFields.add("abhaAddress");
    openapiFields.add("fullName");
    openapiFields.add("profilePhoto");
    openapiFields.add("firstName");
    openapiFields.add("middleName");
    openapiFields.add("lastName");
    openapiFields.add("dayOfBirth");
    openapiFields.add("monthOfBirth");
    openapiFields.add("yearOfBirth");
    openapiFields.add("dateOfBirth");
    openapiFields.add("gender");
    openapiFields.add("email");
    openapiFields.add("mobile");
    openapiFields.add("abhaNumber");
    openapiFields.add("address");
    openapiFields.add("stateName");
    openapiFields.add("pinCode");
    openapiFields.add("stateCode");
    openapiFields.add("districtCode");
    openapiFields.add("authMethods");
    openapiFields.add("status");
    openapiFields.add("subDistrictCode");
    openapiFields.add("subDistrictName");
    openapiFields.add("emailVerified");
    openapiFields.add("mobileVerified");
    openapiFields.add("kycStatus");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response is not found in the empty JSON string", AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("abhaAddress") != null && !jsonObj.get("abhaAddress").isJsonNull()) && !jsonObj.get("abhaAddress").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `abhaAddress` to be a primitive type in the JSON string but got `%s`", jsonObj.get("abhaAddress").toString()));
      }
      if ((jsonObj.get("fullName") != null && !jsonObj.get("fullName").isJsonNull()) && !jsonObj.get("fullName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `fullName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("fullName").toString()));
      }
      if ((jsonObj.get("profilePhoto") != null && !jsonObj.get("profilePhoto").isJsonNull()) && !jsonObj.get("profilePhoto").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `profilePhoto` to be a primitive type in the JSON string but got `%s`", jsonObj.get("profilePhoto").toString()));
      }
      if ((jsonObj.get("firstName") != null && !jsonObj.get("firstName").isJsonNull()) && !jsonObj.get("firstName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `firstName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("firstName").toString()));
      }
      if ((jsonObj.get("middleName") != null && !jsonObj.get("middleName").isJsonNull()) && !jsonObj.get("middleName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `middleName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("middleName").toString()));
      }
      if ((jsonObj.get("lastName") != null && !jsonObj.get("lastName").isJsonNull()) && !jsonObj.get("lastName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `lastName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("lastName").toString()));
      }
      if ((jsonObj.get("dayOfBirth") != null && !jsonObj.get("dayOfBirth").isJsonNull()) && !jsonObj.get("dayOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dayOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dayOfBirth").toString()));
      }
      if ((jsonObj.get("monthOfBirth") != null && !jsonObj.get("monthOfBirth").isJsonNull()) && !jsonObj.get("monthOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `monthOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("monthOfBirth").toString()));
      }
      if ((jsonObj.get("yearOfBirth") != null && !jsonObj.get("yearOfBirth").isJsonNull()) && !jsonObj.get("yearOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `yearOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("yearOfBirth").toString()));
      }
      if ((jsonObj.get("dateOfBirth") != null && !jsonObj.get("dateOfBirth").isJsonNull()) && !jsonObj.get("dateOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dateOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dateOfBirth").toString()));
      }
      if ((jsonObj.get("gender") != null && !jsonObj.get("gender").isJsonNull()) && !jsonObj.get("gender").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `gender` to be a primitive type in the JSON string but got `%s`", jsonObj.get("gender").toString()));
      }
      if ((jsonObj.get("email") != null && !jsonObj.get("email").isJsonNull()) && !jsonObj.get("email").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `email` to be a primitive type in the JSON string but got `%s`", jsonObj.get("email").toString()));
      }
      if ((jsonObj.get("mobile") != null && !jsonObj.get("mobile").isJsonNull()) && !jsonObj.get("mobile").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `mobile` to be a primitive type in the JSON string but got `%s`", jsonObj.get("mobile").toString()));
      }
      if ((jsonObj.get("abhaNumber") != null && !jsonObj.get("abhaNumber").isJsonNull()) && !jsonObj.get("abhaNumber").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `abhaNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("abhaNumber").toString()));
      }
      if ((jsonObj.get("address") != null && !jsonObj.get("address").isJsonNull()) && !jsonObj.get("address").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `address` to be a primitive type in the JSON string but got `%s`", jsonObj.get("address").toString()));
      }
      if ((jsonObj.get("stateName") != null && !jsonObj.get("stateName").isJsonNull()) && !jsonObj.get("stateName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateName").toString()));
      }
      if ((jsonObj.get("pinCode") != null && !jsonObj.get("pinCode").isJsonNull()) && !jsonObj.get("pinCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `pinCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("pinCode").toString()));
      }
      if ((jsonObj.get("stateCode") != null && !jsonObj.get("stateCode").isJsonNull()) && !jsonObj.get("stateCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateCode").toString()));
      }
      if ((jsonObj.get("districtCode") != null && !jsonObj.get("districtCode").isJsonNull()) && !jsonObj.get("districtCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `districtCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("districtCode").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("authMethods") != null && !jsonObj.get("authMethods").isJsonNull() && !jsonObj.get("authMethods").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `authMethods` to be an array in the JSON string but got `%s`", jsonObj.get("authMethods").toString()));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("subDistrictCode") != null && !jsonObj.get("subDistrictCode").isJsonNull()) && !jsonObj.get("subDistrictCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `subDistrictCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subDistrictCode").toString()));
      }
      if ((jsonObj.get("subDistrictName") != null && !jsonObj.get("subDistrictName").isJsonNull()) && !jsonObj.get("subDistrictName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `subDistrictName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subDistrictName").toString()));
      }
      if ((jsonObj.get("emailVerified") != null && !jsonObj.get("emailVerified").isJsonNull()) && !jsonObj.get("emailVerified").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `emailVerified` to be a primitive type in the JSON string but got `%s`", jsonObj.get("emailVerified").toString()));
      }
      if ((jsonObj.get("mobileVerified") != null && !jsonObj.get("mobileVerified").isJsonNull()) && !jsonObj.get("mobileVerified").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `mobileVerified` to be a primitive type in the JSON string but got `%s`", jsonObj.get("mobileVerified").toString()));
      }
      if ((jsonObj.get("kycStatus") != null && !jsonObj.get("kycStatus").isJsonNull()) && !jsonObj.get("kycStatus").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `kycStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("kycStatus").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.class));

       return (TypeAdapter<T>) new TypeAdapter<AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response>() {
           @Override
           public void write(JsonWriter out, AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response
   * @throws IOException if the JSON string is invalid with respect to AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response
   */
  public static AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response.class);
  }

  /**
   * Convert an instance of AbhaApiV3PhrWebLoginProfileAbhaProfileGet200Response to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

