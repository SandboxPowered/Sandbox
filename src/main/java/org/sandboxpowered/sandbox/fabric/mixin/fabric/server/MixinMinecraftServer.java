package org.sandboxpowered.sandbox.fabric.mixin.fabric.server;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    @Final
    private ResourcePackManager<ResourcePackProfile> dataPackManager;

//    @ModifyVariable(method = "main", at = @At("HEAD"), ordinal = 0)
//    private static String[] main(String[] args) {
//        SandboxServer.ARGS = args;
//        ArrayUtil.removeAll(args, "-noaddons");
//        return args;
//    }

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

    /**
     * @author B0undarybreaker
     */
    @ModifyConstant(method = "getServerModName")
    public String getServerModName(String original) {
        return "Sandbox";
    }
}