package org.sandboxpowered.sandbox.fabric.mixin.impl.enchant;

import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.enchantment.Enchantment.class)
@Implements(@Interface(iface = Enchantment.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinEnchantment {

    @Shadow
    public abstract boolean isCursed();

    @Shadow
    public abstract String getTranslationKey();

    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack itemStack_1);

    @Shadow
    public abstract boolean isTreasure();

    @Shadow
    public abstract int getMinLevel();

    @Shadow
    public abstract int getMaxLevel();

    public int sbx$getMinimumLevel() {
        return this.getMinLevel();
    }

    public int sbx$getMaximumLevel() {
        return this.getMaxLevel();
    }

    public boolean sbx$isAcceptableItem(ItemStack stack) {
        return this.isAcceptableItem(WrappingUtil.convert(stack));
    }

    public boolean sbx$isCurse() {
        return this.isCursed();
    }

    public boolean sbx$isTreasure() {
        return this.isTreasure();
    }
}