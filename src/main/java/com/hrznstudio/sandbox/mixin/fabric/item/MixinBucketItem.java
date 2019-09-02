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
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        HitResult hitResult_1 = rayTrace(world_1, playerEntity_1, this.fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);
        if (hitResult_1.getType() == HitResult.Type.MISS) {
            return new TypedActionResult<>(ActionResult.PASS, itemStack_1);
        } else if (hitResult_1.getType() != HitResult.Type.BLOCK) {
            return new TypedActionResult<>(ActionResult.PASS, itemStack_1);
        } else {
            BlockHitResult blockHitResult_1 = (BlockHitResult) hitResult_1;
            BlockPos blockPos_1 = blockHitResult_1.getBlockPos();
            if (world_1.canPlayerModifyAt(playerEntity_1, blockPos_1) && playerEntity_1.canPlaceOn(blockPos_1, blockHitResult_1.getSide(), itemStack_1)) {
                BlockState blockState_1;
                if (this.fluid == Fluids.EMPTY) {
                    blockState_1 = world_1.getBlockState(blockPos_1);
                    if (blockState_1.getBlock() instanceof FluidDrainable) {
                        Fluid fluid_1 = ((FluidDrainable) blockState_1.getBlock()).tryDrainFluid(world_1, blockPos_1, blockState_1);
                        if (fluid_1 != Fluids.EMPTY) {
                            playerEntity_1.incrementStat(Stats.USED.getOrCreateStat(this));
                            playerEntity_1.playSound(fluid_1.matches(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack itemStack_2 = this.getFilledStack(itemStack_1, playerEntity_1, fluid_1.getBucketItem());
                            if (!world_1.isClient) {
                                Criterions.FILLED_BUCKET.handle((ServerPlayerEntity) playerEntity_1, new ItemStack(fluid_1.getBucketItem()));
                            }

                            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack_2);
                        }
                    }

                    return new TypedActionResult<>(ActionResult.FAIL, itemStack_1);
                } else {
                    blockState_1 = world_1.getBlockState(blockPos_1);
                    BlockPos blockPos_2 = blockState_1.getBlock() instanceof FluidFillable && ((FluidFillable) blockState_1.getBlock()).canFillWithFluid(world_1, blockPos_1, blockState_1, this.fluid) ? blockPos_1 : blockHitResult_1.getBlockPos().offset(blockHitResult_1.getSide());
                    if (this.placeFluid(playerEntity_1, world_1, blockPos_2, blockHitResult_1)) {
                        this.onEmptied(world_1, itemStack_1, blockPos_2);
                        if (playerEntity_1 instanceof ServerPlayerEntity) {
                            Criterions.PLACED_BLOCK.handle((ServerPlayerEntity) playerEntity_1, blockPos_2, itemStack_1);
                        }

                        playerEntity_1.incrementStat(Stats.USED.getOrCreateStat(this));
                        return new TypedActionResult<>(ActionResult.SUCCESS, this.getEmptiedStack(itemStack_1, playerEntity_1));
                    } else {
                        return new TypedActionResult<>(ActionResult.FAIL, itemStack_1);
                    }
                }
            } else {
                return new TypedActionResult<>(ActionResult.FAIL, itemStack_1);
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
                } else if (state.getBlock() instanceof FluidFillable) {
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