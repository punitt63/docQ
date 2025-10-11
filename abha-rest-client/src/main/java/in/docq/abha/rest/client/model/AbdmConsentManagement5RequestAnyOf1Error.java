package in.docq.abha.rest.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

public class AbdmConsentManagement5RequestAnyOf1Error {
    public static final String SERIALIZED_NAME_CODE = "code";
    @SerializedName("code")
    @Nullable
    private String code;
    public static final String SERIALIZED_NAME_MESSAGE = "message";
    @SerializedName("message")
    @Nullable
    private String message;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement5RequestAnyOf1Error() {
    }

    public AbdmConsentManagement5RequestAnyOf1Error code(@Nullable String code) {
        this.code = code;
        return this;
    }

    @Nullable
    public String getCode() {
        return this.code;
    }

    public void setCode(@Nullable String code) {
        this.code = code;
    }

    public AbdmConsentManagement5RequestAnyOf1Error message(@Nullable String message) {
        this.message = message;
        return this;
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement5RequestAnyOf1Error abdmConsentManagement5RequestAnyOf1Error = (AbdmConsentManagement5RequestAnyOf1Error)o;
            return Objects.equals(this.code, abdmConsentManagement5RequestAnyOf1Error.code) && Objects.equals(this.message, abdmConsentManagement5RequestAnyOf1Error.message);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.code, this.message});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement5RequestAnyOf1Error {\n");
        sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
        sb.append("    message: ").append(this.toIndentedString(this.message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement5RequestAnyOf1Error is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.get("code") != null && !jsonObj.get("code").isJsonNull() && !jsonObj.get("code").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `code` to be a primitive type in the JSON string but got `%s`", jsonObj.get("code").toString()));
                    }

                    if (jsonObj.get("message") != null && !jsonObj.get("message").isJsonNull() && !jsonObj.get("message").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `message` to be a primitive type in the JSON string but got `%s`", jsonObj.get("message").toString()));
                    }

                    return;
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement5RequestAnyOf1Error` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement5RequestAnyOf1Error fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement5RequestAnyOf1Error)JSON.getGson().fromJson(jsonString, AbdmConsentManagement5RequestAnyOf1Error.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("code");
        openapiFields.add("message");
        openapiRequiredFields = new HashSet();
    }
}

