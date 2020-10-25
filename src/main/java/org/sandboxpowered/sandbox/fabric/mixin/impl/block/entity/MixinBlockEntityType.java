package org.sandboxpowered.sandbox.fabric.mixin.impl.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.util.Identity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
@Implements(@Interface(iface = BlockEntity.Type.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinBlockEntityType {
    private Identity identity;

    public Identity sbx$getIdentity() {
        return identity;
    }

    public Content<?> sbx$setIdentity(Identity identity) {
        if (this.identity != null)
            throw new UnsupportedOperationException("Cannot set identity on content with existing identity");
        this.identity = identity;
        return (BlockEntity.Type) this;
    }
}