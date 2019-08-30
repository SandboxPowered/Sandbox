package com.hrznstudio.sandbox.mixin.impl.util;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.*;

@Mixin(Vector3f.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.util.math.Vec3f.class, prefix = "sbx$"))
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