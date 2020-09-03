package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.client.util.math.MatrixStack;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.util.math.Vec3f;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MatrixStack.class)
@Implements(@Interface(iface = org.sandboxpowered.api.util.math.MatrixStack.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinMatrixStack {
    public void sbx$push() {

    }

    public void sbx$scale(double x, double y, double z) {

    }

    public void sbx$scale(float x, float y, float z) {

    }

    public void sbx$scale(Vec3f vec) {

    }

    public void sbx$scale(Vec3d vec) {

    }

    public void sbx$translate(double x, double y, double z) {

    }

    public void sbx$translate(float x, float y, float z) {

    }

    public void sbx$translate(Vec3d vec) {

    }

    public void sbx$translate(Vec3f vec) {

    }

    public org.sandboxpowered.api.util.math.MatrixStack.Entry sbx$peek() {
        return null;
    }

    public boolean sbx$isEmpty() {
        return false;
    }

    public void sbx$pop() {

    }
}