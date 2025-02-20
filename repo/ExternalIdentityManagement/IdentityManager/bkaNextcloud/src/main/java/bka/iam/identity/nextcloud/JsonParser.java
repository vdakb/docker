package bka.iam.identity.nextcloud;

import bka.iam.identity.nextcloud.model.apitop.MetaResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.ToNumberPolicy;

public class JsonParser {
    private String raw;
    private JsonElement root;
    private MetaResponse metaParse;

    JsonParser(String raw) {
        this.raw = raw;
        this.root = com.google.gson.JsonParser.parseString(raw);
    }

    public <T> T as(Class<T> classOfT) {
        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        return gson.fromJson(this.root, classOfT);
    }

    public boolean isError() {
        metaParse = as(MetaResponse.class);
        return metaParse.getOcs().getMeta().getStatuscode() != 100;
    }

    public String getError() {
        if (metaParse == null) {
            metaParse = as(MetaResponse.class);
        }
        return metaParse.getOcs().getMeta().toString();
    }
}