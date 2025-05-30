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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-21T15:03:26.931725+05:30[Asia/Kolkata]", comments = "Generator version: 7.11.0")
public class AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 {
  public static final String SERIALIZED_NAME_HEALTH_ID_NUMBER = "healthIdNumber";
  @SerializedName(SERIALIZED_NAME_HEALTH_ID_NUMBER)
  @javax.annotation.Nullable
  private String healthIdNumber;

  public static final String SERIALIZED_NAME_HEALTH_ID = "healthId";
  @SerializedName(SERIALIZED_NAME_HEALTH_ID)
  @javax.annotation.Nullable
  private String healthId;

  public static final String SERIALIZED_NAME_MOBILE = "mobile";
  @SerializedName(SERIALIZED_NAME_MOBILE)
  @javax.annotation.Nullable
  private String mobile;

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

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_YEAR_OF_BIRTH = "yearOfBirth";
  @SerializedName(SERIALIZED_NAME_YEAR_OF_BIRTH)
  @javax.annotation.Nullable
  private String yearOfBirth;

  public static final String SERIALIZED_NAME_DAY_OF_BIRTH = "dayOfBirth";
  @SerializedName(SERIALIZED_NAME_DAY_OF_BIRTH)
  @javax.annotation.Nullable
  private String dayOfBirth;

  public static final String SERIALIZED_NAME_MONTH_OF_BIRTH = "monthOfBirth";
  @SerializedName(SERIALIZED_NAME_MONTH_OF_BIRTH)
  @javax.annotation.Nullable
  private String monthOfBirth;

  public static final String SERIALIZED_NAME_GENDER = "gender";
  @SerializedName(SERIALIZED_NAME_GENDER)
  @javax.annotation.Nullable
  private String gender;

  public static final String SERIALIZED_NAME_STATE_CODE = "stateCode";
  @SerializedName(SERIALIZED_NAME_STATE_CODE)
  @javax.annotation.Nullable
  private String stateCode;

  public static final String SERIALIZED_NAME_DISTRICT_CODE = "districtCode";
  @SerializedName(SERIALIZED_NAME_DISTRICT_CODE)
  @javax.annotation.Nullable
  private String districtCode;

  public static final String SERIALIZED_NAME_STATE_NAME = "stateName";
  @SerializedName(SERIALIZED_NAME_STATE_NAME)
  @javax.annotation.Nullable
  private String stateName;

  public static final String SERIALIZED_NAME_DISTRICT_NAME = "districtName";
  @SerializedName(SERIALIZED_NAME_DISTRICT_NAME)
  @javax.annotation.Nullable
  private String districtName;

  public static final String SERIALIZED_NAME_KYC_VERIFIED = "kycVerified";
  @SerializedName(SERIALIZED_NAME_KYC_VERIFIED)
  @javax.annotation.Nullable
  private Boolean kycVerified;

  public static final String SERIALIZED_NAME_TOKEN = "token";
  @SerializedName(SERIALIZED_NAME_TOKEN)
  @javax.annotation.Nullable
  private String token;

  public static final String SERIALIZED_NAME_JWT_RESPONSE = "jwtResponse";
  @SerializedName(SERIALIZED_NAME_JWT_RESPONSE)
  @javax.annotation.Nullable
  private AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens jwtResponse;

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  @javax.annotation.Nullable
  private String status;

  public static final String SERIALIZED_NAME_NEW = "new";
  @SerializedName(SERIALIZED_NAME_NEW)
  @javax.annotation.Nullable
  private String _new;

  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1() {
  }

  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 healthIdNumber(@javax.annotation.Nullable String healthIdNumber) {
    this.healthIdNumber = healthIdNumber;
    return this;
  }

  /**
   * Get healthIdNumber
   * @return healthIdNumber
   */
  @javax.annotation.Nullable
  public String getHealthIdNumber() {
    return healthIdNumber;
  }

