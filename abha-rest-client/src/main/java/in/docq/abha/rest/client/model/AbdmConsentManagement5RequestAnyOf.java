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

public class AbdmConsentManagement5RequestAnyOf {
    public static final String SERIALIZED_NAME_HI_REQUEST = "hiRequest";
    @SerializedName("hiRequest")
    @Nonnull
    private AbdmConsentManagement5RequestAnyOfHiRequest hiRequest;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmConsentManagement5RequestAnyOfResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement5RequestAnyOf() {
    }

    public AbdmConsentManagement5RequestAnyOf hiRequest(@Nonnull AbdmConsentManagement5RequestAnyOfHiRequest hiRequest) {
        this.hiRequest = hiRequest;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement5RequestAnyOfHiRequest getHiRequest() {
        return this.hiRequest;
    }

    public void setHiRequest(@Nonnull AbdmConsentManagement5RequestAnyOfHiRequest hiRequest) {
        this.hiRequest = hiRequest;
    }

    public AbdmConsentManagement5RequestAnyOf response(@Nonnull AbdmConsentManagement5RequestAnyOfResponse response) {
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
            AbdmConsentManagement5RequestAnyOf abdmConsentManagement5RequestAnyOf = (AbdmConsentManagement5RequestAnyOf)o;
            return Objects.equals(this.hiRequest, abdmConsentManagement5RequestAnyOf.hiRequest) && Objects.equals(this.response, abdmConsentManagement5RequestAnyOf.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.hiRequest, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement5RequestAnyOf {\n");
        sb.append("    hiRequest: ").append(this.toIndentedString(this.hiRequest)).append("\n");
        sb.append("    response: ").append(this.toIndentedString(this.response)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement5RequestAnyOf is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmConsentManagement5RequestAnyOfHiRequest.validateJsonElement(jsonObj.get("hiRequest"));
                            AbdmConsentManagement5RequestAnyOfResponse.validateJsonElement(jsonObj.get("response"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement5RequestAnyOf` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement5RequestAnyOf fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement5RequestAnyOf)JSON.getGson().fromJson(jsonString, AbdmConsentManagement5RequestAnyOf.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("hiRequest");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("hiRequest");
        openapiRequiredFields.add("response");
    }
}

