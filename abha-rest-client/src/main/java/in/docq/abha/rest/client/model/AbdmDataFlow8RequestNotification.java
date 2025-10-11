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

public class AbdmDataFlow8RequestNotification {
    public static final String SERIALIZED_NAME_CONSENT_ID = "consentId";
    @SerializedName("consentId")
    @Nonnull
    private String consentId;
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private String transactionId;
    public static final String SERIALIZED_NAME_DONE_AT = "doneAt";
    @SerializedName("doneAt")
    @Nonnull
    private String doneAt;
    public static final String SERIALIZED_NAME_NOTIFIER = "notifier";
    @SerializedName("notifier")
    @Nonnull
    private AbdmDataFlow8RequestNotificationNotifier notifier;
    public static final String SERIALIZED_NAME_STATUS_NOTIFICATION = "statusNotification";
    @SerializedName("statusNotification")
    @Nonnull
    private AbdmDataFlow8RequestNotificationStatusNotification statusNotification;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow8RequestNotification() {
    }

    public AbdmDataFlow8RequestNotification consentId(@Nonnull String consentId) {
        this.consentId = consentId;
        return this;
    }

    @Nonnull
    public String getConsentId() {
        return this.consentId;
    }

    public void setConsentId(@Nonnull String consentId) {
        this.consentId = consentId;
    }

    public AbdmDataFlow8RequestNotification transactionId(@Nonnull String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Nonnull
    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(@Nonnull String transactionId) {
        this.transactionId = transactionId;
    }

    public AbdmDataFlow8RequestNotification doneAt(@Nonnull String doneAt) {
        this.doneAt = doneAt;
        return this;
    }

    @Nonnull
    public String getDoneAt() {
        return this.doneAt;
    }

    public void setDoneAt(@Nonnull String doneAt) {
        this.doneAt = doneAt;
    }

    public AbdmDataFlow8RequestNotification notifier(@Nonnull AbdmDataFlow8RequestNotificationNotifier notifier) {
        this.notifier = notifier;
        return this;
    }

    @Nonnull
    public AbdmDataFlow8RequestNotificationNotifier getNotifier() {
        return this.notifier;
    }

    public void setNotifier(@Nonnull AbdmDataFlow8RequestNotificationNotifier notifier) {
        this.notifier = notifier;
    }

    public AbdmDataFlow8RequestNotification statusNotification(@Nonnull AbdmDataFlow8RequestNotificationStatusNotification statusNotification) {
        this.statusNotification = statusNotification;
        return this;
    }

    @Nonnull
    public AbdmDataFlow8RequestNotificationStatusNotification getStatusNotification() {
        return this.statusNotification;
    }

    public void setStatusNotification(@Nonnull AbdmDataFlow8RequestNotificationStatusNotification statusNotification) {
        this.statusNotification = statusNotification;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow8RequestNotification abdmDataFlow8RequestNotification = (AbdmDataFlow8RequestNotification)o;
            return Objects.equals(this.consentId, abdmDataFlow8RequestNotification.consentId) && Objects.equals(this.transactionId, abdmDataFlow8RequestNotification.transactionId) && Objects.equals(this.doneAt, abdmDataFlow8RequestNotification.doneAt) && Objects.equals(this.notifier, abdmDataFlow8RequestNotification.notifier) && Objects.equals(this.statusNotification, abdmDataFlow8RequestNotification.statusNotification);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consentId, this.transactionId, this.doneAt, this.notifier, this.statusNotification});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow8RequestNotification {\n");
        sb.append("    consentId: ").append(this.toIndentedString(this.consentId)).append("\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    doneAt: ").append(this.toIndentedString(this.doneAt)).append("\n");
        sb.append("    notifier: ").append(this.toIndentedString(this.notifier)).append("\n");
        sb.append("    statusNotification: ").append(this.toIndentedString(this.statusNotification)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow8RequestNotification is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("consentId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `consentId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("consentId").toString()));
                            }

                            if (!jsonObj.get("transactionId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `transactionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("transactionId").toString()));
                            }

                            if (!jsonObj.get("doneAt").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `doneAt` to be a primitive type in the JSON string but got `%s`", jsonObj.get("doneAt").toString()));
                            }

                            AbdmDataFlow8RequestNotificationNotifier.validateJsonElement(jsonObj.get("notifier"));
                            AbdmDataFlow8RequestNotificationStatusNotification.validateJsonElement(jsonObj.get("statusNotification"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow8RequestNotification` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow8RequestNotification fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow8RequestNotification)JSON.getGson().fromJson(jsonString, AbdmDataFlow8RequestNotification.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("consentId");
        openapiFields.add("transactionId");
        openapiFields.add("doneAt");
        openapiFields.add("notifier");
        openapiFields.add("statusNotification");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("consentId");
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("doneAt");
        openapiRequiredFields.add("notifier");
        openapiRequiredFields.add("statusNotification");
    }
}

