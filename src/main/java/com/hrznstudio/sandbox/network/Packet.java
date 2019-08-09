package com.hrznstudio.sandbox.network;

import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public interface Packet {
    void read(PacketByteBuf var1) throws IOException;

    void write(PacketByteBuf var1) throws IOException;

    void apply();
}
