package org.sandboxpowered.sandbox.fabric.network;

import net.minecraft.util.PacketByteBuf;

public class PacketWrapper implements Packet {
    org.sandboxpowered.sandbox.api.network.Packet packet;

    public PacketWrapper(org.sandboxpowered.sandbox.api.network.Packet packet) {
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
