package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.MinecraftClient;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.screen.Screen;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.client.gui.screen.Screen.class)
@Implements(@Interface(iface = Screen.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinScreen {

    @Shadow
    public abstract void render(int int_1, int int_2, float float_1);

    @Shadow
    public abstract void init(MinecraftClient minecraftClient_1, int int_1, int int_2);

    public void sbx$init(Client client, int width, int height) {
        init((MinecraftClient) client, width, height);
    }

    public void sbx$draw(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY, partialTicks);
    }

    public Identity sbx$getId() {
        return Identity.of("minecraft", getClass().getName().toLowerCase());
    }
}