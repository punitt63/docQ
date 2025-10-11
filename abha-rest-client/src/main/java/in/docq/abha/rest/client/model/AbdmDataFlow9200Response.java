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

public class AbdmDataFlow9200Response {
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private UUID transactionId;
    public static final String SERIALIZED_NAME_STATUS = "status";
    @SerializedName("status")
    @Nonnull
    private String status;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow9200Response() {
    }

    public AbdmDataFlow9200Response transactionId(@Nonnull UUID transactionId) {
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

    public AbdmDataFlow9200Response status(@Nonnull String status) {
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow9200Response abdmDataFlow9200Response = (AbdmDataFlow9200Response)o;
            return Objects.equals(this.transactionId, abdmDataFlow9200Response.transactionId) && Objects.equals(this.status, abdmDataFlow9200Response.status);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.transactionId, this.status});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow9200Response {\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    status: ").append(this.toIndentedString(this.status)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow9200Response is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            if (!jsonObj.get("status").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow9200Response` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow9200Response fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow9200Response)JSON.getGson().fromJson(jsonString, AbdmDataFlow9200Response.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("transactionId");
        openapiFields.add("status");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("status");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmDataFlow9200Response.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmDataFlow9200Response> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmDataFlow9200Response.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmDataFlow9200Response>() {
                    public void write(JsonWriter out, AbdmDataFlow9200Response value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmDataFlow9200Response read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmDataFlow9200Response.validateJsonElement(jsonElement);
                        return (AbdmDataFlow9200Response)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

