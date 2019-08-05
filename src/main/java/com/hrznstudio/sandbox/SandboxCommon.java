package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public abstract class SandboxCommon {
    public List<AddonInfo> loadedAddons = Collections.emptyList();

    protected Map<Class, List<Identifier>> CONTENT_LIST = new HashMap<>();

    public List<Identifier> getContentList(Class contentClass) {
        return CONTENT_LIST.computeIfAbsent(contentClass, a -> new ArrayList<>());
    }

    protected abstract void setup();

    public void shutdown() {
        for (Identifier identifier : getContentList(Block.class)) {
            ((SandboxRegistry) Registry.BLOCK).remove(identifier);
        }
        for (Identifier identifier : getContentList(Item.class)) {
            ((SandboxRegistry) Registry.ITEM).remove(identifier);
        }
    }
}
