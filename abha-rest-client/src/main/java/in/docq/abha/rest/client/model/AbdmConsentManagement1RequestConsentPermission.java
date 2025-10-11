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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmConsentManagement1RequestConsentPermission {
    public static final String SERIALIZED_NAME_ACCESS_MODE = "accessMode";
    @SerializedName("accessMode")
    @Nonnull
    private AccessModeEnum accessMode;
    public static final String SERIALIZED_NAME_DATE_RANGE = "dateRange";
    @SerializedName("dateRange")
    @Nonnull
    private AbdmConsentManagement1RequestConsentPermissionDateRange dateRange;
    public static final String SERIALIZED_NAME_DATA_ERASE_AT = "dataEraseAt";
    @SerializedName("dataEraseAt")
    @Nonnull
    private OffsetDateTime dataEraseAt;
    public static final String SERIALIZED_NAME_FREQUENCY = "frequency";
    @SerializedName("frequency")
    @Nonnull
    private ConsentManagement1RequestNotificationConsentDetailPermissionFrequency frequency;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentPermission() {
    }

    public AbdmConsentManagement1RequestConsentPermission accessMode(@Nonnull AccessModeEnum accessMode) {
        this.accessMode = accessMode;
        return this;
    }

    @Nonnull
    public AccessModeEnum getAccessMode() {
        return this.accessMode;
    }

    public void setAccessMode(@Nonnull AccessModeEnum accessMode) {
        this.accessMode = accessMode;
    }

    public AbdmConsentManagement1RequestConsentPermission dateRange(@Nonnull AbdmConsentManagement1RequestConsentPermissionDateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentPermissionDateRange getDateRange() {
        return this.dateRange;
    }

    public void setDateRange(@Nonnull AbdmConsentManagement1RequestConsentPermissionDateRange dateRange) {
        this.dateRange = dateRange;
    }

    public AbdmConsentManagement1RequestConsentPermission dataEraseAt(@Nonnull OffsetDateTime dataEraseAt) {
        this.dataEraseAt = dataEraseAt;
        return this;
    }

    @Nonnull
    public OffsetDateTime getDataEraseAt() {
        return this.dataEraseAt;
    }

    public void setDataEraseAt(@Nonnull OffsetDateTime dataEraseAt) {
        this.dataEraseAt = dataEraseAt;
    }

    public AbdmConsentManagement1RequestConsentPermission frequency(@Nonnull ConsentManagement1RequestNotificationConsentDetailPermissionFrequency frequency) {
        this.frequency = frequency;
        return this;
    }

    @Nonnull
    public ConsentManagement1RequestNotificationConsentDetailPermissionFrequency getFrequency() {
        return this.frequency;
    }

    public void setFrequency(@Nonnull ConsentManagement1RequestNotificationConsentDetailPermissionFrequency frequency) {
        this.frequency = frequency;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentPermission abdmConsentManagement1RequestConsentPermission = (AbdmConsentManagement1RequestConsentPermission)o;
            return Objects.equals(this.accessMode, abdmConsentManagement1RequestConsentPermission.accessMode) && Objects.equals(this.dateRange, abdmConsentManagement1RequestConsentPermission.dateRange) && Objects.equals(this.dataEraseAt, abdmConsentManagement1RequestConsentPermission.dataEraseAt) && Objects.equals(this.frequency, abdmConsentManagement1RequestConsentPermission.frequency);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.accessMode, this.dateRange, this.dataEraseAt, this.frequency});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentPermission {\n");
        sb.append("    accessMode: ").append(this.toIndentedString(this.accessMode)).append("\n");
        sb.append("    dateRange: ").append(this.toIndentedString(this.dateRange)).append("\n");
        sb.append("    dataEraseAt: ").append(this.toIndentedString(this.dataEraseAt)).append("\n");
        sb.append("    frequency: ").append(this.toIndentedString(this.frequency)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentPermission is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("accessMode").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `accessMode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("accessMode").toString()));
                            }

                            AbdmConsentManagement1RequestConsentPermission.AccessModeEnum.validateJsonElement(jsonObj.get("accessMode"));
                            AbdmConsentManagement1RequestConsentPermissionDateRange.validateJsonElement(jsonObj.get("dateRange"));
                            ConsentManagement1RequestNotificationConsentDetailPermissionFrequency.validateJsonElement(jsonObj.get("frequency"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentPermission` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentPermission fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentPermission)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentPermission.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("accessMode");
        openapiFields.add("dateRange");
        openapiFields.add("dataEraseAt");
        openapiFields.add("frequency");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("accessMode");
        openapiRequiredFields.add("dateRange");
        openapiRequiredFields.add("dataEraseAt");
        openapiRequiredFields.add("frequency");
    }

    @JsonAdapter(AccessModeEnum.Adapter.class)
    public static enum AccessModeEnum {
        VIEW("VIEW"),
        STORE("STORE"),
        STREAM("STREAM"),
        QUERY("QUERY");

        private String value;

        private AccessModeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static AccessModeEnum fromValue(String value) {
            AccessModeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                AccessModeEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<AccessModeEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, AccessModeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public AccessModeEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement1RequestConsentPermission.AccessModeEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement1RequestConsentPermission.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement1RequestConsentPermission> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement1RequestConsentPermission.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement1RequestConsentPermission>() {
                    public void write(JsonWriter out, AbdmConsentManagement1RequestConsentPermission value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement1RequestConsentPermission read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement1RequestConsentPermission.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement1RequestConsentPermission)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}


