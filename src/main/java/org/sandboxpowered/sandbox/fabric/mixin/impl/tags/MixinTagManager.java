package org.sandboxpowered.sandbox.fabric.mixin.impl.tags;

import net.minecraft.entity.EntityType;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.entity.Entity.Type;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.tags.TagGroup;
import org.sandboxpowered.api.tags.TagManager;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.tag.TagManager.class)
@Implements(@Interface(iface = TagManager.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinTagManager {
    @Shadow public abstract net.minecraft.tag.TagGroup<net.minecraft.block.Block> getBlocks();

    @Shadow public abstract net.minecraft.tag.TagGroup<net.minecraft.item.Item> getItems();

    @Shadow public abstract net.minecraft.tag.TagGroup<net.minecraft.fluid.Fluid> getFluids();

    @Shadow public abstract net.minecraft.tag.TagGroup<EntityType<?>> getEntityTypes();

    public TagGroup<Block> sbx$getBlocks() {
        return (TagGroup<Block>) getBlocks();
    }
    public TagGroup<Item> sbx$getItems() {
        return (TagGroup<Item>) getItems();
    }
    public TagGroup<Fluid> sbx$getFluids() {
        return (TagGroup<Fluid>) getFluids();
    }

    public TagGroup<Type> sbx$getEntities() {
        return (TagGroup<Type>) getEntityTypes();
    }
}
