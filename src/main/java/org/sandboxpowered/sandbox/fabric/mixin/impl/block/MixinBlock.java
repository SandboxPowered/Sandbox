package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.FluidLoggable;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.component.Components;
import org.sandboxpowered.api.component.fluid.FluidLoggingContainer;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.Items;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.Mono;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.SidedRespective;
import org.sandboxpowered.sandbox.fabric.util.wrapper.StateFactoryImpl;
import org.sandboxpowered.sandbox.fabric.util.wrapper.V2SInventory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlock extends AbstractBlock implements SandboxInternal.StateFactoryHolder {
    @Shadow @Final protected StateManager<net.minecraft.block.Block, net.minecraft.block.BlockState> stateManager;

    @Shadow public abstract net.minecraft.block.BlockState getDefaultState();

    @Shadow public abstract void onPlaced(net.minecraft.world.World world, BlockPos blockPos, net.minecraft.block.BlockState blockState, @Nullable LivingEntity livingEntity, net.minecraft.item.ItemStack itemStack);

    @Shadow public abstract void onBroken(IWorld iWorld, BlockPos blockPos, net.minecraft.block.BlockState blockState);

    private StateFactory<Block, BlockState> sandboxFactory;
    private Block.Settings sbxSettings;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(net.minecraft.block.Block.Settings settings, CallbackInfo info) {
        sandboxFactory = new StateFactoryImpl<>(this.stateManager, b -> (Block) b, s -> (BlockState) s);
        ((SandboxInternal.StateFactory) this.stateManager).setSboxFactory(sandboxFactory);
    }

    public MixinBlock(Settings settings) {
        super(settings);
    }

    public Block.Settings sbx$getSettings() {
        if (sbxSettings == null) {
            sbxSettings = Block.Settings.builder(WrappingUtil.convert(material))
                    .setHardness(0)
                    .setLuminance(0)
                    .setResistance(resistance)
                    .setSlipperiness(slipperiness)
                    .setJumpVelocity(jumpVelocityMultiplier)
                    .setVelocity(velocityMultiplier)
                    .build();
        }
        return sbxSettings;
    }

    @Override
    public StateFactory getSandboxStateFactory() {
        return sandboxFactory;
    }
//
//    public boolean sbx$isAir(BlockState state) {
//        return this.isAir(WrappingUtil.convert(state));
//    }

    public BlockState sbx$getBaseState() {
        return (BlockState) this.getDefaultState();
    }

    public InteractionResult sbx$onBlockUsed(World world, Position pos, BlockState state, PlayerEntity player, Hand hand, Direction side, Vec3f hit) {
        return InteractionResult.IGNORE;
    }

    public InteractionResult sbx$onBlockClicked(World world, Position pos, BlockState state, PlayerEntity player) {
        return InteractionResult.IGNORE;
    }

    public Optional<Item> sbx$asItem() {
        Item item = (Item) asItem();
        if (Items.AIR.matches(item))
            return Optional.empty();
        return Optional.of(item);
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

    public void sbx$onBlockBroken(World world, Position position, BlockState state) {
        this.onBroken((net.minecraft.world.World) world, (BlockPos) position, (net.minecraft.block.BlockState) state);
    }

    public boolean sbx$hasBlockEntity() {
        return this.hasBlockEntity();
    }

    public <X> Mono<X> sbx$getComponent(WorldReader reader, Position position, BlockState state, Component<X> component, @Nullable Direction side) {
        if (component == Components.INVENTORY_COMPONENT) {
            if (this instanceof InventoryProvider) {
                SidedInventory inventory = ((InventoryProvider) this).getInventory((net.minecraft.block.BlockState) state, (IWorld) reader, (BlockPos) position);
                if (side != null)
                    return Mono.of(new SidedRespective(inventory, side)).cast();
                return Mono.of(new V2SInventory(inventory)).cast();
            }
            net.minecraft.block.entity.BlockEntity entity = ((BlockView) reader).getBlockEntity((BlockPos) position);
            if (entity instanceof Inventory) {
                if (side != null && entity instanceof SidedInventory)
                    return Mono.of(new SidedRespective((SidedInventory) entity, side)).cast();
                return Mono.of(new V2SInventory((Inventory) entity)).cast();
            }
        }
        if (component == Components.FLUID_COMPONENT) {
            if (this instanceof Waterloggable) {
                FluidLoggingContainer container = new FluidLoggingContainer((FluidLoggable) this, reader, position, state, side);
                return Mono.of(container).cast();
            }
        }
        return Mono.empty();
    }

    @Nullable
    public BlockEntity sbx$createBlockEntity(WorldReader reader) {
        if (hasBlockEntity())
            return (BlockEntity) ((BlockEntityProvider) this).createBlockEntity(WrappingUtil.convert(reader));
        return null;
    }
}