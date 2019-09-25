package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.TextRenderer;
import org.sandboxpowered.sandbox.api.client.screen.Screen;
import org.sandboxpowered.sandbox.api.entity.player.PlayerEntity;
import org.sandboxpowered.sandbox.api.world.World;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
@Implements(@Interface(iface = Client.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinMinecraftClient {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientWorld world;

    @Shadow
    @Nullable
    public net.minecraft.client.gui.screen.Screen currentScreen;

    @Shadow
    public net.minecraft.client.font.TextRenderer textRenderer;

    public PlayerEntity sbx$getPlayer() {
        return (PlayerEntity) player;
    }

    public World sbx$getWorld() {
        return (World) world;
    }

    @Nullable
    public Screen sbx$getCurrentScreen() {
        return WrappingUtil.convert(currentScreen);
    }

    public TextRenderer sbx$getTextRenderer() {
        return (TextRenderer) textRenderer;
    }
}