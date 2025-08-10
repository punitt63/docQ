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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-21T15:03:26.931725+05:30[Asia/Kolkata]", comments = "Generator version: 7.11.0")
public class AbhaApiV3ProfileBenefitSearchPost400ResponseInner extends AbstractOpenApiSchema {
    private static final Logger log = Logger.getLogger(AbhaApiV3ProfileBenefitSearchPost400ResponseInner.class.getName());

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbhaApiV3ProfileBenefitSearchPost400ResponseInner.class.isAssignableFrom(type.getRawType())) {
                return null; // this class only serializes 'AbhaApiV3ProfileBenefitSearchPost400ResponseInner' and its subtypes
            }
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
            final TypeAdapter<AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf> adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf.class));
            final TypeAdapter<AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1> adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1 = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1.class));
            final TypeAdapter<AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2> adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2 = gson.getDelegateAdapter(this, TypeToken.get(AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2.class));

            return (TypeAdapter<T>) new TypeAdapter<AbhaApiV3ProfileBenefitSearchPost400ResponseInner>() {
                @Override
                public void write(JsonWriter out, AbhaApiV3ProfileBenefitSearchPost400ResponseInner value) throws IOException {
                    if (value == null || value.getActualInstance() == null) {
                        elementAdapter.write(out, null);
                        return;
                    }

                    // check if the actual instance is of the type `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf`
                    if (value.getActualInstance() instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf) {
                        JsonElement element = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf.toJsonTree((AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf)value.getActualInstance());
                        elementAdapter.write(out, element);
                        return;
                    }
                    // check if the actual instance is of the type `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1`
                    if (value.getActualInstance() instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1) {
                        JsonElement element = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1.toJsonTree((AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1)value.getActualInstance());
                        elementAdapter.write(out, element);
                        return;
                    }
                    // check if the actual instance is of the type `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2`
                    if (value.getActualInstance() instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2) {
                        JsonElement element = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2.toJsonTree((AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2)value.getActualInstance());
                        elementAdapter.write(out, element);
                        return;
                    }
                    throw new IOException("Failed to serialize as the type doesn't match anyOf schemas: AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2");
                }

                @Override
                public AbhaApiV3ProfileBenefitSearchPost400ResponseInner read(JsonReader in) throws IOException {
                    Object deserialized = null;
                    JsonElement jsonElement = elementAdapter.read(in);

                    ArrayList<String> errorMessages = new ArrayList<>();
                    TypeAdapter actualAdapter = elementAdapter;

                    // deserialize AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf
                    try {
                        // validate the JSON object to see if any exception is thrown
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf.validateJsonElement(jsonElement);
                        actualAdapter = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf;
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInner ret = new AbhaApiV3ProfileBenefitSearchPost400ResponseInner();
                        ret.setActualInstance(actualAdapter.fromJsonTree(jsonElement));
                        return ret;
                    } catch (Exception e) {
                        // deserialization failed, continue
                        errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf failed with `%s`.", e.getMessage()));
                        log.log(Level.FINER, "Input data does not match schema 'AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf'", e);
                    }
                    // deserialize AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1
                    try {
                        // validate the JSON object to see if any exception is thrown
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1.validateJsonElement(jsonElement);
                        actualAdapter = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1;
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInner ret = new AbhaApiV3ProfileBenefitSearchPost400ResponseInner();
                        ret.setActualInstance(actualAdapter.fromJsonTree(jsonElement));
                        return ret;
                    } catch (Exception e) {
                        // deserialization failed, continue
                        errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1 failed with `%s`.", e.getMessage()));
                        log.log(Level.FINER, "Input data does not match schema 'AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1'", e);
                    }
                    // deserialize AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2
                    try {
                        // validate the JSON object to see if any exception is thrown
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2.validateJsonElement(jsonElement);
                        actualAdapter = adapterAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2;
                        AbhaApiV3ProfileBenefitSearchPost400ResponseInner ret = new AbhaApiV3ProfileBenefitSearchPost400ResponseInner();
                        ret.setActualInstance(actualAdapter.fromJsonTree(jsonElement));
                        return ret;
                    } catch (Exception e) {
                        // deserialization failed, continue
                        errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2 failed with `%s`.", e.getMessage()));
                        log.log(Level.FINER, "Input data does not match schema 'AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2'", e);
                    }

                    throw new IOException(String.format("Failed deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInner: no class matches result, expected at least 1. Detailed failure message for anyOf schemas: %s. JSON: %s", errorMessages, jsonElement.toString()));
                }
            }.nullSafe();
        }
    }

    // store a list of schema names defined in anyOf
    public static final Map<String, Class<?>> schemas = new HashMap<String, Class<?>>();

    public AbhaApiV3ProfileBenefitSearchPost400ResponseInner() {
        super("anyOf", Boolean.FALSE);
    }

    public AbhaApiV3ProfileBenefitSearchPost400ResponseInner(Object o) {
        super("anyOf", Boolean.FALSE);
        setActualInstance(o);
    }

    static {
        schemas.put("AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf", AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf.class);
        schemas.put("AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1", AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1.class);
        schemas.put("AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2", AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2.class);
    }

    @Override
    public Map<String, Class<?>> getSchemas() {
        return AbhaApiV3ProfileBenefitSearchPost400ResponseInner.schemas;
    }

    /**
     * Set the instance that matches the anyOf child schema, check
     * the instance parameter is valid against the anyOf child schemas:
     * AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2
     *
     * It could be an instance of the 'anyOf' schemas.
     */
    @Override
    public void setActualInstance(Object instance) {
        if (instance instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf) {
            super.setActualInstance(instance);
            return;
        }

        if (instance instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1) {
            super.setActualInstance(instance);
            return;
        }

        if (instance instanceof AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2) {
            super.setActualInstance(instance);
            return;
        }

        throw new RuntimeException("Invalid instance type. Must be AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2");
    }

    /**
     * Get the actual instance, which can be the following:
     * AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2
     *
     * @return The actual instance (AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object getActualInstance() {
        return super.getActualInstance();
    }

    /**
     * Get the actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf`. If the actual instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf`,
     * the ClassCastException will be thrown.
     *
     * @return The actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf`
     * @throws ClassCastException if the instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf`
     */
    public AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf getAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf() throws ClassCastException {
        return (AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf)super.getActualInstance();
    }

    /**
     * Get the actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1`. If the actual instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1`,
     * the ClassCastException will be thrown.
     *
     * @return The actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1`
     * @throws ClassCastException if the instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1`
     */
    public AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1 getAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1() throws ClassCastException {
        return (AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1)super.getActualInstance();
    }

    /**
     * Get the actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2`. If the actual instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2`,
     * the ClassCastException will be thrown.
     *
     * @return The actual instance of `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2`
     * @throws ClassCastException if the instance is not `AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2`
     */
    public AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2 getAbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2() throws ClassCastException {
        return (AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2)super.getActualInstance();
    }

    /**
     * Validates the JSON Element and throws an exception if issues found
     *
     * @param jsonElement JSON Element
     * @throws IOException if the JSON Element is invalid with respect to AbhaApiV3ProfileBenefitSearchPost400ResponseInner
     */
    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        // validate anyOf schemas one by one
        ArrayList<String> errorMessages = new ArrayList<>();
        // validate the json string with AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf
        try {
            AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf.validateJsonElement(jsonElement);
            return;
        } catch (Exception e) {
            errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf failed with `%s`.", e.getMessage()));
            // continue to the next one
        }
        // validate the json string with AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1
        try {
            AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1.validateJsonElement(jsonElement);
            return;
        } catch (Exception e) {
            errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1 failed with `%s`.", e.getMessage()));
            // continue to the next one
        }
        // validate the json string with AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2
        try {
            AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2.validateJsonElement(jsonElement);
            return;
        } catch (Exception e) {
            errorMessages.add(String.format("Deserialization for AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2 failed with `%s`.", e.getMessage()));
            // continue to the next one
        }
        throw new IOException(String.format("The JSON string is invalid for AbhaApiV3ProfileBenefitSearchPost400ResponseInner with anyOf schemas: AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf1, AbhaApiV3ProfileBenefitSearchPost400ResponseInnerAnyOf2. no class match the result, expected at least 1. Detailed failure message for anyOf schemas: %s. JSON: %s", errorMessages, jsonElement.toString()));
    }

    /**
     * Create an instance of AbhaApiV3ProfileBenefitSearchPost400ResponseInner given an JSON string
     *
     * @param jsonString JSON string
     * @return An instance of AbhaApiV3ProfileBenefitSearchPost400ResponseInner
     * @throws IOException if the JSON string is invalid with respect to AbhaApiV3ProfileBenefitSearchPost400ResponseInner
     */
    public static AbhaApiV3ProfileBenefitSearchPost400ResponseInner fromJson(String jsonString) throws IOException {
        return JSON.getGson().fromJson(jsonString, AbhaApiV3ProfileBenefitSearchPost400ResponseInner.class);
    }

    /**
     * Convert an instance of AbhaApiV3ProfileBenefitSearchPost400ResponseInner to an JSON string
     *
     * @return JSON string
     */
    public String toJson() {
        return JSON.getGson().toJson(this);
    }
}

