package org.sandboxpowered.sandbox.fabric.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.sandboxpowered.api.addon.AddonInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JsonUtil {
    public static int getInt(JsonObject object, String value) {
        if (!object.has(value))
            return 0;
        return object.get(value).getAsInt();
    }

    @NotNull
    public static InputStream generatePackMCMeta(AddonInfo info, Gson gson) {
        JsonObject meta = new JsonObject();
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", 6);
        pack.addProperty("description", info.getDescription());
        meta.add("pack", pack);
        return new ByteArrayInputStream(gson.toJson(meta).getBytes(StandardCharsets.UTF_8));
    }
}