package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.api.events.args.ArrowTypeArgs;
import org.sandboxpowered.sandbox.fabric.impl.event.ArrowTypeArgsImpl;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
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
     * @author Coded
     */
    @Inject(method = "getArrowType", at = @At("RETURN"), cancellable = true)
    public void getModifiedArrowType(ItemStack weapon, CallbackInfoReturnable<ItemStack> info) {
        ArrowTypeArgs args = new ArrowTypeArgsImpl(WrappingUtil.convert(weapon), WrappingUtil.convert(info.getReturnValue()));
        ItemEvents.GET_ARROW_TYPE.accept(WrappingUtil.convert((PlayerEntity) (Object) this), args);
        info.setReturnValue(WrappingUtil.convert(args.getArrow()));
    }
}
