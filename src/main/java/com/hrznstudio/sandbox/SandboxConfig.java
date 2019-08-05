package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.config.Config;
import com.hrznstudio.sandbox.config.ConfigValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SandboxConfig {
    public static Config config;

    public static ConfigValue<Boolean> enchantmentDecimal;

    static {
        try {
            Files.createDirectories(Paths.get("data"));
            config = new Config(Paths.get("data", "sandbox.toml"));
            enchantmentDecimal = config.get("enchantment.decimal");
            enchantmentDecimal.add(false);
            enchantmentDecimal.setComment(" Whether the Enchantment tooltip uses decimal or roman numerals");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}