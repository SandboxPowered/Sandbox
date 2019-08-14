package com.hrznstudio.sandbox.mixin.common.block;

import com.hrznstudio.sandbox.api.block.Block;
import com.hrznstudio.sandbox.api.block.entity.BlockEntity;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.entity.Entity;
import com.hrznstudio.sandbox.api.entity.player.Hand;
import com.hrznstudio.sandbox.api.util.Activation;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3f;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "sbx$"))
@Unique
public abstract class MixinBlock implements Block {

    @Shadow
    public abstract boolean hasBlockEntity();

    @Override
    public Properties createProperties() {
        return null;
    }

    @Override
    public Activation onBlockUsed(World world, Position pos, BlockState state, Entity player, Hand hand, Direction side, Vec3f hit) {
        return Activation.IGNORE;
    }

    @Override
    public Activation onBlockClicked(World world, Position pos, BlockState state, Entity player, Direction side) {
        return Activation.IGNORE;
    }

    @Override
    public void onBlockPlaced(World world, Position position, BlockState state) {

    }

    @Override
    public void onBlockDestroyed(World world, Position position, BlockState state) {

    }

    public boolean sbx$hasBlockEntity() {
        return this.hasBlockEntity();
    }

    @Override
    public BlockEntity createBlockEntity(WorldReader reader) {
        if (hasBlockEntity())
            return (BlockEntity) ((BlockEntityProvider) this).createBlockEntity(WrappingUtil.convert(reader));
        return null;
    }
}