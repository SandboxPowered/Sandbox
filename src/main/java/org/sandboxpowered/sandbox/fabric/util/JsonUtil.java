package org.sandboxpowered.sandbox.fabric.util;

import com.google.gson.JsonObject;

public final class JsonUtil {
    public static int getInt(JsonObject object, String value) {
        if (!object.has(value))
            return 0;
        return object.get(value).getAsInt();
    }
}