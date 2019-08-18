package com.hrznstudio.sandbox.mixin.client;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    // Tick method
    @Inject(method = "tickEntities", at = @At("RETURN"))
    public void tickEntities(CallbackInfo ci) {
//        Sandbox.ragdolls.updateRagdolls();
    }

}