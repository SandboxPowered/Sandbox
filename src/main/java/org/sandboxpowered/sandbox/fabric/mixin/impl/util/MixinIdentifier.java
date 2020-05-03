package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.util.Identifier;
import org.sandboxpowered.api.util.Identity;
import org.spongepowered.asm.mixin.*;

@Mixin(Identifier.class)
@Implements(@Interface(iface = Identity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinIdentifier {
    @Shadow
    public abstract String getNamespace();

    @Shadow
    public abstract String getPath();

    public String sbx$getNamespace() {
        return getNamespace();
    }

    public String sbx$getPath() {
        return getPath();
    }
}