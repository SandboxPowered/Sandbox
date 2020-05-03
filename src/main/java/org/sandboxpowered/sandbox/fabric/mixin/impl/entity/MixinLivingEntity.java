package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import org.sandboxpowered.api.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.entity.LivingEntity.class)
@Implements(@Interface(iface = LivingEntity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinLivingEntity {
    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float float_1);

    public void sbx$setHealth(float health) {
        this.setHealth(health);
    }

    public float sbx$getHealth() {
        return this.getHealth();
    }
}