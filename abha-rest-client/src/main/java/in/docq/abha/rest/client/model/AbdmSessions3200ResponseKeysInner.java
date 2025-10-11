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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

public class AbdmSessions3200ResponseKeysInner {
    public static final String SERIALIZED_NAME_E = "e";
    @SerializedName("e")
    @Nullable
    private String e;
    public static final String SERIALIZED_NAME_KID = "kid";
    @SerializedName("kid")
    @Nullable
    private String kid;
    public static final String SERIALIZED_NAME_KTY = "kty";
    @SerializedName("kty")
    @Nullable
    private String kty;
    public static final String SERIALIZED_NAME_N = "n";
    @SerializedName("n")
    @Nullable
    private String n;
    public static final String SERIALIZED_NAME_USE = "use";
    @SerializedName("use")
    @Nullable
    private String use;
    public static final String SERIALIZED_NAME_X5C = "x5c";
    @SerializedName("x5c")
    @Nullable
    private List<String> x5c = new ArrayList();
    public static final String SERIALIZED_NAME_X5T = "x5t";
    @SerializedName("x5t")
    @Nullable
    private String x5t;
    public static final String SERIALIZED_NAME_X5T2 = "x5t2";
    @SerializedName("x5t2")
    @Nullable
    private String x5t2;
    public static final String SERIALIZED_NAME_ALG = "alg";
    @SerializedName("alg")
    @Nullable
    private String alg;
    public static HashSet<String> openapiFields = new HashSet();
    public static HashSet<String> openapiRequiredFields;

    public AbdmSessions3200ResponseKeysInner() {
    }

    public AbdmSessions3200ResponseKeysInner e(@Nullable String e) {
        this.e = e;
        return this;
    }

    @Nullable
    public String getE() {
        return this.e;
    }

    public void setE(@Nullable String e) {
        this.e = e;
    }

    public AbdmSessions3200ResponseKeysInner kid(@Nullable String kid) {
        this.kid = kid;
        return this;
    }

    @Nullable
    public String getKid() {
        return this.kid;
    }

    public void setKid(@Nullable String kid) {
        this.kid = kid;
    }

    public AbdmSessions3200ResponseKeysInner kty(@Nullable String kty) {
        this.kty = kty;
        return this;
    }

    @Nullable
    public String getKty() {
        return this.kty;
    }

    public void setKty(@Nullable String kty) {
        this.kty = kty;
    }

    public AbdmSessions3200ResponseKeysInner n(@Nullable String n) {
        this.n = n;
        return this;
    }

    @Nullable
    public String getN() {
        return this.n;
    }

    public void setN(@Nullable String n) {
        this.n = n;
    }

    public AbdmSessions3200ResponseKeysInner use(@Nullable String use) {
        this.use = use;
        return this;
    }

    @Nullable
    public String getUse() {
        return this.use;
    }

    public void setUse(@Nullable String use) {
        this.use = use;
    }

    public AbdmSessions3200ResponseKeysInner x5c(@Nullable List<String> x5c) {
        this.x5c = x5c;
        return this;
    }

    public AbdmSessions3200ResponseKeysInner addX5cItem(String x5cItem) {
        if (this.x5c == null) {
            this.x5c = new ArrayList();
        }

        this.x5c.add(x5cItem);
        return this;
    }

    @Nullable
    public List<String> getX5c() {
        return this.x5c;
    }

    public void setX5c(@Nullable List<String> x5c) {
        this.x5c = x5c;
    }

    public AbdmSessions3200ResponseKeysInner x5t(@Nullable String x5t) {
        this.x5t = x5t;
        return this;
    }

    @Nullable
    public String getX5t() {
        return this.x5t;
    }

    public void setX5t(@Nullable String x5t) {
        this.x5t = x5t;
    }

    public AbdmSessions3200ResponseKeysInner x5t2(@Nullable String x5t2) {
        this.x5t2 = x5t2;
        return this;
    }

    @Nullable
    public String getX5t2() {
        return this.x5t2;
    }

    public void setX5t2(@Nullable String x5t2) {
        this.x5t2 = x5t2;
    }

    public AbdmSessions3200ResponseKeysInner alg(@Nullable String alg) {
        this.alg = alg;
        return this;
    }

    @Nullable
    public String getAlg() {
        return this.alg;
    }

