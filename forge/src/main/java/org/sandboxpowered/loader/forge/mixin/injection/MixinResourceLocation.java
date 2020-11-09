package org.sandboxpowered.loader.forge.mixin.injection;

import net.minecraft.util.ResourceLocation;
import org.sandboxpowered.api.util.Identity;
import org.spongepowered.asm.mixin.*;

@Mixin(ResourceLocation.class)
@Implements(@Interface(iface = Identity.class, prefix = "identity$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinResourceLocation {
    @Shadow
    @Final
    protected String path;

    @Shadow
    @Final
    protected String namespace;

    public String identity$getNamespace() {
        return namespace;
    }

    public String identity$getPath() {
        return path;
    }
}