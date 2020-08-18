package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

    @Shadow
    @Final
    public net.minecraft.entity.player.PlayerEntity player;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMiningSpeedMultiplier(Lnet/minecraft/block/BlockState;)F"), method = "getBlockBreakingSpeed")
    public float getMiningSpeedMultiplier(ItemStack originalStack, BlockState originalState) {
        float miningSpeed = originalStack.getMiningSpeedMultiplier(originalState);
        if (ItemEvents.MINING_SPEED.hasSubscribers()) {
            org.sandboxpowered.api.item.ItemStack stack = WrappingUtil.convert(originalStack);
            org.sandboxpowered.api.state.BlockState state = WrappingUtil.convert(originalState);
            PlayerEntity player = WrappingUtil.convert(this.player);
            miningSpeed = ItemEvents.MINING_SPEED.post((event, speed) -> event.onEvent(player, stack, state, speed), miningSpeed);
        }
        return miningSpeed;
    }
}