  public void setHealthIdNumber(@javax.annotation.Nullable String healthIdNumber) {
    this.healthIdNumber = healthIdNumber;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 healthId(@javax.annotation.Nullable String healthId) {
    this.healthId = healthId;
    return this;
  }

  /**
   * Get healthId
   * @return healthId
   */
  @javax.annotation.Nullable
  public String getHealthId() {
    return healthId;
  }

  public void setHealthId(@javax.annotation.Nullable String healthId) {
    this.healthId = healthId;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 mobile(@javax.annotation.Nullable String mobile) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 firstName(@javax.annotation.Nullable String firstName) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 middleName(@javax.annotation.Nullable String middleName) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 lastName(@javax.annotation.Nullable String lastName) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 name(@javax.annotation.Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(@javax.annotation.Nullable String name) {
    this.name = name;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 yearOfBirth(@javax.annotation.Nullable String yearOfBirth) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 dayOfBirth(@javax.annotation.Nullable String dayOfBirth) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 monthOfBirth(@javax.annotation.Nullable String monthOfBirth) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 gender(@javax.annotation.Nullable String gender) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 stateCode(@javax.annotation.Nullable String stateCode) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 districtCode(@javax.annotation.Nullable String districtCode) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 stateName(@javax.annotation.Nullable String stateName) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 districtName(@javax.annotation.Nullable String districtName) {
    this.districtName = districtName;
    return this;
  }

  /**
   * Get districtName
   * @return districtName
   */
  @javax.annotation.Nullable
  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(@javax.annotation.Nullable String districtName) {
    this.districtName = districtName;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 kycVerified(@javax.annotation.Nullable Boolean kycVerified) {
    this.kycVerified = kycVerified;
    return this;
  }

  /**
   * Get kycVerified
   * @return kycVerified
   */
  @javax.annotation.Nullable
  public Boolean getKycVerified() {
    return kycVerified;
  }

  public void setKycVerified(@javax.annotation.Nullable Boolean kycVerified) {
    this.kycVerified = kycVerified;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 token(@javax.annotation.Nullable String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  @javax.annotation.Nullable
  public String getToken() {
    return token;
  }

  public void setToken(@javax.annotation.Nullable String token) {
    this.token = token;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 jwtResponse(@javax.annotation.Nullable AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens jwtResponse) {
    this.jwtResponse = jwtResponse;
    return this;
  }

  /**
   * Get jwtResponse
   * @return jwtResponse
   */
  @javax.annotation.Nullable
  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens getJwtResponse() {
    return jwtResponse;
  }

  public void setJwtResponse(@javax.annotation.Nullable AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens jwtResponse) {
    this.jwtResponse = jwtResponse;
  }


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 status(@javax.annotation.Nullable String status) {
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


  public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 _new(@javax.annotation.Nullable String _new) {
    this._new = _new;
    return this;
  }

  /**
   * Get _new
   * @return _new
   */
  @javax.annotation.Nullable
  public String getNew() {
    return _new;
  }

  public void setNew(@javax.annotation.Nullable String _new) {
    this._new = _new;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 = (AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1) o;
    return Objects.equals(this.healthIdNumber, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.healthIdNumber) &&
        Objects.equals(this.healthId, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.healthId) &&
        Objects.equals(this.mobile, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.mobile) &&
        Objects.equals(this.firstName, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.firstName) &&
        Objects.equals(this.middleName, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.middleName) &&
        Objects.equals(this.lastName, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.lastName) &&
        Objects.equals(this.name, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.name) &&
        Objects.equals(this.yearOfBirth, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.yearOfBirth) &&
        Objects.equals(this.dayOfBirth, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.dayOfBirth) &&
        Objects.equals(this.monthOfBirth, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.monthOfBirth) &&
        Objects.equals(this.gender, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.gender) &&
        Objects.equals(this.stateCode, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.stateCode) &&
        Objects.equals(this.districtCode, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.districtCode) &&
        Objects.equals(this.stateName, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.stateName) &&
        Objects.equals(this.districtName, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.districtName) &&
        Objects.equals(this.kycVerified, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.kycVerified) &&
        Objects.equals(this.token, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.token) &&
        Objects.equals(this.jwtResponse, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.jwtResponse) &&
        Objects.equals(this.status, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.status) &&
        Objects.equals(this._new, abhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1._new);
  }

  @Override
  public int hashCode() {
    return Objects.hash(healthIdNumber, healthId, mobile, firstName, middleName, lastName, name, yearOfBirth, dayOfBirth, monthOfBirth, gender, stateCode, districtCode, stateName, districtName, kycVerified, token, jwtResponse, status, _new);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 {\n");
    sb.append("    healthIdNumber: ").append(toIndentedString(healthIdNumber)).append("\n");
    sb.append("    healthId: ").append(toIndentedString(healthId)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    yearOfBirth: ").append(toIndentedString(yearOfBirth)).append("\n");
    sb.append("    dayOfBirth: ").append(toIndentedString(dayOfBirth)).append("\n");
    sb.append("    monthOfBirth: ").append(toIndentedString(monthOfBirth)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    stateCode: ").append(toIndentedString(stateCode)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    stateName: ").append(toIndentedString(stateName)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    kycVerified: ").append(toIndentedString(kycVerified)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    jwtResponse: ").append(toIndentedString(jwtResponse)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    _new: ").append(toIndentedString(_new)).append("\n");
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
    openapiFields.add("healthIdNumber");
    openapiFields.add("healthId");
    openapiFields.add("mobile");
    openapiFields.add("firstName");
    openapiFields.add("middleName");
    openapiFields.add("lastName");
    openapiFields.add("name");
    openapiFields.add("yearOfBirth");
    openapiFields.add("dayOfBirth");
    openapiFields.add("monthOfBirth");
    openapiFields.add("gender");
    openapiFields.add("stateCode");
    openapiFields.add("districtCode");
    openapiFields.add("stateName");
    openapiFields.add("districtName");
    openapiFields.add("kycVerified");
    openapiFields.add("token");
    openapiFields.add("jwtResponse");
    openapiFields.add("status");
    openapiFields.add("new");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 is not found in the empty JSON string", AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("healthIdNumber") != null && !jsonObj.get("healthIdNumber").isJsonNull()) && !jsonObj.get("healthIdNumber").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `healthIdNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("healthIdNumber").toString()));
      }
      if ((jsonObj.get("healthId") != null && !jsonObj.get("healthId").isJsonNull()) && !jsonObj.get("healthId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `healthId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("healthId").toString()));
      }
      if ((jsonObj.get("mobile") != null && !jsonObj.get("mobile").isJsonNull()) && !jsonObj.get("mobile").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `mobile` to be a primitive type in the JSON string but got `%s`", jsonObj.get("mobile").toString()));
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
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("yearOfBirth") != null && !jsonObj.get("yearOfBirth").isJsonNull()) && !jsonObj.get("yearOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `yearOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("yearOfBirth").toString()));
      }
      if ((jsonObj.get("dayOfBirth") != null && !jsonObj.get("dayOfBirth").isJsonNull()) && !jsonObj.get("dayOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dayOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dayOfBirth").toString()));
      }
      if ((jsonObj.get("monthOfBirth") != null && !jsonObj.get("monthOfBirth").isJsonNull()) && !jsonObj.get("monthOfBirth").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `monthOfBirth` to be a primitive type in the JSON string but got `%s`", jsonObj.get("monthOfBirth").toString()));
      }
      if ((jsonObj.get("gender") != null && !jsonObj.get("gender").isJsonNull()) && !jsonObj.get("gender").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `gender` to be a primitive type in the JSON string but got `%s`", jsonObj.get("gender").toString()));
      }
      if ((jsonObj.get("stateCode") != null && !jsonObj.get("stateCode").isJsonNull()) && !jsonObj.get("stateCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateCode").toString()));
      }
      if ((jsonObj.get("districtCode") != null && !jsonObj.get("districtCode").isJsonNull()) && !jsonObj.get("districtCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `districtCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("districtCode").toString()));
      }
      if ((jsonObj.get("stateName") != null && !jsonObj.get("stateName").isJsonNull()) && !jsonObj.get("stateName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateName").toString()));
      }
      if ((jsonObj.get("districtName") != null && !jsonObj.get("districtName").isJsonNull()) && !jsonObj.get("districtName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `districtName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("districtName").toString()));
      }
      if ((jsonObj.get("token") != null && !jsonObj.get("token").isJsonNull()) && !jsonObj.get("token").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `token` to be a primitive type in the JSON string but got `%s`", jsonObj.get("token").toString()));
      }
      // validate the optional field `jwtResponse`
      if (jsonObj.get("jwtResponse") != null && !jsonObj.get("jwtResponse").isJsonNull()) {
        AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens.validateJsonElement(jsonObj.get("jwtResponse"));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("new") != null && !jsonObj.get("new").isJsonNull()) && !jsonObj.get("new").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `new` to be a primitive type in the JSON string but got `%s`", jsonObj.get("new").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.class));

       return (TypeAdapter<T>) new TypeAdapter<AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1>() {
           @Override
           public void write(JsonWriter out, AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1
   * @throws IOException if the JSON string is invalid with respect to AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1
   */
  public static AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1.class);
  }

  /**
   * Convert an instance of AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOf1 to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

