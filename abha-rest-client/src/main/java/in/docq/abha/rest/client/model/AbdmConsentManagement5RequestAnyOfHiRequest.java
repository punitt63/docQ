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

public class AbdmConsentManagement5RequestAnyOfHiRequest {
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private String transactionId;
    public static final String SERIALIZED_NAME_SESSION_STATUS = "sessionStatus";
    @SerializedName("sessionStatus")
    @Nonnull
    private String sessionStatus;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement5RequestAnyOfHiRequest() {
    }

    public AbdmConsentManagement5RequestAnyOfHiRequest transactionId(@Nonnull String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Nonnull
    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(@Nonnull String transactionId) {
        this.transactionId = transactionId;
    }

    public AbdmConsentManagement5RequestAnyOfHiRequest sessionStatus(@Nonnull String sessionStatus) {
        this.sessionStatus = sessionStatus;
        return this;
    }

    @Nonnull
    public String getSessionStatus() {
        return this.sessionStatus;
    }

    public void setSessionStatus(@Nonnull String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement5RequestAnyOfHiRequest abdmConsentManagement5RequestAnyOfHiRequest = (AbdmConsentManagement5RequestAnyOfHiRequest)o;
            return Objects.equals(this.transactionId, abdmConsentManagement5RequestAnyOfHiRequest.transactionId) && Objects.equals(this.sessionStatus, abdmConsentManagement5RequestAnyOfHiRequest.sessionStatus);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.transactionId, this.sessionStatus});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement5RequestAnyOfHiRequest {\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    sessionStatus: ").append(this.toIndentedString(this.sessionStatus)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement5RequestAnyOfHiRequest is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("transactionId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `transactionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("transactionId").toString()));
                            }

                            if (!jsonObj.get("sessionStatus").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `sessionStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sessionStatus").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement5RequestAnyOfHiRequest` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement5RequestAnyOfHiRequest fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement5RequestAnyOfHiRequest)JSON.getGson().fromJson(jsonString, AbdmConsentManagement5RequestAnyOfHiRequest.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("transactionId");
        openapiFields.add("sessionStatus");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("sessionStatus");
    }
}

