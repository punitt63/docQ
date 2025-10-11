package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
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

public class AbdmConsentManagement6RequestKeyMaterialDhPublicKey {
    public static final String SERIALIZED_NAME_EXPIRY = "expiry";
    @SerializedName("expiry")
    @Nonnull
    private String expiry;
    public static final String SERIALIZED_NAME_PARAMETERS = "parameters";
    @SerializedName("parameters")
    @Nonnull
    private ParametersEnum parameters;
    public static final String SERIALIZED_NAME_KEY_VALUE = "keyValue";
    @SerializedName("keyValue")
    @Nonnull
    private String keyValue;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey() {
    }

    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey expiry(@Nonnull String expiry) {
        this.expiry = expiry;
        return this;
    }

    @Nonnull
    public String getExpiry() {
        return this.expiry;
    }

    public void setExpiry(@Nonnull String expiry) {
        this.expiry = expiry;
    }

    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey parameters(@Nonnull ParametersEnum parameters) {
        this.parameters = parameters;
        return this;
    }

    @Nonnull
    public ParametersEnum getParameters() {
        return this.parameters;
    }

    public void setParameters(@Nonnull ParametersEnum parameters) {
        this.parameters = parameters;
    }

    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey keyValue(@Nonnull String keyValue) {
        this.keyValue = keyValue;
        return this;
    }

    @Nonnull
    public String getKeyValue() {
        return this.keyValue;
    }

    public void setKeyValue(@Nonnull String keyValue) {
        this.keyValue = keyValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement6RequestKeyMaterialDhPublicKey abdmConsentManagement6RequestKeyMaterialDhPublicKey = (AbdmConsentManagement6RequestKeyMaterialDhPublicKey)o;
            return Objects.equals(this.expiry, abdmConsentManagement6RequestKeyMaterialDhPublicKey.expiry) && Objects.equals(this.parameters, abdmConsentManagement6RequestKeyMaterialDhPublicKey.parameters) && Objects.equals(this.keyValue, abdmConsentManagement6RequestKeyMaterialDhPublicKey.keyValue);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.expiry, this.parameters, this.keyValue});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement6RequestKeyMaterialDhPublicKey {\n");
        sb.append("    expiry: ").append(this.toIndentedString(this.expiry)).append("\n");
        sb.append("    parameters: ").append(this.toIndentedString(this.parameters)).append("\n");
        sb.append("    keyValue: ").append(this.toIndentedString(this.keyValue)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement6RequestKeyMaterialDhPublicKey is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("expiry").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `expiry` to be a primitive type in the JSON string but got `%s`", jsonObj.get("expiry").toString()));
                            }

                            if (!jsonObj.get("parameters").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `parameters` to be a primitive type in the JSON string but got `%s`", jsonObj.get("parameters").toString()));
                            }

                            AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.validateJsonElement(jsonObj.get("parameters"));
                            if (!jsonObj.get("keyValue").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `keyValue` to be a primitive type in the JSON string but got `%s`", jsonObj.get("keyValue").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement6RequestKeyMaterialDhPublicKey` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement6RequestKeyMaterialDhPublicKey fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement6RequestKeyMaterialDhPublicKey)JSON.getGson().fromJson(jsonString, AbdmConsentManagement6RequestKeyMaterialDhPublicKey.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("expiry");
        openapiFields.add("parameters");
        openapiFields.add("keyValue");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("expiry");
        openapiRequiredFields.add("parameters");
        openapiRequiredFields.add("keyValue");
    }

    @JsonAdapter(ParametersEnum.Adapter.class)
    public static enum ParametersEnum {
        CURVE25519_32BYTE_RANDOM_KEY("Curve25519/32byte random key");

        private String value;

        private ParametersEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static ParametersEnum fromValue(String value) {
            ParametersEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ParametersEnum b = var1[var3];
                if (b.value.equals(value)) {
                    return b;
                }
            }

            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public static void validateJsonElement(JsonElement jsonElement) throws IOException {
            String value = jsonElement.getAsString();
            fromValue(value);
        }

        public static class Adapter extends TypeAdapter<ParametersEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, ParametersEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public ParametersEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement6RequestKeyMaterialDhPublicKey.ParametersEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement6RequestKeyMaterialDhPublicKey.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement6RequestKeyMaterialDhPublicKey> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement6RequestKeyMaterialDhPublicKey.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement6RequestKeyMaterialDhPublicKey>() {
                    public void write(JsonWriter out, AbdmConsentManagement6RequestKeyMaterialDhPublicKey value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement6RequestKeyMaterialDhPublicKey.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement6RequestKeyMaterialDhPublicKey)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
