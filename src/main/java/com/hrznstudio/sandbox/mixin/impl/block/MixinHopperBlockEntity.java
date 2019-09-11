package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.api.component.Components;
import com.hrznstudio.sandbox.api.component.Inventory;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.state.BlockState;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.Mono;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.block.entity.HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends BlockEntity {
    public MixinHopperBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    public void insert(CallbackInfoReturnable<Boolean> info) {
        World world = (World) this.getWorld();
        Position position = (Position) getPos();
        if (world == null) {
            info.setReturnValue(false);
            return;
        }
        BlockState state = (BlockState) getCachedState();
        Direction direction = WrappingUtil.convert(getCachedState().get(HopperBlock.FACING));
        Inventory hopperInventory = state.getComponent(world, position, Components.INVENTORY_COMPONENT).orElse(null);
        if (hopperInventory != null) {
            Position offset = position.offset(direction);
            BlockState other = world.getBlockState(offset);
            Mono<Inventory> mono = other.getComponent(world, offset, Components.INVENTORY_COMPONENT, direction.getOppositeDirection());
            if (mono.isPresent()) {
                Inventory inputInv = mono.get();
                for (int slot : hopperInventory) {
                    ItemStack input = hopperInventory.extract(slot, 1, true);
                    if (!input.isEmpty()) {
                        if (inputInv.insert(input, true).isEmpty()) {
                            inputInv.insert(hopperInventory.extract(slot, 1));
                            info.setReturnValue(true);
                            return;
                        }
                    }
                }
            }
        }
        info.setReturnValue(false);
    }

    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;)Z", at = @At("HEAD"), cancellable = true)
    private static void extract(Hopper hopper, CallbackInfoReturnable<Boolean> info) {
        World world = (World) hopper.getWorld();
        if (world == null) {
            info.setReturnValue(false);
            return;
        }
        Position position = Position.create((int) Math.floor(hopper.getHopperX()), (int) Math.floor(hopper.getHopperY()), (int) Math.floor(hopper.getHopperZ()));
        Position abovePos = position.up();
        BlockState aboveState = world.getBlockState(abovePos);
        Mono<Inventory> mono = aboveState.getComponent(world, abovePos, Components.INVENTORY_COMPONENT, Direction.DOWN);
        if (mono.isPresent()) {
            Inventory inventory = mono.get();
            if (inventory.isEmpty()) {
                info.setReturnValue(false);
                return;
            }

            for (int slot : inventory) {
                ItemStack stack = inventory.extract(slot, 1, true);
                if (!stack.isEmpty()) {
                    for (int hopperSlot = 0; hopperSlot < hopper.getInvSize(); hopperSlot++) {
                        if (hopper.isValidInvStack(hopperSlot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class))) {
                            stack = inventory.extract(slot, 1);
                            net.minecraft.item.ItemStack hopperStack = hopper.getInvStack(hopperSlot);
                            if (hopperStack.isEmpty()) {
                                hopper.setInvStack(hopperSlot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class));
                                info.setReturnValue(true);
                                return;
                            } else if (hopper.getInvMaxStackAmount() >= hopperStack.getCount() + stack.getCount()) {
                                net.minecraft.item.ItemStack copy = hopperStack.copy();
                                copy.increment(stack.getCount());
                                hopper.setInvStack(hopperSlot, copy);
                                info.setReturnValue(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
        info.setReturnValue(false);
    }
}