package org.sandboxpowered.loader.wrapper;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.loader.Wrappers;

import java.util.IdentityHashMap;

public class WrappedBlock extends net.minecraft.world.level.block.Block {
    private static final IdentityHashMap<Block, WrappedBlock> BLOCK_MAP = new IdentityHashMap<>();
    private final Block block;

    public static Properties from(Block.Settings settings) {
        Properties properties = Properties.of(Wrappers.MATERIAL.toVanilla(settings.getMaterial()));
        if (settings.doesRandomTick())
            properties.randomTicks();
        if (settings.getVelocity() > 0)
            properties.speedFactor(settings.getVelocity());
        properties.jumpFactor(settings.getJumpVelocity());
        return properties;
    }

    public WrappedBlock(Block block) {
        super(from(block.getSettings()));
        this.block = block;
    }

    public static net.minecraft.world.level.block.Block convertSandboxBlock(Block block) {
        if (block instanceof net.minecraft.world.level.block.Block)
            return (net.minecraft.world.level.block.Block) block;
        return BLOCK_MAP.computeIfAbsent(block, toWrap -> {
            if (toWrap.getSettings().hasBlockEntity()) {
                return new WithBE(toWrap);
            }
            return new WrappedBlock(toWrap);
        });
    }

    public static Block convertVanillaBlock(net.minecraft.world.level.block.Block block) {
        if (block instanceof WrappedBlock)
            return ((WrappedBlock) block).block;
        return (Block) block;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return super.isRandomlyTicking(blockState);
    }

    public Block getBlock() {
        return block;
    }

    public static class WithBE extends WrappedBlock implements EntityBlock {
        public WithBE(Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockGetter blockGetter) {
//            return Wrappers.BLOCK_ENTITY.toVanilla(getBlock().createBlockEntity(
//                    Wrappers.WORLD_READER.toSandbox(blockGetter)
//            ));
            return null;
        }
    }

}