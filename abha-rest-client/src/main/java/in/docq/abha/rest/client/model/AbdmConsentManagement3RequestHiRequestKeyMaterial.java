package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbdmConsentManagement3RequestHiRequestKeyMaterial {
    public static final String SERIALIZED_NAME_CRYPTO_ALG = "cryptoAlg";
    @SerializedName("cryptoAlg")
    @Nonnull
    private String cryptoAlg;
    public static final String SERIALIZED_NAME_CURVE = "curve";
    @SerializedName("curve")
    @Nonnull
    private String curve;
    public static final String SERIALIZED_NAME_DH_PUBLIC_KEY = "dhPublicKey";
    @SerializedName("dhPublicKey")
    @Nonnull
    private AbdmConsentManagement3RequestHiRequestKeyMaterialDhPublicKey dhPublicKey;
    public static final String SERIALIZED_NAME_NONCE = "nonce";
    @SerializedName("nonce")
    @Nullable
    private UUID nonce;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmConsentManagement3RequestHiRequestKeyMaterial() {
    }

    public AbdmConsentManagement3RequestHiRequestKeyMaterial cryptoAlg(@Nonnull String cryptoAlg) {
        this.cryptoAlg = cryptoAlg;
        return this;
    }

    @Nonnull
    public String getCryptoAlg() {
        return this.cryptoAlg;
    }

    public void setCryptoAlg(@Nonnull String cryptoAlg) {
        this.cryptoAlg = cryptoAlg;
    }

    public AbdmConsentManagement3RequestHiRequestKeyMaterial curve(@Nonnull String curve) {
        this.curve = curve;
        return this;
    }

    @Nonnull
    public String getCurve() {
        return this.curve;
    }

    public void setCurve(@Nonnull String curve) {
        this.curve = curve;
    }

    public AbdmConsentManagement3RequestHiRequestKeyMaterial dhPublicKey(@Nonnull AbdmConsentManagement3RequestHiRequestKeyMaterialDhPublicKey dhPublicKey) {
        this.dhPublicKey = dhPublicKey;
        return this;
    }

    @Nonnull
    public AbdmConsentManagement3RequestHiRequestKeyMaterialDhPublicKey getDhPublicKey() {
        return this.dhPublicKey;
    }

    public void setDhPublicKey(@Nonnull AbdmConsentManagement3RequestHiRequestKeyMaterialDhPublicKey dhPublicKey) {
        this.dhPublicKey = dhPublicKey;
    }

    public AbdmConsentManagement3RequestHiRequestKeyMaterial nonce(@Nullable UUID nonce) {
        this.nonce = nonce;
        return this;
    }

    @Nullable
    public UUID getNonce() {
        return this.nonce;
    }

    public void setNonce(@Nullable UUID nonce) {
        this.nonce = nonce;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmConsentManagement3RequestHiRequestKeyMaterial abdmConsentManagement3RequestHiRequestKeyMaterial = (AbdmConsentManagement3RequestHiRequestKeyMaterial)o;
            return Objects.equals(this.cryptoAlg, abdmConsentManagement3RequestHiRequestKeyMaterial.cryptoAlg) && Objects.equals(this.curve, abdmConsentManagement3RequestHiRequestKeyMaterial.curve) && Objects.equals(this.dhPublicKey, abdmConsentManagement3RequestHiRequestKeyMaterial.dhPublicKey) && Objects.equals(this.nonce, abdmConsentManagement3RequestHiRequestKeyMaterial.nonce);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.cryptoAlg, this.curve, this.dhPublicKey, this.nonce});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmConsentManagement3RequestHiRequestKeyMaterial {\n");
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
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmConsentManagement3RequestHiRequestKeyMaterial is not found in the empty JSON string", openapiRequiredFields.toString()));
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

                            if (!jsonObj.get("curve").isJsonPrimitive()) {
                                throw new IllegalArgumentException(String.format("Expected the field `curve` to be a primitive type in the JSON string but got `%s`", jsonObj.get("curve").toString()));
                            }

                            AbdmConsentManagement3RequestHiRequestKeyMaterialDhPublicKey.validateJsonElement(jsonObj.get("dhPublicKey"));
                            if (jsonObj.get("nonce") != null && !jsonObj.get("nonce").isJsonNull() && !jsonObj.get("nonce").isJsonPrimitive()) {
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

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmConsentManagement3RequestHiRequestKeyMaterial` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmConsentManagement3RequestHiRequestKeyMaterial fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement3RequestHiRequestKeyMaterial)JSON.getGson().fromJson(jsonString, AbdmConsentManagement3RequestHiRequestKeyMaterial.class);
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
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement3RequestHiRequestKeyMaterial.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement3RequestHiRequestKeyMaterial> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement3RequestHiRequestKeyMaterial.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement3RequestHiRequestKeyMaterial>() {
                    public void write(JsonWriter out, AbdmConsentManagement3RequestHiRequestKeyMaterial value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmConsentManagement3RequestHiRequestKeyMaterial read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmConsentManagement3RequestHiRequestKeyMaterial.validateJsonElement(jsonElement);
                        return (AbdmConsentManagement3RequestHiRequestKeyMaterial)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

