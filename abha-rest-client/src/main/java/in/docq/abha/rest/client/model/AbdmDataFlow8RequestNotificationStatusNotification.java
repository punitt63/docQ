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

public class AbdmDataFlow8RequestNotificationStatusNotification {
    public static final String SERIALIZED_NAME_SESSION_STATUS = "sessionStatus";
    @SerializedName("sessionStatus")
    @Nonnull
    private String sessionStatus;
    public static final String SERIALIZED_NAME_HIP_ID = "hipId";
    @SerializedName("hipId")
    @Nonnull
    private String hipId;
    public static final String SERIALIZED_NAME_STATUS_RESPONSES = "statusResponses";
    @SerializedName("statusResponses")
    @Nonnull
    private List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> statusResponses = new ArrayList();
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow8RequestNotificationStatusNotification() {
    }

    public AbdmDataFlow8RequestNotificationStatusNotification sessionStatus(@Nonnull String sessionStatus) {
        this.sessionStatus = sessionStatus;
        return this;
    }

    @Nonnull
    public String getSessionStatus() {
        return this.sessionStatus;
    }

    public void setSessionStatus(@Nonnull String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public AbdmDataFlow8RequestNotificationStatusNotification hipId(@Nonnull String hipId) {
        this.hipId = hipId;
        return this;
    }

    @Nonnull
    public String getHipId() {
        return this.hipId;
    }

    public void setHipId(@Nonnull String hipId) {
        this.hipId = hipId;
    }

    public AbdmDataFlow8RequestNotificationStatusNotification statusResponses(@Nonnull List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> statusResponses) {
        this.statusResponses = statusResponses;
        return this;
    }

    public AbdmDataFlow8RequestNotificationStatusNotification addStatusResponsesItem(AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner statusResponsesItem) {
        if (this.statusResponses == null) {
            this.statusResponses = new ArrayList();
        }

        this.statusResponses.add(statusResponsesItem);
        return this;
    }

    @Nonnull
    public List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> getStatusResponses() {
        return this.statusResponses;
    }

    public void setStatusResponses(@Nonnull List<AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner> statusResponses) {
        this.statusResponses = statusResponses;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow8RequestNotificationStatusNotification abdmDataFlow8RequestNotificationStatusNotification = (AbdmDataFlow8RequestNotificationStatusNotification)o;
            return Objects.equals(this.sessionStatus, abdmDataFlow8RequestNotificationStatusNotification.sessionStatus) && Objects.equals(this.hipId, abdmDataFlow8RequestNotificationStatusNotification.hipId) && Objects.equals(this.statusResponses, abdmDataFlow8RequestNotificationStatusNotification.statusResponses);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.sessionStatus, this.hipId, this.statusResponses});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow8RequestNotificationStatusNotification {\n");
        sb.append("    sessionStatus: ").append(this.toIndentedString(this.sessionStatus)).append("\n");
        sb.append("    hipId: ").append(this.toIndentedString(this.hipId)).append("\n");
        sb.append("    statusResponses: ").append(this.toIndentedString(this.statusResponses)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow8RequestNotificationStatusNotification is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow8RequestNotificationStatusNotification` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            if (!jsonObj.get("sessionStatus").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `sessionStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sessionStatus").toString()));
            } else if (!jsonObj.get("hipId").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `hipId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hipId").toString()));
            } else if (!jsonObj.get("statusResponses").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `statusResponses` to be an array in the JSON string but got `%s`", jsonObj.get("statusResponses").toString()));
            } else {
                JsonArray jsonArraystatusResponses = jsonObj.getAsJsonArray("statusResponses");

                for(int i = 0; i < jsonArraystatusResponses.size(); ++i) {
                    AbdmDataFlow8RequestNotificationStatusNotificationStatusResponsesInner.validateJsonElement(jsonArraystatusResponses.get(i));
                }

            }
        }
    }

    public static AbdmDataFlow8RequestNotificationStatusNotification fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow8RequestNotificationStatusNotification)JSON.getGson().fromJson(jsonString, AbdmDataFlow8RequestNotificationStatusNotification.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("sessionStatus");
        openapiFields.add("hipId");
        openapiFields.add("statusResponses");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("sessionStatus");
        openapiRequiredFields.add("hipId");
        openapiRequiredFields.add("statusResponses");
    }
}

