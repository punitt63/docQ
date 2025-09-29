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

public class AbdmConsentManagement6RequestEntriesInner {
    public static final String SERIALIZED_NAME_CONTENT = "content";
    @SerializedName("content")
    @Nonnull
    private String content;
    public static final String SERIALIZED_NAME_MEDIA = "media";
    @SerializedName("media")
    @Nonnull
    private MediaEnum media;
    public static final String SERIALIZED_NAME_CHECKSUM = "checksum";
    @SerializedName("checksum")
    @Nonnull
    private String checksum;
    public static final String SERIALIZED_NAME_CARE_CONTEXT_REFERENCE = "careContextReference";
    @SerializedName("careContextReference")
    @Nonnull
    private String careContextReference;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement6RequestEntriesInner() {
    }

    public AbdmConsentManagement6RequestEntriesInner content(@Nonnull String content) {
        this.content = content;
        return this;
    }

    @Nonnull
    public String getContent() {
        return this.content;
    }

    public void setContent(@Nonnull String content) {
        this.content = content;
    }

    public AbdmConsentManagement6RequestEntriesInner media(@Nonnull MediaEnum media) {
        this.media = media;
        return this;
    }

    @Nonnull
    public MediaEnum getMedia() {
        return this.media;
    }

    public void setMedia(@Nonnull MediaEnum media) {
        this.media = media;
    }

    public AbdmConsentManagement6RequestEntriesInner checksum(@Nonnull String checksum) {
        this.checksum = checksum;
        return this;
    }

    @Nonnull
    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(@Nonnull String checksum) {
        this.checksum = checksum;
    }

    public AbdmConsentManagement6RequestEntriesInner careContextReference(@Nonnull String careContextReference) {
        this.careContextReference = careContextReference;
        return this;
    }

    @Nonnull
    public String getCareContextReference() {
        return this.careContextReference;
    }

    public void setCareContextReference(@Nonnull String careContextReference) {
        this.careContextReference = careContextReference;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement6RequestEntriesInner abdmConsentManagement6RequestEntriesInner = (AbdmConsentManagement6RequestEntriesInner)o;
            return Objects.equals(this.content, abdmConsentManagement6RequestEntriesInner.content) && Objects.equals(this.media, abdmConsentManagement6RequestEntriesInner.media) && Objects.equals(this.checksum, abdmConsentManagement6RequestEntriesInner.checksum) && Objects.equals(this.careContextReference, abdmConsentManagement6RequestEntriesInner.careContextReference);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.content, this.media, this.checksum, this.careContextReference});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement6RequestEntriesInner {\n");
        sb.append("    content: ").append(this.toIndentedString(this.content)).append("\n");
        sb.append("    media: ").append(this.toIndentedString(this.media)).append("\n");
        sb.append("    checksum: ").append(this.toIndentedString(this.checksum)).append("\n");
        sb.append("    careContextReference: ").append(this.toIndentedString(this.careContextReference)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement6RequestEntriesInner is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("content").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `content` to be a primitive type in the JSON string but got `%s`", jsonObj.get("content").toString()));
                            }

                            if (!jsonObj.get("media").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `media` to be a primitive type in the JSON string but got `%s`", jsonObj.get("media").toString()));
                            }

                            AbdmConsentManagement6RequestEntriesInner.MediaEnum.validateJsonElement(jsonObj.get("media"));
                            if (!jsonObj.get("checksum").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `checksum` to be a primitive type in the JSON string but got `%s`", jsonObj.get("checksum").toString()));
                            }

                            if (!jsonObj.get("careContextReference").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `careContextReference` to be a primitive type in the JSON string but got `%s`", jsonObj.get("careContextReference").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement6RequestEntriesInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement6RequestEntriesInner fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement6RequestEntriesInner) JSON.getGson().fromJson(jsonString, AbdmConsentManagement6RequestEntriesInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("content");
        openapiFields.add("media");
        openapiFields.add("checksum");
        openapiFields.add("careContextReference");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("content");
        openapiRequiredFields.add("media");
        openapiRequiredFields.add("checksum");
        openapiRequiredFields.add("careContextReference");
    }

    @JsonAdapter(MediaEnum.Adapter.class)
    public static enum MediaEnum {
        APPLICATION_FHIR_JSON("application/fhir+json");

        private String value;

        private MediaEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static MediaEnum fromValue(String value) {
            MediaEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MediaEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<MediaEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, MediaEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public MediaEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement6RequestEntriesInner.MediaEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement6RequestEntriesInner.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement6RequestEntriesInner> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement6RequestEntriesInner.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement6RequestEntriesInner>() {
                    public void write(JsonWriter out, AbdmConsentManagement6RequestEntriesInner value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement6RequestEntriesInner read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement6RequestEntriesInner.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement6RequestEntriesInner)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
