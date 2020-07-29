package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.options.Option;
import org.apache.commons.lang3.ArrayUtils;
import org.sandboxpowered.sandbox.fabric.SandboxOptions;
import org.sandboxpowered.sandbox.fabric.internal.ISandboxScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen implements ISandboxScreen {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/options/Option;)V"))
    public void addAll(ButtonListWidget widget, Option[] options) {
        widget.addAll(ArrayUtils.add(options, SandboxOptions.WORLD_BORDER));
    }
}