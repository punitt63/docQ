package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

public class SendSmsNotificationRequest {
    public static final String SERIALIZED_NAME_REQUEST_ID = "requestId";
    @SerializedName("requestId")
    @Nonnull
    private String requestId;
    public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
    @SerializedName("timestamp")
    @Nonnull
    private String timestamp;
    public static final String SERIALIZED_NAME_NOTIFICATION = "notification";
    @SerializedName("notification")
    @Nonnull
    private SendSmsNotificationRequestNotification notification;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public SendSmsNotificationRequest() {
    }

    public SendSmsNotificationRequest requestId(@Nonnull String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Nonnull
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(@Nonnull String requestId) {
        this.requestId = requestId;
    }

    public SendSmsNotificationRequest timestamp(@Nonnull String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Nonnull
    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(@Nonnull String timestamp) {
        this.timestamp = timestamp;
    }

    public SendSmsNotificationRequest notification(@Nonnull SendSmsNotificationRequestNotification notification) {
        this.notification = notification;
        return this;
    }

    @Nonnull
    public SendSmsNotificationRequestNotification getNotification() {
        return this.notification;
    }

    public void setNotification(@Nonnull SendSmsNotificationRequestNotification notification) {
        this.notification = notification;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SendSmsNotificationRequest sendSmsNotificationRequest = (SendSmsNotificationRequest)o;
            return Objects.equals(this.requestId, sendSmsNotificationRequest.requestId) && Objects.equals(this.timestamp, sendSmsNotificationRequest.timestamp) && Objects.equals(this.notification, sendSmsNotificationRequest.notification);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.requestId, this.timestamp, this.notification});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SendSmsNotificationRequest {\n");
        sb.append("    requestId: ").append(this.toIndentedString(this.requestId)).append("\n");
        sb.append("    timestamp: ").append(this.toIndentedString(this.timestamp)).append("\n");
        sb.append("    notification: ").append(this.toIndentedString(this.notification)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in SendSmsNotificationRequest is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("requestId").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `requestId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("requestId").toString()));
                            }

                            SendSmsNotificationRequestNotification.validateJsonElement(jsonObj.get("notification"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `SendSmsNotificationRequest` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static SendSmsNotificationRequest fromJson(String jsonString) throws IOException {
        return (SendSmsNotificationRequest) JSON.getGson().fromJson(jsonString, SendSmsNotificationRequest.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("requestId");
        openapiFields.add("timestamp");
        openapiFields.add("notification");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("requestId");
        openapiRequiredFields.add("timestamp");
        openapiRequiredFields.add("notification");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!SendSmsNotificationRequest.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<SendSmsNotificationRequest> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(SendSmsNotificationRequest.class));
                return (TypeAdapter<T>) (new TypeAdapter<SendSmsNotificationRequest>() {
                    public void write(JsonWriter out, SendSmsNotificationRequest value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public SendSmsNotificationRequest read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        SendSmsNotificationRequest.validateJsonElement(jsonElement);
                        return (SendSmsNotificationRequest)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
