package com.minecraft.minepay.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class JsonUtil {

    @NotNull
    private JsonObject object;

    public boolean isJsonNull(String name) {
        return getObject().get(name).isJsonNull();
    }

    public int getInt(String name) {
        if (isJsonNull(name)) return -1;
        return getObject().get(name).getAsInt();
    }

    public String getString(String name) {
        if (isJsonNull(name)) return null;
        return getObject().get(name).getAsString();
    }

    public boolean getBoolean(String name) {
        if (isJsonNull(name)) return false;
        return getObject().get(name).getAsBoolean();
    }

    public JsonObject getJsonObject(String name) {
        if (isJsonNull(name)) return null;
        return getObject().get(name).getAsJsonObject();
    }

    public JsonArray getJsonArray(String name) {
        if (isJsonNull(name)) return null;
        return getObject().get(name).getAsJsonArray();
    }

}
