package org.sandboxpowered.sandbox.fabric.mixin.fabric.registry;

import com.google.common.collect.BiMap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.Log;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Mixin(SimpleRegistry.class)
public abstract class MixinSimpleRegistry<T> extends MutableRegistry<T> implements SandboxInternal.Registry {

    @Shadow
    @Final
    protected BiMap<Identifier, T> entries;

    @Shadow
    @Final
    protected Int2ObjectBiMap<T> indexedEntries;

    @Shadow
    protected Object[] randomEntries;
    protected Int2ObjectBiMap<T> storedIndex = new Int2ObjectBiMap<>(256);
    @Shadow
    private int nextId;
    private int vanillaNext;
    private boolean hasStored;
    private Set<Identifier> identifiers = new HashSet<>();

    private BasicRegistry sboxRegistry;

    @Shadow
    @Nullable
    public abstract T get(Identifier identifier_1);

    @Shadow
    public abstract <V extends T> V add(Identifier identifier_1, V object_1);

    @Override
    public void store() {
        vanillaNext = nextId;
        storedIndex.clear();
        for (int i = 0; i < vanillaNext; i++) {
            storedIndex.put(indexedEntries.get(i), i);
        }
        randomEntries = null;
        identifiers.clear();
        hasStored = true;
        Log.debug("Stored " + vanillaNext + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this));
    }

    @Inject(method = "set", at = @At(value = "HEAD"), cancellable = true)
    public <V extends T> void set(int i, Identifier identifier, V object, CallbackInfoReturnable<V> ci) {
        if (hasStored) {
            identifiers.add(identifier);
            if (object instanceof BlockItem) {
                ((BlockItem) object).appendBlocks(Item.BLOCK_ITEMS, (BlockItem) object);
            }
            if (object instanceof Block) {
                ((Block) object).getStateManager().getStates().forEach(Block.STATE_IDS::add);
                //TODO: Also need to reset the state ids
            }
        }
    }

    @Override
    public void set(BasicRegistry registry) {
        this.sboxRegistry = registry;
    }

    @Override
    public BasicRegistry get() {
        return this.sboxRegistry;
    }

    @Override
    public void reset() {
        if (nextId != vanillaNext) {
            Log.debug("Resetting " + (nextId - vanillaNext) + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this));
            sboxRegistry.clearCache();
            nextId = vanillaNext;
            indexedEntries.clear();
            for (int i = 0; i < vanillaNext; i++) {
                indexedEntries.put(storedIndex.get(i), i);
            }
            randomEntries = null;
            identifiers.forEach(id -> entries.remove(id));
        }
    }

}