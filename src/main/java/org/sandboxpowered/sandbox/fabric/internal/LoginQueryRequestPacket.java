package org.sandboxpowered.sandbox.fabric.internal;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface LoginQueryRequestPacket {
    void setQueryId(int queryId);

    void setChannel(Identifier channel);

    void setPayload(PacketByteBuf payload);
}