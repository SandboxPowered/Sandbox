package org.sandboxpowered.sandbox.fabric.mixin.event.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow
    @Nullable
    public Screen currentScreen;

//    @ModifyVariable(method = "openScreen", at = @At("HEAD"), ordinal = 0)
//    public Screen openScreen(Screen screen) {
//        if (screen == null) {
//            ScreenEvent.Close close = EventDispatcher.publish(new ScreenEvent.Close(WrappingUtil.convert(currentScreen)));
//            if (close.isCancelled())
//                return currentScreen;
//            return null;
//        } else {
//            ScreenEvent.Open open = EventDispatcher.publish(new ScreenEvent.Open(WrappingUtil.convert(screen)));
//            if (open.isCancelled())
//                return currentScreen;
//            return WrappingUtil.convert(open.getScreen());
//        }
//    }
}