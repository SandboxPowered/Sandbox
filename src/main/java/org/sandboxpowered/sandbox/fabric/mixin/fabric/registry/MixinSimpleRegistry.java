package org.sandboxpowered.sandbox.fabric.mixin.fabric.registry;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.RegistryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(SimpleRegistry.class)
public abstract class MixinSimpleRegistry<T, C extends Content<C>> extends MutableRegistry<T> implements SandboxInternal.Registry<C, T> {
    private final Set<RegistryKey<T>> keys = new HashSet<>();

    @Shadow
    protected Object[] randomEntries;
    protected Int2ObjectBiMap<T> storedIndex = new Int2ObjectBiMap<>(256);
    @Shadow
    private int nextId;
    private int vanillaNext;
    private boolean hasStored;
    private BasicRegistry<C, T> sboxRegistry;
    @Shadow
    @Final
    private ObjectList<T> rawIdToEntry;
    @Shadow
    @Final
    private BiMap<Identifier, T> idToEntry;
    @Shadow
    @Final
    private BiMap<RegistryKey<T>, T> keyToEntry;

    public MixinSimpleRegistry(RegistryKey<Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    @Shadow
    @Nullable
    public abstract T get(Identifier id);

    @Override
    public void sandboxStore() {
        vanillaNext = nextId;
        storedIndex.clear();
        for (int i = 0; i < vanillaNext; i++) {
            storedIndex.put(this.rawIdToEntry.get(i), i);
        }
        randomEntries = null;
        keys.clear();
        hasStored = true;
//        Log.debug("Stored " + vanillaNext + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this)); TODO
    }


    @Inject(method = "set(ILnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;Z)Ljava/lang/Object;", at = @At(value = "RETURN"))
    public <V extends T> void set(int i, RegistryKey<T> registryKey, V object, Lifecycle lifecycle, boolean bl, CallbackInfoReturnable<V> cir) {
        if (hasStored) {
            keys.add(registryKey);
            RegistryUtil.doOnSet(object);
        }
    }

    @Override
    public void sandboxSet(BasicRegistry<C, T> registry) {
        this.sboxRegistry = registry;
    }

    @Override
    public BasicRegistry<C, T> sandboxGet() {
        return this.sboxRegistry;
    }

    @Override
    public void sandboxReset() {
        if (nextId != vanillaNext) {
//            Log.debug("Resetting " + (nextId - vanillaNext) + " objects in " + net.minecraft.util.registry.Registry.REGISTRIES.getId(this));
            sboxRegistry.clearCache();
            nextId = vanillaNext;
            this.rawIdToEntry.clear();
            for (int i = 0; i < vanillaNext; i++) {
                this.rawIdToEntry.set(i, storedIndex.get(i));
            }
            randomEntries = null;
            keys.forEach(id -> {
                idToEntry.remove(id.getValue());
                keyToEntry.remove(id);
            });
        }
    }
}