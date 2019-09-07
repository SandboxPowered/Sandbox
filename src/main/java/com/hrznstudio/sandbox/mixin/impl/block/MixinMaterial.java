package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.api.block.Material;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.piston.PistonBehavior;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.block.Material.class)
@Implements(@Interface(iface = Material.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinMaterial {

    @Shadow
    public abstract PistonBehavior getPistonBehavior();

    @Shadow
    public abstract boolean blocksMovement();

    @Shadow
    public abstract boolean isBurnable();

    @Shadow
    public abstract boolean isLiquid();

    @Shadow
    public abstract boolean blocksLight();

    @Shadow
    public abstract boolean isReplaceable();

    @Shadow
    public abstract boolean isSolid();

    public Material.PistonInteraction sbx$getPistonInteraction() {
        return WrappingUtil.convert(getPistonBehavior());
    }

    public boolean sbx$doesBlockMovement() {
        return blocksMovement();
    }

    public boolean sbx$isBurnable() {
        return isBurnable();
    }

    public boolean sbx$isBreakByHand() {
        return sbx$isBreakByHand();
    }

    public boolean sbx$isLiquid() {
        return isLiquid();
    }

    public boolean sbx$doesBlockLight() {
        return blocksLight();
    }

    public boolean sbx$isReplaceable() {
        return isReplaceable();
    }

    public boolean sbx$isSolid() {
        return isSolid();
    }
}