package com.hrznstudio.sandbox.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import com.hrznstudio.sandbox.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SandboxLoader {
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static List<AddonInfo> locateAddons(File addonsFolder) {
        if (!addonsFolder.exists()) {
            addonsFolder.mkdir();
            return Collections.emptyList();
        }
        File[] addons = addonsFolder.listFiles(File::isDirectory);
        if (addons == null)
            return Collections.emptyList();
        return Stream.of(addons)
                .map(f -> new File(f, "addon.json"))
                .filter(File::exists)
                .map(f -> {
                    try {
                        return GSON.fromJson(FileUtils.readFileToString(f, StandardCharsets.UTF_8), AddonInfo.class).setFile(new AddonInfo.AddonFolder(f.getParentFile()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .peek(info -> Log.info("Adding filesystem addon " + info.getId()))
                .collect(Collectors.toList());
    }
}