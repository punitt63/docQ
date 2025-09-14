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
import com.google.gson.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.Base64;
import java.io.IOException;
import java.security.PublicKey;
import java.util.*;
import javax.annotation.Nonnull;

public class AbdmSessions3200Response {
    public static final String SERIALIZED_NAME_KEYS = "keys";
    @SerializedName("keys")
    @Nonnull
    private List<AbdmSessions3200ResponseKeysInner> keys = new ArrayList();
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmSessions3200Response() {
    }

    public AbdmSessions3200Response keys(@Nonnull List<AbdmSessions3200ResponseKeysInner> keys) {
        this.keys = keys;
        return this;
    }

    public AbdmSessions3200Response addKeysItem(AbdmSessions3200ResponseKeysInner keysItem) {
        if (this.keys == null) {
            this.keys = new ArrayList();
        }

        this.keys.add(keysItem);
        return this;
    }

    @Nonnull
    public List<AbdmSessions3200ResponseKeysInner> getKeys() {
        return this.keys;
    }

    private List<PublicKey> getPublicKeys() throws GeneralSecurityException {
        List<PublicKey> publicKeys = new ArrayList<>();
        for (AbdmSessions3200ResponseKeysInner keyInner : this.keys) {
            byte[] nBytes = Base64.getUrlDecoder().decode(keyInner.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(keyInner.getE());

            BigInteger modulus = new BigInteger(1, nBytes);
            BigInteger exponent = new BigInteger(1, eBytes);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKeys.add(kf.generatePublic(keySpec));
        }
        return publicKeys;
    }

    public boolean verifySignature(String data, String signatureB64) {
        try {
            byte[] sigBytes = Base64.getDecoder().decode(signatureB64);
            List<PublicKey> publicKeys = getPublicKeys();
            for (PublicKey publicKey : publicKeys) {
                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(publicKey);
                sig.update(data.getBytes());
                if (sig.verify(sigBytes)) {
                    return true;
                }
            }
            return false;
        } catch (Exception exception) {
            return false;
        }
    }

    public void setKeys(@Nonnull List<AbdmSessions3200ResponseKeysInner> keys) {
        this.keys = keys;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmSessions3200Response abdmSessions3200Response = (AbdmSessions3200Response)o;
            return Objects.equals(this.keys, abdmSessions3200Response.keys);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.keys});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmSessions3200Response {\n");
        sb.append("    keys: ").append(this.toIndentedString(this.keys)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmSessions3200Response is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            while(var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
                if (!openapiFields.contains(entry.getKey())) {
                    throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmSessions3200Response` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
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
            if (!jsonObj.get("keys").isJsonArray()) {
                throw new IllegalArgumentException(String.format("Expected the field `keys` to be an array in the JSON string but got `%s`", jsonObj.get("keys").toString()));
            } else {
                JsonArray jsonArraykeys = jsonObj.getAsJsonArray("keys");

                for(int i = 0; i < jsonArraykeys.size(); ++i) {
                    AbdmSessions3200ResponseKeysInner.validateJsonElement(jsonArraykeys.get(i));
                }

            }
        }
    }

    public static AbdmSessions3200Response fromJson(String jsonString) throws IOException {
        return (AbdmSessions3200Response)JSON.getGson().fromJson(jsonString, AbdmSessions3200Response.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("keys");
        openapiRequiredFields = new HashSet();
        openapiRequiredFields.add("keys");
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmSessions3200Response.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmSessions3200Response> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmSessions3200Response.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmSessions3200Response>() {
                    public void write(JsonWriter out, AbdmSessions3200Response value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmSessions3200Response read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmSessions3200Response.validateJsonElement(jsonElement);
                        return (AbdmSessions3200Response)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

