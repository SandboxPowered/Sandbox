package com.hrznstudio.sandbox.event.client;

import com.hrznstudio.sandbox.event.CancellableEvent;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends CancellableEvent {
    private Screen screen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        validateChange();
        this.screen = screen;
    }
}