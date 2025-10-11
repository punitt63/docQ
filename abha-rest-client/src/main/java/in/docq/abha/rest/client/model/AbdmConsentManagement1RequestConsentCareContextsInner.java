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

public class AbdmConsentManagement1RequestConsentCareContextsInner {
    public static final String SERIALIZED_NAME_PATIENT_REFERENCE = "patientReference";
    @SerializedName("patientReference")
    @Nonnull
    private String patientReference;
    public static final String SERIALIZED_NAME_CARE_CONTEXT_REFERENCE = "careContextReference";
    @SerializedName("careContextReference")
    @Nonnull
    private String careContextReference;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentCareContextsInner() {
    }

    public AbdmConsentManagement1RequestConsentCareContextsInner patientReference(@Nonnull String patientReference) {
        this.patientReference = patientReference;
        return this;
    }

    @Nonnull
    public String getPatientReference() {
        return this.patientReference;
    }

    public void setPatientReference(@Nonnull String patientReference) {
        this.patientReference = patientReference;
    }

    public AbdmConsentManagement1RequestConsentCareContextsInner careContextReference(@Nonnull String careContextReference) {
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
            AbdmConsentManagement1RequestConsentCareContextsInner abdmConsentManagement1RequestConsentCareContextsInner = (AbdmConsentManagement1RequestConsentCareContextsInner)o;
            return Objects.equals(this.patientReference, abdmConsentManagement1RequestConsentCareContextsInner.patientReference) && Objects.equals(this.careContextReference, abdmConsentManagement1RequestConsentCareContextsInner.careContextReference);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.patientReference, this.careContextReference});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentCareContextsInner {\n");
        sb.append("    patientReference: ").append(this.toIndentedString(this.patientReference)).append("\n");
        sb.append("    careContextReference: ").append(this.toIndentedString(this.careContextReference)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentCareContextsInner is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("patientReference").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `patientReference` to be a primitive type in the JSON string but got `%s`", jsonObj.get("patientReference").toString()));
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

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentCareContextsInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentCareContextsInner fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentCareContextsInner)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentCareContextsInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("patientReference");
        openapiFields.add("careContextReference");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("patientReference");
        openapiRequiredFields.add("careContextReference");
    }
}

