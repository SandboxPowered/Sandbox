package org.sandboxpowered.sandbox.fabric.mixin.fabric.server;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;
import org.sandboxpowered.sandbox.fabric.util.SandboxStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(value = MinecraftDedicatedServer.class)
public class MixinDedicatedServer extends MixinMinecraftServer {
    @Inject(method = "setupServer",
            at = @At(value = "INVOKE",
                    target = "net/minecraft/server/dedicated/MinecraftDedicatedServer.setPlayerManager(Lnet/minecraft/server/PlayerManager;)V",
                    shift = At.Shift.AFTER),
            cancellable = true
    )
    public void setupServer(CallbackInfoReturnable<Boolean> info) {
        try {
            SandboxStorage.server = (Server) this;
            new SandboxLoader().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}