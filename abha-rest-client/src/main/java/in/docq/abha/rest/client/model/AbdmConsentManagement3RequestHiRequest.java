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

public class AbdmConsentManagement3RequestHiRequest {
    public static final String SERIALIZED_NAME_CONSENT = "consent";
    @SerializedName("consent")
    @Nonnull
    private AbdmConsentManagement3RequestHiRequestConsent consent;
    public static final String SERIALIZED_NAME_DATE_RANGE = "dateRange";
    @SerializedName("dateRange")
    @Nonnull
    private AbdmConsentManagement3RequestHiRequestDateRange dateRange;
    public static final String SERIALIZED_NAME_DATA_PUSH_URL = "dataPushUrl";
    @SerializedName("dataPushUrl")
    @Nonnull
    private String dataPushUrl;
    public static final String SERIALIZED_NAME_KEY_MATERIAL = "keyMaterial";
    @SerializedName("keyMaterial")
    @Nonnull
    private AbdmConsentManagement3RequestHiRequestKeyMaterial keyMaterial;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3RequestHiRequest() {
    }

    public AbdmConsentManagement3RequestHiRequest consent(@Nonnull AbdmConsentManagement3RequestHiRequestConsent consent) {
        this.consent = consent;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3RequestHiRequestConsent getConsent() {
        return this.consent;
    }

    public void setConsent(@Nonnull AbdmConsentManagement3RequestHiRequestConsent consent) {
        this.consent = consent;
    }

    public AbdmConsentManagement3RequestHiRequest dateRange(@Nonnull AbdmConsentManagement3RequestHiRequestDateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3RequestHiRequestDateRange getDateRange() {
        return this.dateRange;
    }

    public void setDateRange(@Nonnull AbdmConsentManagement3RequestHiRequestDateRange dateRange) {
        this.dateRange = dateRange;
    }

    public AbdmConsentManagement3RequestHiRequest dataPushUrl(@Nonnull String dataPushUrl) {
        this.dataPushUrl = dataPushUrl;
        return this;
    }

    @Nonnull
    public String getDataPushUrl() {
        return this.dataPushUrl;
    }

    public void setDataPushUrl(@Nonnull String dataPushUrl) {
        this.dataPushUrl = dataPushUrl;
    }

    public AbdmConsentManagement3RequestHiRequest keyMaterial(@Nonnull AbdmConsentManagement3RequestHiRequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3RequestHiRequestKeyMaterial getKeyMaterial() {
        return this.keyMaterial;
    }

    public void setKeyMaterial(@Nonnull AbdmConsentManagement3RequestHiRequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3RequestHiRequest abdmConsentManagement3RequestHiRequest = (AbdmConsentManagement3RequestHiRequest)o;
            return Objects.equals(this.consent, abdmConsentManagement3RequestHiRequest.consent) && Objects.equals(this.dateRange, abdmConsentManagement3RequestHiRequest.dateRange) && Objects.equals(this.dataPushUrl, abdmConsentManagement3RequestHiRequest.dataPushUrl) && Objects.equals(this.keyMaterial, abdmConsentManagement3RequestHiRequest.keyMaterial);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consent, this.dateRange, this.dataPushUrl, this.keyMaterial});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3RequestHiRequest {\n");
        sb.append("    consent: ").append(this.toIndentedString(this.consent)).append("\n");
        sb.append("    dateRange: ").append(this.toIndentedString(this.dateRange)).append("\n");
        sb.append("    dataPushUrl: ").append(this.toIndentedString(this.dataPushUrl)).append("\n");
        sb.append("    keyMaterial: ").append(this.toIndentedString(this.keyMaterial)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3RequestHiRequest is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmConsentManagement3RequestHiRequestConsent.validateJsonElement(jsonObj.get("consent"));
                            AbdmConsentManagement3RequestHiRequestDateRange.validateJsonElement(jsonObj.get("dateRange"));
                            if (!jsonObj.get("dataPushUrl").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `dataPushUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dataPushUrl").toString()));
                            }

                            AbdmConsentManagement3RequestHiRequestKeyMaterial.validateJsonElement(jsonObj.get("keyMaterial"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3RequestHiRequest` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement3RequestHiRequest fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3RequestHiRequest)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3RequestHiRequest.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("consent");
        openapiFields.add("dateRange");
        openapiFields.add("dataPushUrl");
        openapiFields.add("keyMaterial");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("consent");
        openapiRequiredFields.add("dateRange");
        openapiRequiredFields.add("dataPushUrl");
        openapiRequiredFields.add("keyMaterial");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement3RequestHiRequest.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3RequestHiRequest> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3RequestHiRequest.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3RequestHiRequest>() {
                    public void write(JsonWriter out, AbdmConsentManagement3RequestHiRequest value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3RequestHiRequest read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3RequestHiRequest.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3RequestHiRequest)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

