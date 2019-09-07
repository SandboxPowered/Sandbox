package com.hrznstudio.sandbox.mixin.fabric.enchant;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    /**
     * @author B0undarybreaker
     */
    @ModifyVariable(method = "getHighestApplicableEnchantmentsAtPower", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;"))
    private static List<InfoEnchantment> getEnchantmentList(List<InfoEnchantment> original) {
        return Lists.newLinkedList();
    }
}