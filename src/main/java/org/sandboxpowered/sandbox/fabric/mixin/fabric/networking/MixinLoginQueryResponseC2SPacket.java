package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.util.PacketByteBuf;
import org.sandboxpowered.sandbox.fabric.internal.CustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LoginQueryResponseC2SPacket.class)
public class MixinLoginQueryResponseC2SPacket implements CustomPayloadPacket.LoginQueryPacket {
    @Shadow
    private int queryId;

    @Shadow
    private PacketByteBuf response;

    @Override
    public int getQueryId() {
        return this.queryId;
    }

    @Override
    public PacketByteBuf getBuffer() {
        return this.response;
    }
}
