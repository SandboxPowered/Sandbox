package com.hrznstudio.sandbox.mixin.networking;

import com.hrznstudio.sandbox.api.CustomPayloadPacket;
import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import net.minecraft.util.PacketByteBuf;
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
