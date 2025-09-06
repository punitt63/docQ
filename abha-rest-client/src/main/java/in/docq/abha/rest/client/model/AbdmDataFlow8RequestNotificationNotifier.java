package in.docq.abha.rest.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

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
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmDataFlow8RequestNotificationNotifier {
    public static final String SERIALIZED_NAME_TYPE = "type";
    @SerializedName("type")
    @Nonnull
    private TypeEnum type;
    public static final String SERIALIZED_NAME_ID = "id";
    @SerializedName("id")
    @Nonnull
    private String id;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow8RequestNotificationNotifier() {
    }

    public AbdmDataFlow8RequestNotificationNotifier type(@Nonnull TypeEnum type) {
        this.type = type;
        return this;
    }

    @Nonnull
    public TypeEnum getType() {
        return this.type;
    }

    public void setType(@Nonnull TypeEnum type) {
        this.type = type;
    }

    public AbdmDataFlow8RequestNotificationNotifier id(@Nonnull String id) {
        this.id = id;
        return this;
    }

    @Nonnull
    public String getId() {
        return this.id;
    }

    public void setId(@Nonnull String id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow8RequestNotificationNotifier abdmDataFlow8RequestNotificationNotifier = (AbdmDataFlow8RequestNotificationNotifier)o;
            return Objects.equals(this.type, abdmDataFlow8RequestNotificationNotifier.type) && Objects.equals(this.id, abdmDataFlow8RequestNotificationNotifier.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.id});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow8RequestNotificationNotifier {\n");
        sb.append("    type: ").append(this.toIndentedString(this.type)).append("\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow8RequestNotificationNotifier is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("type").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
                            }

                            AbdmDataFlow8RequestNotificationNotifier.TypeEnum.validateJsonElement(jsonObj.get("type"));
                            if (!jsonObj.get("id").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow8RequestNotificationNotifier` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow8RequestNotificationNotifier fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow8RequestNotificationNotifier)JSON.getGson().fromJson(jsonString, AbdmDataFlow8RequestNotificationNotifier.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("type");
        openapiFields.add("id");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("type");
        openapiRequiredFields.add("id");
    }

    @JsonAdapter(TypeEnum.Adapter.class)
    public static enum TypeEnum {
        HIU("HIU"),
        HIP("HIP");

        private String value;

        private TypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static TypeEnum fromValue(String value) {
            TypeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                TypeEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<TypeEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, TypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public TypeEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmDataFlow8RequestNotificationNotifier.TypeEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmDataFlow8RequestNotificationNotifier.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmDataFlow8RequestNotificationNotifier> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmDataFlow8RequestNotificationNotifier.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmDataFlow8RequestNotificationNotifier>() {
                    public void write(JsonWriter out, AbdmDataFlow8RequestNotificationNotifier value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmDataFlow8RequestNotificationNotifier read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmDataFlow8RequestNotificationNotifier.validateJsonElement(jsonElement);
                        return (AbdmDataFlow8RequestNotificationNotifier)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}


