package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.SpawnGroup;
import org.sandboxpowered.api.entity.EntityCategory;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnGroup.class)
@Implements(@Interface(iface = EntityCategory.class, prefix = "sbx$"))
public abstract class MixinSpawnGroup {
    @Shadow public abstract String getName();

    @Shadow public abstract int getCapacity();

    @Shadow public abstract boolean isPeaceful();

    @Shadow public abstract boolean isAnimal();

    @Shadow public abstract int getImmediateDespawnRange();

    public String sbx$getName() {
        return getName();
    }

    public int sbx$getSpawnLimit() {
        return getCapacity();
    }

    public boolean sbx$isPeaceful() {
        return isPeaceful();
    }

    public boolean sbx$isAnimal() {
        return isAnimal();
    }

    public int sbx$getRemoveRange() {
        return getImmediateDespawnRange();
    }

    public int sbx$getDespawnRange() {
        return getImmediateDespawnRange();
    }
}
