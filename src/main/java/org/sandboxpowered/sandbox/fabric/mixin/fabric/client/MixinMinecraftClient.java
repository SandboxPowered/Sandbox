package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.resource.ClientResourcePackProfile;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Unit;
import org.sandboxpowered.sandbox.fabric.SandboxHooks;
import org.sandboxpowered.sandbox.fabric.client.*;
import org.sandboxpowered.sandbox.fabric.resources.SandboxResourceCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    private void addonResourcePackModifications(List<ResourcePack> packs) {
        if (SandboxClient.INSTANCE != null) {
            SandboxClient.INSTANCE.loader.getAddons().forEach(spec -> {
                try {
                    Path path = Paths.get(spec.getPath().toURI());
                    if (Files.isDirectory(path))
                        packs.add(new AddonFolderResourcePack(path, spec));
                    else
                        packs.add(new AddonResourcePack(path.toFile()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V"))
    public void constructor(ResourcePackManager<ClientResourcePackProfile> manager) {
        manager.registerProvider(new SandboxResourceCreator());
        manager.scanPacks();
    }

// TODO: Fix crash stuff
//
//    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"))
//    private CrashReport create(Throwable throwable_1, String string_1) {
//        Log.fatal(string_1, throwable_1);
//        return CrashReport.create(throwable_1, string_1);
//    }
//
//    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V"))
//    private void fatal(Logger logger, String message, Throwable t) {
//        Log.fatal(message, t);
//    }

    @Inject(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;start()V"))
    public void clientSetup(CallbackInfo info) {
        SandboxClient.constructAndSetup();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V", shift = At.Shift.BEFORE))
    public void renderStart(CallbackInfo info) {
        PanoramaHandler.renderTick(true);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw()V", shift = At.Shift.AFTER))
    public void renderEnd(CallbackInfo info) {
        PanoramaHandler.renderTick(false);
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void shutdownGlobal(CallbackInfo info) {
        SandboxHooks.shutdownGlobal();
    }

    @Redirect(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;beginMonitoredReload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReloadMonitor;"))
    public ResourceReloadMonitor reloadResources(ReloadableResourceManager manager, Executor var1, Executor var2, CompletableFuture<Unit> var3, List<ResourcePack> var4) {
        addonResourcePackModifications(var4);
        System.out.println(var4);
        return manager.beginMonitoredReload(var1, var2, var3, var4);
    }


    @ModifyVariable(method = "openScreen", at = @At("HEAD"), ordinal = 0)
    public Screen openScreen(Screen screen) {
        if (screen instanceof TitleScreen || (screen == null && MinecraftClient.getInstance().world == null)) {
            screen = new SandboxTitleScreen();
        }
        return screen;
    }

    /**
     * @reason Sandbox Client Branding
     * @author Coded
     */
    @Overwrite
    public String getVersionType() {
        return "Sandbox";
    }
}