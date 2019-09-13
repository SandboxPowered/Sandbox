package com.hrznstudio.sandbox.mixin.impl.world;

import com.hrznstudio.sandbox.api.util.Side;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

@Mixin(World.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.world.World.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinWorld {
    @Shadow
    public abstract boolean isClient();

    public Side sbx$getSide() {
        return this.isClient() ? Side.CLIENT : Side.SERVER;
    }
}