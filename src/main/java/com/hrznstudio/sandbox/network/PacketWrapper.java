package com.hrznstudio.sandbox.network;

import net.minecraft.util.PacketByteBuf;

public class PacketWrapper implements Packet {
    com.hrznstudio.sandbox.api.network.Packet packet;

    public PacketWrapper(com.hrznstudio.sandbox.api.network.Packet packet) {
        this.packet = packet;
    }

    @Override
    public void read(PacketByteBuf buf) {

    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply() {

    }
}
