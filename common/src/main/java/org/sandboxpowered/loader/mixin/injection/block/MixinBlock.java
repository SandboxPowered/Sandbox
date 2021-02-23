package org.sandboxpowered.loader.mixin.injection.block;

import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.tool.ToolType;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.wrapper.IVanillaBlock;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collections;
import java.util.List;

@Mixin(net.minecraft.world.level.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "block$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlock extends BlockBehaviour implements IVanillaBlock {
    private int sandbox_harvestLevel = -1;
    private ToolType sandbox_toolType;

    public MixinBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    public List<ItemStack> block$getDrops(@NotNull World world, @NotNull Position position, @NotNull BlockState state) {
        if (!(world instanceof ServerLevel))
            return Collections.emptyList();
        return (List<ItemStack>) (Object) net.minecraft.world.level.block.Block.getDrops(
                Wrappers.BLOCKSTATE.toVanilla(state),
                (ServerLevel) world,
                Wrappers.POSITION.toVanilla(position),
                null
        );
    }

    public @NotNull Identity block$getIdentity() {
        return Wrappers.IDENTITY.toSandbox( Registry.BLOCK.getKey((net.minecraft.world.level.block.Block) (Object) this));
    }


    public int block$getHarvestLevel(@NotNull BlockState state) {
        return sandbox_harvestLevel;
    }

    public ToolType block$getHarvestTool(@NotNull BlockState state) {
        return sandbox_toolType;
    }

    @Override
    public void sandbox_setHarvestParams(@NotNull ToolType type, int harvestLevel) {
        sandbox_toolType = type;
        sandbox_harvestLevel = harvestLevel;
    }

    public boolean block$doesRequireCorrectToolForDrops(@NotNull BlockState state) {
        return Wrappers.BLOCKSTATE.toVanilla(state).requiresCorrectToolForDrops();
    }
}