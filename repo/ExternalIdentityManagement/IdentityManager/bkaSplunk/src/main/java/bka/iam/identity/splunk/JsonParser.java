package bka.iam.identity.splunk;

import bka.iam.identity.splunk.model.ErrorResponse;
import bka.iam.identity.splunk.model.UserResponse;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParser {
    private static final Type TYPE_USERRESPONSE_ARRAY = new TypeToken<UserResponse>() {}.getType();

    private String raw;
    private JsonElement root;

    JsonParser(String raw) {
        this.raw = raw;
        this.root = com.google.gson.JsonParser.parseString(raw);
    }
    public ErrorResponse getSplunkError() {
        if (! this.root.isJsonObject()) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(this.root, ErrorResponse.class);
    }

    public boolean isEmptyArray() {
        return this.root.isJsonArray() && this.root.getAsJsonArray().size() == 0;
    }

    public UserResponse asUserResponse() {
        Gson gson = new Gson();
        return gson.fromJson(this.root, TYPE_USERRESPONSE_ARRAY);
    }

    public <T> T as(Class<T> classOfT) {
        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        return gson.fromJson(this.root, classOfT);
    }

    public boolean isNull() {
        return this.root.isJsonNull();
    }

    public static final String serialiseObject(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}