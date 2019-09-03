package com.hrznstudio.sandbox.mixin.fabric.item;

import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem extends Item {
    public MixinBucketItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Shadow
    @Final
    private Fluid fluid;

    @Shadow
    protected abstract void playEmptyingSound(@Nullable PlayerEntity playerEntity_1, IWorld iWorld_1, BlockPos blockPos_1);


    @Shadow
    public abstract void onEmptied(World world_1, ItemStack itemStack_1, BlockPos blockPos_1);

    @Shadow
    protected abstract ItemStack getFilledStack(ItemStack itemStack_1, PlayerEntity playerEntity_1, Item item_1);

    @Shadow
    protected abstract ItemStack getEmptiedStack(ItemStack itemStack_1, PlayerEntity playerEntity_1);

    /**
     * @author Coded
     */
    @Overwrite
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getStackInHand(hand);
        HitResult hit = rayTrace(world, player, this.fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);
        if (hit.getType() == HitResult.Type.MISS) {
            return new TypedActionResult<>(ActionResult.PASS, heldStack);
        } else if (hit.getType() != HitResult.Type.BLOCK) {
            return new TypedActionResult<>(ActionResult.PASS, heldStack);
        } else {
            BlockHitResult blockHit = (BlockHitResult) hit;
            BlockPos pos = blockHit.getBlockPos();
            if (world.canPlayerModifyAt(player, pos) && player.canPlaceOn(pos, blockHit.getSide(), heldStack)) {
                BlockState state;
                if (this.fluid == Fluids.EMPTY) {
                    state = world.getBlockState(pos);
                    if (state.getBlock() instanceof FluidDrainable) {
                        Fluid drainedFluid = ((FluidDrainable) state.getBlock()).tryDrainFluid(world, pos, state);
                        if (drainedFluid != Fluids.EMPTY) {
                            player.incrementStat(Stats.USED.getOrCreateStat(this));
                            player.playSound(drainedFluid.matches(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack filledStack = this.getFilledStack(heldStack, player, drainedFluid.getBucketItem());
                            if (!world.isClient) {
                                Criterions.FILLED_BUCKET.handle((ServerPlayerEntity) player, new ItemStack(drainedFluid.getBucketItem()));
                            }

                            return new TypedActionResult<>(ActionResult.SUCCESS, filledStack);
                        }
                    }

                    return new TypedActionResult<>(ActionResult.FAIL, heldStack);
                } else {
                    state = world.getBlockState(pos);
                    BlockPos pos2 = state.getBlock() instanceof FluidFillable && this.fluid == Fluids.WATER && ((FluidFillable) state.getBlock()).canFillWithFluid(world, pos, state, this.fluid) ? pos : blockHit.getBlockPos().offset(blockHit.getSide());
                    if (this.placeFluid(player, world, pos2, blockHit)) {
                        this.onEmptied(world, heldStack, pos2);
                        if (player instanceof ServerPlayerEntity) {
                            Criterions.PLACED_BLOCK.handle((ServerPlayerEntity) player, pos2, heldStack);
                        }

                        player.incrementStat(Stats.USED.getOrCreateStat(this));
                        return new TypedActionResult<>(ActionResult.SUCCESS, this.getEmptiedStack(heldStack, player));
                    } else {
                        return new TypedActionResult<>(ActionResult.FAIL, heldStack);
                    }
                }
            } else {
                return new TypedActionResult<>(ActionResult.FAIL, heldStack);
            }
        }
    }

    /**
     * @author Coded
     */
    @Overwrite
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(this.fluid instanceof BaseFluid)) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos);
            Material material = state.getMaterial();
            boolean nonSolid = !material.isSolid();
            boolean replaceable = material.isReplaceable();
            if (world.isAir(pos) || nonSolid || replaceable || state.getBlock() instanceof FluidFillable && ((FluidFillable) state.getBlock()).canFillWithFluid(world, pos, state, this.fluid)) {
                if (world.dimension.doesWaterVaporize() && this.fluid.matches(FluidTags.WATER)) {
                    int posX = pos.getX();
                    int posY = pos.getY();
                    int posZ = pos.getZ();
                    world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int i = 0; i < 8; ++i) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, (double) posX + Math.random(), (double) posY + Math.random(), (double) posZ + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (state.getBlock() instanceof FluidFillable && this.fluid == Fluids.WATER) {
                    if (((FluidFillable) state.getBlock()).tryFillWithFluid(world, pos, state, ((BaseFluid) this.fluid).getStill(false))) {
                        this.playEmptyingSound(player, world, pos);
                    }
                } else {
                    if (!world.isClient && (nonSolid || replaceable) && !material.isLiquid()) {
                        world.breakBlock(pos, true);
                    }

                    this.playEmptyingSound(player, world, pos);
                    world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), 11);
                }

                return true;
            } else {
                return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
            }
        }
    }

}