package org.sandboxpowered.sandbox.fabric.mixin.event.world;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.CollisionView;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.events.WorldEvents;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Mixin(BlockCollisionSpliterator.class)
public class MixinBlockCollisionSpliterator {
    @Shadow
    @Final
    private Box box;

    @Shadow
    @Final
    private CollisionView world;

    @Shadow
    @Final
    private ShapeContext context;
    @Shadow
    @Final
    private @Nullable Entity entity;
    private boolean sandbox_shouldAlterCollisionBoxes;
    private Spliterator<Shape> sandbox_shapes;

    @Inject(method = "<init>(Lnet/minecraft/world/CollisionView;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/BiPredicate;)V", at = @At("RETURN"))
    public void constructor(CallbackInfo info) {
        if (WorldEvents.ADD_COLLISION_BOXES.hasSubscribers()) {
            sandbox_shouldAlterCollisionBoxes = true;
            org.sandboxpowered.api.entity.Entity sandboxEnt = WrappingUtil.convert(this.entity);
            List<Shape> shapes = new ArrayList<>();
            sandbox_shapes = WorldEvents.ADD_COLLISION_BOXES.post((event, original) -> event.getShapes(sandboxEnt, original), shapes).spliterator();
        }
    }

    @Inject(method = "offerBlockShape", at = @At("RETURN"), cancellable = true)
    public void offerShape(Consumer<? super VoxelShape> consumer, CallbackInfoReturnable<Boolean> info) {
        if (sandbox_shouldAlterCollisionBoxes) {
            if (!info.getReturnValue()) {
                AtomicReference<Shape> ref = new AtomicReference<>();
                while (true) {
                    if (sandbox_shapes.tryAdvance(ref::set)) {
                        VoxelShape area = WrappingUtil.convert(ref.get());
                        if (!area.isEmpty() && area.getBoundingBox().intersects(this.box)) {
                            consumer.accept(area);
                            info.setReturnValue(true);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }
}