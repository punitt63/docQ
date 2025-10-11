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

public class AbdmConsentManagement1RequestConsentPurpose {
    public static final String SERIALIZED_NAME_TEXT = "text";
    @SerializedName("text")
    @Nonnull
    private TextEnum text;
    public static final String SERIALIZED_NAME_CODE = "code";
    @SerializedName("code")
    @Nonnull
    private CodeEnum code;
    public static final String SERIALIZED_NAME_REF_URI = "refUri";
    @SerializedName("refUri")
    @Nonnull
    private String refUri;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentPurpose() {
    }

    public AbdmConsentManagement1RequestConsentPurpose text(@Nonnull TextEnum text) {
        this.text = text;
        return this;
    }

    @Nonnull
    public TextEnum getText() {
        return this.text;
    }

    public void setText(@Nonnull TextEnum text) {
        this.text = text;
    }

    public AbdmConsentManagement1RequestConsentPurpose code(@Nonnull CodeEnum code) {
        this.code = code;
        return this;
    }

    @Nonnull
    public CodeEnum getCode() {
        return this.code;
    }

    public void setCode(@Nonnull CodeEnum code) {
        this.code = code;
    }

    public AbdmConsentManagement1RequestConsentPurpose refUri(@Nonnull String refUri) {
        this.refUri = refUri;
        return this;
    }

    @Nonnull
    public String getRefUri() {
        return this.refUri;
    }

    public void setRefUri(@Nonnull String refUri) {
        this.refUri = refUri;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentPurpose abdmConsentManagement1RequestConsentPurpose = (AbdmConsentManagement1RequestConsentPurpose)o;
            return Objects.equals(this.text, abdmConsentManagement1RequestConsentPurpose.text) && Objects.equals(this.code, abdmConsentManagement1RequestConsentPurpose.code) && Objects.equals(this.refUri, abdmConsentManagement1RequestConsentPurpose.refUri);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.text, this.code, this.refUri});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentPurpose {\n");
        sb.append("    text: ").append(this.toIndentedString(this.text)).append("\n");
        sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
        sb.append("    refUri: ").append(this.toIndentedString(this.refUri)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentPurpose is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("text").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `text` to be a primitive type in the JSON string but got `%s`", jsonObj.get("text").toString()));
                            }

                            AbdmConsentManagement1RequestConsentPurpose.TextEnum.validateJsonElement(jsonObj.get("text"));
                            if (!jsonObj.get("code").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `code` to be a primitive type in the JSON string but got `%s`", jsonObj.get("code").toString()));
                            }

                            AbdmConsentManagement1RequestConsentPurpose.CodeEnum.validateJsonElement(jsonObj.get("code"));
                            if (!jsonObj.get("refUri").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `refUri` to be a primitive type in the JSON string but got `%s`", jsonObj.get("refUri").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentPurpose` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentPurpose fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentPurpose)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentPurpose.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("text");
        openapiFields.add("code");
        openapiFields.add("refUri");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("text");
        openapiRequiredFields.add("code");
        openapiRequiredFields.add("refUri");
    }

    @JsonAdapter(TextEnum.Adapter.class)
    public static enum TextEnum {
        CARE_MANAGEMENT("Care Management"),
        BREAK_THE_GLASS("Break the Glass"),
        PUBLIC_HEALTH("Public Health"),
        HEALTHCARE_PAYMENT("Healthcare Payment"),
        DISEASE_SPECIFIC_HEALTHCARE_RESEARCH("Disease Specific Healthcare Research"),
        SELF_REQUESTED("Self Requested");

        private String value;

        private TextEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static TextEnum fromValue(String value) {
            TextEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                TextEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<TextEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, TextEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public TextEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement1RequestConsentPurpose.TextEnum.fromValue(value);
            }
        }
    }

    @JsonAdapter(CodeEnum.Adapter.class)
    public static enum CodeEnum {
        CAREMGT("CAREMGT"),
        BTG("BTG"),
        PUBHLTH("PUBHLTH"),
        HPAYMT("HPAYMT"),
        DSRCH("DSRCH"),
        PATRQT("PATRQT");

        private String value;

        private CodeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static CodeEnum fromValue(String value) {
            CodeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CodeEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<CodeEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, CodeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public CodeEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement1RequestConsentPurpose.CodeEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement1RequestConsentPurpose.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement1RequestConsentPurpose> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement1RequestConsentPurpose.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement1RequestConsentPurpose>() {
                    public void write(JsonWriter out, AbdmConsentManagement1RequestConsentPurpose value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement1RequestConsentPurpose read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement1RequestConsentPurpose.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement1RequestConsentPurpose)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}


