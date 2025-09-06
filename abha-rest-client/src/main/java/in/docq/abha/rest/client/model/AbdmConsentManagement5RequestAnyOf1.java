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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbdmConsentManagement5RequestAnyOf1 {
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmConsentManagement5RequestAnyOf1Error error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmConsentManagement5RequestAnyOfResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement5RequestAnyOf1() {
    }

    public AbdmConsentManagement5RequestAnyOf1 error(@Nullable AbdmConsentManagement5RequestAnyOf1Error error) {
        this.error = error;
        return this;
    }

    @Nullable
    public AbdmConsentManagement5RequestAnyOf1Error getError() {
        return this.error;
    }

    public void setError(@Nullable AbdmConsentManagement5RequestAnyOf1Error error) {
        this.error = error;
    }

    public AbdmConsentManagement5RequestAnyOf1 response(@Nonnull AbdmConsentManagement5RequestAnyOfResponse response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement5RequestAnyOfResponse getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmConsentManagement5RequestAnyOfResponse response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement5RequestAnyOf1 abdmConsentManagement5RequestAnyOf1 = (AbdmConsentManagement5RequestAnyOf1)o;
            return Objects.equals(this.error, abdmConsentManagement5RequestAnyOf1.error) && Objects.equals(this.response, abdmConsentManagement5RequestAnyOf1.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement5RequestAnyOf1 {\n");
        sb.append("    error: ").append(this.toIndentedString(this.error)).append("\n");
        sb.append("    response: ").append(this.toIndentedString(this.response)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement5RequestAnyOf1 is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    var2 = openapiRequiredFields.iterator();

                    String requiredField;
                    do {
                        if (!var2.hasNext()) {
                            JsonObject jsonObj = jsonElement.getAsJsonObject();
                            if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                                AbdmConsentManagement5RequestAnyOf1Error.validateJsonElement(jsonObj.get("error"));
                            }

                            AbdmConsentManagement5RequestAnyOfResponse.validateJsonElement(jsonObj.get("response"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement5RequestAnyOf1` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement5RequestAnyOf1 fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement5RequestAnyOf1)JSON.getGson().fromJson(jsonString, AbdmConsentManagement5RequestAnyOf1.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("error");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("response");
    }
}

