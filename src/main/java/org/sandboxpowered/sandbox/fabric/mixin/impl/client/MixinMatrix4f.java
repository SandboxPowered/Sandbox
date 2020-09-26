package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public class MixinMatrix4f implements SandboxInternal.MagicMatrix {
    @Shadow
    protected float a00;

    @Shadow
    protected float a11;

    @Shadow
    protected float a22;

    @Shadow
    protected float a23;

    @Shadow
    protected float a33;

    @Shadow
    protected float a30;

    @Shadow
    protected float a31;

    @Shadow
    protected float a32;

    @Shadow
    protected float a13;

    @Shadow protected float a03;

    @Override
    public void sandbox_ortho(float n, float f, float width, float height) {
        float l = -(width / 2);
        float r = (width / 2);
        float b = -(height / 2);
        float t = (height / 2);

        a00 = 2f / (r - l);
        a11 = 2f / (t - b);
        a22 = -2f / (f - n);

        a03 = -(r + l) / (r - l);
        a13 = -(t + b) / (t - b);
        a23 = -(f + n) / (f - n);

        a33 = 1;
    }
}