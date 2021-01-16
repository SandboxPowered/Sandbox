package org.sandboxpowered.loader.mixin.fixes.spawner_rendering;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class MixinBee extends Animal implements NeutralMob {
    public MixinBee(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Fixes <a href="https://bugs.mojang.com/browse/MC-189565">MC-189565</a>
     */
    @Inject(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;numCropsGrownSincePollination:I", shift = At.Shift.AFTER), cancellable = true)
    private void worldCheckAngerFromTag(CompoundTag tag, CallbackInfo ci) {
        if (!this.level.isClientSide) {
            this.readPersistentAngerSaveData((ServerLevel) level, tag);
        }

        ci.cancel();
    }
}
