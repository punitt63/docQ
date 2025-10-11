package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

public class AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner {
    public static final String SERIALIZED_NAME_REFERENCE_NUMBER = "referenceNumber";
    @SerializedName("referenceNumber")
    @Nonnull
    private String referenceNumber;
    public static final String SERIALIZED_NAME_DISPLAY = "display";
    @SerializedName("display")
    @Nonnull
    private String display;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner() {
    }

    public AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner referenceNumber(@Nonnull String referenceNumber) {
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

    public AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner display(@Nonnull String display) {
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner abdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner = (AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner)o;
            return Objects.equals(this.referenceNumber, abdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.referenceNumber) && Objects.equals(this.display, abdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.display);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.referenceNumber, this.display});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner {\n");
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
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            if (!jsonObj.get("display").isJsonPrimitive()) {
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

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner fromJson(String jsonString) throws IOException {
        return (AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner)JSON.getGson().fromJson(jsonString, AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.class);
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

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.class));
                return (TypeAdapter<T>) new TypeAdapter<AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner>() {

                    @Override
                    public void write(JsonWriter out, AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    @Override
                    public AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner read(JsonReader in) throws IOException {
                        JsonElement jsonElement = elementAdapter.read(in);
                        AbdmHipInitiatedLinkingHip1RequestPatientInnerCareContextsInner.validateJsonElement(jsonElement);
                        return thisAdapter.fromJsonTree(jsonElement);
                    }
                }.nullSafe();
            }
        }
    }
}
