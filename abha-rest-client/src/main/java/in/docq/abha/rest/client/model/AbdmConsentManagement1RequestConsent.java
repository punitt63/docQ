package in.docq.abha.rest.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class AbdmConsentManagement1RequestConsent {
    public static final String SERIALIZED_NAME_PURPOSE = "purpose";
    @SerializedName("purpose")
    @Nonnull
    private AbdmConsentManagement1RequestConsentPurpose purpose;
    public static final String SERIALIZED_NAME_PATIENT = "patient";
    @SerializedName("patient")
    @Nonnull
    private AbdmConsentManagement1RequestConsentPatient patient;
    public static final String SERIALIZED_NAME_HIP = "hip";
    @SerializedName("hip")
    @Nullable
    private AbdmConsentManagement1RequestConsentHip hip;
    public static final String SERIALIZED_NAME_HIU = "hiu";
    @SerializedName("hiu")
    @Nonnull
    private AbdmConsentManagement1RequestConsentHiu hiu;
    public static final String SERIALIZED_NAME_CARE_CONTEXTS = "careContexts";
    @SerializedName("careContexts")
    @Nullable
    private List<AbdmConsentManagement1RequestConsentCareContextsInner> careContexts = new ArrayList();
    public static final String SERIALIZED_NAME_REQUESTER = "requester";
    @SerializedName("requester")
    @Nullable
    private AbdmConsentManagement1RequestConsentRequester requester;
    public static final String SERIALIZED_NAME_HI_TYPES = "hiTypes";
    @SerializedName("hiTypes")
    @Nonnull
    private List<HiTypesEnum> hiTypes = new ArrayList();
    public static final String SERIALIZED_NAME_PERMISSION = "permission";
    @SerializedName("permission")
    @Nonnull
    private AbdmConsentManagement1RequestConsentPermission permission;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsent() {
    }

    public AbdmConsentManagement1RequestConsent purpose(@Nonnull AbdmConsentManagement1RequestConsentPurpose purpose) {
        this.purpose = purpose;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentPurpose getPurpose() {
        return this.purpose;
    }

    public void setPurpose(@Nonnull AbdmConsentManagement1RequestConsentPurpose purpose) {
        this.purpose = purpose;
    }

    public AbdmConsentManagement1RequestConsent patient(@Nonnull AbdmConsentManagement1RequestConsentPatient patient) {
        this.patient = patient;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentPatient getPatient() {
        return this.patient;
    }

    public void setPatient(@Nonnull AbdmConsentManagement1RequestConsentPatient patient) {
        this.patient = patient;
    }

    public AbdmConsentManagement1RequestConsent hip(@Nullable AbdmConsentManagement1RequestConsentHip hip) {
        this.hip = hip;
        return this;
    }

    @Nullable
    public AbdmConsentManagement1RequestConsentHip getHip() {
        return this.hip;
    }

    public void setHip(@Nullable AbdmConsentManagement1RequestConsentHip hip) {
        this.hip = hip;
    }

    public AbdmConsentManagement1RequestConsent hiu(@Nonnull AbdmConsentManagement1RequestConsentHiu hiu) {
        this.hiu = hiu;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentHiu getHiu() {
        return this.hiu;
    }

    public void setHiu(@Nonnull AbdmConsentManagement1RequestConsentHiu hiu) {
        this.hiu = hiu;
    }

    public AbdmConsentManagement1RequestConsent careContexts(@Nullable List<AbdmConsentManagement1RequestConsentCareContextsInner> careContexts) {
        this.careContexts = careContexts;
        return this;
    }

    public AbdmConsentManagement1RequestConsent addCareContextsItem(AbdmConsentManagement1RequestConsentCareContextsInner careContextsItem) {
        if (this.careContexts == null) {
            this.careContexts = new ArrayList();
        }

        this.careContexts.add(careContextsItem);
        return this;
    }

    @Nullable
    public List<AbdmConsentManagement1RequestConsentCareContextsInner> getCareContexts() {
        return this.careContexts;
    }

    public void setCareContexts(@Nullable List<AbdmConsentManagement1RequestConsentCareContextsInner> careContexts) {
        this.careContexts = careContexts;
    }

    public AbdmConsentManagement1RequestConsent requester(@Nullable AbdmConsentManagement1RequestConsentRequester requester) {
        this.requester = requester;
        return this;
    }

    @Nullable
    public AbdmConsentManagement1RequestConsentRequester getRequester() {
        return this.requester;
    }

    public void setRequester(@Nullable AbdmConsentManagement1RequestConsentRequester requester) {
        this.requester = requester;
    }

    public AbdmConsentManagement1RequestConsent hiTypes(@Nonnull List<HiTypesEnum> hiTypes) {
        this.hiTypes = hiTypes;
        return this;
    }

    public AbdmConsentManagement1RequestConsent addHiTypesItem(HiTypesEnum hiTypesItem) {
        if (this.hiTypes == null) {
            this.hiTypes = new ArrayList();
        }

        this.hiTypes.add(hiTypesItem);
        return this;
    }

    @Nonnull
    public List<HiTypesEnum> getHiTypes() {
        return this.hiTypes;
    }

    public void setHiTypes(@Nonnull List<HiTypesEnum> hiTypes) {
        this.hiTypes = hiTypes;
    }

    public AbdmConsentManagement1RequestConsent permission(@Nonnull AbdmConsentManagement1RequestConsentPermission permission) {
        this.permission = permission;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement1RequestConsentPermission getPermission() {
        return this.permission;
    }

    public void setPermission(@Nonnull AbdmConsentManagement1RequestConsentPermission permission) {
        this.permission = permission;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsent abdmConsentManagement1RequestConsent = (AbdmConsentManagement1RequestConsent)o;
            return Objects.equals(this.purpose, abdmConsentManagement1RequestConsent.purpose) && Objects.equals(this.patient, abdmConsentManagement1RequestConsent.patient) && Objects.equals(this.hip, abdmConsentManagement1RequestConsent.hip) && Objects.equals(this.hiu, abdmConsentManagement1RequestConsent.hiu) && Objects.equals(this.careContexts, abdmConsentManagement1RequestConsent.careContexts) && Objects.equals(this.requester, abdmConsentManagement1RequestConsent.requester) && Objects.equals(this.hiTypes, abdmConsentManagement1RequestConsent.hiTypes) && Objects.equals(this.permission, abdmConsentManagement1RequestConsent.permission);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.purpose, this.patient, this.hip, this.hiu, this.careContexts, this.requester, this.hiTypes, this.permission});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsent {\n");
        sb.append("    purpose: ").append(this.toIndentedString(this.purpose)).append("\n");
        sb.append("    patient: ").append(this.toIndentedString(this.patient)).append("\n");
        sb.append("    hip: ").append(this.toIndentedString(this.hip)).append("\n");
        sb.append("    hiu: ").append(this.toIndentedString(this.hiu)).append("\n");
        sb.append("    careContexts: ").append(this.toIndentedString(this.careContexts)).append("\n");
        sb.append("    requester: ").append(this.toIndentedString(this.requester)).append("\n");
        sb.append("    hiTypes: ").append(this.toIndentedString(this.hiTypes)).append("\n");
        sb.append("    permission: ").append(this.toIndentedString(this.permission)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsent is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsent` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            AbdmConsentManagement1RequestConsentPurpose.validateJsonElement(jsonObj.get("purpose"));
            AbdmConsentManagement1RequestConsentPatient.validateJsonElement(jsonObj.get("patient"));
            if (jsonObj.get("hip") != null && !jsonObj.get("hip").isJsonNull()) {
                AbdmConsentManagement1RequestConsentHip.validateJsonElement(jsonObj.get("hip"));
            }

            AbdmConsentManagement1RequestConsentHiu.validateJsonElement(jsonObj.get("hiu"));
            if (jsonObj.get("careContexts") != null && !jsonObj.get("careContexts").isJsonNull()) {
                JsonArray jsonArraycareContexts = jsonObj.getAsJsonArray("careContexts");
                if (jsonArraycareContexts != null) {
                    if (!jsonObj.get("careContexts").isJsonArray()) {
                        throw new IllegalArgumentException(String.format("Expected the field `careContexts` to be an array in the JSON string but got `%s`", jsonObj.get("careContexts").toString()));
                    }

                    for(int i = 0; i < jsonArraycareContexts.size(); ++i) {
                        AbdmConsentManagement1RequestConsentCareContextsInner.validateJsonElement(jsonArraycareContexts.get(i));
                    }
                }
            }

            AbdmConsentManagement1RequestConsentRequester.validateJsonElement(jsonObj.get("requester"));
            if (jsonObj.get("hiTypes") == null) {
                throw new IllegalArgumentException("Expected the field `linkedContent` to be an array in the JSON string but got `null`");
            } else if (!jsonObj.get("hiTypes").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `hiTypes` to be an array in the JSON string but got `%s`", jsonObj.get("hiTypes").toString()));
            } else {
                AbdmConsentManagement1RequestConsentPermission.validateJsonElement(jsonObj.get("permission"));
            }
        }
    }

    public static AbdmConsentManagement1RequestConsent fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsent)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsent.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("purpose");
        openapiFields.add("patient");
        openapiFields.add("hip");
        openapiFields.add("hiu");
        openapiFields.add("careContexts");
        openapiFields.add("requester");
        openapiFields.add("hiTypes");
        openapiFields.add("permission");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("purpose");
        openapiRequiredFields.add("patient");
        openapiRequiredFields.add("hiu");
        openapiRequiredFields.add("requester");
        openapiRequiredFields.add("hiTypes");
        openapiRequiredFields.add("permission");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement1RequestConsent.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement1RequestConsent> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement1RequestConsent.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement1RequestConsent>() {
                    public void write(JsonWriter out, AbdmConsentManagement1RequestConsent value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement1RequestConsent read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement1RequestConsent.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement1RequestConsent)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }

    @JsonAdapter(HiTypesEnum.Adapter.class)
    public static enum HiTypesEnum {
        PRESCRIPTION("Prescription"),
        DIAGNOSTIC_REPORT("DiagnosticReport"),
        OP_CONSULTATION("OPConsultation"),
        DISCHARGE_SUMMARY("DischargeSummary"),
        IMMUNIZATION_RECORD("ImmunizationRecord"),
        HEALTH_DOCUMENT_RECORD("HealthDocumentRecord"),
        WELLNESS_RECORD("WellnessRecord"),
        INVOICE("Invoice");

        private String value;

        private HiTypesEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static HiTypesEnum fromValue(String value) {
            HiTypesEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                HiTypesEnum b = var1[var3];
                if (b.value.equals(value)) {
                    return b;
                }
            }

            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public static void validateJsonElement(JsonElement jsonElement) throws IOException {
            String value = jsonElement.getAsString();
            fromValue(value);
        }

        public static class Adapter extends TypeAdapter<HiTypesEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, HiTypesEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public HiTypesEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement1RequestConsent.HiTypesEnum.fromValue(value);
            }
        }
    }
}


