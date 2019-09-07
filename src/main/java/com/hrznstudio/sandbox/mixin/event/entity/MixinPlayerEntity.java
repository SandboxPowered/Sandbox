package com.hrznstudio.sandbox.mixin.event.entity;

import com.hrznstudio.sandbox.api.entity.ILivingEntity;
import com.hrznstudio.sandbox.api.event.ItemEvent;
import com.hrznstudio.sandbox.api.event.entity.LivingEvent;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    public MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo info) {
        LivingEvent.Death event = EventDispatcher.publish(new LivingEvent.Death((ILivingEntity) this));
        if (event.isCancelled())
            info.cancel();
    }

    /**
     * @author B0undarybreaker
     */
    @Inject(method = "getArrowType", at = @At("RETURN"))
    public void getModifiedArrowType(ItemStack weapon, CallbackInfoReturnable<ItemStack> info) {
        ItemEvent.GetArrowType event = EventDispatcher.publish(new ItemEvent.GetArrowType(
                WrappingUtil.cast(weapon, com.hrznstudio.sandbox.api.item.ItemStack.class),
                WrappingUtil.cast(info.getReturnValue(), com.hrznstudio.sandbox.api.item.ItemStack.class)
        ));
        info.setReturnValue(WrappingUtil.convert(event.getArrow()));
    }
}
