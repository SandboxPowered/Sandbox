package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.event.CancellableEvent;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.client.OpenScreenEvent;
import com.hrznstudio.sandbox.event.mod.ModInitEvent;
import net.minecraft.client.gui.screen.LanguageOptionsScreen;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TestAddon {
    public TestAddon() {
        EventDispatcher.getServerDispatcher()
                .on(ModInitEvent.class)
                .subscribe(ev -> {
                    Registry.ITEM.add(new Identifier("test", "test"), new Item(new Item.Settings()));
                    Registry.ITEM.add(new Identifier("test", "test2"), new Item(new Item.Settings()));
                    Registry.ITEM.add(new Identifier("test", "test3"), new Item(new Item.Settings()));
                });
        EventDispatcher.getClientDispatcher()
                .on(OpenScreenEvent.class)
                .filter(event -> event.getScreen() instanceof LanguageOptionsScreen)
                .subscribe(CancellableEvent::cancel);
    }
}