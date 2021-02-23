package org.sandboxpowered.loader.mixin.injection;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.sandboxpowered.api.item.tool.ToolItem;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentCategory.class)
public class MixinEnchantmentCategory {

    @Mixin(targets = "net.minecraft.world.item.enchantment.EnchantmentCategory$7")
    public static class MixinDiggerCategory {
        @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
        public void canEnchant(Item item, CallbackInfoReturnable<Boolean> info) {
            org.sandboxpowered.api.item.Item sandbox = Wrappers.ITEM.toSandbox(item);
            if (sandbox instanceof ToolItem)
                info.setReturnValue(true);
        }
    }
}
