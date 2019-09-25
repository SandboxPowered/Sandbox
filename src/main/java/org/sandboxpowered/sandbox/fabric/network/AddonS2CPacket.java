package org.sandboxpowered.sandbox.fabric.network;

import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import org.sandboxpowered.sandbox.fabric.Sandbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddonS2CPacket implements Packet {

    public int count;
    public String prefix;
    public List<Pair<String, String>> addons;

    public AddonS2CPacket(int count, String prefix, List<Pair<String, String>> addons) {
        this.count = count;
        this.prefix = prefix;
        this.addons = addons;
    }

    public AddonS2CPacket() {
    }

    @Override
    public void read(PacketByteBuf var1) {
        count = var1.readInt();
        prefix = var1.readString(Short.MAX_VALUE);
        if (count > 0) {
            addons = new ArrayList<>();
        } else {
            addons = Collections.emptyList();
        }
        for (int i = 0; i < count; i++) {
            String suffix = var1.readString(Short.MAX_VALUE);
            String hash = var1.readString(Short.MAX_VALUE);
            addons.add(new Pair<>(suffix, hash));
        }
    }

    @Override
    public void write(PacketByteBuf var1) {
        var1.writeInt(count);
        var1.writeString(prefix);
        addons.forEach(pair -> {
            var1.writeString(pair.getLeft(), Short.MAX_VALUE);
            var1.writeString(pair.getRight(), Short.MAX_VALUE);
        });
    }

    @Override
    public void apply() {
        Sandbox.open(prefix, addons);
    }
}