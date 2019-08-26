package com.hrznstudio.sandbox.mixin.impl.enchant;

import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.*;

@Mixin(Enchantment.class)
@Implements(@Interface(iface = IEnchantment.class, prefix = "sbx$"))
@Unique
public abstract class MixinEnchantment {

    @Shadow
    public abstract boolean isCursed();

    @Shadow
    public abstract String getTranslationKey();

    @Shadow
    public abstract int getMaximumLevel();

    @Shadow
    public abstract int getMinimumLevel();

    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack itemStack_1);

    @Shadow
    public abstract boolean isTreasure();

    public int sbx$getMinimumLevel() {
        return this.getMinimumLevel();
    }

    public int sbx$getMaximumLevel() {
        return this.getMaximumLevel();
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