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

public class SendSmsNotificationRequestNotificationHip {
    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName("name")
    @Nullable
    private String name;
    public static final String SERIALIZED_NAME_ID = "id";
    @SerializedName("id")
    @Nullable
    private String id;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public SendSmsNotificationRequestNotificationHip() {
    }

    public SendSmsNotificationRequestNotificationHip name(@Nullable String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public SendSmsNotificationRequestNotificationHip id(@Nullable String id) {
        this.id = id;
        return this;
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SendSmsNotificationRequestNotificationHip sendSmsNotificationRequestNotificationHip = (SendSmsNotificationRequestNotificationHip)o;
            return Objects.equals(this.name, sendSmsNotificationRequestNotificationHip.name) && Objects.equals(this.id, sendSmsNotificationRequestNotificationHip.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.id});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SendSmsNotificationRequestNotificationHip {\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in SendSmsNotificationRequestNotificationHip is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull() && !jsonObj.get("name").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
                    }

                    if (jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull() && !jsonObj.get("id").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
                    }

                    return;
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `SendSmsNotificationRequestNotificationHip` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static SendSmsNotificationRequestNotificationHip fromJson(String jsonString) throws IOException {
        return (SendSmsNotificationRequestNotificationHip)JSON.getGson().fromJson(jsonString, SendSmsNotificationRequestNotificationHip.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("name");
        openapiFields.add("id");
        openapiRequiredFields = new HashSet();
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!SendSmsNotificationRequestNotificationHip.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<SendSmsNotificationRequestNotificationHip> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(SendSmsNotificationRequestNotificationHip.class));
                return (TypeAdapter<T>) (new TypeAdapter<SendSmsNotificationRequestNotificationHip>() {
                    public void write(JsonWriter out, SendSmsNotificationRequestNotificationHip value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public SendSmsNotificationRequestNotificationHip read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        SendSmsNotificationRequestNotificationHip.validateJsonElement(jsonElement);
                        return (SendSmsNotificationRequestNotificationHip)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
