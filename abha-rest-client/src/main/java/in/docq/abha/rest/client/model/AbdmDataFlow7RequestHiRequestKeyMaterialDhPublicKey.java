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

public class AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey {
    public static final String SERIALIZED_NAME_EXPIRY = "expiry";
    @SerializedName("expiry")
    @Nonnull
    private String expiry;
    public static final String SERIALIZED_NAME_PARAMETERS = "parameters";
    @SerializedName("parameters")
    @Nonnull
    private String parameters;
    public static final String SERIALIZED_NAME_KEY_VALUE = "keyValue";
    @SerializedName("keyValue")
    @Nonnull
    private String keyValue;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey() {
    }

    public AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey expiry(@Nonnull String expiry) {
        this.expiry = expiry;
        return this;
    }

    @Nonnull
    public String getExpiry() {
        return this.expiry;
    }

    public void setExpiry(@Nonnull String expiry) {
        this.expiry = expiry;
    }

    public AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey parameters(@Nonnull String parameters) {
        this.parameters = parameters;
        return this;
    }

    @Nonnull
    public String getParameters() {
        return this.parameters;
    }

    public void setParameters(@Nonnull String parameters) {
        this.parameters = parameters;
    }

    public AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey keyValue(@Nonnull String keyValue) {
        this.keyValue = keyValue;
        return this;
    }

    @Nonnull
    public String getKeyValue() {
        return this.keyValue;
    }

    public void setKeyValue(@Nonnull String keyValue) {
        this.keyValue = keyValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey abdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey = (AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey)o;
            return Objects.equals(this.expiry, abdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey.expiry) && Objects.equals(this.parameters, abdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey.parameters) && Objects.equals(this.keyValue, abdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey.keyValue);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.expiry, this.parameters, this.keyValue});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey {\n");
        sb.append("    expiry: ").append(this.toIndentedString(this.expiry)).append("\n");
        sb.append("    parameters: ").append(this.toIndentedString(this.parameters)).append("\n");
        sb.append("    keyValue: ").append(this.toIndentedString(this.keyValue)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("expiry").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `expiry` to be a primitive type in the JSON string but got `%s`", jsonObj.get("expiry").toString()));
                            }

                            if (!jsonObj.get("parameters").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `parameters` to be a primitive type in the JSON string but got `%s`", jsonObj.get("parameters").toString()));
                            }

                            if (!jsonObj.get("keyValue").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `keyValue` to be a primitive type in the JSON string but got `%s`", jsonObj.get("keyValue").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey)JSON.getGson().fromJson(jsonString, AbdmDataFlow7RequestHiRequestKeyMaterialDhPublicKey.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("expiry");
        openapiFields.add("parameters");
        openapiFields.add("keyValue");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("expiry");
        openapiRequiredFields.add("parameters");
        openapiRequiredFields.add("keyValue");
    }
}

