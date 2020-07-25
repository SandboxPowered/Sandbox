package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.SandboxOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    private static final Identifier DOTS = new Identifier("sandbox", "textures/misc/dot.png");
    private static final Identifier LINES = new Identifier("sandbox", "textures/misc/lines.png");
    private static final Identifier GRID = new Identifier("sandbox", "textures/misc/grid.png");

    @Redirect(method = "renderWorldBorder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V"))
    public void bindTexture(TextureManager manager, Identifier identifier) {
        switch (SandboxOptions.border) {
            case LINES:
                identifier = LINES;
                break;
            case GRID:
                identifier = GRID;
                break;
            case DOTS:
                identifier = DOTS;
                break;
        }
        manager.bindTexture(identifier);
    }
}