package com.hrznstudio.sandbox.fabric.mixin.client;

import com.hrznstudio.sandbox.fabric.Sandbox;
import com.hrznstudio.sandbox.fabric.mixin.MixinMinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.script.ScriptException;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer extends MixinMinecraftServer {
    @Inject(method = "setupServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/integrated/IntegratedServer;loadWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Lcom/google/gson/JsonElement;)V",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    public void setupServer(CallbackInfoReturnable<Boolean> info) throws ScriptException {
        if (!Sandbox.setup()) {
            info.setReturnValue(false);
        }
    }
}