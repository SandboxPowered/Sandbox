package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.*;

@Mixin(Vec3d.class)
@Implements(@Interface(iface = org.sandboxpowered.api.util.math.Vec3d.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinVec3d {
    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract Vec3d add(double x, double y, double z);

    public double sbx$getX() {
        return this.getX();
    }

    public double sbx$getY() {
        return this.getY();
    }

    public double sbx$getZ() {
        return this.getZ();
    }
}