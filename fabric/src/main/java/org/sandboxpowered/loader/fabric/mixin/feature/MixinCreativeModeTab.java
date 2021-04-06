package org.sandboxpowered.loader.fabric.mixin.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.sandboxpowered.api.events.ItemEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeTab.class)
public class MixinCreativeModeTab {
    @Shadow
    @Final
    private int id;

    @Inject(method = "fillItemList", at = @At("RETURN"))
    public void appendStacks(NonNullList<ItemStack> stacks, CallbackInfo info) {
        boolean opOnly = Minecraft.getInstance().player.hasPermissions(2);
        if (this.id == CreativeModeTab.TAB_BUILDING_BLOCKS.getId()) {
            stacks.add(new ItemStack(Items.BARRIER));
        }
        if (this.id == CreativeModeTab.TAB_DECORATIONS.getId()) {
            stacks.add(new ItemStack(Items.DRAGON_EGG));
        }
        if (opOnly) {
            if (this.id == CreativeModeTab.TAB_TRANSPORTATION.getId()) {
                stacks.add(new ItemStack(Items.COMMAND_BLOCK_MINECART));
            }
            if (this.id == CreativeModeTab.TAB_REDSTONE.getId()) {
                stacks.add(new ItemStack(Items.COMMAND_BLOCK));
            }
            if (this.id == CreativeModeTab.TAB_MISC.getId()) {
                stacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
                stacks.add(new ItemStack(Items.STRUCTURE_VOID));
                stacks.add(new ItemStack(Items.DEBUG_STICK));
            }
        }
    }
}