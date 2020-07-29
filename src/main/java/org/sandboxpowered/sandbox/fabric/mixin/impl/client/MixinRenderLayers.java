package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.BlockWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class MixinRenderLayers {
    @Inject(method = "getBlockLayer", at = @At(value = "HEAD"), cancellable = true)
    private static void getBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> info) {
        if (state.getBlock() instanceof BlockWrapper)
            info.setReturnValue(getRenderLayerFromState(WrappingUtil.convert(state)));
    }

    @Inject(method = "method_29359", at = @At(value = "HEAD"), cancellable = true)
    private static void getMovingBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> info) {
        if (state.getBlock() instanceof BlockWrapper) {
            RenderLayer layer = getRenderLayerFromState(WrappingUtil.convert(state));
            if (layer == RenderLayer.getTranslucent())
                layer = RenderLayer.getTranslucentMovingBlock();
            info.setReturnValue(layer);
        }
    }

    private static RenderLayer getRenderLayerFromState(org.sandboxpowered.api.state.BlockState state) {
        //TODO: Maybe allow blocks to fully say what render layer they want even if its custom.
        Block.BlockRenderLayer layer = state.getRenderLayer(WrappingUtil.convert(MinecraftClient.getInstance().options.graphicsMode));
        switch (layer) {
            case SOLID:
                return RenderLayer.getSolid();
            case TRANSPARENT:
                return RenderLayer.getTranslucent();
            case CUTOUT:
                return RenderLayer.getCutout();
            case CUTOUT_MIPPED:
                return RenderLayer.getCutoutMipped();
        }
        return RenderLayer.getSolid();
    }
}