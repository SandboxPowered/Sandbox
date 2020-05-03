package org.sandboxpowered.sandbox.fabric.network;

import net.minecraft.network.PacketByteBuf;

public interface Packet {
    void read(PacketByteBuf buf);

    void write(PacketByteBuf buf);

    void apply();
}
