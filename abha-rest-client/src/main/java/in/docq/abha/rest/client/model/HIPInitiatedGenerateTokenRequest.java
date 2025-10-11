package in.docq.abha.rest.client.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

public class HIPInitiatedGenerateTokenRequest {
    public static final String SERIALIZED_NAME_ABHA_NUMBER = "abhaNumber";
    @SerializedName("abhaNumber")
    @Nonnull
    private String abhaNumber;
    public static final String SERIALIZED_NAME_ABHA_ADDRESS = "abhaAddress";
    @SerializedName("abhaAddress")
    @Nonnull
    private String abhaAddress;
    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName("name")
    @Nonnull
    private String name;
    public static final String SERIALIZED_NAME_GENDER = "gender";
    @SerializedName("gender")
    @Nonnull
    private GenderEnum gender;
    public static final String SERIALIZED_NAME_YEAR_OF_BIRTH = "yearOfBirth";
    @SerializedName("yearOfBirth")
    @Nonnull
    private Integer yearOfBirth;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public HIPInitiatedGenerateTokenRequest() {
    }

    public HIPInitiatedGenerateTokenRequest abhaNumber(@Nonnull String abhaNumber) {
        this.abhaNumber = abhaNumber;
        return this;
    }

    @Nonnull
    public String getAbhaNumber() {
        return this.abhaNumber;
    }

    public void setAbhaNumber(@Nonnull String abhaNumber) {
        this.abhaNumber = abhaNumber;
    }

    public HIPInitiatedGenerateTokenRequest abhaAddress(@Nonnull String abhaAddress) {
        this.abhaAddress = abhaAddress;
        return this;
    }

    @Nonnull
    public String getAbhaAddress() {
        return this.abhaAddress;
    }

    public void setAbhaAddress(@Nonnull String abhaAddress) {
        this.abhaAddress = abhaAddress;
    }

    public HIPInitiatedGenerateTokenRequest name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public HIPInitiatedGenerateTokenRequest gender(@Nonnull GenderEnum gender) {
        this.gender = gender;
        return this;
    }

    @Nonnull
    public GenderEnum getGender() {
        return this.gender;
    }

    public void setGender(@Nonnull GenderEnum gender) {
        this.gender = gender;
    }

    public HIPInitiatedGenerateTokenRequest yearOfBirth(@Nonnull Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
        return this;
    }

    @Nonnull
    public Integer getYearOfBirth() {
        return this.yearOfBirth;
    }

    public void setYearOfBirth(@Nonnull Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            HIPInitiatedGenerateTokenRequest HIPInitiatedGenerateTokenRequest = (HIPInitiatedGenerateTokenRequest)o;
            return Objects.equals(this.abhaNumber, HIPInitiatedGenerateTokenRequest.abhaNumber) && Objects.equals(this.abhaAddress, HIPInitiatedGenerateTokenRequest.abhaAddress) && Objects.equals(this.name, HIPInitiatedGenerateTokenRequest.name) && Objects.equals(this.gender, HIPInitiatedGenerateTokenRequest.gender) && Objects.equals(this.yearOfBirth, HIPInitiatedGenerateTokenRequest.yearOfBirth);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.abhaNumber, this.abhaAddress, this.name, this.gender, this.yearOfBirth});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GenerateTokenRequest {\n");
        sb.append("    abhaNumber: ").append(this.toIndentedString(this.abhaNumber)).append("\n");
        sb.append("    abhaAddress: ").append(this.toIndentedString(this.abhaAddress)).append("\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    gender: ").append(this.toIndentedString(this.gender)).append("\n");
        sb.append("    yearOfBirth: ").append(this.toIndentedString(this.yearOfBirth)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in GenerateTokenRequest is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("abhaNumber").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `abhaNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("abhaNumber").toString()));
                            }

                            if (!jsonObj.get("abhaAddress").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `abhaAddress` to be a primitive type in the JSON string but got `%s`", jsonObj.get("abhaAddress").toString()));
                            }

                            if (!jsonObj.get("name").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
                            }

                            if (!jsonObj.get("gender").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `gender` to be a primitive type in the JSON string but got `%s`", jsonObj.get("gender").toString()));
                            }

                            HIPInitiatedGenerateTokenRequest.GenderEnum.validateJsonElement(jsonObj.get("gender"));
                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `GenerateTokenRequest` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static HIPInitiatedGenerateTokenRequest fromJson(String jsonString) throws IOException {
        return (HIPInitiatedGenerateTokenRequest) JSON.getGson().fromJson(jsonString, HIPInitiatedGenerateTokenRequest.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("abhaNumber");
        openapiFields.add("abhaAddress");
        openapiFields.add("name");
        openapiFields.add("gender");
        openapiFields.add("yearOfBirth");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("abhaNumber");
        openapiRequiredFields.add("abhaAddress");
        openapiRequiredFields.add("name");
        openapiRequiredFields.add("gender");
        openapiRequiredFields.add("yearOfBirth");
    }

    @JsonAdapter(GenderEnum.Adapter.class)
    public static enum GenderEnum {
        M("M"),
        F("F"),
        O("O");

        private String value;

        private GenderEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static GenderEnum fromValue(String value) {
            GenderEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                GenderEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<GenderEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, GenderEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public GenderEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return HIPInitiatedGenerateTokenRequest.GenderEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!HIPInitiatedGenerateTokenRequest.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<HIPInitiatedGenerateTokenRequest> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(HIPInitiatedGenerateTokenRequest.class));
                return (TypeAdapter<T>) (new TypeAdapter<HIPInitiatedGenerateTokenRequest>() {

                    @Override
                    public void write(JsonWriter out, HIPInitiatedGenerateTokenRequest value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    @Override
                    public HIPInitiatedGenerateTokenRequest read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        HIPInitiatedGenerateTokenRequest.validateJsonElement(jsonElement);
                        return thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
