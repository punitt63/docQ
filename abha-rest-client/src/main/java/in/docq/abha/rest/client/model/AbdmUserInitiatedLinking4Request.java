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
import javax.annotation.Nullable;

public class AbdmUserInitiatedLinking4Request {
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nullable
    private String transactionId;
    public static final String SERIALIZED_NAME_LINK = "link";
    @SerializedName("link")
    @Nonnull
    private AbdmUserInitiatedLinking4RequestLink link;
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmUserInitiatedLinking4RequestError error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmUserInitiatedLinking4RequestResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking4Request() {
    }

    public AbdmUserInitiatedLinking4Request transactionId(@Nullable String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Nullable
    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(@Nullable String transactionId) {
        this.transactionId = transactionId;
    }

    public AbdmUserInitiatedLinking4Request link(@Nonnull AbdmUserInitiatedLinking4RequestLink link) {
        this.link = link;
        return this;
    }

    @Nonnull
    public AbdmUserInitiatedLinking4RequestLink getLink() {
        return this.link;
    }

    public void setLink(@Nonnull AbdmUserInitiatedLinking4RequestLink link) {
        this.link = link;
    }

    public AbdmUserInitiatedLinking4Request error(@Nullable AbdmUserInitiatedLinking4RequestError error) {
        this.error = error;
        return this;
    }

    @Nullable
    public AbdmUserInitiatedLinking4RequestError getError() {
        return this.error;
    }

    public void setError(@Nullable AbdmUserInitiatedLinking4RequestError error) {
        this.error = error;
    }

    public AbdmUserInitiatedLinking4Request response(@Nonnull AbdmUserInitiatedLinking4RequestResponse response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmUserInitiatedLinking4RequestResponse getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmUserInitiatedLinking4RequestResponse response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking4Request abdmUserInitiatedLinking4Request = (AbdmUserInitiatedLinking4Request)o;
            return Objects.equals(this.transactionId, abdmUserInitiatedLinking4Request.transactionId) && Objects.equals(this.link, abdmUserInitiatedLinking4Request.link) && Objects.equals(this.error, abdmUserInitiatedLinking4Request.error) && Objects.equals(this.response, abdmUserInitiatedLinking4Request.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.transactionId, this.link, this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking4Request {\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    link: ").append(this.toIndentedString(this.link)).append("\n");
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
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking4Request is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (jsonObj.get("transactionId") != null && !jsonObj.get("transactionId").isJsonNull() && !jsonObj.get("transactionId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `transactionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("transactionId").toString()));
                            }

                            AbdmUserInitiatedLinking4RequestLink.validateJsonElement(jsonObj.get("link"));
                            if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                                AbdmUserInitiatedLinking4RequestError.validateJsonElement(jsonObj.get("error"));
                            }

                            AbdmUserInitiatedLinking4RequestResponse.validateJsonElement(jsonObj.get("response"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking4Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking4Request fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking4Request)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking4Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("transactionId");
        openapiFields.add("link");
        openapiFields.add("error");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("link");
        openapiRequiredFields.add("response");
    }
}
