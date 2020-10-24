package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;
import org.sandboxpowered.sandbox.fabric.client.PanoramaHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void debugWarn(String string, Object... objects);

    @Redirect(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ScreenshotUtils;saveScreenshot(Ljava/io/File;IILnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V"))
    public void takeScreenshot(File file, int width, int height, Framebuffer framebuffer, Consumer<Text> consumer) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_P)) {
            PanoramaHandler.INSTANCE.takeScreenshot(consumer);
        } else {
            ScreenshotUtils.saveScreenshot(file, width, height, framebuffer, consumer);
        }
    }

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    public void processF3(int button, CallbackInfoReturnable<Boolean> b) {
        if (button == 81) {
            this.debugWarn("debug.help.message");
            ChatHud chat = this.client.inGameHud.getChatHud();
            chat.addMessage(new TranslatableText("debug.reload_chunks.help"));
            chat.addMessage(new TranslatableText("debug.show_hitboxes.help"));
            chat.addMessage(new TranslatableText("debug.copy_location.help"));
            chat.addMessage(new TranslatableText("debug.clear_chat.help"));
            chat.addMessage(new TranslatableText("debug.cycle_renderdistance.help"));
            chat.addMessage(new TranslatableText("debug.chunk_boundaries.help"));
            chat.addMessage(new TranslatableText("debug.advanced_tooltips.help"));
            chat.addMessage(new TranslatableText("debug.inspect.help"));
            chat.addMessage(new TranslatableText("debug.creative_spectator.help"));
            chat.addMessage(new TranslatableText("debug.pause_focus.help"));
            chat.addMessage(new TranslatableText("debug.help.help"));
            chat.addMessage(new TranslatableText("debug.reload_resourcepacks.help"));
            chat.addMessage(new TranslatableText("debug.pause.help"));
            chat.addMessage(new LiteralText("Test"));
            b.setReturnValue(true);
        }
    }
}