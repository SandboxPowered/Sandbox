package org.sandboxpowered.sandbox.fabric.mixin.event.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
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
//        EntityEvent.Spawn event = EventDispatcher.publish(new EntityEvent.Spawn(
//                WrappingUtil.convert(entity)
//        ));
//        if (event.isCancelled()) {
//            info.setReturnValue(false);
//        }
    }
}