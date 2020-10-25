package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EntityType;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.util.Identity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.class)
public class MixinEntityType implements Entity.Type {
    private Identity identity;

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public Entity.Type setIdentity(Identity identity) {
        if (this.identity != null)
            throw new UnsupportedOperationException("Cannot set identity on content with existing identity");
        this.identity = identity;
        return this;
    }
}
