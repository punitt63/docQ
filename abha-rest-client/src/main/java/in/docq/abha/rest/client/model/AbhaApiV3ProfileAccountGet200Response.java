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
 * AbhaApiV3ProfileAccountGet200Response
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-21T15:03:26.931725+05:30[Asia/Kolkata]", comments = "Generator version: 7.11.0")
public class AbhaApiV3ProfileAccountGet200Response {
  public static final String SERIALIZED_NAME_AB_H_A_NUMBER = "ABHANumber";
  @SerializedName(SERIALIZED_NAME_AB_H_A_NUMBER)
  @javax.annotation.Nullable
  private String abHANumber;

  public static final String SERIALIZED_NAME_PREFERRED_ABHA_ADDRESS = "preferredAbhaAddress";
  @SerializedName(SERIALIZED_NAME_PREFERRED_ABHA_ADDRESS)
  @javax.annotation.Nullable
  private String preferredAbhaAddress;

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

  public static final String SERIALIZED_NAME_PROFILE_PHOTO = "profilePhoto";
  @SerializedName(SERIALIZED_NAME_PROFILE_PHOTO)
  @javax.annotation.Nullable
  private String profilePhoto;

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  @javax.annotation.Nullable
  private String status;

  public static final String SERIALIZED_NAME_STATE_CODE = "stateCode";
  @SerializedName(SERIALIZED_NAME_STATE_CODE)
  @javax.annotation.Nullable
  private String stateCode;

  public static final String SERIALIZED_NAME_DISTRICT_CODE = "districtCode";
  @SerializedName(SERIALIZED_NAME_DISTRICT_CODE)
  @javax.annotation.Nullable
  private String districtCode;

  public static final String SERIALIZED_NAME_PINCODE = "pincode";
  @SerializedName(SERIALIZED_NAME_PINCODE)
  @javax.annotation.Nullable
  private String pincode;

  public static final String SERIALIZED_NAME_ADDRESS = "address";
  @SerializedName(SERIALIZED_NAME_ADDRESS)
  @javax.annotation.Nullable
  private String address;

  public static final String SERIALIZED_NAME_KYC_PHOTO = "kycPhoto";
  @SerializedName(SERIALIZED_NAME_KYC_PHOTO)
  @javax.annotation.Nullable
  private String kycPhoto;

  public static final String SERIALIZED_NAME_STATE_NAME = "stateName";
  @SerializedName(SERIALIZED_NAME_STATE_NAME)
  @javax.annotation.Nullable
  private String stateName;

  public static final String SERIALIZED_NAME_DISTRICT_NAME = "districtName";
  @SerializedName(SERIALIZED_NAME_DISTRICT_NAME)
  @javax.annotation.Nullable
  private String districtName;

  public static final String SERIALIZED_NAME_SUBDISTRICT_NAME = "subdistrictName";
  @SerializedName(SERIALIZED_NAME_SUBDISTRICT_NAME)
  @javax.annotation.Nullable
  private String subdistrictName;

  public static final String SERIALIZED_NAME_AUTH_METHODS = "authMethods";
  @SerializedName(SERIALIZED_NAME_AUTH_METHODS)
  @javax.annotation.Nullable
  private List<String> authMethods = new ArrayList<>();

  public static final String SERIALIZED_NAME_TAGS = "tags";
  @SerializedName(SERIALIZED_NAME_TAGS)
  @javax.annotation.Nullable
  private Object tags;

  public static final String SERIALIZED_NAME_KYC_VERIFIED = "kycVerified";
  @SerializedName(SERIALIZED_NAME_KYC_VERIFIED)
  @javax.annotation.Nullable
  private Boolean kycVerified;

  public static final String SERIALIZED_NAME_VERIFICATION_STATUS = "verificationStatus";
  @SerializedName(SERIALIZED_NAME_VERIFICATION_STATUS)
  @javax.annotation.Nullable
  private String verificationStatus;

  public static final String SERIALIZED_NAME_VERIFICATION_TYPE = "verificationType";
  @SerializedName(SERIALIZED_NAME_VERIFICATION_TYPE)
  @javax.annotation.Nullable
  private String verificationType;

