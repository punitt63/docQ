//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

public class UserEntityResponseDTO {
    public static final String SERIALIZED_NAME_HPR_ID_NUMBER = "hprIdNumber";
    @SerializedName("hprIdNumber")
    @Nullable
    private String hprIdNumber;
    public static final String SERIALIZED_NAME_NAME = "name";
    @SerializedName("name")
    @Nullable
    private String name;
    public static final String SERIALIZED_NAME_AUTH_METHODS = "authMethods";
    @SerializedName("authMethods")
    @Nullable
    private List<AuthMethodsEnum> authMethods = new ArrayList();
    public static final String SERIALIZED_NAME_HPR_ID = "hprId";
    @SerializedName("hprId")
    @Nullable
    private String hprId;
    public static final String SERIALIZED_NAME_CATEGORY_ID = "categoryId";
    @SerializedName("categoryId")
    @Nullable
    private String categoryId;
    public static final String SERIALIZED_NAME_SUB_CATEGORY_ID = "subCategoryId";
    @SerializedName("subCategoryId")
    @Nullable
    private String subCategoryId;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public UserEntityResponseDTO() {
    }

    public UserEntityResponseDTO hprIdNumber(@Nullable String hprIdNumber) {
        this.hprIdNumber = hprIdNumber;
        return this;
    }

    @Nullable
    public String getHprIdNumber() {
        return this.hprIdNumber;
    }

    public void setHprIdNumber(@Nullable String hprIdNumber) {
        this.hprIdNumber = hprIdNumber;
    }

    public UserEntityResponseDTO name(@Nullable String name) {
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

    public UserEntityResponseDTO authMethods(@Nullable List<AuthMethodsEnum> authMethods) {
        this.authMethods = authMethods;
        return this;
    }

    public UserEntityResponseDTO addAuthMethodsItem(AuthMethodsEnum authMethodsItem) {
        if (this.authMethods == null) {
            this.authMethods = new ArrayList();
        }

        this.authMethods.add(authMethodsItem);
        return this;
    }

    @Nullable
    public List<AuthMethodsEnum> getAuthMethods() {
        return this.authMethods;
    }

    public void setAuthMethods(@Nullable List<AuthMethodsEnum> authMethods) {
        this.authMethods = authMethods;
    }

    public UserEntityResponseDTO hprId(@Nullable String hprId) {
        this.hprId = hprId;
        return this;
    }

    @Nullable
    public String getHprId() {
        return this.hprId;
    }

    public void setHprId(@Nullable String hprId) {
        this.hprId = hprId;
    }

    public UserEntityResponseDTO categoryId(@Nullable String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    @Nullable
    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(@Nullable String categoryId) {
        this.categoryId = categoryId;
    }

    public UserEntityResponseDTO subCategoryId(@Nullable String subCategoryId) {
        this.subCategoryId = subCategoryId;
        return this;
    }

    @Nullable
    public String getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(@Nullable String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UserEntityResponseDTO UserEntityResponseDTO = (UserEntityResponseDTO)o;
            return Objects.equals(this.hprIdNumber, UserEntityResponseDTO.hprIdNumber) && Objects.equals(this.name, UserEntityResponseDTO.name) && Objects.equals(this.authMethods, UserEntityResponseDTO.authMethods) && Objects.equals(this.hprId, UserEntityResponseDTO.hprId) && Objects.equals(this.categoryId, UserEntityResponseDTO.categoryId) && Objects.equals(this.subCategoryId, UserEntityResponseDTO.subCategoryId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.hprIdNumber, this.name, this.authMethods, this.hprId, this.categoryId, this.subCategoryId});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserEntityResponseDTO {\n");
        sb.append("    hprIdNumber: ").append(this.toIndentedString(this.hprIdNumber)).append("\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    authMethods: ").append(this.toIndentedString(this.authMethods)).append("\n");
        sb.append("    hprId: ").append(this.toIndentedString(this.hprId)).append("\n");
        sb.append("    categoryId: ").append(this.toIndentedString(this.categoryId)).append("\n");
        sb.append("    subCategoryId: ").append(this.toIndentedString(this.subCategoryId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in UserEntityResponseDTO is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.get("hprIdNumber") != null && !jsonObj.get("hprIdNumber").isJsonNull() && !jsonObj.get("hprIdNumber").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `hprIdNumber` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hprIdNumber").toString()));
                    }

                    if (jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull() && !jsonObj.get("name").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
                    }

                    if (jsonObj.get("authMethods") != null && !jsonObj.get("authMethods").isJsonNull() && !jsonObj.get("authMethods").isJsonArray()) {
                        throw new IllegalArgumentException(String.format("Expected the field `authMethods` to be an array in the JSON string but got `%s`", jsonObj.get("authMethods").toString()));
                    }

                    if (jsonObj.get("hprId") != null && !jsonObj.get("hprId").isJsonNull() && !jsonObj.get("hprId").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `hprId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("hprId").toString()));
                    }

                    if (jsonObj.get("categoryId") != null && !jsonObj.get("categoryId").isJsonNull() && !jsonObj.get("categoryId").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `categoryId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("categoryId").toString()));
                    }

                    if (jsonObj.get("subCategoryId") != null && !jsonObj.get("subCategoryId").isJsonNull() && !jsonObj.get("subCategoryId").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `subCategoryId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subCategoryId").toString()));
                    }

                    return;
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UserEntityResponseDTO` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static UserEntityResponseDTO fromJson(String jsonString) throws IOException {
        return (UserEntityResponseDTO) JSON.getGson().fromJson(jsonString, UserEntityResponseDTO.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("hprIdNumber");
        openapiFields.add("name");
        openapiFields.add("authMethods");
        openapiFields.add("hprId");
        openapiFields.add("categoryId");
        openapiFields.add("subCategoryId");
        openapiRequiredFields = new HashSet();
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!UserEntityResponseDTO.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<UserEntityResponseDTO> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(UserEntityResponseDTO.class));
                return ( TypeAdapter<T>) (new TypeAdapter<UserEntityResponseDTO>() {
                    public void write(JsonWriter out, UserEntityResponseDTO value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public UserEntityResponseDTO read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        UserEntityResponseDTO.validateJsonElement(jsonElement);
                        return (UserEntityResponseDTO)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }

    @JsonAdapter(AuthMethodsEnum.Adapter.class)
    public static enum AuthMethodsEnum {
        AADHAAR_OTP("AADHAAR_OTP"),
        MOBILE_OTP("MOBILE_OTP"),
        PASSWORD("PASSWORD"),
        DEMOGRAPHICS("DEMOGRAPHICS"),
        AADHAAR_BIO("AADHAAR_BIO"),
        EMAIL_OTP("EMAIL_OTP"),
        MOBILE_OTP_RESET_PASSWROD("MOBILE_OTP_RESET_PASSWROD"),
        FACE_AUTH("FACE_AUTH");

        private String value;

        private AuthMethodsEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static AuthMethodsEnum fromValue(String value) {
            AuthMethodsEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                AuthMethodsEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<AuthMethodsEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, AuthMethodsEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public AuthMethodsEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return UserEntityResponseDTO.AuthMethodsEnum.fromValue(value);
            }
        }
    }
}
