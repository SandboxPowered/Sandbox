package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EntityType;
import org.sandboxpowered.sandbox.api.entity.Entity;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.entity.Entity.class)
@Implements(@Interface(iface = Entity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinEntity {
    
    @Shadow public abstract EntityType<?> getType();

    public Entity.Type sbx$getType() {
        return (Entity.Type)getType();
    }
}