package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.*;

@Mixin(Vec3i.class)
@Implements(@Interface(iface = org.sandboxpowered.api.util.math.Vec3i.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinVec3i {
    @Shadow
    public abstract int getX();

    @Shadow
    public abstract int getY();

    @Shadow
    public abstract int getZ();

    public int sbx$getX() {
        return this.getX();
    }

    public int sbx$getY() {
        return this.getY();
    }

    public int sbx$getZ() {
        return this.getZ();
    }
}