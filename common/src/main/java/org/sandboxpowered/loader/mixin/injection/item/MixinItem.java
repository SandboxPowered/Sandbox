package org.sandboxpowered.loader.mixin.injection.item;

import net.minecraft.core.Registry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.world.item.Item.class)
@Implements(@Interface(iface = Item.class, prefix = "item$", remap = Interface.Remap.NONE))
@Unique
public class MixinItem implements Content<Item> {
    @Override
    public Class<Item> getContentType() {
        return Item.class;
    }

    @Override
    public Identity getIdentity() {
        return Wrappers.IDENTITY.toSandbox(Registry.ITEM.getKey(net.minecraft.world.item.Item.class.cast(this)));
    }

    @Override
    public Item setIdentity(Identity identity) {
        throw new RuntimeException();
    }
}