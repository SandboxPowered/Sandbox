package org.sandboxpowered.sandbox.fabric.mixin.fabric.server;

import net.minecraft.server.MinecraftServer;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinMinecraftServer {

    @Shadow
    public abstract boolean isDedicated();

    @Inject(method = "isOnlineMode", at = @At("HEAD"), cancellable = true)
    public void isOnlineMode(CallbackInfoReturnable<Boolean> info) {
        if (this.isDedicated() && SandboxConfig.forwarding.getEnum(SandboxConfig.ServerForwarding.class).isForwarding())
            info.setReturnValue(false);
    }

    /**
     * @author B0undarybreaker
     */
    @ModifyConstant(method = "getServerModName")
    public String getServerModName(String original) {
        return "Sandbox";
    }
}