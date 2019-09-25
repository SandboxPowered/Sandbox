package org.sandboxpowered.sandbox.fabric.util;

import com.google.common.net.InetAddresses;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VelocityUtil {
    public static final Identifier PLAYER_INFO_CHANNEL = new Identifier("velocity", "player_info");

    public static boolean checkIntegrity(final PacketByteBuf buf) {
        final byte[] signature = new byte[32];
        buf.readBytes(signature);

        final byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), data);

        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SandboxConfig.velocityKey.get().getBytes(), "HmacSHA256"));
            final byte[] mySignature = mac.doFinal(data);
            if (!MessageDigest.isEqual(signature, mySignature)) {
                return false;
            }
        } catch (final InvalidKeyException | NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }

        int version = buf.readVarInt();
        if (version != 1) {
            throw new IllegalStateException("Unsupported forwarding version " + version + ", wanted " + 1);
        }

        return true;
    }

    public static InetAddress readAddress(final PacketByteBuf buf) {
        return InetAddresses.forString(buf.readString(32767));
    }

    public static GameProfile createProfile(final PacketByteBuf buf) {
        final GameProfile profile = new GameProfile(buf.readUuid(), buf.readString(16));
        readProperties(buf, profile);
        return profile;
    }

    private static void readProperties(final PacketByteBuf buf, final GameProfile profile) {
        if (buf.readableBytes() < 4)
            return;
        final int properties = buf.readInt();
        for (int i1 = 0; i1 < properties; i1++) {
            final String name = buf.readString(32767);
            final String value = buf.readString(32767);
            final String signature = buf.readBoolean() ? buf.readString(32767) : null;
            profile.getProperties().put(name, new Property(name, value, signature));
        }
    }

    public static LoginQueryRequestS2CPacket create(int id) {
        LoginQueryRequestS2CPacket packet = new LoginQueryRequestS2CPacket();
        try {
            ReflectionHelper.setPrivateField(LoginQueryRequestS2CPacket.class, packet, "queryId", id);
            ReflectionHelper.setPrivateField(LoginQueryRequestS2CPacket.class, packet, "channel", PLAYER_INFO_CHANNEL);
            ReflectionHelper.setPrivateField(LoginQueryRequestS2CPacket.class, packet, "payload", new PacketByteBuf(Unpooled.buffer()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return packet;
    }
}