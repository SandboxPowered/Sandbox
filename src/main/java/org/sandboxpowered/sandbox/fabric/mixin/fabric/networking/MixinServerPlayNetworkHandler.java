package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.CustomPayloadPacket;
import org.sandboxpowered.sandbox.fabric.network.NetworkManager;
import org.sandboxpowered.sandbox.fabric.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    public void onCustomPayload(CustomPayloadC2SPacket c2s, CallbackInfo info) {
        NetworkThreadUtils.forceMainThread(c2s, (ServerPlayNetworkHandler) (Object) this, this.player.getServerWorld());
        CustomPayloadPacket customPayloadPacket = (CustomPayloadPacket) c2s;
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