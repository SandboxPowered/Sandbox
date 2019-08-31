package com.hrznstudio.sandbox.mixin.event.client;

import com.hrznstudio.sandbox.api.event.ScreenEvent;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow
    @Nullable
    public Screen currentScreen;

    @ModifyVariable(method = "openScreen", at = @At("HEAD"), ordinal = 0)
    public Screen openScreen(Screen screen) {
        if (screen == null) {
            ScreenEvent.Close close = EventDispatcher.publish(new ScreenEvent.Close(WrappingUtil.convert(currentScreen)));
            if (close.isCancelled())
                return currentScreen;
            return null;
        } else {
            ScreenEvent.Open open = EventDispatcher.publish(new ScreenEvent.Open(WrappingUtil.convert(screen)));
            return WrappingUtil.convert(open.getScreen());
        }
    }
}