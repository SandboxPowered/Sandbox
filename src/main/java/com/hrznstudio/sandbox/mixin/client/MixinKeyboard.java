package com.hrznstudio.sandbox.mixin.client;

import com.hrznstudio.sandbox.client.PanoramaHandler;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {

    @Redirect(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ScreenshotUtils;method_1659(Ljava/io/File;IILnet/minecraft/client/gl/GlFramebuffer;Ljava/util/function/Consumer;)V"))
    public void takeScreenshot(File file_1, int int_1, int int_2, GlFramebuffer glFramebuffer_1, Consumer<Text> consumer_1) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_P)) {
            PanoramaHandler.takeScreenshot(consumer_1);
        } else {
            ScreenshotUtils.method_1659(file_1, int_1, int_2, glFramebuffer_1, consumer_1);
        }
    }
}