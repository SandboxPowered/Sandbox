package com.hrznstudio.sandbox.event.client;

import com.hrznstudio.sandbox.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class ScreenEvent extends Event {
    private final Screen screen;

    public ScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public static class Init extends ScreenEvent {
        public Init(Screen screen) {
            super(screen);
        }
    }

    @Cancellable
    public static class Open extends ScreenEvent {
        private Screen screen;

        public Open(Screen screen) {
            super(screen);
            this.screen = screen;
        }

        @Override
        public Screen getScreen() {
            return screen;
        }

        public void setScreen(Screen screen) {
            this.screen = screen;
        }
    }
}