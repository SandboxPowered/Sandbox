package com.hrznstudio.sandbox.mixin.networking;

import com.hrznstudio.sandbox.api.CustomPayloadPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    public void onCustomPayload(CustomPayloadC2SPacket c2s, CallbackInfo info) {
        CustomPayloadPacket packet = (CustomPayloadPacket) c2s;
        PacketByteBuf data = null;
        try {
            Identifier channel = packet.channel();
            if (!channel.getNamespace().equals("minecraft")) { //Override non-vanilla packets
                data = packet.data();
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