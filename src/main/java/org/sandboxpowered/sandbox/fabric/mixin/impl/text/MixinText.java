package org.sandboxpowered.sandbox.fabric.mixin.impl.text;

import com.mojang.brigadier.Message;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(Text.class)
@Implements(@Interface(iface = org.sandboxpowered.api.util.text.Text.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinText extends Message {

    @Shadow
    String asString();

    default void sbx$append(org.sandboxpowered.api.util.text.Text text) {
        if (this instanceof MutableText) {
            ((MutableText) this).append(WrappingUtil.convert(text));
        }
    }

    default String sbx$asString() {
        return this.asString();
    }

    default String sbx$asFormattedString() {
        return asString();
    }
}