package org.sandboxpowered.sandbox.fabric.mixin.fabric.registry;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
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
    private final Set<RegistryKey<T>> keys = new HashSet<>();
    @Shadow
    @Final
    protected Int2ObjectBiMap<T> indexedEntries;
    @Shadow
    protected Object[] randomEntries;
    protected Int2ObjectBiMap<T> storedIndex = new Int2ObjectBiMap<>(256);
    @Shadow
    @Final
    protected BiMap<Identifier, T> entriesById;
    @Shadow
    private int nextId;
    private int vanillaNext;
    private boolean hasStored;
    private BasicRegistry sboxRegistry;
    @Shadow
    @Final
    private BiMap<RegistryKey<T>, T> entriesByKey;

    public MixinSimpleRegistry(RegistryKey<Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    @Shadow
    @Nullable
    public abstract T get(Identifier identifier_1);

    @Override
    public void store() {
        vanillaNext = nextId;
        storedIndex.clear();
        for (int i = 0; i < vanillaNext; i++) {
            storedIndex.put(indexedEntries.get(i), i);
        }
        randomEntries = null;
        keys.clear();
        hasStored = true;
//        Log.debug("Stored " + vanillaNext + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this)); TODO
    }


    @Inject(method = "set", at = @At(value = "HEAD"), cancellable = true)
    public <V extends T> void set(int i, RegistryKey<T> registryKey, V object, CallbackInfoReturnable<V> ci) {
        if (hasStored) {
            keys.add(registryKey);
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
//            Log.debug("Resetting " + (nextId - vanillaNext) + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this));
            sboxRegistry.clearCache();
            nextId = vanillaNext;
            indexedEntries.clear();
            for (int i = 0; i < vanillaNext; i++) {
                indexedEntries.put(storedIndex.get(i), i);
            }
            randomEntries = null;
            keys.forEach(id -> {
                entriesById.remove(id.getValue());
                entriesByKey.remove(id);
            });
        }
    }
}