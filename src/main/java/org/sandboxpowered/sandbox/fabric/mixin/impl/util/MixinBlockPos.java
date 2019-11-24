package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.sandboxpowered.sandbox.api.util.Direction;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.util.math.Position.Mutable;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockPos.class)
@Implements(@Interface(iface = Position.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockPos extends Vec3i {
    public MixinBlockPos() {
        super(0, 0, 0);
    }

    public Mutable sbx$toMutable() {
        return (Mutable) new BlockPos.Mutable((BlockPos) (Object) this);
    }

    public Position sbx$toImmutable() {
        return (Position) this;
    }

    public Position sbx$offset(Direction direction, int amount) {
        return (Position) this.offset(WrappingUtil.convert(direction), amount);
    }

    @Mixin(BlockPos.Mutable.class)
    @Implements(@Interface(iface = Position.Mutable.class, prefix = "sbx$"))
    @Unique
    public static abstract class MixinMutable extends BlockPos {
        public MixinMutable() {
            super(0, 0, 0);
        }

        @Shadow
        public abstract Mutable set(int int_1, int int_2, int int_3);

        public Position.Mutable sbx$toMutable() {
            return (Position.Mutable) this;
        }

        public Position sbx$toImmutable() {
            return (Position) new BlockPos(this);
        }

        public Position.Mutable sbx$set(int x, int y, int z) {
            return (Position.Mutable) this.set(x, y, z);
        }
    }
}