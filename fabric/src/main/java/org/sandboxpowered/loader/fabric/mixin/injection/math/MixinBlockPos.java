package org.sandboxpowered.loader.fabric.mixin.injection.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.math.Position;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockPos.class)
@Implements(@Interface(iface = Position.class, prefix = "pos$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockPos extends Vec3i {
    public MixinBlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    @Shadow
    public abstract BlockPos immutable();

    @Shadow
    public abstract BlockPos offset(int i, int j, int k);

    public Position pos$add(int x, int y, int z) {
        return (Position) offset(x, y, z);
    }

    public Position pos$sub(int x, int y, int z) {
        return pos$add(-x, -y, -z);
    }

    public Position pos$offset(Direction direction, int amount) {
        return null;
    }

    public Position.Mutable pos$toMutable() {
        return null;
    }

    public Position pos$toImmutable() {
        return (Position) immutable();
    }
}