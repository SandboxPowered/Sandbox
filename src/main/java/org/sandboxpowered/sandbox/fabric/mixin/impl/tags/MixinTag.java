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
@SuppressWarnings({"java:S100", "java:S1610"})
public interface MixinTag<C extends Content<C>> {
    @Shadow
    boolean contains(Object object);

    @Shadow
    List<Object> values();

    @Shadow
    Object getRandom(Random random);

    default boolean sbx$contains(C content) {
        return contains(WrappingUtil.convertUnsafe(content));
    }

    default Stream<C> sbx$values() {
        return values().stream().map(WrappingUtil::convertUnsafe);
    }

    default C sbx$getRandom(Random random) {
        return WrappingUtil.convertUnsafe(getRandom(random));
    }
}