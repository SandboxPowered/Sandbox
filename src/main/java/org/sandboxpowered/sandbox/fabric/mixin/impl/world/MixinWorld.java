package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(World.class)
@Implements(@Interface(iface = org.sandboxpowered.api.world.World.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinWorld {
    @Shadow
    public abstract boolean isClient();

    @Shadow
    public abstract List<net.minecraft.entity.Entity> getOtherEntities(net.minecraft.entity.@Nullable Entity entity, net.minecraft.util.math.Box box, @Nullable Predicate<? super net.minecraft.entity.Entity> predicate);

    public Side sbx$getSide() {
        return this.isClient() ? Side.CLIENT : Side.SERVER;
    }

    public Stream<Entity> sbx$getEntitiesWithin(Box box) {
        return getOtherEntities(null, WrappingUtil.convert(box), null).stream().map(WrappingUtil::convert);
    }

    public <T extends Entity> Stream<T> sbx$getEntitiesWithin(Box box, Class<T> filter) {
        return getOtherEntities(null, WrappingUtil.convert(box), null).stream().map(e -> {
            Entity it = WrappingUtil.convert(e);
            return it.getClass().isAssignableFrom(filter) ? filter.cast(it) : null;
        }).filter(Objects::nonNull);
    }
}