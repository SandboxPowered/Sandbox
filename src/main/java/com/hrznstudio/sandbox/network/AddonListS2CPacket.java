package com.hrznstudio.sandbox.network;

import net.minecraft.util.PacketByteBuf;

public class AddonListS2CPacket implements Packet {

    @Override
    public void read(PacketByteBuf packet) {
        // TODO do something with this data this is just for layouts now
        int modCount = packet.readInt();
        String urlPrefix = packet.readString(Short.MAX_VALUE);
        for (int i = 0; i < modCount; i++) {
            String modUrlSuffix = packet.readString(Short.MAX_VALUE);
            String modHash = packet.readString(Short.MAX_VALUE);
        }
    }

    @Override
    public void write(PacketByteBuf packet) {
        // TODO get a list of addons
    }

    @Override
    public void apply() {

    }
}
