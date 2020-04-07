package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TextRenderer.class)
@Unique
public abstract class MixinTextRenderer {
}