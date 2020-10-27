package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ReloadableResourceManager;
import org.sandboxpowered.sandbox.fabric.SandboxHooks;
import org.sandboxpowered.sandbox.fabric.client.PanoramaHandler;
import org.sandboxpowered.sandbox.fabric.service.rendering.AtlasReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;init(Lnet/minecraft/client/MinecraftClient;)V"))
    public void setOverlay(MinecraftClient client) {
        resourceManager.registerListener(new AtlasReloader());
        SplashScreen.init(client);
    }

// TODO: Fix crash stuff
//
//    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"))
//    private CrashReport create(Throwable throwable_1, String string) {
//        Log.fatal(string, throwable_1);
//        return CrashReport.create(throwable_1, string);
//    }
//
//    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V"))
//    private void fatal(Logger logger, String message, Throwable t) {
//        Log.fatal(message, t);
//    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V", shift = At.Shift.BEFORE))
    public void renderStart(CallbackInfo info) {
        PanoramaHandler.INSTANCE.renderTick(true);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
    public void renderEnd(CallbackInfo info) {
        PanoramaHandler.INSTANCE.renderTick(false);
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void shutdownGlobal(CallbackInfo info) {
        SandboxHooks.close();
    }

    @ModifyVariable(method = "openScreen", at = @At("HEAD"), ordinal = 0)
    public Screen openScreen(Screen screen) {
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