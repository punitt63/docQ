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

public class AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner {
    public static final String SERIALIZED_NAME_REFERENCE_NUMBER = "referenceNumber";
    @SerializedName("referenceNumber")
    @Nonnull
    private String referenceNumber;
    public static final String SERIALIZED_NAME_DISPLAY = "display";
    @SerializedName("display")
    @Nullable
    private String display;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner() {
    }

    public AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner referenceNumber(@Nonnull String referenceNumber) {
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

    public AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner display(@Nullable String display) {
        this.display = display;
        return this;
    }

    @Nullable
    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(@Nullable String display) {
        this.display = display;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner abdmUserInitiatedLinking6RequestPatientInnerCareContextsInner = (AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner)o;
            return Objects.equals(this.referenceNumber, abdmUserInitiatedLinking6RequestPatientInnerCareContextsInner.referenceNumber) && Objects.equals(this.display, abdmUserInitiatedLinking6RequestPatientInnerCareContextsInner.display);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.referenceNumber, this.display});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner {\n");
        sb.append("    referenceNumber: ").append(this.toIndentedString(this.referenceNumber)).append("\n");
        sb.append("    display: ").append(this.toIndentedString(this.display)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            if (jsonObj.get("display") != null && !jsonObj.get("display").isJsonNull() && !jsonObj.get("display").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `display` to be a primitive type in the JSON string but got `%s`", jsonObj.get("display").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking6RequestPatientInnerCareContextsInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("referenceNumber");
        openapiFields.add("display");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("referenceNumber");
        openapiRequiredFields.add("display");
    }
}
