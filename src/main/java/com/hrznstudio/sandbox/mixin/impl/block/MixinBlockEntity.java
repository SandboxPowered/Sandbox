package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(net.minecraft.block.entity.BlockEntity.class)
@Implements(@Interface(iface = IBlockEntity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
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

    public IBlockEntity.Type<?> sbx$getType() {
        return (IBlockEntity.Type<?>) this.getType();
    }
}