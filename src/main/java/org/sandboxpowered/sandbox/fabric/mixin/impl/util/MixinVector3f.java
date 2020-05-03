package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.client.util.math.Vector3f;
import org.sandboxpowered.api.util.math.Vec3f;
import org.spongepowered.asm.mixin.*;

@Mixin(Vector3f.class)
@Implements(@Interface(iface = Vec3f.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinVector3f {
    @Shadow
    public abstract float getX();

    @Shadow
    public abstract float getY();

    @Shadow
    public abstract float getZ();

    public float sbx$getX() {
        return this.getX();
    }

    public float sbx$getY() {
        return this.getY();
    }

    public float sbx$getZ() {
        return this.getZ();
    }
}