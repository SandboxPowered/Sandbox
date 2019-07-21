package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.ScriptEngine;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public abstract class SandboxCommon {
    protected ScriptEngine engine = new ScriptEngine();
    public List<AddonInfo> loadedAddons = Collections.emptyList();

    protected Map<Class, List<Identifier>> CONTENT_LIST = new HashMap<>();

    public List<Identifier> getContentList(Class contentClass) {
        return CONTENT_LIST.computeIfAbsent(contentClass, a -> new ArrayList<>());
    }

    public void loadBlock(Identifier identifier, Block block, ItemGroup itemGroup) {
        getContentList(Block.class).add(identifier);
        Registry.register(Registry.BLOCK, identifier, block);
        loadItem(identifier, new BlockItem(block, new Item.Settings().group(itemGroup)));
    }

    public void loadItem(Identifier identifier, Item item) {
        getContentList(Item.class).add(identifier);
        Registry.register(Registry.ITEM, identifier, item);
        if (item instanceof BlockItem)
            Item.BLOCK_ITEMS.put(((BlockItem) item).getBlock(), item);
    }

    protected abstract void setup();

    public void shutdown() {
        for (Identifier identifier : getContentList(Block.class)) {
            ((SandboxRegistry) Registry.BLOCK).remove(identifier);
        }
        for (Identifier identifier : getContentList(Item.class)) {
            ((SandboxRegistry) Registry.ITEM).remove(identifier);
        }
        engine.shutdown();
    }

    public ScriptEngine getEngine() {
        return engine;
    }
}
