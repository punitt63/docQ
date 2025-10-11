package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class SendSmsNotificationRequestNotification {
    public static final String SERIALIZED_NAME_PHONE_NO = "phoneNo";
    @SerializedName("phoneNo")
    @Nullable
    private String phoneNo;
    public static final String SERIALIZED_NAME_HIP = "hip";
    @SerializedName("hip")
    @Nullable
    private SendSmsNotificationRequestNotificationHip hip;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public SendSmsNotificationRequestNotification() {
    }

    public SendSmsNotificationRequestNotification phoneNo(@Nullable String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }

    @Nullable
    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(@Nullable String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public SendSmsNotificationRequestNotification hip(@Nullable SendSmsNotificationRequestNotificationHip hip) {
        this.hip = hip;
        return this;
    }

    @Nullable
    public SendSmsNotificationRequestNotificationHip getHip() {
        return this.hip;
    }

    public void setHip(@Nullable SendSmsNotificationRequestNotificationHip hip) {
        this.hip = hip;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SendSmsNotificationRequestNotification sendSmsNotificationRequestNotification = (SendSmsNotificationRequestNotification)o;
            return Objects.equals(this.phoneNo, sendSmsNotificationRequestNotification.phoneNo) && Objects.equals(this.hip, sendSmsNotificationRequestNotification.hip);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.phoneNo, this.hip});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SendSmsNotificationRequestNotification {\n");
        sb.append("    phoneNo: ").append(this.toIndentedString(this.phoneNo)).append("\n");
        sb.append("    hip: ").append(this.toIndentedString(this.hip)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in SendSmsNotificationRequestNotification is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.get("phoneNo") != null && !jsonObj.get("phoneNo").isJsonNull() && !jsonObj.get("phoneNo").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `phoneNo` to be a primitive type in the JSON string but got `%s`", jsonObj.get("phoneNo").toString()));
                    }

                    if (jsonObj.get("hip") != null && !jsonObj.get("hip").isJsonNull()) {
                        SendSmsNotificationRequestNotificationHip.validateJsonElement(jsonObj.get("hip"));
                    }

                    return;
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `SendSmsNotificationRequestNotification` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static SendSmsNotificationRequestNotification fromJson(String jsonString) throws IOException {
        return (SendSmsNotificationRequestNotification) JSON.getGson().fromJson(jsonString, SendSmsNotificationRequestNotification.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("phoneNo");
        openapiFields.add("hip");
        openapiRequiredFields = new HashSet();
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!SendSmsNotificationRequestNotification.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<SendSmsNotificationRequestNotification> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(SendSmsNotificationRequestNotification.class));
                return (TypeAdapter<T>) (new TypeAdapter<SendSmsNotificationRequestNotification>() {
                    public void write(JsonWriter out, SendSmsNotificationRequestNotification value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public SendSmsNotificationRequestNotification read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        SendSmsNotificationRequestNotification.validateJsonElement(jsonElement);
                        return (SendSmsNotificationRequestNotification)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
