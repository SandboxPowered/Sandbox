package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.network.Packet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.PacketByteBuf;

public class TestPacket implements Packet {
    private String text;

    public TestPacket() {
    }

    public TestPacket(String text) {
        this.text = text;
    }

    @Override
    public void read(PacketByteBuf var1) {
        text = var1.readString();
    }

    @Override
    public void write(PacketByteBuf var1) {
        var1.writeString(text);
    }

    @Override
    public void apply() {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().player.addChatMessage(new LiteralText(text), true));
    }
}