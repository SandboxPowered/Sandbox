package com.hrznstudio.sandbox.fabric.mixin;

import com.google.common.collect.BiMap;
import com.hrznstudio.sandbox.fabric.api.SandboxRegistry;
import com.hrznstudio.sandbox.fabric.util.Log;
import net.minecraft.util.Identifier;
import net.minecraft.util.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleRegistry.class)
public abstract class MixinSimpleRegistry extends MutableRegistry implements SandboxRegistry {
    @Shadow
    @Final
    protected BiMap<Identifier, Object> entries;

    @Shadow
    @Final
    protected Int2ObjectBiMap<Object> indexedEntries;

    @Shadow
    protected Object[] randomEntries;

    @Override
    //TODO: Figure out a way to completely remove
    public boolean remove(Identifier identifier) {
        Log.info("Removing " + identifier);
        entries.remove(identifier);
        randomEntries = null;
        return true;
    }
}