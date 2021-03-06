package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.CustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadS2CPacket.class)
public class MixinCustomPayloadS2CPacket implements CustomPayloadPacket {
    @Shadow
    private Identifier channel;

    @Shadow
    private PacketByteBuf data;

    @Override
    public Identifier channel() {
        return this.channel;
    }

    @Override
    public PacketByteBuf data() {
        return this.data;
    }
}
