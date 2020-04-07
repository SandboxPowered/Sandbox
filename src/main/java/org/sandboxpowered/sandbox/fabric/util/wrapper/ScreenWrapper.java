package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.screen.BaseScreen;
import org.sandboxpowered.sandbox.api.client.screen.ContainerScreen;
import org.sandboxpowered.sandbox.fabric.container.ContainerWrapper;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

public class ScreenWrapper extends Screen {
    public BaseScreen screen;

    public ScreenWrapper(BaseScreen screen) {
        super(WrappingUtil.convert(screen.getTitle()));
        this.screen = screen;
    }

    public static Screen create(BaseScreen s) {
        return new ScreenWrapper(s);
    }

    @Override
    public void init(MinecraftClient minecraftClient_1, int int_1, int int_2) {
        super.init(minecraftClient_1, int_1, int_2);
        screen.init((Client) minecraftClient_1, int_1, int_2);
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        super.render(int_1, int_2, float_1);
        screen.draw(int_1, int_2, float_1);
    }

    public static class ContainerScreenWrapper extends AbstractContainerScreen<ContainerWrapper> {
        public ContainerScreen screen;

        public ContainerScreenWrapper(ContainerScreen screen, ContainerWrapper wrapper, PlayerInventory playerInventory_1) {
            super(wrapper, playerInventory_1, WrappingUtil.convert(screen.getTitle()));
            this.screen = screen;
        }

        @Override
        protected void drawBackground(float var1, int var2, int var3) {
            screen.draw(var2, var3, var1);
        }


    }
}