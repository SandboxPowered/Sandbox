package org.sandboxpowered.sandbox.fabric;

import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.config.Config;
import org.sandboxpowered.sandbox.fabric.config.ConfigValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SandboxConfig {
    public static final ConfigValue<Boolean> enchantmentDecimal;
    public static final ConfigValue<String> velocityKey;
    public static final ConfigValue<ServerForwarding> forwarding;
    public static final ConfigValue<String> addonSyncURL;
    public static final ConfigValue<Boolean> disableAutoCrashSending;
    public static final ConfigValue<WorldBorder> worldBorder;
    public static final Config config;

    static {
        try {
            Path data = Paths.get("data");
            if (Files.notExists(data))
                Files.createDirectories(data);
            config = new Config(data.resolve("sandbox.toml"));
            enchantmentDecimal = config.get("enchantment.decimal");
            enchantmentDecimal.add(false);
            enchantmentDecimal.setComment(" Whether the Enchantment tooltip uses decimal or roman numerals");
            forwarding = config.get("server.forwarding.enable");
            forwarding.add(ServerForwarding.NONE);
            forwarding.setComment(" Use player info forwarding, 'NONE', 'BUNGEE', 'VELOCITY'");
            velocityKey = config.get("server.forwarding.key");
            velocityKey.add("KEY_HERE");
            velocityKey.setComment(" Secret key to authenticate with velocity");
            addonSyncURL = config.get("server.sync.url");
            addonSyncURL.add("https://example.com");
            addonSyncURL.setComment(" URL Prefix for the client to download server addons");

            worldBorder = config.get("client.world-border");
            worldBorder.add(WorldBorder.VANILLA);
            worldBorder.setComment(" Changes the texture of the world border");

            disableAutoCrashSending = config.get("crash.disable-auto-report");
            disableAutoCrashSending.add(false);
            disableAutoCrashSending.setComment(" Disables Sandbox automatically reporting crashes to the developers");
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateBorderType(WorldBorder border) {
        worldBorder.set(border);
        config.save();
    }

    public enum ServerForwarding {
        NONE,
        BUNGEE,
        VELOCITY;

        public boolean isForwarding() {
            return this != NONE;
        }
    }

    public enum WorldBorder {
        VANILLA("options.sandbox.worldborder.vanilla"),
        LINES("options.sandbox.worldborder.lines", new Identifier("sandbox", "textures/misc/lines.png")),
        GRID("options.sandbox.worldborder.grid", new Identifier("sandbox", "textures/misc/grid.png")),
        DOTS("options.sandbox.worldborder.dots", new Identifier("sandbox", "textures/misc/dot.png"));

        private static final WorldBorder[] VALUES = values();

        private final String translation;
        private final Identifier texture;

        WorldBorder(String translation, Identifier texture) {
            this.translation = translation;
            this.texture = texture;
        }

        WorldBorder(String translation) {
            this(translation, null);
        }

        public String getTranslation() {
            return translation;
        }

        public Identifier getTexture() {
            return texture;
        }

        public WorldBorder next() {
            int next = ordinal() + 1;
            if (next == VALUES.length)
                next = 0;
            return VALUES[next];
        }
    }
}