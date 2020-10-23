package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends net.minecraft.entity.LivingEntity {
    public MixinServerPlayerEntity(EntityType<? extends net.minecraft.entity.LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo info) {
//        LivingEvent.Death event = EventDispatcher.publish(new LivingEvent.Death((LivingEntity) this));
//        if (event.isCancelled())
//            info.cancel();
    }
}
