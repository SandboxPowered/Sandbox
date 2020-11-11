package org.sandboxpowered.loader.fabric.mixin.injection.hasBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.loader.util.ConditionalBlockEntityProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public abstract class MixinWorldGenRegion implements WorldGenLevel {
    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
    public void getBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> info) {
        ChunkAccess chunkAccess = this.getChunk(pos);
        BlockEntity blockEntity = chunkAccess.getBlockEntity(pos);
        if (blockEntity != null) {
            info.setReturnValue(blockEntity);
        }
        CompoundTag compoundTag = chunkAccess.getBlockEntityNbt(pos);
        BlockState blockState = chunkAccess.getBlockState(pos);
        ConditionalBlockEntityProvider provider = (ConditionalBlockEntityProvider) blockState.getBlock();
        boolean hasEntity = provider.hasBlockEntity(blockState);
        if (compoundTag != null) {
            if ("DUMMY".equals(compoundTag.getString("id"))) {
                if (hasEntity) {
                    blockEntity = provider.createBlockEntity(this.level, blockState);
                }
            } else {
                blockEntity = BlockEntity.loadStatic(blockState, compoundTag);
            }
        }
        if (blockEntity == null && hasEntity) {
            LOGGER.warn("Tried to access a block entity before it was created. {}", pos);
        }
        info.setReturnValue(blockEntity);
    }
}