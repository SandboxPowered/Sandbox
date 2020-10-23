package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EntityType;
import org.sandboxpowered.api.entity.Entity;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.entity.Entity.class)
@Implements(@Interface(iface = Entity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinEntity {

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract boolean isSneaking();

    public Entity.Type sbx$getType() {
        return (Entity.Type) getType();
    }

    public boolean sbx$isSneaking() {
        return isSneaking();
    }
}