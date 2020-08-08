package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Redirect(method = "renderWorldBorder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V"))
    public void bindTexture(TextureManager manager, Identifier identifier) {
        SandboxConfig.WorldBorder border = SandboxConfig.worldBorder.getEnum(SandboxConfig.WorldBorder.class);
        if (border.getTexture() != null)
            identifier = border.getTexture();
        manager.bindTexture(identifier);
    }
}