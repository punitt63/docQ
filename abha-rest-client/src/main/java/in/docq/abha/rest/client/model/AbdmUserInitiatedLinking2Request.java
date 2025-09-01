package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class AbdmUserInitiatedLinking2Request {
    public static final String SERIALIZED_NAME_TRANSACTION_ID = "transactionId";
    @SerializedName("transactionId")
    @Nonnull
    private String transactionId;
    public static final String SERIALIZED_NAME_PATIENT = "patient";
    @SerializedName("patient")
    @Nonnull
    private List<AbdmUserInitiatedLinking2RequestPatientInner> patient = new ArrayList();
    public static final String SERIALIZED_NAME_MATCHED_BY = "matchedBy";
    @SerializedName("matchedBy")
    @Nonnull
    private List<MatchedByEnum> matchedBy = new ArrayList();
    public static final String SERIALIZED_NAME_ERROR = "error";
    @SerializedName("error")
    @Nullable
    private AbdmUserInitiatedLinking2RequestError error;
    public static final String SERIALIZED_NAME_RESPONSE = "response";
    @SerializedName("response")
    @Nonnull
    private AbdmUserInitiatedLinking2RequestResponse response;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmUserInitiatedLinking2Request() {
    }

    public AbdmUserInitiatedLinking2Request transactionId(@Nonnull String transactionId) {
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

    public AbdmUserInitiatedLinking2Request patient(@Nonnull List<AbdmUserInitiatedLinking2RequestPatientInner> patient) {
        this.patient = patient;
        return this;
    }

    public AbdmUserInitiatedLinking2Request addPatientItem(AbdmUserInitiatedLinking2RequestPatientInner patientItem) {
        if (this.patient == null) {
            this.patient = new ArrayList();
        }

        this.patient.add(patientItem);
        return this;
    }

    @Nonnull
    public List<AbdmUserInitiatedLinking2RequestPatientInner> getPatient() {
        return this.patient;
    }

    public void setPatient(@Nonnull List<AbdmUserInitiatedLinking2RequestPatientInner> patient) {
        this.patient = patient;
    }

    public AbdmUserInitiatedLinking2Request matchedBy(@Nonnull List<MatchedByEnum> matchedBy) {
        this.matchedBy = matchedBy;
        return this;
    }

    public AbdmUserInitiatedLinking2Request addMatchedByItem(MatchedByEnum matchedByItem) {
        if (this.matchedBy == null) {
            this.matchedBy = new ArrayList();
        }

        this.matchedBy.add(matchedByItem);
        return this;
    }

    @Nonnull
    public List<MatchedByEnum> getMatchedBy() {
        return this.matchedBy;
    }

    public void setMatchedBy(@Nonnull List<MatchedByEnum> matchedBy) {
        this.matchedBy = matchedBy;
    }

    public AbdmUserInitiatedLinking2Request error(@Nullable AbdmUserInitiatedLinking2RequestError error) {
        this.error = error;
        return this;
    }

    @Nullable
    public AbdmUserInitiatedLinking2RequestError getError() {
        return this.error;
    }

    public void setError(@Nullable AbdmUserInitiatedLinking2RequestError error) {
        this.error = error;
    }

    public AbdmUserInitiatedLinking2Request response(@Nonnull AbdmUserInitiatedLinking2RequestResponse response) {
        this.response = response;
        return this;
    }

    @Nonnull
    public AbdmUserInitiatedLinking2RequestResponse getResponse() {
        return this.response;
    }

    public void setResponse(@Nonnull AbdmUserInitiatedLinking2RequestResponse response) {
        this.response = response;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmUserInitiatedLinking2Request abdmUserInitiatedLinking2Request = (AbdmUserInitiatedLinking2Request)o;
            return Objects.equals(this.transactionId, abdmUserInitiatedLinking2Request.transactionId) && Objects.equals(this.patient, abdmUserInitiatedLinking2Request.patient) && Objects.equals(this.matchedBy, abdmUserInitiatedLinking2Request.matchedBy) && Objects.equals(this.error, abdmUserInitiatedLinking2Request.error) && Objects.equals(this.response, abdmUserInitiatedLinking2Request.response);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.transactionId, this.patient, this.matchedBy, this.error, this.response});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmUserInitiatedLinking2Request {\n");
        sb.append("    transactionId: ").append(this.toIndentedString(this.transactionId)).append("\n");
        sb.append("    patient: ").append(this.toIndentedString(this.patient)).append("\n");
        sb.append("    matchedBy: ").append(this.toIndentedString(this.matchedBy)).append("\n");
        sb.append("    error: ").append(this.toIndentedString(this.error)).append("\n");
        sb.append("    response: ").append(this.toIndentedString(this.response)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmUserInitiatedLinking2Request is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmUserInitiatedLinking2Request` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            } else if (!jsonObj.get("patient").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `patient` to be an array in the JSON string but got `%s`", jsonObj.get("patient").toString()));
            } else {
                JsonArray jsonArraypatient = jsonObj.getAsJsonArray("patient");

                for(int i = 0; i < jsonArraypatient.size(); ++i) {
                    AbdmUserInitiatedLinking2RequestPatientInner.validateJsonElement(jsonArraypatient.get(i));
                }

                if (jsonObj.get("matchedBy") == null) {
                    throw new IllegalArgumentException("Expected the field `linkedContent` to be an array in the JSON string but got `null`");
                } else if (!jsonObj.get("matchedBy").isJsonArray()) {
                    throw new IllegalArgumentException(String.format("Expected the field `matchedBy` to be an array in the JSON string but got `%s`", jsonObj.get("matchedBy").toString()));
                } else {
                    if (jsonObj.get("error") != null && !jsonObj.get("error").isJsonNull()) {
                        AbdmUserInitiatedLinking2RequestError.validateJsonElement(jsonObj.get("error"));
                    }

                    AbdmUserInitiatedLinking2RequestResponse.validateJsonElement(jsonObj.get("response"));
                }
            }
        }
    }

    public static AbdmUserInitiatedLinking2Request fromJson(String jsonString) throws IOException {
        return (AbdmUserInitiatedLinking2Request)JSON.getGson().fromJson(jsonString, AbdmUserInitiatedLinking2Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("transactionId");
        openapiFields.add("patient");
        openapiFields.add("matchedBy");
        openapiFields.add("error");
        openapiFields.add("response");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("transactionId");
        openapiRequiredFields.add("patient");
        openapiRequiredFields.add("matchedBy");
        openapiRequiredFields.add("response");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {

        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmUserInitiatedLinking2Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmUserInitiatedLinking2Request> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmUserInitiatedLinking2Request.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmUserInitiatedLinking2Request>() {
                    public void write(JsonWriter out, AbdmUserInitiatedLinking2Request value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmUserInitiatedLinking2Request read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmUserInitiatedLinking2Request.validateJsonElement(jsonElement);
                        return (AbdmUserInitiatedLinking2Request)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }

    @JsonAdapter(MatchedByEnum.Adapter.class)
    public static enum MatchedByEnum {
        MR("MR"),
        MOBILE("MOBILE"),
        ABHA_NUMBER("ABHA_NUMBER"),
        ABHA_ADDRESS("ABHA_ADDRESS");

        private String value;

        private MatchedByEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static MatchedByEnum fromValue(String value) {
            MatchedByEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MatchedByEnum b = var1[var3];
                if (b.value.equals(value)) {
                    return b;
                }
            }

            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public static void validateJsonElement(JsonElement jsonElement) throws IOException {
            String value = jsonElement.getAsString();
            fromValue(value);
        }

        public static class Adapter extends TypeAdapter<MatchedByEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, MatchedByEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public MatchedByEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmUserInitiatedLinking2Request.MatchedByEnum.fromValue(value);
            }
        }
    }
}
