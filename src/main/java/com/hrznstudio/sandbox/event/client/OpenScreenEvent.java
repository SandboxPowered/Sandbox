package com.hrznstudio.sandbox.event.client;

import com.hrznstudio.sandbox.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends Event {
    private Screen screen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
