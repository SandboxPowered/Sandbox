package org.sandboxpowered.loader.fabric.mixin.injection.block;

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

    org.sandboxpowered.api.block.Material.PistonInteraction mat$getPistonInteraction() {
        return org.sandboxpowered.api.block.Material.PistonInteraction.IGNORE;
    }

    boolean mat$doesBlockMovement() {
        return blocksMotion();
    }

    boolean mat$isBurnable() {
        return isFlammable();
    }


    //TODO figure out which method this is supposed to be
    boolean mat$isBreakByHand() {
        return false;
    }

    boolean mat$isLiquid() {
        return this.isLiquid();
    }

    boolean mat$doesBlockLight() {
        return this.isSolidBlocking();
    }

    boolean mat$isReplaceable() {
        return this.isReplaceable();
    }

    boolean mat$isSolid() {
        return this.isSolid();
    }
}
