package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import net.minecraft.util.registry.Registry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.item.Item.class)
@Implements(@Interface(iface = Item.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinItem {
    private Identity identity;

    public Identity sbx$getIdentity() {
        if (this instanceof SandboxInternal.IItemWrapper) {
            return ((SandboxInternal.IItemWrapper) this).getSandboxItem().getIdentity();
        }
        if (this.identity == null)
            this.identity = WrappingUtil.convert(Registry.ITEM.getId(WrappingUtil.cast(this, net.minecraft.item.Item.class)));
        return identity;
    }

    public Content<?> sbx$setIdentity(Identity identity) {
        if (this instanceof SandboxInternal.IItemWrapper) {
            return ((SandboxInternal.IItemWrapper) this).getSandboxItem().setIdentity(identity);
        }
        throw new UnsupportedOperationException("Cannot set identity on content with existing identity");
    }
}