package com.hrznstudio.sandbox.mixin;

import com.google.common.collect.BiMap;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(SimpleRegistry.class)
public abstract class MixinSimpleRegistry<T> extends MutableRegistry<T> implements SandboxRegistry<T> {

    @Shadow
    @Final
    protected BiMap<Identifier, T> entries;

    @Shadow
    @Final
    protected Int2ObjectBiMap<T> indexedEntries;

    @Shadow
    protected Object[] randomEntries;

    @Shadow
    @Nullable
    public abstract T get(Identifier identifier_1);

    @Shadow
    public abstract <V extends T> V add(Identifier identifier_1, V object_1);

    @Override
    //TODO: Figure out a way to completely remove
    public T remove(Identifier identifier) {
        Log.info("Removing " + identifier);
        randomEntries = null;
        return entries.remove(identifier);
    }

    @Override
    public void register(Identifier identifier, T object) {
        add(identifier, object);
        if (object instanceof BlockItem) {
            ((BlockItem) object).appendBlocks(Item.BLOCK_ITEMS, (BlockItem) object);
        }
        if (object instanceof Block) {
            ((Block) object).getStateFactory().getStates().forEach(Block.STATE_IDS::add);
        }
    }
}