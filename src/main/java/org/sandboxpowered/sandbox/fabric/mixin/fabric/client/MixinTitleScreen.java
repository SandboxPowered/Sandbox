package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen {
    @Shadow
    @Final
    private static Identifier MINECRAFT_TITLE_TEXTURE;
    @Shadow
    @Final
    private static Identifier EDITION_TITLE_TEXTURE;
    @Shadow
    @Final
    private static Identifier PANORAMA_OVERLAY;
    @Shadow
    @Final
    public static CubeMapRenderer PANORAMA_CUBE_MAP;
    @Mutable
    @Shadow
    @Final
    private RotatingCubeMapRenderer backgroundRenderer;
    private static final CubeMapRenderer[] PANORAMA_CUBE_MAPS = new CubeMapRenderer[5];
    private final Random random = new Random();

    static {
        for (int i = 0; i < 5; i++) {
            PANORAMA_CUBE_MAPS[i] = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama_" + i + "/panorama"));
        }
    }

    @Inject(method = "<init>(Z)V", at = @At("RETURN"))
    public void constructor(boolean bl, CallbackInfo info) {
        this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAPS[random.nextInt(PANORAMA_CUBE_MAPS.length)]);
    }

    @Inject(method = "loadTexturesAsync", at = @At("HEAD"), cancellable = true)
    private static void loadTextures(TextureManager textureManager, Executor executor, CallbackInfoReturnable<CompletableFuture<Void>> info) {
        info.setReturnValue(CompletableFuture.allOf(
                textureManager.loadTextureAsync(MINECRAFT_TITLE_TEXTURE, executor),
                textureManager.loadTextureAsync(EDITION_TITLE_TEXTURE, executor),
                textureManager.loadTextureAsync(PANORAMA_OVERLAY, executor),
                PANORAMA_CUBE_MAP.loadTexturesAsync(textureManager, executor),
                CompletableFuture.allOf(Arrays.stream(PANORAMA_CUBE_MAPS).map(cube -> cube.loadTexturesAsync(textureManager, executor)).toArray(CompletableFuture[]::new))
        ));
    }

}