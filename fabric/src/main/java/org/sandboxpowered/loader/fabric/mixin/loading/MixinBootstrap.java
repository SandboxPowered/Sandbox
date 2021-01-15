package org.sandboxpowered.loader.fabric.mixin.loading;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minecraft.server.Bootstrap;
import org.sandboxpowered.loader.fabric.SandboxFabric;
import org.sandboxpowered.loader.fabric.inject.FabricImplementationModule;
import org.sandboxpowered.loader.inject.SandboxImplementationModule;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bootstrap.class)
public class MixinBootstrap {
    @Shadow
    protected static void wrapStreams() {
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Bootstrap;wrapStreams()V"))
    private static void vanillaSnapshot() {
        wrapStreams();
        Injector injector = Guice.createInjector(new FabricImplementationModule());
        SandboxPlatform platform = injector.getInstance(SandboxPlatform.class);
        platform.init();
    }
}