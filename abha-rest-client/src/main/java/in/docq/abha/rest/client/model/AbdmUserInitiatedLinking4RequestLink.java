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
import java.util.UUID;
import javax.annotation.Nonnull;

public class AbdmUserInitiatedLinking4RequestLink {
    public static final String SERIALIZED_NAME_REFERENCE_NUMBER = "referenceNumber";
    @SerializedName("referenceNumber")
    @Nonnull
    private String referenceNumber;
    public static final String SERIALIZED_NAME_AUTHENTICATION_TYPE = "authenticationType";
    @SerializedName("authenticationType")
    @Nonnull
    private AuthenticationTypeEnum authenticationType;
    public static final String SERIALIZED_NAME_META = "meta";
    @SerializedName("meta")
    @Nonnull
    private AbdmUserInitiatedLinking4RequestLinkMeta meta;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking4RequestLink() {
    }

    public AbdmUserInitiatedLinking4RequestLink referenceNumber(@Nonnull String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    @Nonnull
    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public void setReferenceNumber(@Nonnull String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public AbdmUserInitiatedLinking4RequestLink authenticationType(@Nonnull AuthenticationTypeEnum authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }

    @Nonnull
    public AuthenticationTypeEnum getAuthenticationType() {
        return this.authenticationType;
    }

    public void setAuthenticationType(@Nonnull AuthenticationTypeEnum authenticationType) {
        this.authenticationType = authenticationType;
    }

    public AbdmUserInitiatedLinking4RequestLink meta(@Nonnull AbdmUserInitiatedLinking4RequestLinkMeta meta) {
        this.meta = meta;
        return this;
    }

    @Nonnull
    public AbdmUserInitiatedLinking4RequestLinkMeta getMeta() {
        return this.meta;
    }

    public void setMeta(@Nonnull AbdmUserInitiatedLinking4RequestLinkMeta meta) {
        this.meta = meta;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking4RequestLink abdmUserInitiatedLinking4RequestLink = (AbdmUserInitiatedLinking4RequestLink)o;
            return Objects.equals(this.referenceNumber, abdmUserInitiatedLinking4RequestLink.referenceNumber) && Objects.equals(this.authenticationType, abdmUserInitiatedLinking4RequestLink.authenticationType) && Objects.equals(this.meta, abdmUserInitiatedLinking4RequestLink.meta);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.referenceNumber, this.authenticationType, this.meta});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking4RequestLink {\n");
        sb.append("    referenceNumber: ").append(this.toIndentedString(this.referenceNumber)).append("\n");
        sb.append("    authenticationType: ").append(this.toIndentedString(this.authenticationType)).append("\n");
        sb.append("    meta: ").append(this.toIndentedString(this.meta)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking4RequestLink is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("referenceNumber").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `referenceNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("referenceNumber").toString()));
                            }

                            if (!jsonObj.get("authenticationType").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `authenticationType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("authenticationType").toString()));
                            }

                            AbdmUserInitiatedLinking4RequestLink.AuthenticationTypeEnum.validateJsonElement(jsonObj.get("authenticationType"));
                            AbdmUserInitiatedLinking4RequestLinkMeta.validateJsonElement(jsonObj.get("meta"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking4RequestLink` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking4RequestLink fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking4RequestLink)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking4RequestLink.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("referenceNumber");
        openapiFields.add("authenticationType");
        openapiFields.add("meta");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("referenceNumber");
        openapiRequiredFields.add("authenticationType");
        openapiRequiredFields.add("meta");
    }

    @JsonAdapter(AuthenticationTypeEnum.Adapter.class)
    public static enum AuthenticationTypeEnum {
        DIRECT("DIRECT"),
        MEDIATED("MEDIATED");

        private String value;

        private AuthenticationTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static AuthenticationTypeEnum fromValue(String value) {
            AuthenticationTypeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                AuthenticationTypeEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<AuthenticationTypeEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, AuthenticationTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public AuthenticationTypeEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmUserInitiatedLinking4RequestLink.AuthenticationTypeEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmUserInitiatedLinking4RequestLink.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmUserInitiatedLinking4RequestLink> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmUserInitiatedLinking4RequestLink.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmUserInitiatedLinking4RequestLink>() {
                    public void write(JsonWriter out, AbdmUserInitiatedLinking4RequestLink value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmUserInitiatedLinking4RequestLink read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmUserInitiatedLinking4RequestLink.validateJsonElement(jsonElement);
                        return (AbdmUserInitiatedLinking4RequestLink)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
