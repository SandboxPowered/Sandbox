package com.hrznstudio.sandbox.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.hrznstudio.sandbox.server.SandboxServer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class NetworkManager {

    private static final BiMap<Identifier, Class> packetMap = HashBiMap.create();
    private static final BiMap<Class, Identifier> packetMapInverse = packetMap.inverse();

    public static <T extends Packet> void add(Identifier id, Class<T> packetClass) {
        packetMap.put(id, packetClass);
    }

    public static Class<? extends Packet> get(Identifier id) {
        return packetMap.get(id);
    }

    public static void sendToServer(Packet packet) {
        Identifier id = getId(packet);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(c2s(id, packet));
    }

    public static void sendToAll(Packet packet) {
        Identifier id = getId(packet);
        SandboxServer.INSTANCE.getServer().getPlayerManager().sendToAll(s2c(id, packet));
    }

    public static void sendTo(Packet packet, ServerPlayerEntity player) {
        Identifier id = getId(packet);
        player.networkHandler.sendPacket(s2c(id, packet));
    }

    private static Identifier getId(Packet packet) {
        return packetMapInverse.get(packet.getClass());
    }

    private static CustomPayloadC2SPacket c2s(Identifier id, Packet packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.write(buf);
        return new CustomPayloadC2SPacket(id, buf);
    }

    private static CustomPayloadS2CPacket s2c(Identifier id, Packet packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.write(buf);
        return new CustomPayloadS2CPacket(id, buf);
    }
}