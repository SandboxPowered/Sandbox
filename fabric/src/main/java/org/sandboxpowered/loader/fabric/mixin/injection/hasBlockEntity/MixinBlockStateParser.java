package org.sandboxpowered.loader.fabric.mixin.injection.hasBlockEntity;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.sandboxpowered.loader.util.ConditionalBlockEntityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockStateParser.class)
public class MixinBlockStateParser {
    @Shadow
    private BlockState state;

    @Redirect(method = "hasBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;isEntityBlock()Z", ordinal = 0))
    public boolean hasBlockEntitySelfCheck(Block block) {
        return ((ConditionalBlockEntityProvider) block).hasBlockEntity(this.state);
    }

    @Redirect(method = "hasBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;isEntityBlock()Z", ordinal = 1))
    public boolean hasBlockEntityLoop(Block block) {
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            if (((ConditionalBlockEntityProvider) block).hasBlockEntity(state))
                return true;
        }
        return false;
    }
}
