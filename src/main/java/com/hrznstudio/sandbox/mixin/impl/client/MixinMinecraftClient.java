package com.hrznstudio.sandbox.mixin.impl.client;

import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.client.screen.IScreen;
import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
@Implements(@Interface(iface = Client.class, prefix = "sbx$"))
@Unique
public class MixinMinecraftClient {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientWorld world;

    @Shadow
    @Nullable
    public Screen currentScreen;

    public Player sbx$getPlayer() {
        return (Player) player;
    }

    public World sbx$getWorld() {
        return (World) world;
    }

    @Nullable
    public IScreen sbx$getCurrentScreen() {
        return WrappingUtil.convert(currentScreen);
    }
}