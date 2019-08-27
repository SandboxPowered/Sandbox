package com.hrznstudio.sandbox.mixin.impl.entity;

import com.hrznstudio.sandbox.api.entity.ILivingEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;

@Mixin(LivingEntity.class)
@Implements(@Interface(iface = ILivingEntity.class, prefix = "sbx$"))
@Unique
public abstract class MixinLivingEntity {
    @Shadow
    public abstract void setHealth(float float_1);

    @Shadow
    public abstract float getHealth();

    public void sbx$setHealth(float health) {
        this.setHealth(health);
    }

    public float sbx$getHealth() {
        return this.getHealth();
    }
}