package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import org.sandboxpowered.sandbox.fabric.mixin.fabric.server.MixinMinecraftServer;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
import net.minecraft.server.MinecraftServer;
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
        SandboxServer.constructAndSetup((MinecraftServer) (Object) this);
    }
}