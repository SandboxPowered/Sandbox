package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.config.Config;
import com.hrznstudio.sandbox.config.ConfigValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SandboxConfig {
    private static Config config;

    public static ConfigValue<Boolean> enchantmentDecimal;
    public static ConfigValue<Boolean> velocity;
    public static ConfigValue<String> velocityKey;
    public static ConfigValue<String> addonSyncURL;

    static {
        try {
            Path data = Paths.get("data");
            if (Files.notExists(data))
                Files.createDirectories(data);
            config = new Config(Paths.get("data", "sandbox.toml"));
            enchantmentDecimal = config.get("enchantment.decimal");
            enchantmentDecimal.add(false);
            enchantmentDecimal.setComment(" Whether the IEnchantment tooltip uses decimal or roman numerals");
            velocity = config.get("server.velocity.enable");
            velocity.add(false);
            velocity.setComment(" Use Velocity Modern Forwarding or not");
            velocityKey = config.get("server.velocity.key");
            velocityKey.add("KEY_HERE");
            velocityKey.setComment(" Secret key to authenticate with velocity");
            addonSyncURL = config.get("server.sync.url");
            addonSyncURL.add("https://example.com");
            addonSyncURL.setComment(" URL Prefix for the client to download server addons");
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}