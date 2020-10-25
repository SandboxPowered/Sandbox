package org.sandboxpowered.sandbox.fabric.mixin.fabric.client.screen;

import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.sandboxpowered.api.client.rendering.RenderPipeline;
import org.sandboxpowered.api.client.rendering.ui.DynamicRenderer;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceScreen.class)
public abstract class MixinAbstractFurnaceScreen extends HandledScreen {
    private static final Identity BACKGROUND = Identity.of("sandbox", "background");
    private static final Identity SLOT = Identity.of("sandbox", "slot");
    private static final Identity FIRE_ON = Identity.of("sandbox", "fire_on");
    private static final Identity FIRE_OFF = Identity.of("sandbox", "fire_off");
    private static final Identity ARROW_FULL = Identity.of("sandbox", "arrow_full");
    private static final Identity ARROW_EMPTY = Identity.of("sandbox", "arrow_empty");
    public MixinAbstractFurnaceScreen(ScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "drawBackground", at = @At(value = "HEAD"), cancellable = true)
    public void renderBackground(MatrixStack matrixStack, float f, int i, int j, CallbackInfo info) {
        info.cancel();
        DynamicRenderer renderer = RenderPipeline.getUniversalPipeline().getDynamicRenderer();

        org.sandboxpowered.api.util.math.MatrixStack stack = WrappingUtil.convert(matrixStack);

        renderer.renderSprite(stack, BACKGROUND, this.x, this.y, this.backgroundWidth, this.backgroundHeight);

        renderer.renderSpriteArray(stack, SLOT, this.x + 7, this.y + 83, 9, 3);
        renderer.renderSpriteArray(stack, SLOT, this.x + 7, this.y + 141, 9, 1);
        renderer.renderSprite(stack, SLOT, this.x + 55, this.y + 16);
        renderer.renderSprite(stack, SLOT, this.x + 55, this.y + 52);
        renderer.renderSprite(stack, SLOT, this.x + 111, this.y + 30, 26, 26);

        renderer.renderSprite(stack, FIRE_OFF, this.x + 56, this.y + 36);
        renderer.renderSprite(stack, ARROW_EMPTY, this.x + 79, this.y + 34);

        if (((AbstractFurnaceScreenHandler) this.handler).isBurning()) {
            float progress = ((AbstractFurnaceScreenHandler) this.handler).getFuelProgress();
            renderer.renderSliceSprite(stack, FIRE_ON, this.x + 56, this.y + 36, DynamicRenderer.SliceDirection.UP, progress / 13f);
        }

        float progress = ((AbstractFurnaceScreenHandler) this.handler).getCookProgress();
        renderer.renderSliceSprite(stack, ARROW_FULL, this.x + 79, this.y + 34, DynamicRenderer.SliceDirection.RIGHT, progress / 24f);
    }
}