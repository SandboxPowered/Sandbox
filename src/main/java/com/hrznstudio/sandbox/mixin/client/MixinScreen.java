package com.hrznstudio.sandbox.mixin.client;

import com.hrznstudio.sandbox.api.ISandboxScreen;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.client.ScreenEvent;
import com.hrznstudio.sandbox.server.SandboxServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
    public void init(CallbackInfo info) {
        if (SandboxClient.INSTANCE != null)
            SandboxClient.INSTANCE.getDispatcher().publish(new ScreenEvent.Init((Screen) (Object) this));
    }
}