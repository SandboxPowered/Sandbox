package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.entity.Entity;
import com.hrznstudio.sandbox.api.entity.player.Hand;
import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3f;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.StateFactoryImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = IBlock.class, prefix = "sbx$"))
@Unique
public abstract class MixinBlock {
    @Shadow
    @Final
    protected StateFactory<Block, net.minecraft.block.BlockState> stateFactory;
    private com.hrznstudio.sandbox.api.block.state.StateFactory<IBlock, BlockState> sandboxFactory;

    @Shadow
    public abstract boolean hasBlockEntity();

    @Shadow
    public abstract void onPlaced(net.minecraft.world.World world_1, BlockPos blockPos_1, net.minecraft.block.BlockState blockState_1, @Nullable LivingEntity livingEntity_1, net.minecraft.item.ItemStack itemStack_1);

    @Shadow
    public abstract net.minecraft.item.Item asItem();

    @Shadow
    public abstract net.minecraft.block.BlockState getDefaultState();

    @Shadow
    @Deprecated
    public abstract boolean isAir(net.minecraft.block.BlockState blockState_1);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(Block.Settings settings, CallbackInfo info) {
        sandboxFactory = new StateFactoryImpl<>(this.stateFactory, b -> (IBlock) b, s -> (com.hrznstudio.sandbox.api.block.state.BlockState) s);
        ((SandboxInternal.StateFactory) this.stateFactory).setSboxFactory(sandboxFactory);
    }

    public IBlock.Properties sbx$createProperties() {
        return null;
    }

    public boolean sbx$isAir(BlockState state) {
        return this.isAir(WrappingUtil.convert(state));
    }

    public BlockState sbx$getBaseState() {
        return (BlockState) this.getDefaultState();
    }

    public InteractionResult sbx$onBlockUsed(World world, Position pos, BlockState state, Player player, Hand hand, Direction side, Vec3f hit) {
        return InteractionResult.IGNORE;
    }

    public InteractionResult sbx$onBlockClicked(World world, Position pos, BlockState state, Entity player, Direction side) {
        return InteractionResult.IGNORE;
    }

    public IItem sbx$asItem() {
        return (IItem) asItem();
    }

    public void sbx$onBlockPlaced(World world, Position position, BlockState state, Entity entity, ItemStack itemStack) {
        this.onPlaced(
                WrappingUtil.convert(world),
                WrappingUtil.convert(position),
                WrappingUtil.convert(state),
                null,
                WrappingUtil.convert(itemStack)
        );
    }

    public void sbx$onBlockDestroyed(World world, Position position, BlockState state) {

    }

    public boolean sbx$hasBlockEntity() {
        return this.hasBlockEntity();
    }

    public IBlockEntity sbx$createBlockEntity(WorldReader reader) {
        if (hasBlockEntity())
            return (IBlockEntity) ((BlockEntityProvider) this).createBlockEntity(WrappingUtil.convert(reader));
        return null;
    }
}