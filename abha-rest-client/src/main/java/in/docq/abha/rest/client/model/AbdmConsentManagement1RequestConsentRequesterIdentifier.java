package in.docq.abha.rest.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmConsentManagement1RequestConsentRequesterIdentifier {
    public static final String SERIALIZED_NAME_VALUE = "value";
    @SerializedName("value")
    @Nonnull
    private String value;
    public static final String SERIALIZED_NAME_TYPE = "type";
    @SerializedName("type")
    @Nonnull
    private String type;
    public static final String SERIALIZED_NAME_SYSTEM = "system";
    @SerializedName("system")
    @Nonnull
    private String system;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentRequesterIdentifier() {
    }

    public AbdmConsentManagement1RequestConsentRequesterIdentifier value(@Nonnull String value) {
        this.value = value;
        return this;
    }

    @Nonnull
    public String getValue() {
        return this.value;
    }

    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    public AbdmConsentManagement1RequestConsentRequesterIdentifier type(@Nonnull String type) {
        this.type = type;
        return this;
    }

    @Nonnull
    public String getType() {
        return this.type;
    }

    public void setType(@Nonnull String type) {
        this.type = type;
    }

    public AbdmConsentManagement1RequestConsentRequesterIdentifier system(@Nonnull String system) {
        this.system = system;
        return this;
    }

    @Nonnull
    public String getSystem() {
        return this.system;
    }

    public void setSystem(@Nonnull String system) {
        this.system = system;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentRequesterIdentifier abdmConsentManagement1RequestConsentRequesterIdentifier = (AbdmConsentManagement1RequestConsentRequesterIdentifier)o;
            return Objects.equals(this.value, abdmConsentManagement1RequestConsentRequesterIdentifier.value) && Objects.equals(this.type, abdmConsentManagement1RequestConsentRequesterIdentifier.type) && Objects.equals(this.system, abdmConsentManagement1RequestConsentRequesterIdentifier.system);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.value, this.type, this.system});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentRequesterIdentifier {\n");
        sb.append("    value: ").append(this.toIndentedString(this.value)).append("\n");
        sb.append("    type: ").append(this.toIndentedString(this.type)).append("\n");
        sb.append("    system: ").append(this.toIndentedString(this.system)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentRequesterIdentifier is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("value").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `value` to be a primitive type in the JSON string but got `%s`", jsonObj.get("value").toString()));
                            }

                            if (!jsonObj.get("type").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
                            }

                            if (!jsonObj.get("system").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `system` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentRequesterIdentifier` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentRequesterIdentifier fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentRequesterIdentifier)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentRequesterIdentifier.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("value");
        openapiFields.add("type");
        openapiFields.add("system");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("value");
        openapiRequiredFields.add("type");
        openapiRequiredFields.add("system");
    }
}

