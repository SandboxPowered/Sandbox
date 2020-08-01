package org.sandboxpowered.sandbox.fabric.mixin.impl.shape;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.math.ShapeCombination;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

import java.util.Collections;
import java.util.List;

@Mixin(VoxelShape.class)
@Implements(@Interface(iface = Shape.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinVoxelShape {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract net.minecraft.util.math.Box getBoundingBox();

    @Shadow
    public abstract VoxelShape simplify();

    @Shadow
    public abstract VoxelShape offset(double d, double e, double f);

    @Shadow
    public abstract VoxelShape getFace(net.minecraft.util.math.Direction direction);

    @Shadow
    protected abstract boolean contains(double d, double e, double f);

    public Box sbx$getBoundingBox() {
        return WrappingUtil.convert(getBoundingBox());
    }

    public boolean sbx$isEmpty() {
        return isEmpty();
    }

    public Shape sbx$offset(double x, double y, double z) {
        return WrappingUtil.convert(offset(x, y, z));
    }

    public Shape sbx$simplify() {
        return WrappingUtil.convert(simplify());
    }

    public List<Box> sbx$getBoxes() {
        return Collections.emptyList();
    }

    public Shape sbx$getFace(Direction direction) {
        return WrappingUtil.convert(getFace(WrappingUtil.convert(direction)));
    }

    public boolean sbx$contains(double x, double y, double z) {
        return contains(x, y, z);
    }

    public Shape sbx$combine(Shape shape, ShapeCombination combination) {
        return WrappingUtil.convert(VoxelShapes.combine(WrappingUtil.cast(this, VoxelShape.class), WrappingUtil.convert(shape), combination::combine));
    }
}