package com.hrznstudio.sandbox.network;

import net.minecraft.util.PacketByteBuf;

public interface Packet {
    void read(PacketByteBuf buf);

    void write(PacketByteBuf buf);

    void apply();
}
