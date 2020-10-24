package org.sandboxpowered.sandbox.fabric.mixin.fabric.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.sandboxpowered.sandbox.fabric.util.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Redirect(method = "getName(I)Lnet/minecraft/text/Text;", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;"))
    private MutableText text(MutableText text, Text value, int i) {
        return text.append(SandboxConfig.enchantmentDecimal.getBoolean() ? String.valueOf(i) : NumberUtil.toRoman(i));
    }
}