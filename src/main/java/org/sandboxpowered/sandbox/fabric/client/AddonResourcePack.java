package org.sandboxpowered.sandbox.fabric.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resource.ZipResourcePack;
import org.sandboxpowered.api.addon.AddonInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.sandboxpowered.sandbox.fabric.util.JsonUtil.generatePackMCMeta;

public class AddonResourcePack extends ZipResourcePack {
    private static final Gson GSON = new GsonBuilder().create();
    private final AddonInfo info;

    public AddonResourcePack(File file, AddonInfo info) {
        super(file);
        this.info = info;
    }

    @Override
    public boolean containsFile(String string) {
        if ("pack.mcmeta".equals(string))
            return true; //return early because we are going to substitute a dummy file if it doesn't exist
        return super.containsFile(string);
    }

    @Override
    protected InputStream openFile(String filename) throws IOException {
        if ("pack.mcmeta".equals(filename) && !super.containsFile(filename))
            return generatePackMCMeta(info, GSON); //file not found, substitute one by using the addon spec description
        return super.openFile(filename);
    }
}