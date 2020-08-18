package org.sandboxpowered.sandbox.fabric.mixin.event.block;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.events.EntityEvents;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(AnvilBlock.class)
public abstract class MixinAnvilBlock extends FallingBlock {
    public MixinAnvilBlock(Settings settings) {
        super(settings);
    }

    @Inject(method = "onLanding", at = @At(value = "RETURN"))
    public void onLanding(World world, BlockPos pos, BlockState fallState, BlockState hitState, FallingBlockEntity entity, CallbackInfo ci) {
        if (EntityEvents.ANVIL_FALL.hasSubscribers()) {
            List<Entity> entities = world.getOtherEntities(null, new Box(pos, pos.add(1, 1, 1)))
                    .stream().map(WrappingUtil::convert).collect(Collectors.toList());
            org.sandboxpowered.api.world.World sbxWorld = WrappingUtil.convert(world);
            org.sandboxpowered.api.util.math.Position sbxPos = WrappingUtil.convert(pos);
            org.sandboxpowered.api.state.BlockState sbxFall = WrappingUtil.convert(fallState);
            org.sandboxpowered.api.state.BlockState sbxHit = WrappingUtil.convert(hitState);
            org.sandboxpowered.api.entity.Entity sbxEnt = WrappingUtil.convert(entity);

            EntityEvents.ANVIL_FALL.post(event -> event.onEvent(sbxWorld, sbxPos, sbxFall, sbxHit, sbxEnt, entities));
        }
    }
}