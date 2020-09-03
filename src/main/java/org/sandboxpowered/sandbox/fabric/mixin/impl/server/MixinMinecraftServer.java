package org.sandboxpowered.sandbox.fabric.mixin.impl.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(MinecraftServer.class)
@Implements(@Interface(iface = Server.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinMinecraftServer {
    @Shadow
    @Nullable
    public abstract ServerWorld getWorld(RegistryKey<net.minecraft.world.World> registryKey);

    @Shadow private PlayerManager playerManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(CallbackInfo info) {
//        SandboxCommon.server = (Server) this; TODO
    }

    public World sbx$getWorld(Identity identity) {
        throw new UnsupportedOperationException("Unable to use getWorld"); //TODO
//        return (World) getWorld(DimensionType.byId(WrappingUtil.convert(identity)));
    }

    public Stream<PlayerEntity> sbx$getPlayers() {
        return playerManager.getPlayerList().stream().map(WrappingUtil::convert);
    }
}