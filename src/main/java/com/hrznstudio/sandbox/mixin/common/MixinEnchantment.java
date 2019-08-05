package com.hrznstudio.sandbox.mixin.common;

import com.hrznstudio.sandbox.SandboxConfig;
import com.hrznstudio.sandbox.util.NumberUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Shadow
    public abstract boolean isCursed();

    @Shadow
    public abstract String getTranslationKey();

    @Shadow
    public abstract int getMaximumLevel();

    /**
     * @return Enchantment Text
     * @author Coded
     * @reason Larger than 10 enchantment levels
     */
    @Overwrite
    public Text getName(int level) {
        Text text_1 = new TranslatableText(this.getTranslationKey());
        if (this.isCursed()) {
            text_1.formatted(Formatting.RED);
        } else {
            text_1.formatted(Formatting.GRAY);
        }

        if (level != 1 || this.getMaximumLevel() != 1) {
            text_1.append(" ");

            if (SandboxConfig.enchantmentDecimal.get()) {
                text_1.append(new LiteralText(Integer.toString(level)));
            } else {
                text_1.append(new LiteralText(NumberUtil.toRoman(level)));
            }
        }

        return text_1;
    }
}