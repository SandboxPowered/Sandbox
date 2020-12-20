package org.sandboxpowered.loader.fabric.mixin.injection.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.spongepowered.asm.mixin.*;

@Mixin(Level.class)
@Implements({@Interface(iface = WorldReader.class, prefix = "reader$", remap = Interface.Remap.NONE), @Interface(iface = World.class, prefix = "world$", remap = Interface.Remap.NONE)})
@Unique
public abstract class MixinLevel {
    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();
}