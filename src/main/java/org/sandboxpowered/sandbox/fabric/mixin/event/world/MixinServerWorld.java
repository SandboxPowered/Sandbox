package org.sandboxpowered.sandbox.fabric.mixin.event.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.sandboxpowered.api.events.EntityEvents;
import org.sandboxpowered.eventhandler.Cancellable;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
    @Inject(method = "addEntity",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void addEntity(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (EntityEvents.SPAWN.hasSubscribers()) {
            org.sandboxpowered.api.entity.Entity ent = WrappingUtil.convert(entity);
            Cancellable cancellable = new Cancellable();

//            EntityEvents.SPAWN.post(event -> event.onEvent(ent), cancellable);

            if (cancellable.isCancelled())
                info.setReturnValue(false);
        }
    }
}