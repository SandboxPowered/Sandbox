package org.sandboxpowered.sandbox.fabric.mixin.impl.nbt;

import org.sandboxpowered.sandbox.api.util.nbt.Tag;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.nbt.Tag.class)
@Implements(@Interface(iface = Tag.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinTag {
    @Shadow
    String asString();

    default String sbx$asString() {
        return asString();
    }
}
