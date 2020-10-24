package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.state.property.Properties;
import org.sandboxpowered.api.state.Property;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.SlabType;
import org.sandboxpowered.sandbox.fabric.util.wrapper.EnumPropertyWrapper;

public class PropertyUtil {
    public static final EnumPropertyWrapper<Direction, net.minecraft.util.math.Direction> FACING = new EnumPropertyWrapper<>(
            Properties.FACING,
            WrappingUtil::convert,
            WrappingUtil::convert,
            Direction.class
    );
    public static final EnumPropertyWrapper<Direction, net.minecraft.util.math.Direction> HORIZONTAL = new EnumPropertyWrapper<>(
            Properties.HORIZONTAL_FACING,
            WrappingUtil::convert,
            WrappingUtil::convert,
            Direction.class
    );
    public static final EnumPropertyWrapper<Direction, net.minecraft.util.math.Direction> HOPPER_FACING = new EnumPropertyWrapper<>(
            Properties.HOPPER_FACING,
            WrappingUtil::convert,
            WrappingUtil::convert,
            Direction.class
    );
    public static final EnumPropertyWrapper<Direction.Axis, net.minecraft.util.math.Direction.Axis> AXIS = new EnumPropertyWrapper<>(
            Properties.AXIS,
            WrappingUtil::convert,
            WrappingUtil::convert,
            Direction.Axis.class
    );
    public static final EnumPropertyWrapper<Direction.Axis, net.minecraft.util.math.Direction.Axis> HORIZONTAL_AXIS = new EnumPropertyWrapper<>(
            Properties.HORIZONTAL_AXIS,
            WrappingUtil::convert,
            WrappingUtil::convert,
            Direction.Axis.class
    );
    public static final EnumPropertyWrapper<SlabType, net.minecraft.block.enums.SlabType> SLAB_TYPE = new EnumPropertyWrapper<>(
            Properties.SLAB_TYPE,
            WrappingUtil::convert,
            WrappingUtil::convert,
            SlabType.class
    );
    private PropertyUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Property<T> get(String s) {
        switch (s) {
            case "attached":
                return (Property<T>) Properties.ATTACHED;
            case "bottom":
                return (Property<T>) Properties.BOTTOM;
            case "conditional":
                return (Property<T>) Properties.CONDITIONAL;
            case "disarmed":
                return (Property<T>) Properties.DISARMED;
            case "drag":
                return (Property<T>) Properties.DRAG;
            case "enabled":
                return (Property<T>) Properties.ENABLED;
            case "extended":
                return (Property<T>) Properties.EXTENDED;
            case "eye":
                return (Property<T>) Properties.EYE;
            case "falling":
                return (Property<T>) Properties.FALLING;
            case "hanging":
                return (Property<T>) Properties.HANGING;
            case "has_bottle_0":
                return (Property<T>) Properties.HAS_BOTTLE_0;
            case "has_bottle_1":
                return (Property<T>) Properties.HAS_BOTTLE_1;
            case "has_bottle_2":
                return (Property<T>) Properties.HAS_BOTTLE_2;
            case "has_record":
                return (Property<T>) Properties.HAS_RECORD;
            case "has_book":
                return (Property<T>) Properties.HAS_BOOK;
            case "inverted":
                return (Property<T>) Properties.INVERTED;
            case "in_wall":
                return (Property<T>) Properties.IN_WALL;
            case "lit":
                return (Property<T>) Properties.LIT;
            case "locked":
                return (Property<T>) Properties.LOCKED;
            case "occupied":
                return (Property<T>) Properties.OCCUPIED;
            case "open":
                return (Property<T>) Properties.OPEN;
            case "persistent":
                return (Property<T>) Properties.PERSISTENT;
            case "powered":
                return (Property<T>) Properties.POWERED;
            case "short":
                return (Property<T>) Properties.SHORT;
            case "signal_fire":
                return (Property<T>) Properties.SIGNAL_FIRE;
            case "snowy":
                return (Property<T>) Properties.SNOWY;
            case "triggered":
                return (Property<T>) Properties.TRIGGERED;
            case "unstable":
                return (Property<T>) Properties.UNSTABLE;
            case "waterlogged":
                return (Property<T>) Properties.WATERLOGGED;
            case "fluidlevel":
                return (Property<T>) Properties.LEVEL_1_8;
            case "level_15":
                return (Property<T>) Properties.LEVEL_15;
            case "up":
                return (Property<T>) Properties.UP;
            case "down":
                return (Property<T>) Properties.DOWN;
            case "east":
                return (Property<T>) Properties.EAST;
            case "west":
                return (Property<T>) Properties.WEST;
            case "north":
                return (Property<T>) Properties.NORTH;
            case "south":
                return (Property<T>) Properties.SOUTH;
            case "age_1":
                return (Property<T>) Properties.AGE_1;
            case "age_2":
                return (Property<T>) Properties.AGE_2;
            case "age_3":
                return (Property<T>) Properties.AGE_3;
            case "age_5":
                return (Property<T>) Properties.AGE_5;
            case "age_7":
                return (Property<T>) Properties.AGE_7;
            case "age_15":
                return (Property<T>) Properties.AGE_15;
            case "age_25":
                return (Property<T>) Properties.AGE_25;
            case "bites":
                return (Property<T>) Properties.BITES;
            case "delay":
                return (Property<T>) Properties.DELAY;
            case "distance_1_7":
                return (Property<T>) Properties.DISTANCE_1_7;
            case "eggs":
                return (Property<T>) Properties.EGGS;
            case "hatch":
                return (Property<T>) Properties.HATCH;
            case "layers":
                return (Property<T>) Properties.LAYERS;
            case "level_3":
                return (Property<T>) Properties.LEVEL_3;
            case "level_8":
                return (Property<T>) Properties.LEVEL_8;
            case "level_1_8":
                return (Property<T>) Properties.LEVEL_1_8;
            case "honey_level":
                return (Property<T>) Properties.HONEY_LEVEL;
            case "moisture":
                return (Property<T>) Properties.MOISTURE;
            case "note":
                return (Property<T>) Properties.NOTE;
            case "pickles":
                return (Property<T>) Properties.PICKLES;
            case "power":
                return (Property<T>) Properties.POWER;
            case "stage":
                return (Property<T>) Properties.STAGE;
            case "distance_0_7":
                return (Property<T>) Properties.DISTANCE_0_7;
            case "rotation":
                return (Property<T>) Properties.ROTATION;
            case "facing":
                return (Property<T>) FACING;
            case "horizontal_facing":
                return (Property<T>) HORIZONTAL;
            case "hopper_facing":
                return (Property<T>) HOPPER_FACING;
            case "axis":
                return (Property<T>) AXIS;
            case "horizontal_axis":
                return (Property<T>) HORIZONTAL_AXIS;
            case "slab_type":
                return (Property<T>) SLAB_TYPE;
            default:
                return null;
        }
    }
}