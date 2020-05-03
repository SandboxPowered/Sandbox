package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.CustomPayloadPacket;
import org.sandboxpowered.sandbox.fabric.network.NetworkManager;
import org.sandboxpowered.sandbox.fabric.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Inject(method = "onCustomPayload", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V", shift = At.Shift.AFTER, remap = false), cancellable = true)
    public void onCustomPayload(CustomPayloadS2CPacket s2c, CallbackInfo info) {
        CustomPayloadPacket customPayloadPacket = (CustomPayloadPacket) s2c;
        PacketByteBuf data = null;
        try {
            Identifier channel = customPayloadPacket.channel();
            if (!channel.getNamespace().equals("minecraft")) { //Override non-vanilla packets
                Class<? extends Packet> packetClass = NetworkManager.get(channel);
                if (packetClass != null) {
                    data = customPayloadPacket.data();
                    Packet packet = packetClass.getConstructor().newInstance();
                    packet.read(data);
                    packet.apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.release();
                info.cancel();
            }
        }
    }
}