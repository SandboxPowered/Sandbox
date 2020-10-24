package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends net.minecraft.entity.LivingEntity {

    public MixinPlayerEntity(EntityType<? extends net.minecraft.entity.LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author Coded
     */
    @Inject(method = "getArrowType", at = @At("RETURN"), cancellable = true)
    public void getModifiedArrowType(ItemStack weapon, CallbackInfoReturnable<ItemStack> info) {
        if (ItemEvents.GET_ARROW_TYPE.hasSubscribers()) {
            org.sandboxpowered.api.entity.player.PlayerEntity player = WrappingUtil.convert((PlayerEntity) (Object) this);
            org.sandboxpowered.api.item.ItemStack sWeapon = WrappingUtil.convert(weapon);
            org.sandboxpowered.api.item.ItemStack originalArrow = WrappingUtil.convert(weapon);
            org.sandboxpowered.api.item.ItemStack s = ItemEvents.GET_ARROW_TYPE.post(
                    (event, value) -> event.onEvent(player, sWeapon, value),
                    originalArrow
            );
            if (!s.isEmpty())
                info.setReturnValue(WrappingUtil.convert(s));
        }
    }
}