package com.hrznstudio.sandbox.mixin.impl.server;

import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.api.game.GameMode;
import com.hrznstudio.sandbox.api.server.Server;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Mono;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
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

    public Mono<GameMode> sbx$getGameMode() {
        return Mono.empty();
    }
}