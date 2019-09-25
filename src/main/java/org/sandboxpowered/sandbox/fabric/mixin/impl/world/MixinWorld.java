package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.world.World;
import org.sandboxpowered.sandbox.api.util.Side;
import org.spongepowered.asm.mixin.*;

@Mixin(World.class)
@Implements(@Interface(iface = org.sandboxpowered.sandbox.api.world.World.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinWorld {
    @Shadow
    public abstract boolean isClient();

    public Side sbx$getSide() {
        return this.isClient() ? Side.CLIENT : Side.SERVER;
    }
}