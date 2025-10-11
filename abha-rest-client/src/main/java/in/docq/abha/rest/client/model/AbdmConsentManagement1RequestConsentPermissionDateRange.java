package in.docq.abha.rest.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmConsentManagement1RequestConsentPermissionDateRange {
    public static final String SERIALIZED_NAME_FROM = "from";
    @SerializedName("from")
    @Nonnull
    private OffsetDateTime from;
    public static final String SERIALIZED_NAME_TO = "to";
    @SerializedName("to")
    @Nonnull
    private OffsetDateTime to;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement1RequestConsentPermissionDateRange() {
    }

    public AbdmConsentManagement1RequestConsentPermissionDateRange from(@Nonnull OffsetDateTime from) {
        this.from = from;
        return this;
    }

    @Nonnull
    public OffsetDateTime getFrom() {
        return this.from;
    }

    public void setFrom(@Nonnull OffsetDateTime from) {
        this.from = from;
    }

    public AbdmConsentManagement1RequestConsentPermissionDateRange to(@Nonnull OffsetDateTime to) {
        this.to = to;
        return this;
    }

    @Nonnull
    public OffsetDateTime getTo() {
        return this.to;
    }

    public void setTo(@Nonnull OffsetDateTime to) {
        this.to = to;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement1RequestConsentPermissionDateRange abdmConsentManagement1RequestConsentPermissionDateRange = (AbdmConsentManagement1RequestConsentPermissionDateRange)o;
            return Objects.equals(this.from, abdmConsentManagement1RequestConsentPermissionDateRange.from) && Objects.equals(this.to, abdmConsentManagement1RequestConsentPermissionDateRange.to);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.from, this.to});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement1RequestConsentPermissionDateRange {\n");
        sb.append("    from: ").append(this.toIndentedString(this.from)).append("\n");
        sb.append("    to: ").append(this.toIndentedString(this.to)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement1RequestConsentPermissionDateRange is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement1RequestConsentPermissionDateRange` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement1RequestConsentPermissionDateRange fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement1RequestConsentPermissionDateRange)JSON.getGson().fromJson(jsonString, AbdmConsentManagement1RequestConsentPermissionDateRange.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("from");
        openapiFields.add("to");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("from");
        openapiRequiredFields.add("to");
    }
}

