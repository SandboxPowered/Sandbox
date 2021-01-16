package org.sandboxpowered.loader.fabric.mixin.loading;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minecraft.server.Bootstrap;
import org.sandboxpowered.loader.fabric.inject.FabricImplementationModule;
import org.sandboxpowered.loader.platform.Platform;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.sandboxpowered.loader.util.SandboxImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class MixinBootstrap {
    @Shadow
    private static boolean isBootstrapped;

    @Inject(method = "bootStrap", at = @At(value = "HEAD"))
    private static void vanillaSnapshot(CallbackInfo info) {
        if (!isBootstrapped) {
            Injector injector = Guice.createInjector(new FabricImplementationModule());
            SandboxPlatform platform = Platform.getPlatform();
            SandboxImpl.LOGGER.info("Loading Sandbox implementation '{}'", platform.getIdentity());
            platform.init();
        }
    }
}