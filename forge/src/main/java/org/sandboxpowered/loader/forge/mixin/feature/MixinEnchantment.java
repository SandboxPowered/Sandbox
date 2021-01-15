package org.sandboxpowered.loader.forge.mixin.feature;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.sandboxpowered.loader.util.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Redirect(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/text/IFormattableTextComponent;append(Lnet/minecraft/util/text/ITextComponent;)Lnet/minecraft/util/text/IFormattableTextComponent;"))
    private IFormattableTextComponent text(IFormattableTextComponent text, ITextComponent value, int i) {
        return text.append(NumberUtil.toRomanNumeral(i));
    }
}