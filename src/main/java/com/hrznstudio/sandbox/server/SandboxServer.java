package com.hrznstudio.sandbox.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.sandbox.Sandbox;
import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.api.RegistryOrder;
import com.hrznstudio.sandbox.api.SandboxLocation;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import com.hrznstudio.sandbox.util.FileUtil;
import com.hrznstudio.sandbox.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SandboxServer extends SandboxCommon {
    private static final Gson GSON = new GsonBuilder().create();
    public static  String[] ARGS;
    public static SandboxServer INSTANCE;
    private final boolean isIntegrated;

    private SandboxServer(boolean isIntegrated) {
        this.isIntegrated = isIntegrated;
        INSTANCE = this;
    }

    public static SandboxServer constructAndSetup(boolean integrated) {
        SandboxServer server = new SandboxServer(integrated);
        server.setup();
        return server;
    }

    @Override
    protected void setup() {
        CONTENT_LIST.clear();
        engine.init(Sandbox.SANDBOX);
        Log.info("Setting up Sandbox environment");
        findAddons();
        if (!isIntegrated) {
            setupDedicated();
        }
        loadedAddons.forEach(info -> {
            AddonInfo.FolderStructure scripts = info.getFolder().getSubFolder("scripts");
            for (RegistryOrder order : RegistryOrder.values()) {
                FileUtil.getFiles(scripts.getSubFile(order.getFolder()),
                        ((dir, name) -> name.equals(".js")),
                        true
                ).forEach(file -> engine.executeScript(file).ifPresent(e -> Log.error("Script encountered an error", e.getException())));
            }
        });
    }

    protected void findAddons() {
        loadedAddons = new ArrayList<>();
        for (File file : FileUtil.listFiles(SandboxLocation.ADDONS)) {
            try {
                if (file.isDirectory()) {
                    AddonInfo.FolderStructure root = new AddonInfo.FolderStructure(file);
                    AddonInfo info = GSON.fromJson(FileUtils.readFileToString(root.getSubFile("addon.json"), StandardCharsets.UTF_8), AddonInfo.class);
                    info.setFile(root);
                    loadedAddons.add(info);
                } else if (file.getName().endsWith(".sbx")) {
                    //TODO: Load from file
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setupDedicated() {

    }

    public boolean isIntegrated() {
        return isIntegrated;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        INSTANCE = null;
    }
}
