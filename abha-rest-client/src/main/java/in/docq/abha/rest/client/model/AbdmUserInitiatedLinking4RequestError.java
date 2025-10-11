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

public class AbdmUserInitiatedLinking4RequestError {
    public static final String SERIALIZED_NAME_CODE = "code";
    @SerializedName("code")
    @Nonnull
    private String code;
    public static final String SERIALIZED_NAME_MESSAGE = "message";
    @SerializedName("message")
    @Nonnull
    private String message;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking4RequestError() {
    }

    public AbdmUserInitiatedLinking4RequestError code(@Nonnull String code) {
        this.code = code;
        return this;
    }

    @Nonnull
    public String getCode() {
        return this.code;
    }

    public void setCode(@Nonnull String code) {
        this.code = code;
    }

    public AbdmUserInitiatedLinking4RequestError message(@Nonnull String message) {
        this.message = message;
        return this;
    }

    @Nonnull
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nonnull String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking4RequestError abdmUserInitiatedLinking4RequestError = (AbdmUserInitiatedLinking4RequestError)o;
            return Objects.equals(this.code, abdmUserInitiatedLinking4RequestError.code) && Objects.equals(this.message, abdmUserInitiatedLinking4RequestError.message);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.code, this.message});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking4RequestError {\n");
        sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
        sb.append("    message: ").append(this.toIndentedString(this.message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking4RequestError is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("code").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `code` to be a primitive type in the JSON string but got `%s`", jsonObj.get("code").toString()));
                            }

                            if (!jsonObj.get("message").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `message` to be a primitive type in the JSON string but got `%s`", jsonObj.get("message").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking4RequestError` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking4RequestError fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking4RequestError) JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking4RequestError.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("code");
        openapiFields.add("message");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("code");
        openapiRequiredFields.add("message");
    }
}
