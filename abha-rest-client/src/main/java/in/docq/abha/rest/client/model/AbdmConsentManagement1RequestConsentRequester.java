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

public class AbdmConsentManagement1RequestConsentRequester {
    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName("name")
    @Nonnull
    private String name;
    public static final String SERIALIZED_NAME_IDENTIFIER = "identifier";
    @SerializedName("identifier")
    @Nonnull
    private AbdmConsentManagement1RequestConsentRequesterIdentifier identifier;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentRequester() {
    }

    public AbdmConsentManagement1RequestConsentRequester name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public AbdmConsentManagement1RequestConsentRequester identifier(@Nonnull AbdmConsentManagement1RequestConsentRequesterIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentRequesterIdentifier getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(@Nonnull AbdmConsentManagement1RequestConsentRequesterIdentifier identifier) {
        this.identifier = identifier;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentRequester abdmConsentManagement1RequestConsentRequester = (AbdmConsentManagement1RequestConsentRequester)o;
            return Objects.equals(this.name, abdmConsentManagement1RequestConsentRequester.name) && Objects.equals(this.identifier, abdmConsentManagement1RequestConsentRequester.identifier);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.identifier});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentRequester {\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    identifier: ").append(this.toIndentedString(this.identifier)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentRequester is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("name").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
                            }

                            AbdmConsentManagement1RequestConsentRequesterIdentifier.validateJsonElement(jsonObj.get("identifier"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentRequester` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentRequester fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentRequester)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentRequester.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("name");
        openapiFields.add("identifier");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("name");
        openapiRequiredFields.add("identifier");
    }
}

