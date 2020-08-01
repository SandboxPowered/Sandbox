package org.sandboxpowered.example;

import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.client.GraphicsMode;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.Properties;
import org.sandboxpowered.api.state.Property;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;

import java.util.EnumMap;
import java.util.Map;

public class PipeBlock extends BaseBlock {
    public static final Shape CORE = Shape.cuboid(4, 4, 4, 12, 12, 12);
    public static final Map<Direction, Property<Boolean>> DIRECTION_PROPERTY_MAP = new EnumMap<>(Direction.class);
    public static final Property<Boolean> UP = Properties.UP;
    public static final Property<Boolean> DOWN = Properties.DOWN;
    public static final Property<Boolean> NORTH = Properties.NORTH;
    public static final Property<Boolean> EAST = Properties.EAST;
    public static final Property<Boolean> SOUTH = Properties.SOUTH;
    public static final Property<Boolean> WEST = Properties.WEST;

    static {
        DIRECTION_PROPERTY_MAP.put(Direction.UP, UP);
        DIRECTION_PROPERTY_MAP.put(Direction.DOWN, DOWN);
        DIRECTION_PROPERTY_MAP.put(Direction.NORTH, NORTH);
        DIRECTION_PROPERTY_MAP.put(Direction.EAST, EAST);
        DIRECTION_PROPERTY_MAP.put(Direction.SOUTH, SOUTH);
        DIRECTION_PROPERTY_MAP.put(Direction.WEST, WEST);
    }

    public PipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public BlockState getStateForPlacement(WorldReader reader, Position pos, PlayerEntity player, Hand hand, ItemStack stack, Direction side, Vec3d hitPos) {

        BlockState state = getBaseState();

        for (Direction direction : Direction.ALL) {
            state = state.with(DIRECTION_PROPERTY_MAP.get(direction), reader.getBlockState(pos.offset(direction)).is(this));
        }

        return state;
    }

    @Override
    public BlockState updateOnNeighborChanged(BlockState state, Direction direction, BlockState otherState, World world, Position position, Position otherPosition) {
        state = state.with(DIRECTION_PROPERTY_MAP.get(direction), world.getBlockState(otherPosition).is(this));
        return state;
    }

    @Override
    public BlockRenderLayer getRenderLayer(BlockState state, GraphicsMode mode) {
        return mode.isFancyOrBetter() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public Shape getShape(WorldReader reader, Position position, BlockState state) {
        return CORE;
    }
}