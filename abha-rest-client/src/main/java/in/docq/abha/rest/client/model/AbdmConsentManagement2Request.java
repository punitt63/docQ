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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbdmConsentManagement2Request {
    public static final String SERIALIZED_NAME_ACKNOWLEDGEMENT = "acknowledgement";
    @SerializedName("acknowledgement")
    @Nonnull
    private AbdmConsentManagement2RequestAcknowledgement acknowledgement;
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmConsentManagement2RequestError error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmConsentManagement2RequestResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement2Request() {
    }

    public AbdmConsentManagement2Request acknowledgement(@Nonnull AbdmConsentManagement2RequestAcknowledgement acknowledgement) {
        this.acknowledgement = acknowledgement;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement2RequestAcknowledgement getAcknowledgement() {
        return this.acknowledgement;
    }

    public void setAcknowledgement(@Nonnull AbdmConsentManagement2RequestAcknowledgement acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public AbdmConsentManagement2Request error(@Nullable AbdmConsentManagement2RequestError error) {
        this.error = error;
        return this;
    }

    @Nullable
    public AbdmConsentManagement2RequestError getError() {
        return this.error;
    }

    public void setError(@Nullable AbdmConsentManagement2RequestError error) {
        this.error = error;
    }

    public AbdmConsentManagement2Request response(@Nonnull AbdmConsentManagement2RequestResponse response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement2RequestResponse getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmConsentManagement2RequestResponse response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement2Request abdmConsentManagement2Request = (AbdmConsentManagement2Request)o;
            return Objects.equals(this.acknowledgement, abdmConsentManagement2Request.acknowledgement) && Objects.equals(this.error, abdmConsentManagement2Request.error) && Objects.equals(this.response, abdmConsentManagement2Request.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.acknowledgement, this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement2Request {\n");
        sb.append("    acknowledgement: ").append(this.toIndentedString(this.acknowledgement)).append("\n");
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
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement2Request is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmConsentManagement2RequestAcknowledgement.validateJsonElement(jsonObj.get("acknowledgement"));
                            if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                                AbdmConsentManagement2RequestError.validateJsonElement(jsonObj.get("error"));
                            }

                            AbdmConsentManagement2RequestResponse.validateJsonElement(jsonObj.get("response"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement2Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement2Request fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement2Request)JSON.getGson().fromJson(jsonString, AbdmConsentManagement2Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("acknowledgement");
        openapiFields.add("error");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("acknowledgement");
        openapiRequiredFields.add("response");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement2Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement2Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement2Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement2Request>() {
                    public void write(JsonWriter out, AbdmConsentManagement2Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement2Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement2Request.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement2Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

