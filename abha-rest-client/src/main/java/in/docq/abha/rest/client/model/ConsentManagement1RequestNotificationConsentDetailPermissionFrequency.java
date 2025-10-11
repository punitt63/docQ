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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConsentManagement1RequestNotificationConsentDetailPermissionFrequency {
    public static final String SERIALIZED_NAME_UNIT = "unit";
    @SerializedName("unit")
    @Nonnull
    private UnitEnum unit;
    public static final String SERIALIZED_NAME_VALUE = "value";
    @SerializedName("value")
    @Nonnull
    private BigDecimal value;
    public static final String SERIALIZED_NAME_REPEATS = "repeats";
    @SerializedName("repeats")
    @Nullable
    private BigDecimal repeats;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency() {
    }

    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency unit(@Nonnull UnitEnum unit) {
        this.unit = unit;
        return this;
    }

    @Nonnull
    public UnitEnum getUnit() {
        return this.unit;
    }

    public void setUnit(@Nonnull UnitEnum unit) {
        this.unit = unit;
    }

    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency value(@Nonnull BigDecimal value) {
        this.value = value;
        return this;
    }

    @Nonnull
    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(@Nonnull BigDecimal value) {
        this.value = value;
    }

    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency repeats(@Nullable BigDecimal repeats) {
        this.repeats = repeats;
        return this;
    }

    @Nullable
    public BigDecimal getRepeats() {
        return this.repeats;
    }

    public void setRepeats(@Nullable BigDecimal repeats) {
        this.repeats = repeats;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ConsentManagement1RequestNotificationConsentDetailPermissionFrequency consentManagement1RequestNotificationConsentDetailPermissionFrequency = (ConsentManagement1RequestNotificationConsentDetailPermissionFrequency)o;
            return Objects.equals(this.unit, consentManagement1RequestNotificationConsentDetailPermissionFrequency.unit) && Objects.equals(this.value, consentManagement1RequestNotificationConsentDetailPermissionFrequency.value) && Objects.equals(this.repeats, consentManagement1RequestNotificationConsentDetailPermissionFrequency.repeats);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.unit, this.value, this.repeats});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentManagement1RequestNotificationConsentDetailPermissionFrequency {\n");
        sb.append("    unit: ").append(this.toIndentedString(this.unit)).append("\n");
        sb.append("    value: ").append(this.toIndentedString(this.value)).append("\n");
        sb.append("    repeats: ").append(this.toIndentedString(this.repeats)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in ConsentManagement1RequestNotificationConsentDetailPermissionFrequency is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("unit").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `unit` to be a primitive type in the JSON string but got `%s`", jsonObj.get("unit").toString()));
                            }

                            ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.UnitEnum.validateJsonElement(jsonObj.get("unit"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ConsentManagement1RequestNotificationConsentDetailPermissionFrequency` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static ConsentManagement1RequestNotificationConsentDetailPermissionFrequency fromJson(String jsonString) throws IOException {
        return (ConsentManagement1RequestNotificationConsentDetailPermissionFrequency)JSON.getGson().fromJson(jsonString, ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("unit");
        openapiFields.add("value");
        openapiFields.add("repeats");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("unit");
        openapiRequiredFields.add("value");
        openapiRequiredFields.add("repeats");
    }

    @JsonAdapter(UnitEnum.Adapter.class)
    public static enum UnitEnum {
        HOUR("HOUR"),
        DAY("DAY"),
        WEEK("WEEK"),
        MONTH("MONTH"),
        YEAR("YEAR");

        private String value;

        private UnitEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static UnitEnum fromValue(String value) {
            UnitEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                UnitEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<UnitEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, UnitEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public UnitEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.UnitEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<ConsentManagement1RequestNotificationConsentDetailPermissionFrequency> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.class));
                return (TypeAdapter<T>) (new TypeAdapter<ConsentManagement1RequestNotificationConsentDetailPermissionFrequency>() {
                    public void write(JsonWriter out, ConsentManagement1RequestNotificationConsentDetailPermissionFrequency value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.validateJsonElement(jsonElement);
                        return (ConsentManagement1RequestNotificationConsentDetailPermissionFrequency)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

