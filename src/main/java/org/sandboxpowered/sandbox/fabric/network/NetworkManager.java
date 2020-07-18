package org.sandboxpowered.sandbox.fabric.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class NetworkManager {

    private static final BiMap<Identifier, Class<? extends Packet>> packetMap = HashBiMap.create();
    private static final BiMap<Class<? extends Packet>, Identifier> packetMapInverse = packetMap.inverse();

    static {
        add(new Identifier("sandbox", "addon_sync"), AddonS2CPacket.class);
        add(new Identifier("sandbox", "container_open"), ContainerOpenPacket.class);
    }

    public static <T extends Packet> void add(Identifier id, Class<T> packetClass) {
        packetMap.put(id, packetClass);
    }

    public static Class<? extends Packet> get(Identifier id) {
        return packetMap.get(id);
    }

    public static void sendToServer(Packet packet) {
        Identifier id = getId(packet);
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(c2s(id, packet));
    }

    public static void sendToAll(Packet packet) {
        Identifier id = getId(packet);
//        SandboxServer.INSTANCE.getServer().getPlayerManager().sendToAll(s2c(id, packet)); TODO
    }

    public static void sendTo(Packet packet, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity)
            sendTo(packet, (ServerPlayerEntity) player);
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