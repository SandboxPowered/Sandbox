package org.sandboxpowered.loader.forge.mixin.fixes.spawner_rendering;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class MixinBeeEntity extends AnimalEntity implements IAngerable {
    public MixinBeeEntity(EntityType<? extends AnimalEntity> p_i48568_1_, World p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    /**
     * Fixes <a href="https://bugs.mojang.com/browse/MC-189565">MC-189565</a>
     */
    @Inject(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/BeeEntity;numCropsGrownSincePollination:I", shift = At.Shift.AFTER), cancellable = true)
    private void worldCheckAngerFromTag(CompoundNBT tag, CallbackInfo ci) {
        if (!this.level.isClientSide) {
            this.readPersistentAngerSaveData((ServerWorld) level, tag);
        }

        ci.cancel();
    }
}
