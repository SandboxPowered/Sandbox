package org.sandboxpowered.loader.mixin.injection.math;

import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.*;

@Mixin(Vec3i.class)
@Implements(@Interface(iface = org.sandboxpowered.api.util.math.Vec3i.class, prefix = "vec$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinVec3i {
    @Shadow public abstract int getX();

    @Shadow public abstract int getY();

    @Shadow public abstract int getZ();

    public int pos$getX() {
        return getX();
    }

    public int pos$getY() {
        return getY();
    }

    public int pos$getZ() {
        return getZ();
    }
}
