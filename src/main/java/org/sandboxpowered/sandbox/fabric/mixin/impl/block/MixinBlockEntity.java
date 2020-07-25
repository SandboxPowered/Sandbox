package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.spongepowered.asm.mixin.*;

import org.jetbrains.annotations.Nullable;

@Mixin(net.minecraft.block.entity.BlockEntity.class)
@Implements(@Interface(iface = BlockEntity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockEntity {
    @Shadow
    public abstract BlockPos getPos();

    @Shadow
    public abstract BlockEntityType<?> getType();

    @Shadow
    @Nullable
    public abstract net.minecraft.world.World getWorld();

    public World sbx$getWorld() {
        return (World) this.getWorld();
    }

    public Position sbx$getPosition() {
        return (Position) this.getPos();
    }

    public BlockEntity.Type<?> sbx$getType() {
        return (BlockEntity.Type<?>) this.getType();
    }
}