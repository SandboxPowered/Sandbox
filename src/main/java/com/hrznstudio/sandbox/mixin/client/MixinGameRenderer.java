package com.hrznstudio.sandbox.mixin.client;

import com.hrznstudio.sandbox.client.PanoramaHandler;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "getFov",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getFov(CallbackInfoReturnable<Double> info) {
        if (PanoramaHandler.takingPanorama)
            info.setReturnValue(91.0D);
    }
}