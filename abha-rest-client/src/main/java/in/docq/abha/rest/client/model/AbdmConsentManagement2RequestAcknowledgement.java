package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;

public class AbdmConsentManagement2RequestAcknowledgement {
    public static final String SERIALIZED_NAME_STATUS = "status";
    @SerializedName("status")
    @Nonnull
    private String status;
    public static final String SERIALIZED_NAME_CONSENT_ID = "consentId";
    @SerializedName("consentId")
    @Nonnull
    private UUID consentId;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement2RequestAcknowledgement() {
    }

    public AbdmConsentManagement2RequestAcknowledgement status(@Nonnull String status) {
        this.status = status;
        return this;
    }

    @Nonnull
    public String getStatus() {
        return this.status;
    }

    public void setStatus(@Nonnull String status) {
        this.status = status;
    }

    public AbdmConsentManagement2RequestAcknowledgement consentId(@Nonnull UUID consentId) {
        this.consentId = consentId;
        return this;
    }

    @Nonnull
    public UUID getConsentId() {
        return this.consentId;
    }

    public void setConsentId(@Nonnull UUID consentId) {
        this.consentId = consentId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement2RequestAcknowledgement abdmConsentManagement2RequestAcknowledgement = (AbdmConsentManagement2RequestAcknowledgement)o;
            return Objects.equals(this.status, abdmConsentManagement2RequestAcknowledgement.status) && Objects.equals(this.consentId, abdmConsentManagement2RequestAcknowledgement.consentId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.status, this.consentId});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement2RequestAcknowledgement {\n");
        sb.append("    status: ").append(this.toIndentedString(this.status)).append("\n");
        sb.append("    consentId: ").append(this.toIndentedString(this.consentId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement2RequestAcknowledgement is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("status").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
                            }

                            if (!jsonObj.get("consentId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `consentId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("consentId").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement2RequestAcknowledgement` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement2RequestAcknowledgement fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement2RequestAcknowledgement)JSON.getGson().fromJson(jsonString, AbdmConsentManagement2RequestAcknowledgement.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("status");
        openapiFields.add("consentId");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("status");
        openapiRequiredFields.add("consentId");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement2RequestAcknowledgement.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement2RequestAcknowledgement> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement2RequestAcknowledgement.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement2RequestAcknowledgement>() {
                    public void write(JsonWriter out, AbdmConsentManagement2RequestAcknowledgement value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement2RequestAcknowledgement read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement2RequestAcknowledgement.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement2RequestAcknowledgement)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

