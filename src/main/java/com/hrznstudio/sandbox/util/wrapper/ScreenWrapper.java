package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.client.screen.ContainerScreen;
import com.hrznstudio.sandbox.container.ContainerWrapper;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.entity.player.PlayerInventory;

public class ScreenWrapper extends Screen {
    public com.hrznstudio.sandbox.api.client.screen.Screen screen;

    public ScreenWrapper(com.hrznstudio.sandbox.api.client.screen.Screen screen) {
        super(WrappingUtil.convert(screen.getTitle()));
        this.screen = screen;
    }

    public static Screen create(com.hrznstudio.sandbox.api.client.screen.Screen s) {
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
        public com.hrznstudio.sandbox.api.client.screen.ContainerScreen screen;

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