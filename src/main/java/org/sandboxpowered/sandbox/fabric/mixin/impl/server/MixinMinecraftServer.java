package org.sandboxpowered.sandbox.fabric.mixin.impl.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.sandbox.fabric.SandboxCommon;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
@Implements(@Interface(iface = Server.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinMinecraftServer {
    @Shadow
    public abstract ServerWorld getWorld(DimensionType dimensionType_1);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(CallbackInfo info) {
        SandboxCommon.server = (Server) this;
    }

    public World sbx$getWorld(Identity identity) {
        return (World) getWorld(DimensionType.byId(WrappingUtil.convert(identity)));
    }
}