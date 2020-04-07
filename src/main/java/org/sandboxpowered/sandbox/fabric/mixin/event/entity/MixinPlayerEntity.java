package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends net.minecraft.entity.LivingEntity {

    public MixinPlayerEntity(EntityType<? extends net.minecraft.entity.LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    /**
     * @author B0undarybreaker
     */
    @Inject(method = "getArrowType", at = @At("RETURN"))
    public void getModifiedArrowType(ItemStack weapon, CallbackInfoReturnable<ItemStack> info) {
//        ItemEvent.GetArrowType event = EventDispatcher.publish(new ItemEvent.GetArrowType(
//                WrappingUtil.cast(weapon, org.sandboxpowered.sandbox.api.item.ItemStack.class),
//                WrappingUtil.cast(info.getReturnValue(), org.sandboxpowered.sandbox.api.item.ItemStack.class)
//        ));
//        info.setReturnValue(WrappingUtil.convert(event.getArrow()));
    }
}
