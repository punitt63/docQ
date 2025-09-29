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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public class AbdmConsentManagement6RequestKeyMaterial {
    public static final String SERIALIZED_NAME_CRYPTO_ALG = "cryptoAlg";
    @SerializedName("cryptoAlg")
    @Nonnull
    private CryptoAlgEnum cryptoAlg;
    public static final String SERIALIZED_NAME_CURVE = "curve";
    @SerializedName("curve")
    @Nonnull
    private CurveEnum curve;
    public static final String SERIALIZED_NAME_DH_PUBLIC_KEY = "dhPublicKey";
    @SerializedName("dhPublicKey")
    @Nonnull
    private AbdmConsentManagement6RequestKeyMaterialDhPublicKey dhPublicKey;
    public static final String SERIALIZED_NAME_NONCE = "nonce";
    @SerializedName("nonce")
    @Nonnull
    private String nonce;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement6RequestKeyMaterial() {
    }

    public AbdmConsentManagement6RequestKeyMaterial cryptoAlg(@Nonnull CryptoAlgEnum cryptoAlg) {
        this.cryptoAlg = cryptoAlg;
        return this;
    }

    @Nonnull
    public CryptoAlgEnum getCryptoAlg() {
        return this.cryptoAlg;
    }

    public void setCryptoAlg(@Nonnull CryptoAlgEnum cryptoAlg) {
        this.cryptoAlg = cryptoAlg;
    }

    public AbdmConsentManagement6RequestKeyMaterial curve(@Nonnull CurveEnum curve) {
        this.curve = curve;
        return this;
    }

    @Nonnull
    public CurveEnum getCurve() {
        return this.curve;
    }

    public void setCurve(@Nonnull CurveEnum curve) {
        this.curve = curve;
    }

    public AbdmConsentManagement6RequestKeyMaterial dhPublicKey(@Nonnull AbdmConsentManagement6RequestKeyMaterialDhPublicKey dhPublicKey) {
        this.dhPublicKey = dhPublicKey;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement6RequestKeyMaterialDhPublicKey getDhPublicKey() {
        return this.dhPublicKey;
    }

    public void setDhPublicKey(@Nonnull AbdmConsentManagement6RequestKeyMaterialDhPublicKey dhPublicKey) {
        this.dhPublicKey = dhPublicKey;
    }

    public AbdmConsentManagement6RequestKeyMaterial nonce(@Nonnull String nonce) {
        this.nonce = nonce;
        return this;
    }

    @Nonnull
    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(@Nonnull String nonce) {
        this.nonce = nonce;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement6RequestKeyMaterial abdmConsentManagement6RequestKeyMaterial = (AbdmConsentManagement6RequestKeyMaterial)o;
            return Objects.equals(this.cryptoAlg, abdmConsentManagement6RequestKeyMaterial.cryptoAlg) && Objects.equals(this.curve, abdmConsentManagement6RequestKeyMaterial.curve) && Objects.equals(this.dhPublicKey, abdmConsentManagement6RequestKeyMaterial.dhPublicKey) && Objects.equals(this.nonce, abdmConsentManagement6RequestKeyMaterial.nonce);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.cryptoAlg, this.curve, this.dhPublicKey, this.nonce});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement6RequestKeyMaterial {\n");
        sb.append("    cryptoAlg: ").append(this.toIndentedString(this.cryptoAlg)).append("\n");
        sb.append("    curve: ").append(this.toIndentedString(this.curve)).append("\n");
        sb.append("    dhPublicKey: ").append(this.toIndentedString(this.dhPublicKey)).append("\n");
        sb.append("    nonce: ").append(this.toIndentedString(this.nonce)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement6RequestKeyMaterial is not found in the empty JSON string", openapiRequiredFields.toString()));
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
                            if (!jsonObj.get("cryptoAlg").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `cryptoAlg` to be a primitive type in the JSON string but got `%s`", jsonObj.get("cryptoAlg").toString()));
                            }

                            AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.validateJsonElement(jsonObj.get("cryptoAlg"));
                            if (!jsonObj.get("curve").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `curve` to be a primitive type in the JSON string but got `%s`", jsonObj.get("curve").toString()));
                            }

                            AbdmConsentManagement6RequestKeyMaterial.CurveEnum.validateJsonElement(jsonObj.get("curve"));
                            AbdmConsentManagement6RequestKeyMaterialDhPublicKey.validateJsonElement(jsonObj.get("dhPublicKey"));
                            if (!jsonObj.get("nonce").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `nonce` to be a primitive type in the JSON string but got `%s`", jsonObj.get("nonce").toString()));
                            }

                            return;
                        }

                        requiredField = (String)var2.next();
                    } while(jsonElement.getAsJsonObject().get(requiredField) != null);

                    throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement6RequestKeyMaterial` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement6RequestKeyMaterial fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement6RequestKeyMaterial)JSON.getGson().fromJson(jsonString, AbdmConsentManagement6RequestKeyMaterial.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("cryptoAlg");
        openapiFields.add("curve");
        openapiFields.add("dhPublicKey");
        openapiFields.add("nonce");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("cryptoAlg");
        openapiRequiredFields.add("curve");
        openapiRequiredFields.add("dhPublicKey");
        openapiRequiredFields.add("nonce");
    }

    @JsonAdapter(CurveEnum.Adapter.class)
    public static enum CryptoAlgEnum {
        ECDH("ECDH");

        private String value;

        private CryptoAlgEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static CryptoAlgEnum fromValue(String value) {
            CryptoAlgEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CryptoAlgEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<CryptoAlgEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, CryptoAlgEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public CryptoAlgEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement6RequestKeyMaterial.CryptoAlgEnum.fromValue(value);
            }
        }
    }

    @JsonAdapter(CurveEnum.Adapter.class)
    public static enum CurveEnum {
        CURVE25519("Curve25519");

        private String value;

        private CurveEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }

        public static CurveEnum fromValue(String value) {
            CurveEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CurveEnum b = var1[var3];
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

        public static class Adapter extends TypeAdapter<CurveEnum> {
            public Adapter() {
            }

            public void write(JsonWriter jsonWriter, CurveEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            public CurveEnum read(JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return AbdmConsentManagement6RequestKeyMaterial.CurveEnum.fromValue(value);
            }
        }
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement6RequestKeyMaterial.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement6RequestKeyMaterial> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement6RequestKeyMaterial.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement6RequestKeyMaterial>() {
                    public void write(JsonWriter out, AbdmConsentManagement6RequestKeyMaterial value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement6RequestKeyMaterial read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement6RequestKeyMaterial.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement6RequestKeyMaterial)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}
