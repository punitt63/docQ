

package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.*;
import javax.annotation.Nonnull;

public class AbdmConsentManagement6Request {
    public static final String SERIALIZED_NAME_PAGE_NUMBER = "pageNumber";
    @SerializedName("pageNumber")
    @Nonnull
    private Integer pageNumber;
    public static final String SERIALIZED_NAME_PAGE_COUNT = "pageCount";
    @SerializedName("pageCount")
    @Nonnull
    private Integer pageCount;
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private String transactionId;
    public static final String SERIALIZED_NAME_ENTRIES = "entries";
    @SerializedName("entries")
    @Nonnull
    private List<AbdmConsentManagement6RequestEntriesInner> entries = new ArrayList();
    public static final String SERIALIZED_NAME_KEY_MATERIAL = "keyMaterial";
    @SerializedName("keyMaterial")
    @Nonnull
    private AbdmConsentManagement6RequestKeyMaterial keyMaterial;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement6Request() {
    }

    public AbdmConsentManagement6Request pageNumber(@Nonnull Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    @Nonnull
    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(@Nonnull Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public AbdmConsentManagement6Request pageCount(@Nonnull Integer pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    @Nonnull
    public Integer getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(@Nonnull Integer pageCount) {
        this.pageCount = pageCount;
    }

    public AbdmConsentManagement6Request transactionId(@Nonnull String transactionId) {
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

    public AbdmConsentManagement6Request entries(@Nonnull List<AbdmConsentManagement6RequestEntriesInner> entries) {
        this.entries = entries;
        return this;
    }

    public AbdmConsentManagement6Request addEntriesItem(AbdmConsentManagement6RequestEntriesInner entriesItem) {
        if (this.entries == null) {
            this.entries = new ArrayList();
        }

        this.entries.add(entriesItem);
        return this;
    }

    @Nonnull
    public List<AbdmConsentManagement6RequestEntriesInner> getEntries() {
        return this.entries;
    }

    public void setEntries(@Nonnull List<AbdmConsentManagement6RequestEntriesInner> entries) {
        this.entries = entries;
    }

    public AbdmConsentManagement6Request keyMaterial(@Nonnull AbdmConsentManagement6RequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement6RequestKeyMaterial getKeyMaterial() {
        return this.keyMaterial;
    }

    public void setKeyMaterial(@Nonnull AbdmConsentManagement6RequestKeyMaterial keyMaterial) {
        this.keyMaterial = keyMaterial;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement6Request abdmConsentManagement6Request = (AbdmConsentManagement6Request)o;
            return Objects.equals(this.pageNumber, abdmConsentManagement6Request.pageNumber) && Objects.equals(this.pageCount, abdmConsentManagement6Request.pageCount) && Objects.equals(this.transactionId, abdmConsentManagement6Request.transactionId) && Objects.equals(this.entries, abdmConsentManagement6Request.entries) && Objects.equals(this.keyMaterial, abdmConsentManagement6Request.keyMaterial);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.pageNumber, this.pageCount, this.transactionId, this.entries, this.keyMaterial});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement6Request {\n");
        sb.append("    pageNumber: ").append(this.toIndentedString(this.pageNumber)).append("\n");
        sb.append("    pageCount: ").append(this.toIndentedString(this.pageCount)).append("\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    entries: ").append(this.toIndentedString(this.entries)).append("\n");
        sb.append("    keyMaterial: ").append(this.toIndentedString(this.keyMaterial)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement6Request is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement6Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            if (!jsonObj.get("transactionId").isJsonPrimitive()) {
                throw new IllegalArgumentException(String.format("Expected the field `transactionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("transactionId").toString()));
            } else if (!jsonObj.get("entries").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `entries` to be an array in the JSON string but got `%s`", jsonObj.get("entries").toString()));
            } else {
                JsonArray jsonArrayentries = jsonObj.getAsJsonArray("entries");

                for(int i = 0; i < jsonArrayentries.size(); ++i) {
                    AbdmConsentManagement6RequestEntriesInner.validateJsonElement(jsonArrayentries.get(i));
                }

                AbdmConsentManagement6RequestKeyMaterial.validateJsonElement(jsonObj.get("keyMaterial"));
            }
        }
    }

    public static AbdmConsentManagement6Request fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement6Request) JSON.getGson().fromJson(jsonString, AbdmConsentManagement6Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("pageNumber");
        openapiFields.add("pageCount");
        openapiFields.add("transactionId");
        openapiFields.add("entries");
        openapiFields.add("keyMaterial");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("pageNumber");
        openapiRequiredFields.add("pageCount");
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("entries");
        openapiRequiredFields.add("keyMaterial");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement6Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement6Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement6Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement6Request>() {
                    public void write(JsonWriter out, AbdmConsentManagement6Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement6Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement6Request.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement6Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
