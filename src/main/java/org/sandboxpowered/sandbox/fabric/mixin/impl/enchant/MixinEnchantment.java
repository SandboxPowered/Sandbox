package org.sandboxpowered.sandbox.fabric.mixin.impl.enchant;

import net.minecraft.util.registry.Registry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.enchantment.Enchantment.class)
@Implements(@Interface(iface = Enchantment.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100", "java:S1610"})
public abstract class MixinEnchantment {

    private Identity identity;

    @Shadow
    public abstract boolean isCursed();

    @Shadow
    public abstract String getTranslationKey();

    @Shadow
    public abstract boolean isAcceptableItem(net.minecraft.item.ItemStack stack);

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

    public Identity sbx$getIdentity() {
        if (this instanceof SandboxInternal.IEnchantmentWrapper) {
            return ((SandboxInternal.IEnchantmentWrapper) this).getSandboxEnchantment().getIdentity();
        }
        if (this.identity == null)
            this.identity = WrappingUtil.convert(Registry.FLUID.getId(WrappingUtil.cast(this, net.minecraft.fluid.Fluid.class)));
        return identity;
    }

    public Content<?> sbx$setIdentity(Identity identity) {
        if (this instanceof SandboxInternal.IEnchantmentWrapper) {
            return ((SandboxInternal.IEnchantmentWrapper) this).getSandboxEnchantment().setIdentity(identity);
        }
        throw new UnsupportedOperationException("Cannot set identity on content with existing identity");
    }
}