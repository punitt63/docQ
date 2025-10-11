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

public class AbdmDataFlow7Request {
    public static final String SERIALIZED_NAME_HI_REQUEST = "hiRequest";
    @SerializedName("hiRequest")
    @Nonnull
    private AbdmDataFlow7RequestHiRequest hiRequest;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmDataFlow7Request() {
    }

    public AbdmDataFlow7Request hiRequest(@Nonnull AbdmDataFlow7RequestHiRequest hiRequest) {
        this.hiRequest = hiRequest;
        return this;
    }

    @Nonnull
    public AbdmDataFlow7RequestHiRequest getHiRequest() {
        return this.hiRequest;
    }

    public void setHiRequest(@Nonnull AbdmDataFlow7RequestHiRequest hiRequest) {
        this.hiRequest = hiRequest;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmDataFlow7Request abdmDataFlow7Request = (AbdmDataFlow7Request)o;
            return Objects.equals(this.hiRequest, abdmDataFlow7Request.hiRequest);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.hiRequest});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmDataFlow7Request {\n");
        sb.append("    hiRequest: ").append(this.toIndentedString(this.hiRequest)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmDataFlow7Request is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            AbdmDataFlow7RequestHiRequest.validateJsonElement(jsonObj.get("hiRequest"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmDataFlow7Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmDataFlow7Request fromJson(String jsonString) throws IOException {
        return (AbdmDataFlow7Request)JSON.getGson().fromJson(jsonString, AbdmDataFlow7Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("hiRequest");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("hiRequest");
    }
}

