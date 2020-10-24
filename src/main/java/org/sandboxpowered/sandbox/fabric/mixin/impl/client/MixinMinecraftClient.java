package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import org.sandboxpowered.api.client.Client;
import org.sandboxpowered.api.client.GraphicsMode;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(MinecraftClient.class)
@Implements(@Interface(iface = Client.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinMinecraftClient {
    @Shadow
    @Final
    public GameOptions options;

    public GraphicsMode sbx$getGraphicsMode() {
        return WrappingUtil.convert(options.graphicsMode);
    }
}