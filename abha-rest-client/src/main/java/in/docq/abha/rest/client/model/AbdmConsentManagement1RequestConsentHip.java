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
import javax.annotation.Nullable;

public class AbdmConsentManagement1RequestConsentHip {
    public static final String SERIALIZED_NAME_ID = "id";
    @SerializedName("id")
    @Nonnull
    private String id;
    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName("name")
    @Nullable
    private String name;
    public static final String SERIALIZED_NAME_TYPE = "type";
    @SerializedName("type")
    @Nullable
    private String type;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentHip() {
    }

    public AbdmConsentManagement1RequestConsentHip id(@Nonnull String id) {
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

    public AbdmConsentManagement1RequestConsentHip name(@Nullable String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public AbdmConsentManagement1RequestConsentHip type(@Nullable String type) {
        this.type = type;
        return this;
    }

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentHip abdmConsentManagement1RequestConsentHip = (AbdmConsentManagement1RequestConsentHip)o;
            return Objects.equals(this.id, abdmConsentManagement1RequestConsentHip.id) && Objects.equals(this.name, abdmConsentManagement1RequestConsentHip.name) && Objects.equals(this.type, abdmConsentManagement1RequestConsentHip.type);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.name, this.type});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentHip {\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    type: ").append(this.toIndentedString(this.type)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentHip is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("id").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
                            }

                            if (jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull() && !jsonObj.get("name").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
                            }

                            if (jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull() && !jsonObj.get("type").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentHip` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentHip fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentHip)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentHip.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("id");
        openapiFields.add("name");
        openapiFields.add("type");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("id");
        openapiRequiredFields.add("name");
    }
}

