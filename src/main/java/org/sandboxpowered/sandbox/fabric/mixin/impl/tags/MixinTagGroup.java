package org.sandboxpowered.sandbox.fabric.mixin.impl.tags;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.tags.Tag;
import org.sandboxpowered.api.tags.TagGroup;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.tag.TagGroup.class)
@Implements(@Interface(iface = TagGroup.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinTagGroup<C extends Content<C>> {
    @Shadow
    @Nullable
    public abstract net.minecraft.tag.@Nullable Tag<Object> getTag(Identifier identifier);

    @Shadow
    public abstract net.minecraft.tag.Tag<Object> getTagOrEmpty(Identifier identifier);

    @Shadow
    public abstract Identifier getTagId(net.minecraft.tag.Tag<Object> tag);

    @Nullable
    public Tag<C> sbx$getTag(Identity id) {
        return (Tag<C>) getTag(WrappingUtil.convert(id));
    }

    public Tag<C> sbx$getTagOrEmpty(Identity id) {
        return (Tag<C>) getTagOrEmpty(WrappingUtil.convert(id));
    }

    public Identity sbx$getTagId(Tag<C> tag) {
        return WrappingUtil.convert(getTagId((net.minecraft.tag.Tag) tag));
    }
}
