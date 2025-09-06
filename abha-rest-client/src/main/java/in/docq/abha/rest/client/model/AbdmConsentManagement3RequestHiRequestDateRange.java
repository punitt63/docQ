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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmConsentManagement3RequestHiRequestDateRange {
    public static final String SERIALIZED_NAME_FROM = "from";
    @SerializedName("from")
    @Nonnull
    private OffsetDateTime from;
    public static final String SERIALIZED_NAME_TO = "to";
    @SerializedName("to")
    @Nonnull
    private OffsetDateTime to;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3RequestHiRequestDateRange() {
    }

    public AbdmConsentManagement3RequestHiRequestDateRange from(@Nonnull OffsetDateTime from) {
        this.from = from;
        return this;
    }

    @Nonnull
    public OffsetDateTime getFrom() {
        return this.from;
    }

    public void setFrom(@Nonnull OffsetDateTime from) {
        this.from = from;
    }

    public AbdmConsentManagement3RequestHiRequestDateRange to(@Nonnull OffsetDateTime to) {
        this.to = to;
        return this;
    }

    @Nonnull
    public OffsetDateTime getTo() {
        return this.to;
    }

    public void setTo(@Nonnull OffsetDateTime to) {
        this.to = to;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3RequestHiRequestDateRange abdmConsentManagement3RequestHiRequestDateRange = (AbdmConsentManagement3RequestHiRequestDateRange)o;
            return Objects.equals(this.from, abdmConsentManagement3RequestHiRequestDateRange.from) && Objects.equals(this.to, abdmConsentManagement3RequestHiRequestDateRange.to);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.from, this.to});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3RequestHiRequestDateRange {\n");
        sb.append("    from: ").append(this.toIndentedString(this.from)).append("\n");
        sb.append("    to: ").append(this.toIndentedString(this.to)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3RequestHiRequestDateRange is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3RequestHiRequestDateRange` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement3RequestHiRequestDateRange fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3RequestHiRequestDateRange)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3RequestHiRequestDateRange.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("from");
        openapiFields.add("to");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("from");
        openapiRequiredFields.add("to");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement3RequestHiRequestDateRange.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3RequestHiRequestDateRange> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3RequestHiRequestDateRange.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3RequestHiRequestDateRange>() {
                    public void write(JsonWriter out, AbdmConsentManagement3RequestHiRequestDateRange value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3RequestHiRequestDateRange read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3RequestHiRequestDateRange.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3RequestHiRequestDateRange)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

