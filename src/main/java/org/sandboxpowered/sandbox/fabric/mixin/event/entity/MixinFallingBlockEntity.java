package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FallingBlockEntity.class)
@SuppressWarnings({"java:S100", "java:S1610"})
public abstract class MixinFallingBlockEntity extends Entity {
    public MixinFallingBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FallingBlock;onLanding(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V"))
//    public void onLand(FallingBlock block, World world_1, BlockPos pos, BlockState state, BlockState blockState_2) {
////        EventDispatcher.publish(new BlockEvent.Fall(
////                (org.sandboxpowered.api.world.World) world_1,
////                WrappingUtil.convert(pos),
////                (org.sandboxpowered.api.state.BlockState) state,
////                fallDistance));
//        block.onLanding(world_1, pos, state, blockState_2);
//    }
}