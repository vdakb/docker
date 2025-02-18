package bka.iam.identity.moodle;

import bka.iam.identity.moodle.model.CreateUserResponse;
import bka.iam.identity.moodle.model.ErrorResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.List;
import java.util.Map;

public class JsonParser {
    private static final Type TYPE_CREATEUSERRESPONSE_ARRAY = new TypeToken<List<CreateUserResponse>>() {}.getType();
    private static final Type TYPE_GETUSERSBYFIELD_ARRAY = new TypeToken<List<Map<String, Object>>>() {}.getType();

    private String raw;
    private JsonElement root;

    JsonParser(String raw) {
        this.raw = raw;
        this.root = com.google.gson.JsonParser.parseString(raw);
    }

    public boolean isEmptyArray() {
        return this.root.isJsonArray() && this.root.getAsJsonArray().size() == 0;
    }

    /**
     * @return MoodleError if an error or null
     */
    public ErrorResponse getMoodleError() {
        if (! this.root.isJsonObject()) {
            return null;
        }

        JsonObject obj = this.root.getAsJsonObject();
        if (! obj.has("exception")) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(this.root, ErrorResponse.class);
    }

    public List<CreateUserResponse> asCreateUserReponse() {
        Gson gson = new Gson();
        return gson.fromJson(this.root, TYPE_CREATEUSERRESPONSE_ARRAY);
    }

    public List<Map<String, Object>> asGetUsersByFieldResponse() {
        Gson gson = new Gson();
        return gson.fromJson(this.root, TYPE_GETUSERSBYFIELD_ARRAY);
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
    
    public JsonArray getWarning() {
       if (! this.root.isJsonObject()) {
            return null;
        }

        JsonObject obj = this.root.getAsJsonObject();
        if (! obj.has("warnings")) {
            return null;
        }

        return obj.getAsJsonArray("warnings");
    }
}