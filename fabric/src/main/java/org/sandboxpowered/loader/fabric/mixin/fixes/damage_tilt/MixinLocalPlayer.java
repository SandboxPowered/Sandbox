package org.sandboxpowered.loader.fabric.mixin.fixes.damage_tilt;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {
    public MixinLocalPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile, float hurtDir) {
        super(level, blockPos, f, gameProfile);
        this.hurtDir = hurtDir;
    }

    @Inject(method = "hurtTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", shift = At.Shift.BEFORE))
    public void hurt(float f, CallbackInfo info) {
        DamageSource source = getLastDamageSource();
        if (source == null)
            return;
        Entity entity2 = source.getEntity();
        if (entity2 != null) {
            double i = entity2.getX() - this.getX();

            double j;
            for (j = entity2.getZ() - this.getZ(); i * i + j * j < 1.0E-4D; j = (Math.random() - Math.random()) * 0.01D) {
                i = (Math.random() - Math.random()) * 0.01D;
            }

            this.hurtDir = (float) (Mth.atan2(j, i) * 57.2957763671875D - (double) this.yRot);
        } else {
            this.hurtDir = (float) ((int) (Math.random() * 2.0D) * 180);
        }
    }
}