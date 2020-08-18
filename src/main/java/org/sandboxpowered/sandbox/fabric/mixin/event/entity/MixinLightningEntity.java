package org.sandboxpowered.sandbox.fabric.mixin.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.World;
import org.sandboxpowered.api.events.WorldEvents;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(LightningEntity.class)
public abstract class MixinLightningEntity extends Entity {
    public MixinLightningEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    private boolean attemptedEvent;

    @Inject(method = "tick", at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",
            ordinal = 0, shift = At.Shift.BY, by = 1
    ), locals = LocalCapture.CAPTURE_FAILHARD)
    public void handleLightning(CallbackInfo ci, double d0, List<Entity> list) {
        if (attemptedEvent) return;

        if (WorldEvents.LIGHTNING_STRIKE.hasSubscribers()) {
            org.sandboxpowered.api.world.World world = WrappingUtil.convert(getEntityWorld());
            Vec3d pos = WrappingUtil.convert(getPos());
            List<org.sandboxpowered.api.entity.Entity> sbxList = list.stream().map(WrappingUtil::convert).collect(Collectors.toList());
            WorldEvents.LIGHTNING_STRIKE.post(event -> event.onEvent(world, pos, sbxList));
        }

        attemptedEvent = true;
    }
}