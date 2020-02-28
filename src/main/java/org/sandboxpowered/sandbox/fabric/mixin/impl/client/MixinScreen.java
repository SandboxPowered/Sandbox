package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.client.gui.screen.Screen.class)
@Unique
public abstract class MixinScreen {
}