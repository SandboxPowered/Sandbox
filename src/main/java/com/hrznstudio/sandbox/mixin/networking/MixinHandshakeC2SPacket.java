package com.hrznstudio.sandbox.mixin.networking;

import net.minecraft.server.network.packet.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HandshakeC2SPacket.class)
public class MixinHandshakeC2SPacket {

}