package in.docq.abha.rest.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class AbdmUserInitiatedLinking6Request {
    public static final String SERIALIZED_NAME_PATIENT = "patient";
    @SerializedName("patient")
    @Nonnull
    private List<AbdmUserInitiatedLinking6RequestPatientInner> patient = new ArrayList();
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmUserInitiatedLinking3RequestError error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmUserInitiatedLinking6RequestResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking6Request() {
    }

    public AbdmUserInitiatedLinking6Request patient(@Nonnull List<AbdmUserInitiatedLinking6RequestPatientInner> patient) {
        this.patient = patient;
        return this;
    }

    public AbdmUserInitiatedLinking6Request addPatientItem(AbdmUserInitiatedLinking6RequestPatientInner patientItem) {
        if (this.patient == null) {
            this.patient = new ArrayList();
        }

        this.patient.add(patientItem);
        return this;
    }

    @Nonnull
    public List<AbdmUserInitiatedLinking6RequestPatientInner> getPatient() {
        return this.patient;
    }

    public void setPatient(@Nonnull List<AbdmUserInitiatedLinking6RequestPatientInner> patient) {
        this.patient = patient;
    }

    public AbdmUserInitiatedLinking6Request error(@Nullable AbdmUserInitiatedLinking3RequestError error) {
        this.error = error;
        return this;
    }

    @Nullable
    public AbdmUserInitiatedLinking3RequestError getError() {
        return this.error;
    }

    public void setError(@Nullable AbdmUserInitiatedLinking3RequestError error) {
        this.error = error;
    }

    public AbdmUserInitiatedLinking6Request response(@Nonnull AbdmUserInitiatedLinking6RequestResponse response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmUserInitiatedLinking6RequestResponse getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmUserInitiatedLinking6RequestResponse response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking6Request abdmUserInitiatedLinking6Request = (AbdmUserInitiatedLinking6Request)o;
            return Objects.equals(this.patient, abdmUserInitiatedLinking6Request.patient) && Objects.equals(this.error, abdmUserInitiatedLinking6Request.error) && Objects.equals(this.response, abdmUserInitiatedLinking6Request.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.patient, this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking6Request {\n");
        sb.append("    patient: ").append(this.toIndentedString(this.patient)).append("\n");
        sb.append("    error: ").append(this.toIndentedString(this.error)).append("\n");
        sb.append("    response: ").append(this.toIndentedString(this.response)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking6Request is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking6Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
                }
            }

            var2 = openapiRequiredFields.iterator();

            while(var2.hasNext()) {
                String requiredField = (String)var2.next();
                if (jsonElement.getAsJsonObject().get(requiredField) == null) {
                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }
            }

            JsonObject jsonObj = jsonElement.getAsJsonObject();
            if (!jsonObj.get("patient").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `patient` to be an array in the JSON string but got `%s`", jsonObj.get("patient").toString()));
            } else {
                JsonArray jsonArraypatient = jsonObj.getAsJsonArray("patient");

                for(int i = 0; i < jsonArraypatient.size(); ++i) {
                    AbdmUserInitiatedLinking6RequestPatientInner.validateJsonElement(jsonArraypatient.get(i));
                }

                if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                    AbdmUserInitiatedLinking3RequestError.validateJsonElement(jsonObj.get("error"));
                }

                AbdmUserInitiatedLinking6RequestResponse.validateJsonElement(jsonObj.get("response"));
            }
        }
    }

    public static AbdmUserInitiatedLinking6Request fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking6Request)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking6Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("patient");
        openapiFields.add("error");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("patient");
        openapiRequiredFields.add("response");
    }
}
