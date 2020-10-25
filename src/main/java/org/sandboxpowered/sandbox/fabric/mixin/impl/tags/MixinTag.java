package org.sandboxpowered.sandbox.fabric.mixin.impl.tags;

import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.tags.Tag;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Mixin(net.minecraft.tag.Tag.class)
@Implements(@Interface(iface = Tag.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinTag<C extends Content<C>> {
    @Shadow
    public abstract boolean contains(Object object);

    @Shadow
    public abstract List<Object> values();

    @Shadow
    public abstract Object getRandom(Random random);

    public boolean sbx$contains(C content) {
        return contains(WrappingUtil.convert(content));
    }

    public Stream<C> sbx$values() {
        return values().stream().map(WrappingUtil::convert);
    }

    public C sbx$getRandom(Random random) {
        return WrappingUtil.convert(getRandom(random));
    }
}