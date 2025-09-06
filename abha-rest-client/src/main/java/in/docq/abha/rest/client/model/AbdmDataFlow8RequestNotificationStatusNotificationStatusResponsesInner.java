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

public class AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner {
    public static final String SERIALIZED_NAME_CARE_CONTEXT_REFERENCE = "careContextReference";
    @SerializedName("careContextReference")
    @Nonnull
    private String careContextReference;
    public static final String SERIALIZED_NAME_HI_STATUS = "hiStatus";
    @SerializedName("hiStatus")
    @Nonnull
    private String hiStatus;
    public static final String SERIALIZED_NAME_DESCRIPTION = "description";
    @SerializedName("description")
    @Nonnull
    private String description;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner() {
    }

    public AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner careContextReference(@Nonnull String careContextReference) {
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

    public AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner hiStatus(@Nonnull String hiStatus) {
        this.hiStatus = hiStatus;
        return this;
    }

    @Nonnull
    public String getHiStatus() {
        return this.hiStatus;
    }

    public void setHiStatus(@Nonnull String hiStatus) {
        this.hiStatus = hiStatus;
    }

    public AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner description(@Nonnull String description) {
        this.description = description;
        return this;
    }

    @Nonnull
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner abdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner = (AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner)o;
            return Objects.equals(this.careContextReference, abdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner.careContextReference) && Objects.equals(this.hiStatus, abdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner.hiStatus) && Objects.equals(this.description, abdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner.description);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.careContextReference, this.hiStatus, this.description});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner {\n");
        sb.append("    careContextReference: ").append(this.toIndentedString(this.careContextReference)).append("\n");
        sb.append("    hiStatus: ").append(this.toIndentedString(this.hiStatus)).append("\n");
        sb.append("    description: ").append(this.toIndentedString(this.description)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("careContextReference").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `careContextReference` to be a primitive type in the JSON string but got `%s`", jsonObj.get("careContextReference").toString()));
                            }

                            if (!jsonObj.get("hiStatus").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `hiStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hiStatus").toString()));
                            }

                            if (!jsonObj.get("description").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("description").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner)JSON.getGson().fromJson(jsonString, AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("careContextReference");
        openapiFields.add("hiStatus");
        openapiFields.add("description");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("careContextReference");
        openapiRequiredFields.add("hiStatus");
        openapiRequiredFields.add("description");
    }
}

