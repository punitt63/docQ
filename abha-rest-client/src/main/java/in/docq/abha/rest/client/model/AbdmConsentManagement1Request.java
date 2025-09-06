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

public class AbdmConsentManagement1Request {
    public static final String SERIALIZED_NAME_CONSENT = "consent";
    @SerializedName("consent")
    @Nonnull
    private AbdmConsentManagement1RequestConsent consent;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1Request() {
    }

    public AbdmConsentManagement1Request consent(@Nonnull AbdmConsentManagement1RequestConsent consent) {
        this.consent = consent;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsent getConsent() {
        return this.consent;
    }

    public void setConsent(@Nonnull AbdmConsentManagement1RequestConsent consent) {
        this.consent = consent;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1Request abdmConsentManagement1Request = (AbdmConsentManagement1Request)o;
            return Objects.equals(this.consent, abdmConsentManagement1Request.consent);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consent});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1Request {\n");
        sb.append("    consent: ").append(this.toIndentedString(this.consent)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1Request is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmConsentManagement1RequestConsent.validateJsonElement(jsonObj.get("consent"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1Request fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1Request)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("consent");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("consent");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement1Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement1Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement1Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement1Request>() {
                    public void write(JsonWriter out, AbdmConsentManagement1Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement1Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement1Request.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement1Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

