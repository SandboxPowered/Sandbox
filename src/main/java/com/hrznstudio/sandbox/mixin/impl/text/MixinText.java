package com.hrznstudio.sandbox.mixin.impl.text;

import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

@Mixin(Text.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.util.text.Text.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinText {

    @Shadow
    Text append(Text var1);

    @Shadow
    String asFormattedString();

    @Shadow
    String asString();

    default void sbx$append(com.hrznstudio.sandbox.api.util.text.Text text) {
        this.append(WrappingUtil.convert(text));
    }

    default String sbx$asString() {
        return this.asString();
    }

    default String sbx$asFormattedString() {
        return this.asFormattedString();
    }
}