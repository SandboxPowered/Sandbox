package org.sandboxpowered.loader.mixin.injection.experimental;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.sandboxpowered.loader.loading.SandboxLoader;
import org.sandboxpowered.loader.platform.Platform;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerPacksSource.class)
public class MixinServerPacksSource {
    @Inject(method = "loadPacks", at = @At("HEAD"))
    public void load(Consumer<Pack> consumer, Pack.PackConstructor packConstructor, CallbackInfo ci) {
        SandboxPlatform platform = Platform.getPlatform();
        if(platform.isLoaded()) {
            SandboxLoader loader = platform.getLoader();

            loader.addonPackRespositorySource.loadPacks(consumer, packConstructor);
        }
    }
}