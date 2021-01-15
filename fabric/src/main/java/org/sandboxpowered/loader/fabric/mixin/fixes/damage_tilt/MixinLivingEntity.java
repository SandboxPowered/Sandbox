package org.sandboxpowered.loader.fabric.mixin.fixes.damage_tilt;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow
    public float hurtDir;

    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> info) {
        Entity entity2 = damageSource.getEntity();

        if((Object)this instanceof Player) {
            System.out.println(level.isClientSide);
            if (entity2 != null) {
                double i = entity2.getX() - this.getX();

                double j;
                for (j = entity2.getZ() - this.getZ(); i * i + j * j < 1.0E-4D; j = (Math.random() - Math.random()) * 0.01D) {
                    i = (Math.random() - Math.random()) * 0.01D;
                }

                this.hurtDir = (float) (Mth.atan2(j, i) * 57.2957763671875D - (double) this.yRot);
            } else {
                this.hurtDir = (float) ((int) (Math.random() * 2.0D) * 180);
            }
        }
    }
}