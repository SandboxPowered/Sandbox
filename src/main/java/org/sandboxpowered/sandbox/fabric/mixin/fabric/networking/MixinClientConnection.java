package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import org.sandboxpowered.sandbox.fabric.internal.ClientConnectionInternal;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;

@Mixin(ClientConnection.class)
public class MixinClientConnection implements ClientConnectionInternal {
    @Shadow
    private SocketAddress address;

    @Override
    public void setSocketAddress(SocketAddress address) {
        this.address = address;
    }
}
