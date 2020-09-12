package org.sandboxpowered.sandbox.fabric.mixin.fixes;

import net.minecraft.entity.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {

    @Shadow
    public abstract void equipStack(EquipmentSlot equipmentSlot, ItemStack itemStack);

    private MixinMobEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new UnsupportedOperationException("Mixin not transformed");
    }

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void dropEquipment(DamageSource damageSource, int i, boolean bl, CallbackInfo ci) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            this.equipStack(equipmentSlot, ItemStack.EMPTY);
        }
    }
}