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

public class AbdmConsentManagement3Request1 {
    public static final String SERIALIZED_NAME_CONSENT_REQUEST_ID = "consentRequestId";
    @SerializedName("consentRequestId")
    @Nonnull
    private UUID consentRequestId;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3Request1() {
    }

    public AbdmConsentManagement3Request1 consentRequestId(@Nonnull UUID consentRequestId) {
        this.consentRequestId = consentRequestId;
        return this;
    }

    @Nonnull
    public UUID getConsentRequestId() {
        return this.consentRequestId;
    }

    public void setConsentRequestId(@Nonnull UUID consentRequestId) {
        this.consentRequestId = consentRequestId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3Request1 abdmConsentManagement3Request1 = (AbdmConsentManagement3Request1)o;
            return Objects.equals(this.consentRequestId, abdmConsentManagement3Request1.consentRequestId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consentRequestId});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3Request1 {\n");
        sb.append("    consentRequestId: ").append(this.toIndentedString(this.consentRequestId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3Request1 is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("consentRequestId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `consentRequestId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("consentRequestId").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3Request1` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement3Request1 fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3Request1)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3Request1.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("consentRequestId");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("consentRequestId");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement3Request1.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3Request1> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3Request1.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3Request1>() {
                    public void write(JsonWriter out, AbdmConsentManagement3Request1 value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3Request1 read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3Request1.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3Request1)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

