package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.block.Block;
import com.hrznstudio.sandbox.api.block.Material;
import com.hrznstudio.sandbox.api.item.Item;
import com.hrznstudio.sandbox.api.util.Functions;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.client.SandboxTitleScreen;
import com.hrznstudio.sandbox.impl.BasicRegistry;
import com.hrznstudio.sandbox.security.AddonSecurityPolicy;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.MaterialUtil;
import com.hrznstudio.sandbox.util.ReflectionHelper;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.security.Policy;
import java.util.function.Function;

public class SandboxHooks {
    public static void shutdown() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) {
            MinecraftClient.getInstance().execute(SandboxClient.INSTANCE::shutdown);
            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
                SandboxServer.INSTANCE.shutdown();
        } else {
            SandboxServer.INSTANCE.shutdown();
        }
    }

    public static void setupGlobal() {
        if (FabricLoader.getInstance().getAllMods()
                .stream()
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getId)
                .anyMatch(id -> !id.equals("sandbox") && !id.equals("fabricloader"))) {
            Sandbox.unsupportedModsLoaded = true;
        }
        Policy.setPolicy(new AddonSecurityPolicy());

        try {
            ReflectionHelper.setPrivateField(Functions.class, "identityFunction", (Function<String, Identity>) s -> (Identity) new Identifier(s));
            ReflectionHelper.setPrivateField(Functions.class, "materialFunction", (Function<String, Material>) MaterialUtil::from);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Material.AIR.getPistonInteraction();

        ((SandboxRegistry.Internal) Registry.BLOCK).set(new BasicRegistry<>(Registry.BLOCK, Block.class, WrappingUtil::convert, b -> (Block) b));
        ((SandboxRegistry.Internal) Registry.ITEM).set(new BasicRegistry<>(Registry.ITEM, Item.class, WrappingUtil::convert, b -> (Item) b));

        SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        SandboxDiscord.shutdown();
    }

    public static Screen openScreen(Screen screen) {
        if (screen instanceof TitleScreen || (screen == null && MinecraftClient.getInstance().world == null)) {
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Menu")
                    .setBigImage("logo", "")
                    .build()
            );
            screen = new SandboxTitleScreen();
        }
        if (screen instanceof MultiplayerScreen) {
//            screen = new DownloadScreen();
        }
//        if (SandboxClient.INSTANCE != null && screen != null) {
//            ScreenEvent.Open event = SandboxClient.INSTANCE.getDispatcher().publish(new ScreenEvent.Open(screen));
//            if (event.wasCancelled()) {
//                screen = MinecraftClient.getInstance().currentScreen;
//            } else {
//                screen = event.getScreen();
//            }
//        } else if (SandboxClient.INSTANCE != null) {
//            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
//            ScreenEvent.Close event = SandboxClient.INSTANCE.getDispatcher().publish(new ScreenEvent.Close(currentScreen, screen));
//            if (event.wasCancelled()) {
//                screen = currentScreen;
//            } else {
//                screen = event.getNewScreen();
//            }
//        }

        return screen;
    }
}