package org.sandboxpowered.loader.fabric.mixin.feature;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.sandboxpowered.loader.util.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Redirect(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"))
    private MutableComponent text(MutableComponent text, Component value, int i) {
        return text.append(NumberUtil.toRomanNumeral(i));
    }
}