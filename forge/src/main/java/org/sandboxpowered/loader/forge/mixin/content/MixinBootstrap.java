package org.sandboxpowered.loader.forge.mixin.content;


import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.registries.GameData;
import org.sandboxpowered.loader.forge.SandboxForgeCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bootstrap.class)
public abstract class MixinBootstrap {
    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/GameData;vanillaSnapshot()V"))
    private static void wrapStreamsRedirect() {
        GameData.vanillaSnapshot();
        SandboxForgeCore.CORE.init();
    }
}