package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import org.sandboxpowered.sandbox.fabric.internal.ClientConnectionInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(ClientConnection.class)
public class MixinClientConnection implements ClientConnectionInternal {
    @Shadow
    private SocketAddress address;
    private UUID sandboxUUID;
    private Property[] sandboxProfile;

    @Override
    public void setSocketAddress(SocketAddress address) {
        this.address = address;
    }

    @Override
    public UUID getSandboxUUID() {
        return sandboxUUID;
    }

    @Override
    public void setSandboxUUID(UUID uuid) {
        this.sandboxUUID = uuid;
    }

    @Override
    public Property[] getSandboxProfile() {
        return sandboxProfile;
    }

    @Override
    public void setSandboxProfile(Property[] profile) {
        this.sandboxProfile = profile;
    }
}
