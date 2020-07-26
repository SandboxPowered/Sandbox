package org.sandboxpowered.example;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.events.EnchantmentEvents;
import org.sandboxpowered.api.events.args.ItemResultArgs;
import org.sandboxpowered.api.events.args.Result;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.registry.Registrar;

import java.util.function.BiConsumer;

public class Example implements Addon {
    @Override
    public void init(SandboxAPI api) {
        api.getLog().info("Loading Example Addon");

        EnchantmentEvents.VALID_ITEM.subscribe((enchantment, itemResultArgs) -> itemResultArgs.setResult(Result.FAILURE));
    }

    @Override
    public void register(Registrar registrar) {
        registrar.register("test", new BaseItem(new Item.Settings()));
    }
}