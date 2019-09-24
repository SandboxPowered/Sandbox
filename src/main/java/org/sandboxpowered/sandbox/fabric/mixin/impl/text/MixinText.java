package org.sandboxpowered.sandbox.fabric.mixin.impl.text;

import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

@Mixin(Text.class)
@Implements(@Interface(iface = org.sandboxpowered.sandbox.api.util.text.Text.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinText {

    @Shadow
    Text append(Text var1);

    @Shadow
    String asFormattedString();

    @Shadow
    String asString();

    default void sbx$append(org.sandboxpowered.sandbox.api.util.text.Text text) {
        this.append(WrappingUtil.convert(text));
    }

    default String sbx$asString() {
        return this.asString();
    }

    default String sbx$asFormattedString() {
        return this.asFormattedString();
    }
}