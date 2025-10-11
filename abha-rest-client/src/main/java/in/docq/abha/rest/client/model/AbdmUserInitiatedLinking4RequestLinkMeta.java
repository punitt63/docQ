package in.docq.abha.rest.client.model;

import com.google.gson.*;
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
import javax.annotation.Nullable;

public class AbdmUserInitiatedLinking4RequestLinkMeta {
    public static final String SERIALIZED_NAME_COMMUNICATION_MEDIUM = "communicationMedium";
    @SerializedName("communicationMedium")
    @Nonnull
    private CommunicationMediumEnum communicationMedium;
    public static final String SERIALIZED_NAME_COMMUNICATION_HINT = "communicationHint";
    @SerializedName("communicationHint")
    @Nullable
    private String communicationHint;
    public static final String SERIALIZED_NAME_COMMUNICATION_EXPIRY = "communicationExpiry";
    @SerializedName("communicationExpiry")
    @Nonnull
    private String communicationExpiry;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking4RequestLinkMeta() {
    }

    public AbdmUserInitiatedLinking4RequestLinkMeta communicationMedium(@Nonnull CommunicationMediumEnum communicationMedium) {
        this.communicationMedium = communicationMedium;
        return this;
    }

    @Nonnull
    public CommunicationMediumEnum getCommunicationMedium() {
        return this.communicationMedium;
    }

    public void setCommunicationMedium(@Nonnull CommunicationMediumEnum communicationMedium) {
        this.communicationMedium = communicationMedium;
    }

    public AbdmUserInitiatedLinking4RequestLinkMeta communicationHint(@Nullable String communicationHint) {
        this.communicationHint = communicationHint;
        return this;
    }

    @Nullable
    public String getCommunicationHint() {
        return this.communicationHint;
    }

    public void setCommunicationHint(@Nullable String communicationHint) {
        this.communicationHint = communicationHint;
    }

    public AbdmUserInitiatedLinking4RequestLinkMeta communicationExpiry(@Nonnull String communicationExpiry) {
        this.communicationExpiry = communicationExpiry;
        return this;
    }

    @Nonnull
    public String getCommunicationExpiry() {
        return this.communicationExpiry;
    }

    public void setCommunicationExpiry(@Nonnull String communicationExpiry) {
        this.communicationExpiry = communicationExpiry;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking4RequestLinkMeta abdmUserInitiatedLinking4RequestLinkMeta = (AbdmUserInitiatedLinking4RequestLinkMeta)o;
            return Objects.equals(this.communicationMedium, abdmUserInitiatedLinking4RequestLinkMeta.communicationMedium) && Objects.equals(this.communicationHint, abdmUserInitiatedLinking4RequestLinkMeta.communicationHint) && Objects.equals(this.communicationExpiry, abdmUserInitiatedLinking4RequestLinkMeta.communicationExpiry);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.communicationMedium, this.communicationHint, this.communicationExpiry});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking4RequestLinkMeta {\n");
        sb.append("    communicationMedium: ").append(this.toIndentedString(this.communicationMedium)).append("\n");
        sb.append("    communicationHint: ").append(this.toIndentedString(this.communicationHint)).append("\n");
        sb.append("    communicationExpiry: ").append(this.toIndentedString(this.communicationExpiry)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking4RequestLinkMeta is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("communicationMedium").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `communicationMedium` to be a primitive type in the JSON string but got `%s`", jsonObj.get("communicationMedium").toString()));
                            }

                            AbdmUserInitiatedLinking4RequestLinkMeta.CommunicationMediumEnum.validateJsonElement(jsonObj.get("communicationMedium"));
                            if (jsonObj.get("communicationHint") != null && !jsonObj.get("communicationHint").isJsonNull() && !jsonObj.get("communicationHint").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `communicationHint` to be a primitive type in the JSON string but got `%s`", jsonObj.get("communicationHint").toString()));
                            }

                            if (!jsonObj.get("communicationExpiry").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `communicationExpiry` to be a primitive type in the JSON string but got `%s`", jsonObj.get("communicationExpiry").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking4RequestLinkMeta` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking4RequestLinkMeta fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking4RequestLinkMeta)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking4RequestLinkMeta.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("communicationMedium");
        openapiFields.add("communicationHint");
        openapiFields.add("communicationExpiry");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("communicationMedium");
        openapiRequiredFields.add("communicationHint");
        openapiRequiredFields.add("communicationExpiry");
    }

    @JsonAdapter(CommunicationMediumEnum.Adapter.class)
    public static enum CommunicationMediumEnum {
        MOBILE("MOBILE"),
        EMAIL("EMAIL");

        private String value;

        private CommunicationMediumEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static CommunicationMediumEnum fromValue(String value) {
            CommunicationMediumEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CommunicationMediumEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<CommunicationMediumEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, CommunicationMediumEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public CommunicationMediumEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmUserInitiatedLinking4RequestLinkMeta.CommunicationMediumEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmUserInitiatedLinking4RequestLinkMeta.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmUserInitiatedLinking4RequestLinkMeta> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmUserInitiatedLinking4RequestLinkMeta.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmUserInitiatedLinking4RequestLinkMeta>() {
                    public void write(JsonWriter out, AbdmUserInitiatedLinking4RequestLinkMeta value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmUserInitiatedLinking4RequestLinkMeta read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmUserInitiatedLinking4RequestLinkMeta.validateJsonElement(jsonElement);
                        return (AbdmUserInitiatedLinking4RequestLinkMeta)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