  public static final String SERIALIZED_NAME_LOCALIZED_DETAILS = "localizedDetails";
  @SerializedName(SERIALIZED_NAME_LOCALIZED_DETAILS)
  @javax.annotation.Nullable
  private AbhaApiV3ProfileAccountGet200ResponseLocalizedDetails localizedDetails;

  public static final String SERIALIZED_NAME_CREATED_DATE = "createdDate";
  @SerializedName(SERIALIZED_NAME_CREATED_DATE)
  @javax.annotation.Nullable
  private String createdDate;

  public AbhaApiV3ProfileAccountGet200Response() {
  }

  public AbhaApiV3ProfileAccountGet200Response abHANumber(@javax.annotation.Nullable String abHANumber) {
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


  public AbhaApiV3ProfileAccountGet200Response preferredAbhaAddress(@javax.annotation.Nullable String preferredAbhaAddress) {
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


  public AbhaApiV3ProfileAccountGet200Response mobile(@javax.annotation.Nullable String mobile) {
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


  public AbhaApiV3ProfileAccountGet200Response firstName(@javax.annotation.Nullable String firstName) {
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


  public AbhaApiV3ProfileAccountGet200Response middleName(@javax.annotation.Nullable String middleName) {
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


  public AbhaApiV3ProfileAccountGet200Response lastName(@javax.annotation.Nullable String lastName) {
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


  public AbhaApiV3ProfileAccountGet200Response name(@javax.annotation.Nullable String name) {
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


  public AbhaApiV3ProfileAccountGet200Response yearOfBirth(@javax.annotation.Nullable String yearOfBirth) {
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


  public AbhaApiV3ProfileAccountGet200Response dayOfBirth(@javax.annotation.Nullable String dayOfBirth) {
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


  public AbhaApiV3ProfileAccountGet200Response monthOfBirth(@javax.annotation.Nullable String monthOfBirth) {
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


  public AbhaApiV3ProfileAccountGet200Response gender(@javax.annotation.Nullable String gender) {
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


  public AbhaApiV3ProfileAccountGet200Response profilePhoto(@javax.annotation.Nullable String profilePhoto) {
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


  public AbhaApiV3ProfileAccountGet200Response status(@javax.annotation.Nullable String status) {
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


  public AbhaApiV3ProfileAccountGet200Response stateCode(@javax.annotation.Nullable String stateCode) {
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


  public AbhaApiV3ProfileAccountGet200Response districtCode(@javax.annotation.Nullable String districtCode) {
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


  public AbhaApiV3ProfileAccountGet200Response pincode(@javax.annotation.Nullable String pincode) {
    this.pincode = pincode;
    return this;
  }

  /**
   * Get pincode
   * @return pincode
   */
  @javax.annotation.Nullable
  public String getPincode() {
    return pincode;
  }

  public void setPincode(@javax.annotation.Nullable String pincode) {
    this.pincode = pincode;
  }


  public AbhaApiV3ProfileAccountGet200Response address(@javax.annotation.Nullable String address) {
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


  public AbhaApiV3ProfileAccountGet200Response kycPhoto(@javax.annotation.Nullable String kycPhoto) {
    this.kycPhoto = kycPhoto;
    return this;
  }

  /**
   * Get kycPhoto
   * @return kycPhoto
   */
  @javax.annotation.Nullable
  public String getKycPhoto() {
    return kycPhoto;
  }

  public void setKycPhoto(@javax.annotation.Nullable String kycPhoto) {
    this.kycPhoto = kycPhoto;
  }


  public AbhaApiV3ProfileAccountGet200Response stateName(@javax.annotation.Nullable String stateName) {
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


  public AbhaApiV3ProfileAccountGet200Response districtName(@javax.annotation.Nullable String districtName) {
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


  public AbhaApiV3ProfileAccountGet200Response subdistrictName(@javax.annotation.Nullable String subdistrictName) {
    this.subdistrictName = subdistrictName;
    return this;
  }

  /**
   * Get subdistrictName
   * @return subdistrictName
   */
  @javax.annotation.Nullable
  public String getSubdistrictName() {
    return subdistrictName;
  }

  public void setSubdistrictName(@javax.annotation.Nullable String subdistrictName) {
    this.subdistrictName = subdistrictName;
  }


  public AbhaApiV3ProfileAccountGet200Response authMethods(@javax.annotation.Nullable List<String> authMethods) {
    this.authMethods = authMethods;
    return this;
  }

  public AbhaApiV3ProfileAccountGet200Response addAuthMethodsItem(String authMethodsItem) {
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


  public AbhaApiV3ProfileAccountGet200Response tags(@javax.annotation.Nullable Object tags) {
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


  public AbhaApiV3ProfileAccountGet200Response kycVerified(@javax.annotation.Nullable Boolean kycVerified) {
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


  public AbhaApiV3ProfileAccountGet200Response verificationStatus(@javax.annotation.Nullable String verificationStatus) {
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


  public AbhaApiV3ProfileAccountGet200Response verificationType(@javax.annotation.Nullable String verificationType) {
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


  public AbhaApiV3ProfileAccountGet200Response localizedDetails(@javax.annotation.Nullable AbhaApiV3ProfileAccountGet200ResponseLocalizedDetails localizedDetails) {
    this.localizedDetails = localizedDetails;
    return this;
  }

  /**
   * Get localizedDetails
   * @return localizedDetails
   */
  @javax.annotation.Nullable
  public AbhaApiV3ProfileAccountGet200ResponseLocalizedDetails getLocalizedDetails() {
    return localizedDetails;
  }

  public void setLocalizedDetails(@javax.annotation.Nullable AbhaApiV3ProfileAccountGet200ResponseLocalizedDetails localizedDetails) {
    this.localizedDetails = localizedDetails;
  }


  public AbhaApiV3ProfileAccountGet200Response createdDate(@javax.annotation.Nullable String createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  /**
   * Get createdDate
   * @return createdDate
   */
  @javax.annotation.Nullable
  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(@javax.annotation.Nullable String createdDate) {
    this.createdDate = createdDate;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbhaApiV3ProfileAccountGet200Response abhaApiV3ProfileAccountGet200Response = (AbhaApiV3ProfileAccountGet200Response) o;
    return Objects.equals(this.abHANumber, abhaApiV3ProfileAccountGet200Response.abHANumber) &&
        Objects.equals(this.preferredAbhaAddress, abhaApiV3ProfileAccountGet200Response.preferredAbhaAddress) &&
        Objects.equals(this.mobile, abhaApiV3ProfileAccountGet200Response.mobile) &&
        Objects.equals(this.firstName, abhaApiV3ProfileAccountGet200Response.firstName) &&
        Objects.equals(this.middleName, abhaApiV3ProfileAccountGet200Response.middleName) &&
        Objects.equals(this.lastName, abhaApiV3ProfileAccountGet200Response.lastName) &&
        Objects.equals(this.name, abhaApiV3ProfileAccountGet200Response.name) &&
        Objects.equals(this.yearOfBirth, abhaApiV3ProfileAccountGet200Response.yearOfBirth) &&
        Objects.equals(this.dayOfBirth, abhaApiV3ProfileAccountGet200Response.dayOfBirth) &&
        Objects.equals(this.monthOfBirth, abhaApiV3ProfileAccountGet200Response.monthOfBirth) &&
        Objects.equals(this.gender, abhaApiV3ProfileAccountGet200Response.gender) &&
        Objects.equals(this.profilePhoto, abhaApiV3ProfileAccountGet200Response.profilePhoto) &&
        Objects.equals(this.status, abhaApiV3ProfileAccountGet200Response.status) &&
        Objects.equals(this.stateCode, abhaApiV3ProfileAccountGet200Response.stateCode) &&
        Objects.equals(this.districtCode, abhaApiV3ProfileAccountGet200Response.districtCode) &&
        Objects.equals(this.pincode, abhaApiV3ProfileAccountGet200Response.pincode) &&
        Objects.equals(this.address, abhaApiV3ProfileAccountGet200Response.address) &&
        Objects.equals(this.kycPhoto, abhaApiV3ProfileAccountGet200Response.kycPhoto) &&
        Objects.equals(this.stateName, abhaApiV3ProfileAccountGet200Response.stateName) &&
        Objects.equals(this.districtName, abhaApiV3ProfileAccountGet200Response.districtName) &&
        Objects.equals(this.subdistrictName, abhaApiV3ProfileAccountGet200Response.subdistrictName) &&
        Objects.equals(this.authMethods, abhaApiV3ProfileAccountGet200Response.authMethods) &&
        Objects.equals(this.tags, abhaApiV3ProfileAccountGet200Response.tags) &&
        Objects.equals(this.kycVerified, abhaApiV3ProfileAccountGet200Response.kycVerified) &&
        Objects.equals(this.verificationStatus, abhaApiV3ProfileAccountGet200Response.verificationStatus) &&
        Objects.equals(this.verificationType, abhaApiV3ProfileAccountGet200Response.verificationType) &&
        Objects.equals(this.localizedDetails, abhaApiV3ProfileAccountGet200Response.localizedDetails) &&
        Objects.equals(this.createdDate, abhaApiV3ProfileAccountGet200Response.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abHANumber, preferredAbhaAddress, mobile, firstName, middleName, lastName, name, yearOfBirth, dayOfBirth, monthOfBirth, gender, profilePhoto, status, stateCode, districtCode, pincode, address, kycPhoto, stateName, districtName, subdistrictName, authMethods, tags, kycVerified, verificationStatus, verificationType, localizedDetails, createdDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbhaApiV3ProfileAccountGet200Response {\n");
    sb.append("    abHANumber: ").append(toIndentedString(abHANumber)).append("\n");
    sb.append("    preferredAbhaAddress: ").append(toIndentedString(preferredAbhaAddress)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    yearOfBirth: ").append(toIndentedString(yearOfBirth)).append("\n");
    sb.append("    dayOfBirth: ").append(toIndentedString(dayOfBirth)).append("\n");
    sb.append("    monthOfBirth: ").append(toIndentedString(monthOfBirth)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    profilePhoto: ").append(toIndentedString(profilePhoto)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    stateCode: ").append(toIndentedString(stateCode)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    pincode: ").append(toIndentedString(pincode)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    kycPhoto: ").append(toIndentedString(kycPhoto)).append("\n");
    sb.append("    stateName: ").append(toIndentedString(stateName)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    subdistrictName: ").append(toIndentedString(subdistrictName)).append("\n");
    sb.append("    authMethods: ").append(toIndentedString(authMethods)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    kycVerified: ").append(toIndentedString(kycVerified)).append("\n");
    sb.append("    verificationStatus: ").append(toIndentedString(verificationStatus)).append("\n");
    sb.append("    verificationType: ").append(toIndentedString(verificationType)).append("\n");
    sb.append("    localizedDetails: ").append(toIndentedString(localizedDetails)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
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
    openapiFields.add("ABHANumber");
    openapiFields.add("preferredAbhaAddress");
    openapiFields.add("mobile");
    openapiFields.add("firstName");
    openapiFields.add("middleName");
    openapiFields.add("lastName");
    openapiFields.add("name");
    openapiFields.add("yearOfBirth");
    openapiFields.add("dayOfBirth");
    openapiFields.add("monthOfBirth");
    openapiFields.add("gender");
    openapiFields.add("profilePhoto");
    openapiFields.add("status");
    openapiFields.add("stateCode");
    openapiFields.add("districtCode");
    openapiFields.add("pincode");
    openapiFields.add("address");
    openapiFields.add("kycPhoto");
    openapiFields.add("stateName");
    openapiFields.add("districtName");
    openapiFields.add("subdistrictName");
    openapiFields.add("authMethods");
    openapiFields.add("tags");
    openapiFields.add("kycVerified");
    openapiFields.add("verificationStatus");
    openapiFields.add("verificationType");
    openapiFields.add("localizedDetails");
    openapiFields.add("createdDate");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AbhaApiV3ProfileAccountGet200Response
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AbhaApiV3ProfileAccountGet200Response.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AbhaApiV3ProfileAccountGet200Response is not found in the empty JSON string", AbhaApiV3ProfileAccountGet200Response.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AbhaApiV3ProfileAccountGet200Response.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbhaApiV3ProfileAccountGet200Response` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("ABHANumber") != null && !jsonObj.get("ABHANumber").isJsonNull()) && !jsonObj.get("ABHANumber").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ABHANumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ABHANumber").toString()));
      }
      if ((jsonObj.get("preferredAbhaAddress") != null && !jsonObj.get("preferredAbhaAddress").isJsonNull()) && !jsonObj.get("preferredAbhaAddress").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `preferredAbhaAddress` to be a primitive type in the JSON string but got `%s`", jsonObj.get("preferredAbhaAddress").toString()));
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
      if ((jsonObj.get("profilePhoto") != null && !jsonObj.get("profilePhoto").isJsonNull()) && !jsonObj.get("profilePhoto").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `profilePhoto` to be a primitive type in the JSON string but got `%s`", jsonObj.get("profilePhoto").toString()));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("stateCode") != null && !jsonObj.get("stateCode").isJsonNull()) && !jsonObj.get("stateCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateCode").toString()));
      }
      if ((jsonObj.get("districtCode") != null && !jsonObj.get("districtCode").isJsonNull()) && !jsonObj.get("districtCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `districtCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("districtCode").toString()));
      }
      if ((jsonObj.get("pincode") != null && !jsonObj.get("pincode").isJsonNull()) && !jsonObj.get("pincode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `pincode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("pincode").toString()));
      }
      if ((jsonObj.get("address") != null && !jsonObj.get("address").isJsonNull()) && !jsonObj.get("address").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `address` to be a primitive type in the JSON string but got `%s`", jsonObj.get("address").toString()));
      }
      if ((jsonObj.get("kycPhoto") != null && !jsonObj.get("kycPhoto").isJsonNull()) && !jsonObj.get("kycPhoto").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `kycPhoto` to be a primitive type in the JSON string but got `%s`", jsonObj.get("kycPhoto").toString()));
      }
      if ((jsonObj.get("stateName") != null && !jsonObj.get("stateName").isJsonNull()) && !jsonObj.get("stateName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `stateName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("stateName").toString()));
      }
      if ((jsonObj.get("districtName") != null && !jsonObj.get("districtName").isJsonNull()) && !jsonObj.get("districtName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `districtName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("districtName").toString()));
      }
      if ((jsonObj.get("subdistrictName") != null && !jsonObj.get("subdistrictName").isJsonNull()) && !jsonObj.get("subdistrictName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `subdistrictName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subdistrictName").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("authMethods") != null && !jsonObj.get("authMethods").isJsonNull() && !jsonObj.get("authMethods").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `authMethods` to be an array in the JSON string but got `%s`", jsonObj.get("authMethods").toString()));
      }
      if ((jsonObj.get("verificationStatus") != null && !jsonObj.get("verificationStatus").isJsonNull()) && !jsonObj.get("verificationStatus").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `verificationStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("verificationStatus").toString()));
      }
      if ((jsonObj.get("verificationType") != null && !jsonObj.get("verificationType").isJsonNull()) && !jsonObj.get("verificationType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `verificationType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("verificationType").toString()));
      }
      // validate the optional field `localizedDetails`
      if (jsonObj.get("localizedDetails") != null && !jsonObj.get("localizedDetails").isJsonNull()) {
        AbhaApiV3ProfileAccountGet200ResponseLocalizedDetails.validateJsonElement(jsonObj.get("localizedDetails"));
      }
      if ((jsonObj.get("createdDate") != null && !jsonObj.get("createdDate").isJsonNull()) && !jsonObj.get("createdDate").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `createdDate` to be a primitive type in the JSON string but got `%s`", jsonObj.get("createdDate").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AbhaApiV3ProfileAccountGet200Response.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AbhaApiV3ProfileAccountGet200Response' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AbhaApiV3ProfileAccountGet200Response> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3ProfileAccountGet200Response.class));

       return (TypeAdapter<T>) new TypeAdapter<AbhaApiV3ProfileAccountGet200Response>() {
           @Override
           public void write(JsonWriter out, AbhaApiV3ProfileAccountGet200Response value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AbhaApiV3ProfileAccountGet200Response read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AbhaApiV3ProfileAccountGet200Response given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AbhaApiV3ProfileAccountGet200Response
   * @throws IOException if the JSON string is invalid with respect to AbhaApiV3ProfileAccountGet200Response
   */
  public static AbhaApiV3ProfileAccountGet200Response fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AbhaApiV3ProfileAccountGet200Response.class);
  }

  /**
   * Convert an instance of AbhaApiV3ProfileAccountGet200Response to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

