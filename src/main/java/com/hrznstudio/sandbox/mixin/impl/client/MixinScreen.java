package com.hrznstudio.sandbox.mixin.impl.client;

import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.client.screen.IScreen;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.client.gui.screen.Screen.class)
@Implements(@Interface(iface = IScreen.class, prefix = "sbx$"))
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

}