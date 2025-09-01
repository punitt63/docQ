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
import java.util.UUID;
import javax.annotation.Nonnull;

public class AbdmUserInitiatedLinking2RequestResponse {
    public static final String SERIALIZED_NAME_REQUEST_ID = "requestId";
    @SerializedName("requestId")
    @Nonnull
    private UUID requestId;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking2RequestResponse() {
    }

    public AbdmUserInitiatedLinking2RequestResponse requestId(@Nonnull UUID requestId) {
        this.requestId = requestId;
        return this;
    }

    @Nonnull
    public UUID getRequestId() {
        return this.requestId;
    }

    public void setRequestId(@Nonnull UUID requestId) {
        this.requestId = requestId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking2RequestResponse abdmUserInitiatedLinking2RequestResponse = (AbdmUserInitiatedLinking2RequestResponse)o;
            return Objects.equals(this.requestId, abdmUserInitiatedLinking2RequestResponse.requestId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.requestId});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking2RequestResponse {\n");
        sb.append("    requestId: ").append(this.toIndentedString(this.requestId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking2RequestResponse is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("requestId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `requestId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("requestId").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking2RequestResponse` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking2RequestResponse fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking2RequestResponse)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking2RequestResponse.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("requestId");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("requestId");
    }
}

