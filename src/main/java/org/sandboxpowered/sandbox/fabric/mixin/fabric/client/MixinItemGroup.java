package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemGroup.class)
public class MixinItemGroup {

    @Shadow
    @Final
    private int index;

    @Inject(method = "appendStacks", at = @At("RETURN"))
    public void appendStacks(DefaultedList<ItemStack> stacks, CallbackInfo info) {
        boolean opOnly = MinecraftClient.getInstance().player.hasPermissionLevel(2);
        if (this.index == ItemGroup.BUILDING_BLOCKS.getIndex()) {
            stacks.add(new ItemStack(Items.BARRIER));
        }
        if (this.index == ItemGroup.DECORATIONS.getIndex()) {
            stacks.add(new ItemStack(Items.DRAGON_EGG));
        }
        if (opOnly) {
            if (this.index == ItemGroup.TRANSPORTATION.getIndex()) {
                stacks.add(new ItemStack(Items.COMMAND_BLOCK_MINECART));
            }
            if (this.index == ItemGroup.REDSTONE.getIndex()) {
                stacks.add(new ItemStack(Items.COMMAND_BLOCK));
            }
            if (this.index == ItemGroup.MISC.getIndex()) {
                stacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
                stacks.add(new ItemStack(Items.STRUCTURE_VOID));
                stacks.add(new ItemStack(Items.DEBUG_STICK));
            }
        }
    }
}