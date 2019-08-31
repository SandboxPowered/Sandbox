package com.hrznstudio.sandbox.mixin.impl.nbt;

import com.hrznstudio.sandbox.api.util.nbt.Tag;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.nbt.Tag.class)
@Implements(@Interface(iface = Tag.class, prefix = "sbx$"))
@Unique
public interface MixinTag {
    @Shadow String asString();

    default String sbx$asString() {
        return asString();
    }
}
