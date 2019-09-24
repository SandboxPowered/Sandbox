package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import org.sandboxpowered.sandbox.api.entity.LivingEntity;
import org.sandboxpowered.sandbox.api.event.entity.LivingEvent;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.entity.LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo info) {
        if ((Object) this instanceof PlayerEntity) { // Prevent the event running twice for no reason
            LivingEvent.Death event = EventDispatcher.publish(new LivingEvent.Death((LivingEntity) this));
            if (event.isCancelled())
                info.cancel();
        }
    }
}