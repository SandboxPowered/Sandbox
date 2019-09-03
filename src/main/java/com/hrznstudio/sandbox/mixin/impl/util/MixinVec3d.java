package com.hrznstudio.sandbox.mixin.impl.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.*;

@Mixin(Vec3d.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.util.math.Vec3d.class, prefix = "sbx$"))
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

    @Shadow
    public abstract Vec3d normalize();

    public double sbx$getX() {
        return this.getX();
    }

    public double sbx$getY() {
        return this.getY();
    }

    public double sbx$getZ() {
        return this.getZ();
    }

    public com.hrznstudio.sandbox.api.util.math.Vec3d sbx$normalize() {
        return (com.hrznstudio.sandbox.api.util.math.Vec3d) normalize();
    }

    public com.hrznstudio.sandbox.api.util.math.Vec3d sbx$add(double x, double y, double z) {
        return (com.hrznstudio.sandbox.api.util.math.Vec3d) add(x, y, z);
    }
}