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

public class AbdmDataFlow7RequestHiRequest {
    public static final String SERIALIZED_NAME_CONSENT = "consent";
    @SerializedName("consent")
    @Nonnull
    private AbdmDataFlow7RequestHiRequestConsent consent;
    public static final String SERIALIZED_NAME_DATE_RANGE = "dateRange";
    @SerializedName("dateRange")
    @Nonnull
    private AbdmDataFlow7RequestHiRequestDateRange dateRange;
    public static final String SERIALIZED_NAME_DATA_PUSH_URL = "dataPushUrl";
    @SerializedName("dataPushUrl")
    @Nonnull
    private String dataPushUrl;
    public static final String SERIALIZED_NAME_KEY_MATERIAL = "keyMaterial";
    @SerializedName("keyMaterial")
    @Nonnull
    private AbdmDataFlow7RequestHiRequestKeyMaterial keyMaterial;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow7RequestHiRequest() {
    }

    public AbdmDataFlow7RequestHiRequest consent(@Nonnull AbdmDataFlow7RequestHiRequestConsent consent) {
        this.consent = consent;
        return this;
    }

    @Nonnull
    public AbdmDataFlow7RequestHiRequestConsent getConsent() {
        return this.consent;
    }

    public void setConsent(@Nonnull AbdmDataFlow7RequestHiRequestConsent consent) {
        this.consent = consent;
    }

    public AbdmDataFlow7RequestHiRequest dateRange(@Nonnull AbdmDataFlow7RequestHiRequestDateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    @Nonnull
    public AbdmDataFlow7RequestHiRequestDateRange getDateRange() {
        return this.dateRange;
    }

    public void setDateRange(@Nonnull AbdmDataFlow7RequestHiRequestDateRange dateRange) {
        this.dateRange = dateRange;
    }

    public AbdmDataFlow7RequestHiRequest dataPushUrl(@Nonnull String dataPushUrl) {
        this.dataPushUrl = dataPushUrl;
        return this;
    }

    @Nonnull
    public String getDataPushUrl() {
        return this.dataPushUrl;
    }

    public void setDataPushUrl(@Nonnull String dataPushUrl) {
        this.dataPushUrl = dataPushUrl;
    }

    public AbdmDataFlow7RequestHiRequest keyMaterial(@Nonnull AbdmDataFlow7RequestHiRequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
        return this;
    }

    @Nonnull
    public AbdmDataFlow7RequestHiRequestKeyMaterial getKeyMaterial() {
        return this.keyMaterial;
    }

    public void setKeyMaterial(@Nonnull AbdmDataFlow7RequestHiRequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow7RequestHiRequest abdmDataFlow7RequestHiRequest = (AbdmDataFlow7RequestHiRequest)o;
            return Objects.equals(this.consent, abdmDataFlow7RequestHiRequest.consent) && Objects.equals(this.dateRange, abdmDataFlow7RequestHiRequest.dateRange) && Objects.equals(this.dataPushUrl, abdmDataFlow7RequestHiRequest.dataPushUrl) && Objects.equals(this.keyMaterial, abdmDataFlow7RequestHiRequest.keyMaterial);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consent, this.dateRange, this.dataPushUrl, this.keyMaterial});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow7RequestHiRequest {\n");
        sb.append("    consent: ").append(this.toIndentedString(this.consent)).append("\n");
        sb.append("    dateRange: ").append(this.toIndentedString(this.dateRange)).append("\n");
        sb.append("    dataPushUrl: ").append(this.toIndentedString(this.dataPushUrl)).append("\n");
        sb.append("    keyMaterial: ").append(this.toIndentedString(this.keyMaterial)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow7RequestHiRequest is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmDataFlow7RequestHiRequestConsent.validateJsonElement(jsonObj.get("consent"));
                            AbdmDataFlow7RequestHiRequestDateRange.validateJsonElement(jsonObj.get("dateRange"));
                            if (!jsonObj.get("dataPushUrl").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `dataPushUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dataPushUrl").toString()));
                            }

                            AbdmDataFlow7RequestHiRequestKeyMaterial.validateJsonElement(jsonObj.get("keyMaterial"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow7RequestHiRequest` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow7RequestHiRequest fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow7RequestHiRequest)JSON.getGson().fromJson(jsonString, AbdmDataFlow7RequestHiRequest.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("consent");
        openapiFields.add("dateRange");
        openapiFields.add("dataPushUrl");
        openapiFields.add("keyMaterial");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("consent");
        openapiRequiredFields.add("dateRange");
        openapiRequiredFields.add("dataPushUrl");
        openapiRequiredFields.add("keyMaterial");
    }
}

