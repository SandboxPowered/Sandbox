package org.sandboxpowered.example;

import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.client.GraphicsMode;
import org.sandboxpowered.api.component.Components;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.Properties;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;

import java.util.IdentityHashMap;
import java.util.Map;

public class PipeBlock extends BaseBlock {
    public static final Shape CORE = Shape.cuboid(4, 4, 4, 12, 12, 12);
    public static final Property<Boolean> UP = Properties.UP;
    public static final Property<Boolean> DOWN = Properties.DOWN;
    public static final Property<Boolean> NORTH = Properties.NORTH;
    public static final Property<Boolean> EAST = Properties.EAST;
    public static final Property<Boolean> SOUTH = Properties.SOUTH;
    public static final Property<Boolean> WEST = Properties.WEST;
    public static final Shape CENTER_SHAPE;
    protected static final Property<Boolean>[] CONNECTION_PROPERTIES = new Property[Direction.ALL.length];
    private static final Shape[] SIDE_SHAPES = new Shape[Direction.ALL.length];

    static {
        CENTER_SHAPE = Shape.cuboid(4, 4, 4, 12, 12, 12);
        CONNECTION_PROPERTIES[Direction.UP.getId()] = UP;
        CONNECTION_PROPERTIES[Direction.DOWN.getId()] = DOWN;
        CONNECTION_PROPERTIES[Direction.NORTH.getId()] = NORTH;
        CONNECTION_PROPERTIES[Direction.EAST.getId()] = EAST;
        CONNECTION_PROPERTIES[Direction.SOUTH.getId()] = SOUTH;
        CONNECTION_PROPERTIES[Direction.WEST.getId()] = WEST;

        SIDE_SHAPES[Direction.UP.getId()] = Shape.cuboid(4, 12, 4, 12, 16, 12);
        SIDE_SHAPES[Direction.DOWN.getId()] = Shape.cuboid(4, 0, 4, 12, 4, 12);
        SIDE_SHAPES[Direction.NORTH.getId()] = Shape.cuboid(4, 4, 0, 12, 12, 4);
        SIDE_SHAPES[Direction.EAST.getId()] = Shape.cuboid(12, 4, 4, 16, 12, 12);
        SIDE_SHAPES[Direction.SOUTH.getId()] = Shape.cuboid(4, 4, 12, 12, 12, 16);
        SIDE_SHAPES[Direction.WEST.getId()] = Shape.cuboid(0, 4, 4, 4, 12, 12);
    }

    private final Map<BlockState, Shape> shapeCache = new IdentityHashMap<>();

    public PipeBlock(Settings settings) {
        super(settings);
    }

    private static Shape computeShape(BlockState state) {
        Shape shape = CORE;
        for (Direction direction : Direction.ALL) {
            if (state.get(CONNECTION_PROPERTIES[direction.getId()])) {
                shape = shape.combine(SIDE_SHAPES[direction.getId()]).simplify();
            }
        }
        return shape;
    }

    @Override
    public boolean hasBlockEntity() {
        return true;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(WorldReader reader) {
        return new PipeBlockEntity(Example.pipeEntityType);
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
            Position offsetPos = pos.offset(direction);
            state = state.with(CONNECTION_PROPERTIES[direction.getId()], canConnectTo(reader, offsetPos, reader.getBlockState(offsetPos), direction));
        }
        return state;
    }

    public boolean canConnectTo(WorldReader reader, Position pos, BlockState state, Direction dir) {
        return state.getBlock() instanceof PipeBlock || state.getComponent(reader, pos, Components.INVENTORY_COMPONENT, dir.getOppositeDirection()).isPresent();
    }

    @Override
    public BlockState updateOnNeighborChanged(BlockState state, Direction direction, BlockState otherState, World world, Position position, Position otherPosition) {
        return state.with(CONNECTION_PROPERTIES[direction.getId()], canConnectTo(world, otherPosition, otherState, direction));
    }

    @Override
    public BlockRenderLayer getRenderLayer(BlockState state, GraphicsMode mode) {
        return mode.isFancyOrBetter() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public Shape getShape(WorldReader reader, Position position, BlockState state) {
        return shapeCache.computeIfAbsent(state, PipeBlock::computeShape);
    }
}