package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.MinecraftClient;
import org.sandboxpowered.api.client.Client;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftClient.class)
@Implements(@Interface(iface = Client.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinMinecraftClient {

}