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

public class AbdmDataFlow7RequestHiRequestConsent {
    public static final String SERIALIZED_NAME_ID = "id";
    @SerializedName("id")
    @Nonnull
    private String id;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow7RequestHiRequestConsent() {
    }

    public AbdmDataFlow7RequestHiRequestConsent id(@Nonnull String id) {
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
            AbdmDataFlow7RequestHiRequestConsent abdmDataFlow7RequestHiRequestConsent = (AbdmDataFlow7RequestHiRequestConsent)o;
            return Objects.equals(this.id, abdmDataFlow7RequestHiRequestConsent.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow7RequestHiRequestConsent {\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow7RequestHiRequestConsent is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow7RequestHiRequestConsent` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow7RequestHiRequestConsent fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow7RequestHiRequestConsent)JSON.getGson().fromJson(jsonString, AbdmDataFlow7RequestHiRequestConsent.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("id");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("id");
    }
}

