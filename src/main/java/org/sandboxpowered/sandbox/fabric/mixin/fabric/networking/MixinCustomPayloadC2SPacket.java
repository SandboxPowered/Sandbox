package org.sandboxpowered.sandbox.fabric.mixin.fabric.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.CustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadC2SPacket.class)
public class MixinCustomPayloadC2SPacket implements CustomPayloadPacket {
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
