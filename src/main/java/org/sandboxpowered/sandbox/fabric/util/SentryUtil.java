package org.sandboxpowered.sandbox.fabric.util;

import com.google.common.collect.ImmutableMap;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.UserBuilder;
import io.sentry.event.interfaces.ExceptionInterface;
import net.fabricmc.loader.api.FabricLoader;
import org.sandboxpowered.sandbox.api.client.Session;
import org.sandboxpowered.sandbox.fabric.Sandbox;
import org.sandboxpowered.sandbox.fabric.SandboxCommon;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;

import java.util.Date;
import java.util.Map;

public class SentryUtil {
    private static final boolean doDevCrash = false;

    public static void initSentry() {
        Sentry.init();
        if (SandboxCommon.client != null) {
            Session session = SandboxCommon.client.getSession();
            Sentry.getContext().setUser(new UserBuilder()
                    .setId(session.getUUID())
                    .setUsername(session.getUsername())
                    .build());
        }
    }

    public static void record(EventBuilder e) {
        if (!SandboxConfig.disableAutoCrashSending.get() && (doDevCrash || !FabricLoader.getInstance().isDevelopmentEnvironment())) {
            Sentry.capture(e);
        }
    }

    public static EventBuilder create(String message, Event.Level level, Throwable throwable) {
        return create(message, level)
                .withSentryInterface(new ExceptionInterface(throwable));
    }

    public static EventBuilder create(String message, Event.Level level) {
        EventBuilder builder = new EventBuilder()
                .withTimestamp(new Date())
                .withMessage(message)
                .withEnvironment(FabricLoader.getInstance().isDevelopmentEnvironment() ? "development" : "production")
                .withLevel(level)
                .withExtra("Thread", Thread.currentThread().getName())
                .withExtra("Java Vendor", System.getProperty("java.vendor", "undefined"))
                .withExtra("Java Version", System.getProperty("java.version", "undefined"))
                .withExtra("Memory Total", Runtime.getRuntime().totalMemory())
                .withExtra("Memory Max", Runtime.getRuntime().maxMemory())
                .withExtra("Memory Free", Runtime.getRuntime().freeMemory())
                .withExtra("CPU Cores", Runtime.getRuntime().availableProcessors())
                .withTag("os_name", System.getProperty("os.name"))
                .withTag("os_version", System.getProperty("os.version", "undefined"))
                .withTag("os_arch", System.getProperty("os.arch", "undefined"))
                .withTag("side", Sandbox.SANDBOX.getSide().isClient() ? "client" : "Server")
                .withPlatform("fabric");

        ImmutableMap.Builder<String, Map<String, Object>> contextBuilder = ImmutableMap.builder();
        if (SandboxClient.INSTANCE != null) {
            ImmutableMap.Builder<String, Object> addons = ImmutableMap.builder();
            SandboxClient.INSTANCE.loader.getAddons().forEach(spec -> {
                addons.put(spec.getModid(), spec.getVersion().toString());
            });
            contextBuilder.put("Client Addons", addons.build());
        }
        if (SandboxServer.INSTANCE != null) {
            ImmutableMap.Builder<String, Object> addons = ImmutableMap.builder();
            SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> {
                addons.put(spec.getModid(), spec.getVersion().toString());
            });
            contextBuilder.put("Server Addons", addons.build());
        }
        if (!FabricLoader.getInstance().getAllMods().isEmpty()) {
            ImmutableMap.Builder<String, Object> mods = ImmutableMap.builder();
            FabricLoader.getInstance().getAllMods().forEach(spec -> {
                mods.put(spec.getMetadata().getId(), spec.getMetadata().getVersion().getFriendlyString());
            });
            contextBuilder.put("Fabric Mods", mods.build());
        }
        builder.withContexts(contextBuilder.build());

        return builder;
    }
}