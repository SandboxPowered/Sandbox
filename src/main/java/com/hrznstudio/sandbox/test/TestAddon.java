package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.event.Event;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.client.OpenScreenEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.SoundOptionsScreen;

import java.time.Duration;

public class TestAddon {
    public TestAddon() {
        EventDispatcher.getServerDispatcher()
                .on(Event.class)
                .log()
                .delayElements(Duration.ofSeconds(10))
                .subscribe(event -> {
                    System.out.println("Event Received");
                });

        EventDispatcher.getClientDispatcher().subscribe(OpenScreenEvent.class, event -> {
            if (event.getScreen() instanceof LanguageOptionsScreen) {
                event.setScreen(new SoundOptionsScreen(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance().options));
            }
        });
    }
}