    public void setAlg(@Nullable String alg) {
        this.alg = alg;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbdmSessions3200ResponseKeysInner abdmSessions3200ResponseKeysInner = (AbdmSessions3200ResponseKeysInner)o;
            return Objects.equals(this.e, abdmSessions3200ResponseKeysInner.e) && Objects.equals(this.kid, abdmSessions3200ResponseKeysInner.kid) && Objects.equals(this.kty, abdmSessions3200ResponseKeysInner.kty) && Objects.equals(this.n, abdmSessions3200ResponseKeysInner.n) && Objects.equals(this.use, abdmSessions3200ResponseKeysInner.use) && Objects.equals(this.x5c, abdmSessions3200ResponseKeysInner.x5c) && Objects.equals(this.x5t, abdmSessions3200ResponseKeysInner.x5t) && Objects.equals(this.x5t2, abdmSessions3200ResponseKeysInner.x5t2) && Objects.equals(this.alg, abdmSessions3200ResponseKeysInner.alg);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.e, this.kid, this.kty, this.n, this.use, this.x5c, this.x5t, this.x5t2, this.alg});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AbdmSessions3200ResponseKeysInner {\n");
        sb.append("    e: ").append(this.toIndentedString(this.e)).append("\n");
        sb.append("    kid: ").append(this.toIndentedString(this.kid)).append("\n");
        sb.append("    kty: ").append(this.toIndentedString(this.kty)).append("\n");
        sb.append("    n: ").append(this.toIndentedString(this.n)).append("\n");
        sb.append("    use: ").append(this.toIndentedString(this.use)).append("\n");
        sb.append("    x5c: ").append(this.toIndentedString(this.x5c)).append("\n");
        sb.append("    x5t: ").append(this.toIndentedString(this.x5t)).append("\n");
        sb.append("    x5t2: ").append(this.toIndentedString(this.x5t2)).append("\n");
        sb.append("    alg: ").append(this.toIndentedString(this.alg)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        if (jsonElement == null && !openapiRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(String.format("The required field(s) %s in AbdmSessions3200ResponseKeysInner is not found in the empty JSON string", openapiRequiredFields.toString()));
        } else {
            Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
            Iterator var2 = entries.iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.get("e") != null && !jsonObj.get("e").isJsonNull() && !jsonObj.get("e").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `e` to be a primitive type in the JSON string but got `%s`", jsonObj.get("e").toString()));
                    }

                    if (jsonObj.get("kid") != null && !jsonObj.get("kid").isJsonNull() && !jsonObj.get("kid").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `kid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("kid").toString()));
                    }

                    if (jsonObj.get("kty") != null && !jsonObj.get("kty").isJsonNull() && !jsonObj.get("kty").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `kty` to be a primitive type in the JSON string but got `%s`", jsonObj.get("kty").toString()));
                    }

                    if (jsonObj.get("n") != null && !jsonObj.get("n").isJsonNull() && !jsonObj.get("n").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `n` to be a primitive type in the JSON string but got `%s`", jsonObj.get("n").toString()));
                    }

                    if (jsonObj.get("use") != null && !jsonObj.get("use").isJsonNull() && !jsonObj.get("use").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `use` to be a primitive type in the JSON string but got `%s`", jsonObj.get("use").toString()));
                    }

                    if (jsonObj.get("x5c") != null && !jsonObj.get("x5c").isJsonNull() && !jsonObj.get("x5c").isJsonArray()) {
                        throw new IllegalArgumentException(String.format("Expected the field `x5c` to be an array in the JSON string but got `%s`", jsonObj.get("x5c").toString()));
                    }

                    if (jsonObj.get("x5t") != null && !jsonObj.get("x5t").isJsonNull() && !jsonObj.get("x5t").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `x5t` to be a primitive type in the JSON string but got `%s`", jsonObj.get("x5t").toString()));
                    }

                    if (jsonObj.get("x5t2") != null && !jsonObj.get("x5t2").isJsonNull() && !jsonObj.get("x5t2").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `x5t2` to be a primitive type in the JSON string but got `%s`", jsonObj.get("x5t2").toString()));
                    }

                    if (jsonObj.get("alg") != null && !jsonObj.get("alg").isJsonNull() && !jsonObj.get("alg").isJsonPrimitive()) {
                        throw new IllegalArgumentException(String.format("Expected the field `alg` to be a primitive type in the JSON string but got `%s`", jsonObj.get("alg").toString()));
                    }

                    return;
                }

                entry = (Map.Entry)var2.next();
            } while(openapiFields.contains(entry.getKey()));

            throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AbdmSessions3200ResponseKeysInner` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
    }

    public static AbdmSessions3200ResponseKeysInner fromJson(String jsonString) throws IOException {
        return (AbdmSessions3200ResponseKeysInner) JSON.getGson().fromJson(jsonString, AbdmSessions3200ResponseKeysInner.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        openapiFields.add("e");
        openapiFields.add("kid");
        openapiFields.add("kty");
        openapiFields.add("n");
        openapiFields.add("use");
        openapiFields.add("x5c");
        openapiFields.add("x5t");
        openapiFields.add("x5t2");
        openapiFields.add("alg");
        openapiRequiredFields = new HashSet();
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmSessions3200ResponseKeysInner.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmSessions3200ResponseKeysInner> thisAdapter = gson.getDelegateAdapter(this, TypeToken.get(AbdmSessions3200ResponseKeysInner.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmSessions3200ResponseKeysInner>() {
                    public void write(JsonWriter out, AbdmSessions3200ResponseKeysInner value) throws IOException {
                        JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
                        elementAdapter.write(out, obj);
                    }

                    public AbdmSessions3200ResponseKeysInner read(JsonReader in) throws IOException {
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        AbdmSessions3200ResponseKeysInner.validateJsonElement(jsonElement);
                        return (AbdmSessions3200ResponseKeysInner)thisAdapter.fromJsonTree(jsonElement);
                    }
                }).nullSafe();
            }
        }
    }
}

