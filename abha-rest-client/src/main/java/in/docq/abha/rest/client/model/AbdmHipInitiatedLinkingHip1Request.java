package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class AbdmHipInitiatedLinkingHip1Request {
    public static final String SERIALIZED_NAME_ABHA_NUMBER = "abhaNumber";
    @SerializedName("abhaNumber")
    @Nullable
    private BigDecimal abhaNumber;
    public static final String SERIALIZED_NAME_ABHA_ADDRESS = "abhaAddress";
    @SerializedName("abhaAddress")
    @Nonnull
    private String abhaAddress;
    public static final String SERIALIZED_NAME_PATIENT = "patient";
    @SerializedName("patient")
    @Nonnull
    private List<AbdmHipInitiatedLinkingHip1RequestPatientInner> patients = new ArrayList();
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmHipInitiatedLinkingHip1Request() {
    }

    public AbdmHipInitiatedLinkingHip1Request abhaNumber(@Nullable BigDecimal abhaNumber) {
        this.abhaNumber = abhaNumber;
        return this;
    }

    @Nullable
    public BigDecimal getAbhaNumber() {
        return this.abhaNumber;
    }

    public void setAbhaNumber(@Nullable BigDecimal abhaNumber) {
        this.abhaNumber = abhaNumber;
    }

    public AbdmHipInitiatedLinkingHip1Request abhaAddress(@Nonnull String abhaAddress) {
        this.abhaAddress = abhaAddress;
        return this;
    }

    @Nonnull
    public String getAbhaAddress() {
        return this.abhaAddress;
    }

    public void setAbhaAddress(@Nonnull String abhaAddress) {
        this.abhaAddress = abhaAddress;
    }

    public AbdmHipInitiatedLinkingHip1Request patients(@Nonnull List<AbdmHipInitiatedLinkingHip1RequestPatientInner> patient) {
        this.patients = patient;
        return this;
    }

    public AbdmHipInitiatedLinkingHip1Request addPatientItem(AbdmHipInitiatedLinkingHip1RequestPatientInner patientItem) {
        if (this.patients == null) {
            this.patients = new ArrayList();
        }

        this.patients.add(patientItem);
        return this;
    }

    @Nonnull
    public List<AbdmHipInitiatedLinkingHip1RequestPatientInner> getPatients() {
        return this.patients;
    }

    public void setPatients(@Nonnull List<AbdmHipInitiatedLinkingHip1RequestPatientInner> patients) {
        this.patients = patients;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmHipInitiatedLinkingHip1Request abdmHipInitiatedLinkingHip1Request = (AbdmHipInitiatedLinkingHip1Request)o;
            return Objects.equals(this.abhaNumber, abdmHipInitiatedLinkingHip1Request.abhaNumber) && Objects.equals(this.abhaAddress, abdmHipInitiatedLinkingHip1Request.abhaAddress) && Objects.equals(this.patients, abdmHipInitiatedLinkingHip1Request.patients);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.abhaNumber, this.abhaAddress, this.patients});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmHipInitiatedLinkingHip1Request {\n");
        sb.append("    abhaNumber: ").append(this.toIndentedString(this.abhaNumber)).append("\n");
        sb.append("    abhaAddress: ").append(this.toIndentedString(this.abhaAddress)).append("\n");
        sb.append("    patient: ").append(this.toIndentedString(this.patients)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmHipInitiatedLinkingHip1Request is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmHipInitiatedLinkingHip1Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            if (!jsonObj.get("abhaAddress").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `abhaAddress` to be a primitive type in the JSON string but got `%s`", jsonObj.get("abhaAddress").toString()));
            } else if (!jsonObj.get("patient").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `patient` to be an array in the JSON string but got `%s`", jsonObj.get("patient").toString()));
            } else {
                JsonArray jsonArraypatient = jsonObj.getAsJsonArray("patient");

                for(int i = 0; i < jsonArraypatient.size(); ++i) {
                    AbdmHipInitiatedLinkingHip1RequestPatientInner.validateJsonElement(jsonArraypatient.get(i));
                }

            }
        }
    }

    public static AbdmHipInitiatedLinkingHip1Request fromJson(String jsonString) throws IOException {
        return (AbdmHipInitiatedLinkingHip1Request)JSON.getGson().fromJson(jsonString, AbdmHipInitiatedLinkingHip1Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("abhaNumber");
        openapiFields.add("abhaAddress");
        openapiFields.add("patient");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("abhaAddress");
        openapiRequiredFields.add("patient");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmHipInitiatedLinkingHip1Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmHipInitiatedLinkingHip1Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmHipInitiatedLinkingHip1Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmHipInitiatedLinkingHip1Request>() {
                    public void write(JsonWriter out, AbdmHipInitiatedLinkingHip1Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmHipInitiatedLinkingHip1Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmHipInitiatedLinkingHip1Request.validateJsonElement(jsonElement);
                        return (AbdmHipInitiatedLinkingHip1Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
