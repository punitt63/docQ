package in.docq.abha.rest.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.docq.abha.rest.client.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbdmConsentManagement5Request extends AbstractOpenApiSchema {
    private static final Logger log = Logger.getLogger(AbdmConsentManagement5Request.class.getName());
    public static final Map<String, Class<?>> schemas = new HashMap();

    public AbdmConsentManagement5Request() {
        super("anyOf", Boolean.FALSE);
    }

    public AbdmConsentManagement5Request(Object o) {
        super("anyOf", Boolean.FALSE);
        this.setActualInstance(o);
    }

    public Map<String, Class<?>> getSchemas() {
        return schemas;
    }

    public void setActualInstance(Object instance) {
        if (instance instanceof AbdmConsentManagement5RequestAnyOf) {
            super.setActualInstance(instance);
        } else if (instance instanceof AbdmConsentManagement5RequestAnyOf1) {
            super.setActualInstance(instance);
        } else {
            throw new RuntimeException("Invalid instance type. Must be AbdmConsentManagement5RequestAnyOf, AbdmConsentManagement5RequestAnyOf1");
        }
    }

    public Object getActualInstance() {
        return super.getActualInstance();
    }

    public AbdmConsentManagement5RequestAnyOf getAbdmConsentManagement5RequestAnyOf() throws ClassCastException {
        return (AbdmConsentManagement5RequestAnyOf)super.getActualInstance();
    }

    public AbdmConsentManagement5RequestAnyOf1 getAbdmConsentManagement5RequestAnyOf1() throws ClassCastException {
        return (AbdmConsentManagement5RequestAnyOf1)super.getActualInstance();
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
        ArrayList<String> errorMessages = new ArrayList();

        try {
            AbdmConsentManagement5RequestAnyOf.validateJsonElement(jsonElement);
        } catch (Exception var4) {
            Exception e = var4;
            errorMessages.add(String.format("Deserialization for AbdmConsentManagement5RequestAnyOf failed with `%s`.", e.getMessage()));

            try {
                AbdmConsentManagement5RequestAnyOf1.validateJsonElement(jsonElement);
            } catch (Exception var3) {
                e = var3;
                errorMessages.add(String.format("Deserialization for AbdmConsentManagement5RequestAnyOf1 failed with `%s`.", e.getMessage()));
                throw new IOException(String.format("The JSON string is invalid for AbdmConsentManagement5Request with anyOf schemas: AbdmConsentManagement5RequestAnyOf, AbdmConsentManagement5RequestAnyOf1. no class match the result, expected at least 1. Detailed failure message for anyOf schemas: %s. JSON: %s", errorMessages, jsonElement.toString()));
            }
        }
    }

    public static AbdmConsentManagement5Request fromJson(String jsonString) throws IOException {
        return (AbdmConsentManagement5Request)JSON.getGson().fromJson(jsonString, AbdmConsentManagement5Request.class);
    }

    public String toJson() {
        return JSON.getGson().toJson(this);
    }

    static {
        schemas.put("AbdmConsentManagement5RequestAnyOf", AbdmConsentManagement5RequestAnyOf.class);
            schemas.put("AbdmConsentManagement5RequestAnyOf1", AbdmConsentManagement5RequestAnyOf1.class);
    }

    public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
        public CustomTypeAdapterFactory() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!AbdmConsentManagement5Request.class.isAssignableFrom(type.getRawType())) {
                return null;
            } else {
                final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
                final TypeAdapter<AbdmConsentManagement5RequestAnyOf> adapterAbdmConsentManagement5RequestAnyOf = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement5RequestAnyOf.class));
                final TypeAdapter<AbdmConsentManagement5RequestAnyOf1> adapterAbdmConsentManagement5RequestAnyOf1 = gson.getDelegateAdapter(this, TypeToken.get(AbdmConsentManagement5RequestAnyOf1.class));
                return (TypeAdapter<T>) (new TypeAdapter<AbdmConsentManagement5Request>() {
                    public void write(JsonWriter out, AbdmConsentManagement5Request value) throws IOException {
                        if (value != null && value.getActualInstance() != null) {
                            JsonElement element;
                            if (value.getActualInstance() instanceof AbdmConsentManagement5RequestAnyOf) {
                                element = adapterAbdmConsentManagement5RequestAnyOf.toJsonTree((AbdmConsentManagement5RequestAnyOf)value.getActualInstance());
                                elementAdapter.write(out, element);
                            } else if (value.getActualInstance() instanceof AbdmConsentManagement5RequestAnyOf1) {
                                element = adapterAbdmConsentManagement5RequestAnyOf1.toJsonTree((AbdmConsentManagement5RequestAnyOf1)value.getActualInstance());
                                elementAdapter.write(out, element);
                            } else {
                                throw new IOException("Failed to serialize as the type doesn't match anyOf schemas: AbdmConsentManagement5RequestAnyOf, AbdmConsentManagement5RequestAnyOf1");
                            }
                        } else {
                            elementAdapter.write(out, (JsonElement) null);
                        }
                    }

                    public AbdmConsentManagement5Request read(JsonReader in) throws IOException {
                        Object deserialized = null;
                        JsonElement jsonElement = (JsonElement)elementAdapter.read(in);
                        ArrayList<String> errorMessages = new ArrayList();
                        TypeAdapter actualAdapter = elementAdapter;

                        AbdmConsentManagement5Request ret;
                        try {
                            AbdmConsentManagement5RequestAnyOf.validateJsonElement(jsonElement);
                            actualAdapter = adapterAbdmConsentManagement5RequestAnyOf;
                            ret = new AbdmConsentManagement5Request();
                            ret.setActualInstance(actualAdapter.fromJsonTree(jsonElement));
                            return ret;
                        } catch (Exception var8) {
                            Exception e = var8;
                            errorMessages.add(String.format("Deserialization for AbdmConsentManagement5RequestAnyOf failed with `%s`.", e.getMessage()));
                            AbdmConsentManagement5Request.log.log(Level.FINER, "Input data does not match schema 'AbdmConsentManagement5RequestAnyOf'", e);

                            try {
                                AbdmConsentManagement5RequestAnyOf1.validateJsonElement(jsonElement);
                                actualAdapter = adapterAbdmConsentManagement5RequestAnyOf1;
                                ret = new AbdmConsentManagement5Request();
                                ret.setActualInstance(actualAdapter.fromJsonTree(jsonElement));
                                return ret;
                            } catch (Exception var7) {
                                e = var7;
                                errorMessages.add(String.format("Deserialization for AbdmConsentManagement5RequestAnyOf1 failed with `%s`.", e.getMessage()));
                                AbdmConsentManagement5Request.log.log(Level.FINER, "Input data does not match schema 'AbdmConsentManagement5RequestAnyOf1'", e);
                                throw new IOException(String.format("Failed deserialization for AbdmConsentManagement5Request: no class matches result, expected at least 1. Detailed failure message for anyOf schemas: %s. JSON: %s", errorMessages, jsonElement.toString()));
                            }
                        }
                    }
                }).nullSafe();
            }
        }
    }
}

