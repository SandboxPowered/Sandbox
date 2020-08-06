package org.sandboxpowered.sandbox.fabric.internal;

import com.mojang.authlib.properties.Property;

import java.net.SocketAddress;
import java.util.UUID;

public interface ClientConnectionInternal {
    void setSocketAddress(SocketAddress address);

    UUID getSandboxUUID();

    void setSandboxUUID(UUID uuid);

    Property[] getSandboxProfile();

    void setSandboxProfile(Property[] profile);
}
