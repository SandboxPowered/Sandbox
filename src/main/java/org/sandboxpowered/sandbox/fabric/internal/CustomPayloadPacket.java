package org.sandboxpowered.sandbox.fabric.internal;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface CustomPayloadPacket {
    Identifier channel();

    PacketByteBuf data();

    interface LoginQueryPacket {
        int getQueryId();

        PacketByteBuf getBuffer();
    }

    interface Handshake {
        String getPassword();
    }
}