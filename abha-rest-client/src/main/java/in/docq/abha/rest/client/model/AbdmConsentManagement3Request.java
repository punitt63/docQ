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

public class AbdmConsentManagement3Request {
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private UUID transactionId;
    public static final String SERIALIZED_NAME_HI_REQUEST = "hiRequest";
    @SerializedName("hiRequest")
    @Nonnull
    private AbdmConsentManagement3RequestHiRequest hiRequest;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3Request() {
    }

    public AbdmConsentManagement3Request transactionId(@Nonnull UUID transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Nonnull
    public UUID getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(@Nonnull UUID transactionId) {
        this.transactionId = transactionId;
    }

    public AbdmConsentManagement3Request hiRequest(@Nonnull AbdmConsentManagement3RequestHiRequest hiRequest) {
        this.hiRequest = hiRequest;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3RequestHiRequest getHiRequest() {
        return this.hiRequest;
    }

    public void setHiRequest(@Nonnull AbdmConsentManagement3RequestHiRequest hiRequest) {
        this.hiRequest = hiRequest;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3Request abdmConsentManagement3Request = (AbdmConsentManagement3Request)o;
            return Objects.equals(this.transactionId, abdmConsentManagement3Request.transactionId) && Objects.equals(this.hiRequest, abdmConsentManagement3Request.hiRequest);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.transactionId, this.hiRequest});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3Request {\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    hiRequest: ").append(this.toIndentedString(this.hiRequest)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3Request is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            AbdmConsentManagement3RequestHiRequest.validateJsonElement(jsonObj.get("hiRequest"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement3Request fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3Request)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("transactionId");
        openapiFields.add("hiRequest");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("hiRequest");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement3Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3Request>() {
                    public void write(JsonWriter out, AbdmConsentManagement3Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3Request.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

