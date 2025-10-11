package in.docq.abha.rest.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

public class AbdmUserInitiatedLinking2RequestPatientInner {
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
    private List<AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner> careContexts = new ArrayList();
    public static final String SERIALIZED_NAME_HI_TYPE = "hiType";
    @SerializedName("hiType")
    @Nonnull
    private AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum hiType;
    public static final String SERIALIZED_NAME_COUNT = "count";
    @SerializedName("count")
    @Nonnull
    private Integer count;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking2RequestPatientInner() {
    }

    public AbdmUserInitiatedLinking2RequestPatientInner referenceNumber(@Nonnull String referenceNumber) {
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

    public AbdmUserInitiatedLinking2RequestPatientInner display(@Nonnull String display) {
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

    public AbdmUserInitiatedLinking2RequestPatientInner careContexts(@Nonnull List<AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner> careContexts) {
        this.careContexts = careContexts;
        return this;
    }

    public AbdmUserInitiatedLinking2RequestPatientInner addCareContextsItem(AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner careContextsItem) {
        if (this.careContexts == null) {
            this.careContexts = new ArrayList();
        }

        this.careContexts.add(careContextsItem);
        return this;
    }

    @Nonnull
    public List<AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner> getCareContexts() {
        return this.careContexts;
    }

    public void setCareContexts(@Nonnull List<AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner> careContexts) {
        this.careContexts = careContexts;
    }

    public AbdmUserInitiatedLinking2RequestPatientInner hiType(@Nonnull AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum hiType) {
        this.hiType = hiType;
        return this;
    }

    @Nonnull
    public AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum getHiType() {
        return this.hiType;
    }

    public void setHiType(@Nonnull AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum hiType) {
        this.hiType = hiType;
    }

    public AbdmUserInitiatedLinking2RequestPatientInner count(@Nonnull Integer count) {
        this.count = count;
        return this;
    }

    @Nonnull
    public Integer getCount() {
        return this.count;
    }

    public void setCount(@Nonnull Integer count) {
        this.count = count;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking2RequestPatientInner abdmUserInitiatedLinking2RequestPatientInner = (AbdmUserInitiatedLinking2RequestPatientInner)o;
            return Objects.equals(this.referenceNumber, abdmUserInitiatedLinking2RequestPatientInner.referenceNumber) && Objects.equals(this.display, abdmUserInitiatedLinking2RequestPatientInner.display) && Objects.equals(this.careContexts, abdmUserInitiatedLinking2RequestPatientInner.careContexts) && Objects.equals(this.hiType, abdmUserInitiatedLinking2RequestPatientInner.hiType) && Objects.equals(this.count, abdmUserInitiatedLinking2RequestPatientInner.count);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.referenceNumber, this.display, this.careContexts, this.hiType, this.count});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking2RequestPatientInner {\n");
        sb.append("    referenceNumber: ").append(this.toIndentedString(this.referenceNumber)).append("\n");
        sb.append("    display: ").append(this.toIndentedString(this.display)).append("\n");
        sb.append("    careContexts: ").append(this.toIndentedString(this.careContexts)).append("\n");
        sb.append("    hiType: ").append(this.toIndentedString(this.hiType)).append("\n");
        sb.append("    count: ").append(this.toIndentedString(this.count)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking2RequestPatientInner is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking2RequestPatientInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
                    AbdmUserInitiatedLinking2RequestPatientInnerCareContextsInner.validateJsonElement(jsonArraycareContexts.get(i));
                }

                if (!jsonObj.get("hiType").isJsonPrimitive()) {
                    throw new IllegalArgumentException(String.format("Expected the field `hiType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hiType").toString()));
                } else {
                    AbdmHipInitiatedLinkingHip1RequestPatientInner.HiTypesEnum.validateJsonElement(jsonObj.get("hiType"));
                }
            }
        }
    }

    public static AbdmUserInitiatedLinking2RequestPatientInner fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking2RequestPatientInner)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking2RequestPatientInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("referenceNumber");
        openapiFields.add("display");
        openapiFields.add("careContexts");
        openapiFields.add("hiType");
        openapiFields.add("count");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("referenceNumber");
        openapiRequiredFields.add("display");
        openapiRequiredFields.add("careContexts");
        openapiRequiredFields.add("hiType");
        openapiRequiredFields.add("count");
    }
}
