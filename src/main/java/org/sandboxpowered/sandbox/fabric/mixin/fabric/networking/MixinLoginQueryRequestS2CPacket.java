package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.LoginQueryRequestPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LoginQueryRequestS2CPacket.class)
public class MixinLoginQueryRequestS2CPacket implements LoginQueryRequestPacket {
    @Shadow
    private int queryId;

    @Shadow
    private Identifier channel;

    @Shadow
    private PacketByteBuf payload;

    @Override
    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    @Override
    public void setChannel(Identifier channel) {
        this.channel = channel;
    }

    @Override
    public void setPayload(PacketByteBuf payload) {
        this.payload = payload;
    }
}
