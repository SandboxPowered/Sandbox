package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.sandboxpowered.sandbox.api.event.BlockEvent;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlockEntity extends Entity {
    public MixinFallingBlockEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FallingBlock;onLanding(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V"))
    public void onLand(FallingBlock block, World world_1, BlockPos blockPos_1, BlockState blockState_1, BlockState blockState_2) {
        EventDispatcher.publish(new BlockEvent.Fall(
                (org.sandboxpowered.sandbox.api.world.World) world_1,
                WrappingUtil.convert(blockPos_1),
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                fallDistance));
        block.onLanding(world_1, blockPos_1, blockState_1, blockState_2);
    }
}