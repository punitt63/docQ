package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbdmConsentManagement3Request2 {
    public static final String SERIALIZED_NAME_ACKNOWLEDGEMENT = "acknowledgement";
    @SerializedName("acknowledgement")
    @Nonnull
    private List<AbdmConsentManagement3Request2AcknowledgementInner> acknowledgement = new ArrayList();
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmConsentManagement2RequestError error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmConsentManagement3Request2Response response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3Request2() {
    }

    public AbdmConsentManagement3Request2 acknowledgement(@Nonnull List<AbdmConsentManagement3Request2AcknowledgementInner> acknowledgement) {
        this.acknowledgement = acknowledgement;
        return this;
    }

    public AbdmConsentManagement3Request2 addAcknowledgementItem(AbdmConsentManagement3Request2AcknowledgementInner acknowledgementItem) {
        if (this.acknowledgement == null) {
            this.acknowledgement = new ArrayList();
        }

        this.acknowledgement.add(acknowledgementItem);
        return this;
    }

    @Nonnull
    public List<AbdmConsentManagement3Request2AcknowledgementInner> getAcknowledgement() {
        return this.acknowledgement;
    }

    public void setAcknowledgement(@Nonnull List<AbdmConsentManagement3Request2AcknowledgementInner> acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public AbdmConsentManagement3Request2 error(@Nullable AbdmConsentManagement2RequestError error) {
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

    public AbdmConsentManagement3Request2 response(@Nonnull AbdmConsentManagement3Request2Response response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3Request2Response getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmConsentManagement3Request2Response response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3Request2 abdmConsentManagement3Request2 = (AbdmConsentManagement3Request2)o;
            return Objects.equals(this.acknowledgement, abdmConsentManagement3Request2.acknowledgement) && Objects.equals(this.error, abdmConsentManagement3Request2.error) && Objects.equals(this.response, abdmConsentManagement3Request2.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.acknowledgement, this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3Request2 {\n");
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
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3Request2 is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3Request2` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
                }
            }

            var2 = openapiRequiredFields.iterator();

            while(var2.hasNext()) {
                String requiredField = (String)var2.next();
                if (jsonElement.getAsJsonObject().get(requiredField) == null) {
                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }
            }

            JsonObject jsonObj = jsonElement.getAsJsonObject();
            if (!jsonObj.get("acknowledgement").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `acknowledgement` to be an array in the JSON string but got `%s`", jsonObj.get("acknowledgement").toString()));
            } else {
                JsonArray jsonArrayacknowledgement = jsonObj.getAsJsonArray("acknowledgement");

                for(int i = 0; i < jsonArrayacknowledgement.size(); ++i) {
                    AbdmConsentManagement3Request2AcknowledgementInner.validateJsonElement(jsonArrayacknowledgement.get(i));
                }

                if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                    AbdmConsentManagement2RequestError.validateJsonElement(jsonObj.get("error"));
                }

                AbdmConsentManagement3Request2Response.validateJsonElement(jsonObj.get("response"));
            }
        }
    }

    public static AbdmConsentManagement3Request2 fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3Request2)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3Request2.class);
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
            if (!AbdmConsentManagement3Request2.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3Request2> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3Request2.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3Request2>() {
                    public void write(JsonWriter out, AbdmConsentManagement3Request2 value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3Request2 read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3Request2.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3Request2)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

