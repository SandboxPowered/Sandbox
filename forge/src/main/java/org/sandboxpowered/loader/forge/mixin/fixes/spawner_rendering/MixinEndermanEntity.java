package org.sandboxpowered.loader.forge.mixin.fixes.spawner_rendering;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class MixinEndermanEntity extends MonsterEntity implements IAngerable {
    public MixinEndermanEntity(EntityType<? extends MonsterEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Fixes <a href="https://bugs.mojang.com/browse/MC-189565">MC-189565</a>
     */
    @Inject(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/EndermanEntity;setCarriedBlock(Lnet/minecraft/block/BlockState;)V", shift = At.Shift.AFTER), cancellable = true)
    private void worldCheckAngerFromTag(CompoundNBT tag, CallbackInfo ci) {
        if (!this.level.isClientSide) {
            this.readPersistentAngerSaveData((ServerWorld) level, tag);
        }

        ci.cancel();
    }
}