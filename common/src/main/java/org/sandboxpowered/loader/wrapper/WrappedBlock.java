package org.sandboxpowered.loader.wrapper;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.util.ConditionalBlockEntityProvider;

import java.util.IdentityHashMap;

public class WrappedBlock extends net.minecraft.world.level.block.Block implements ConditionalBlockEntityProvider {
    private final Block block;

    public WrappedBlock(Block block) {
        super(Properties.of(Material.STONE));
        this.block = block;
    }

    private static final IdentityHashMap<Block, WrappedBlock> BLOCK_MAP = new IdentityHashMap<>();

    public static net.minecraft.world.level.block.Block convertSandboxBlock(Block block) {
        if (block instanceof net.minecraft.world.level.block.Block)
            return (net.minecraft.world.level.block.Block) block;
        return BLOCK_MAP.computeIfAbsent(block, WrappedBlock::new);
    }

    public static Block convertVanillaBlock(net.minecraft.world.level.block.Block block) {
        if (block instanceof WrappedBlock)
            return ((WrappedBlock) block).block;
        return (Block) block;
    }

    @Override
    public boolean hasBlockEntity(BlockState state) {
        return block.hasBlockEntity(Wrappers.BLOCKSTATE.toSandbox(state));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockGetter getter, BlockState state) {
        return Wrappers.BLOCK_ENTITY.toVanilla(block.createBlockEntity(
                Wrappers.WORLD_READER.toSandbox(getter),
                Wrappers.BLOCKSTATE.toSandbox(state)
        ));
    }
}
