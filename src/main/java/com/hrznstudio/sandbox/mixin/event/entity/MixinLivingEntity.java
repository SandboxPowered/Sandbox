package com.hrznstudio.sandbox.mixin.event.entity;

import com.hrznstudio.sandbox.api.entity.ILivingEntity;
import com.hrznstudio.sandbox.api.event.entity.LivingEvent;
import com.hrznstudio.sandbox.event.EventDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo info) {
        if ((Object) this instanceof PlayerEntity) { // Prevent the event running twice for no reason
            LivingEvent.Death event = EventDispatcher.publish(new LivingEvent.Death((ILivingEntity) this));
            if (event.isCancelled())
                info.cancel();
        }
    }
}