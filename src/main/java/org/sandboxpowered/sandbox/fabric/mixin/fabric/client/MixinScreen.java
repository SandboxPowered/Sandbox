package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import org.sandboxpowered.sandbox.fabric.internal.ISandboxScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen implements ISandboxScreen {
    @Shadow
    @Final
    protected List<AbstractButtonWidget> buttons;

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T abstractButtonWidget_1);

    @Override
    public List<AbstractButtonWidget> getButtons() {
        return buttons;
    }
}