package com.hrznstudio.sandbox.network;

import net.minecraft.util.PacketByteBuf;

public interface Packet {
    void read(PacketByteBuf var1);

    void write(PacketByteBuf var1);

    void apply();
}
