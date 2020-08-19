package org.sandboxpowered.sandbox.fabric.mixin.fabric.server;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Shadow
    @Final
    private ResourcePackManager dataPackManager;

//    @ModifyVariable(method = "main", at = @At("HEAD"), ordinal = 0)
//    private static String[] main(String[] args) {
//        SandboxServer.ARGS = args;
//        ArrayUtil.removeAll(args, "-noaddons");
//        return args;
//    }

    @Shadow
    public abstract boolean isDedicated();

    @Inject(method = "shutdown",
            at = @At(value = "TAIL")
    )
    public void shutdown(CallbackInfo info) {
//        SandboxServer.INSTANCE.shutdown(); TODO
    }

//    @Inject(method = "loadWorldDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V", shift = At.Shift.BEFORE))
//    public void loadDatapacks(File file_1, LevelProperties levelProperties_1, CallbackInfo info) {
//        this.dataPackManager.registerProvider(new AddonResourceCreator());
//    }

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