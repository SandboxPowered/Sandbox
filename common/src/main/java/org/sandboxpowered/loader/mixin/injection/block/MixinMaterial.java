package org.sandboxpowered.loader.mixin.injection.block;

import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.*;

@Mixin(Material.class)
@Implements(@Interface(iface = org.sandboxpowered.api.block.Material.class, prefix = "mat$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinMaterial {

    @Shadow public abstract boolean blocksMotion();

    @Shadow public abstract boolean isFlammable();

    @Shadow public abstract boolean isLiquid();

    @Shadow public abstract boolean isSolidBlocking();

    @Shadow public abstract boolean isReplaceable();

    @Shadow public abstract boolean isSolid();

    public org.sandboxpowered.api.block.Material.PistonInteraction mat$getPistonInteraction() {
        return org.sandboxpowered.api.block.Material.PistonInteraction.IGNORE;
    }

    public boolean mat$doesBlockMovement() {
        return blocksMotion();
    }

    public boolean mat$isBurnable() {
        return isFlammable();
    }

    //TODO figure out which method this is supposed to be
    public boolean mat$isBreakByHand() {
        return false;
    }

    public boolean mat$isLiquid() {
        return this.isLiquid();
    }

    public boolean mat$doesBlockLight() {
        return this.isSolidBlocking();
    }

    public boolean mat$isReplaceable() {
        return this.isReplaceable();
    }

    public boolean mat$isSolid() {
        return this.isSolid();
    }
}
