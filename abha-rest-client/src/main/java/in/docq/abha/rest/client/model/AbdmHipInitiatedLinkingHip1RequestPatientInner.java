package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class AbdmHipInitiatedLinkingHip1RequestPatientInner {
    public static final String SERIALIZED_NAME_REFERENCE_NUMBER = "referenceNumber";
    @SerializedName("referenceNumber")
    @Nonnull
    private String referenceNumber;
    public static final String SERIALIZED_NAME_DISPLAY = "display";
    @SerializedName("display")
    @Nonnull
    private String display;
    public static final String SERIALIZED_NAME_CARE_CONTEXTS = "careContexts";
    @SerializedName("careContexts")
    @Nonnull
    private List<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner> careContexts = new ArrayList();
    public static final String SERIALIZED_NAME_HI_TYPES = "hiTypes";
    @SerializedName("hiTypes")
    @Nonnull
    private HiTypesEnum hiTypes;
    public static final String SERIALIZED_NAME_COUNT = "count";
    @SerializedName("count")
    @Nonnull
    private BigDecimal count;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmHipInitiatedLinkingHip1RequestPatientInner() {
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInner referenceNumber(@Nonnull String referenceNumber) {
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

    public AbdmHipInitiatedLinkingHip1RequestPatientInner display(@Nonnull String display) {
        this.display = display;
        return this;
    }

    @Nonnull
    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(@Nonnull String display) {
        this.display = display;
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInner careContexts(@Nonnull List<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner> careContexts) {
        this.careContexts = careContexts;
        return this;
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInner addCareContextsItem(AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner careContextsItem) {
        if (this.careContexts == null) {
            this.careContexts = new ArrayList();
        }

        this.careContexts.add(careContextsItem);
        return this;
    }

    @Nonnull
    public List<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner> getCareContexts() {
        return this.careContexts;
    }

    public void setCareContexts(@Nonnull List<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner> careContexts) {
        this.careContexts = careContexts;
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInner hiTypes(@Nonnull HiTypesEnum hiTypes) {
        this.hiTypes = hiTypes;
        return this;
    }

    @Nonnull
    public HiTypesEnum getHiTypes() {
        return this.hiTypes;
    }

    public void setHiTypes(@Nonnull HiTypesEnum hiTypes) {
        this.hiTypes = hiTypes;
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInner count(@Nonnull BigDecimal count) {
        this.count = count;
        return this;
    }

    @Nonnull
    public BigDecimal getCount() {
        return this.count;
    }

    public void setCount(@Nonnull BigDecimal count) {
        this.count = count;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmHipInitiatedLinkingHip1RequestPatientInner abdmHipInitiatedLinkingHip1RequestPatientInner = (AbdmHipInitiatedLinkingHip1RequestPatientInner)o;
            return Objects.equals(this.referenceNumber, abdmHipInitiatedLinkingHip1RequestPatientInner.referenceNumber) && Objects.equals(this.display, abdmHipInitiatedLinkingHip1RequestPatientInner.display) && Objects.equals(this.careContexts, abdmHipInitiatedLinkingHip1RequestPatientInner.careContexts) && Objects.equals(this.hiTypes, abdmHipInitiatedLinkingHip1RequestPatientInner.hiTypes) && Objects.equals(this.count, abdmHipInitiatedLinkingHip1RequestPatientInner.count);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.referenceNumber, this.display, this.careContexts, this.hiTypes, this.count});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmHipInitiatedLinkingHip1RequestPatientInner {\n");
        sb.append("    referenceNumber: ").append(this.toIndentedString(this.referenceNumber)).append("\n");
        sb.append("    display: ").append(this.toIndentedString(this.display)).append("\n");
        sb.append("    careContexts: ").append(this.toIndentedString(this.careContexts)).append("\n");
        sb.append("    hiTypes: ").append(this.toIndentedString(this.hiTypes)).append("\n");
        sb.append("    count: ").append(this.toIndentedString(this.count)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmHipInitiatedLinkingHip1RequestPatientInner is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmHipInitiatedLinkingHip1RequestPatientInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            if (!jsonObj.get("referenceNumber").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `referenceNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("referenceNumber").toString()));
            } else if (!jsonObj.get("display").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `display` to be a primitive type in the JSON string but got `%s`", jsonObj.get("display").toString()));
            } else if (!jsonObj.get("careContexts").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `careContexts` to be an array in the JSON string but got `%s`", jsonObj.get("careContexts").toString()));
            } else {
                JsonArray jsonArraycareContexts = jsonObj.getAsJsonArray("careContexts");

                for(int i = 0; i < jsonArraycareContexts.size(); ++i) {
                    AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.validateJsonElement(jsonArraycareContexts.get(i));
                }

                if (!jsonObj.get("hiTypes").isJsonPrimitive()) {
                    throw new IllegalArgumentException(String.format("Expected the field `hiTypes` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hiTypes").toString()));
                } else {
                    AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.validateJsonElement(jsonObj.get("hiTypes"));
                }
            }
        }
    }

    public static AbdmHipInitiatedLinkingHip1RequestPatientInner fromJson(String jsonString) throws IOException {
        return (AbdmHipInitiatedLinkingHip1RequestPatientInner) JSON.getGson().fromJson(jsonString, AbdmHipInitiatedLinkingHip1RequestPatientInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("referenceNumber");
        openapiFields.add("display");
        openapiFields.add("careContexts");
        openapiFields.add("hiTypes");
        openapiFields.add("count");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("referenceNumber");
        openapiRequiredFields.add("display");
        openapiRequiredFields.add("careContexts");
        openapiRequiredFields.add("hiTypes");
        openapiRequiredFields.add("count");
    }

    @JsonAdapter(HiTypesEnum.HiTypesEnumAdapter.class)
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

        public static class HiTypesEnumAdapter extends TypeAdapter<HiTypesEnum> {
            public HiTypesEnumAdapter() {
            }

            public void write(JsonWriter jsonWriter, HiTypesEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public HiTypesEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmHipInitiatedLinkingHip1RequestPatientInner.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmHipInitiatedLinkingHip1RequestPatientInner> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmHipInitiatedLinkingHip1RequestPatientInner.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmHipInitiatedLinkingHip1RequestPatientInner>() {
                    public void write(JsonWriter out, AbdmHipInitiatedLinkingHip1RequestPatientInner value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmHipInitiatedLinkingHip1RequestPatientInner read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmHipInitiatedLinkingHip1RequestPatientInner.validateJsonElement(jsonElement);
                        return (AbdmHipInitiatedLinkingHip1RequestPatientInner)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
