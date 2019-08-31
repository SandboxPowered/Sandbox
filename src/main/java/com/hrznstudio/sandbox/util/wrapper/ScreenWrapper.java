package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.client.screen.IScreen;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

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
}