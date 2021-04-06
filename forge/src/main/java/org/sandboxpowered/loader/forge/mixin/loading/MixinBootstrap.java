package org.sandboxpowered.loader.forge.mixin.loading;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minecraft.server.Bootstrap;
import org.sandboxpowered.loader.forge.inject.ForgeImplementationModule;
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
    private static void head(CallbackInfo info) {
        if (!isBootstrapped) {
            Injector injector = Guice.createInjector(new ForgeImplementationModule());
            SandboxPlatform platform = Platform.getPlatform();
            SandboxImpl.LOGGER.info("Loading Sandbox implementation '{}'", platform.getIdentity());
        }
    }

    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/GameData;vanillaSnapshot()V"))
    private static void vanillaSnapshot(CallbackInfo info) {
        SandboxPlatform platform = Platform.getPlatform();
        platform.init();
    }
}