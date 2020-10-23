package org.sandboxpowered.sandbox.fabric.util;

import org.sandboxpowered.api.block.Material;

public class MaterialUtil {
    private MaterialUtil() {
    }

    public static Material from(String s) {
        switch (s) {
            case "AIR":
                return WrappingUtil.cast(net.minecraft.block.Material.AIR, Material.class);
            case "ANVIL":
            case "REPAIR_STATION":
                return WrappingUtil.cast(net.minecraft.block.Material.REPAIR_STATION, Material.class);
            case "METAL":
                return WrappingUtil.cast(net.minecraft.block.Material.METAL, Material.class);
            case "BAMBOO":
                return WrappingUtil.cast(net.minecraft.block.Material.BAMBOO, Material.class);
            case "BAMBOO_SAPLING":
                return WrappingUtil.cast(net.minecraft.block.Material.BAMBOO_SAPLING, Material.class);
            case "BARRIER":
                return WrappingUtil.cast(net.minecraft.block.Material.BARRIER, Material.class);
            case "BUBBLE_COLUMN":
                return WrappingUtil.cast(net.minecraft.block.Material.BUBBLE_COLUMN, Material.class);
            case "STONE":
                return WrappingUtil.cast(net.minecraft.block.Material.STONE, Material.class);
            case "LAVA":
                return WrappingUtil.cast(net.minecraft.block.Material.LAVA, Material.class);
            case "WATER":
                return WrappingUtil.cast(net.minecraft.block.Material.WATER, Material.class);
            case "UNDERWATER_PLANT":
                return WrappingUtil.cast(net.minecraft.block.Material.UNDERWATER_PLANT, Material.class);
            case "CACTUS":
                return WrappingUtil.cast(net.minecraft.block.Material.CACTUS, Material.class);
            case "CAKE":
                return WrappingUtil.cast(net.minecraft.block.Material.CAKE, Material.class);
            case "CARPET":
                return WrappingUtil.cast(net.minecraft.block.Material.CARPET, Material.class);
            case "CLAY":
            case "ORGANIC_PRODUCT":
                return WrappingUtil.cast(net.minecraft.block.Material.ORGANIC_PRODUCT, Material.class);
            case "PISTON":
                return WrappingUtil.cast(net.minecraft.block.Material.PISTON, Material.class);
            case "UNUSED_PLANT":
                return WrappingUtil.cast(net.minecraft.block.Material.UNUSED_PLANT, Material.class);
            case "TNT":
                return WrappingUtil.cast(net.minecraft.block.Material.TNT, Material.class);
            case "STRUCTURE_VOID":
                return WrappingUtil.cast(net.minecraft.block.Material.STRUCTURE_VOID, Material.class);
            case "SNOW":
            case "SNOW_LAYER":
                return WrappingUtil.cast(net.minecraft.block.Material.SNOW_LAYER, Material.class);
            case "SNOW_BLOCK":
                return WrappingUtil.cast(net.minecraft.block.Material.SNOW_BLOCK, Material.class);
            case "COBWEB":
                return WrappingUtil.cast(net.minecraft.block.Material.COBWEB, Material.class);
            case "FIRE":
                return WrappingUtil.cast(net.minecraft.block.Material.FIRE, Material.class);
            case "SHULKER_BOX":
                return WrappingUtil.cast(net.minecraft.block.Material.SHULKER_BOX, Material.class);
            case "AGGREGATE":
            case "SAND":
                return WrappingUtil.cast(net.minecraft.block.Material.AGGREGATE, Material.class);
            case "ORGANIC":
                return WrappingUtil.cast(net.minecraft.block.Material.ORGANIC_PRODUCT, Material.class);
            case "PORTAL":
                return WrappingUtil.cast(net.minecraft.block.Material.PORTAL, Material.class);
            case "REPLACEABLE_PLANT":
                return WrappingUtil.cast(net.minecraft.block.Material.REPLACEABLE_PLANT, Material.class);
            case "PLANT":
                return WrappingUtil.cast(net.minecraft.block.Material.PLANT, Material.class);
            case "PUMPKIN":
            case "GOURD":
                return WrappingUtil.cast(net.minecraft.block.Material.GOURD, Material.class);
            case "SEAGRASS":
            case "REPLACEABLE_UNDERWATER_PLANT":
                return WrappingUtil.cast(net.minecraft.block.Material.REPLACEABLE_UNDERWATER_PLANT, Material.class);
            case "SUPPORTED":
            case "PART":
                return WrappingUtil.cast(net.minecraft.block.Material.SUPPORTED, Material.class);
            case "DENSE_ICE":
            case "PACKED_ICE":
                return WrappingUtil.cast(net.minecraft.block.Material.DENSE_ICE, Material.class);
            case "EGG":
                return WrappingUtil.cast(net.minecraft.block.Material.EGG, Material.class);
            case "ICE":
                return WrappingUtil.cast(net.minecraft.block.Material.ICE, Material.class);
            case "EARTH":
            case "SOIL":
                return WrappingUtil.cast(net.minecraft.block.Material.SOIL, Material.class);
            case "REDSTONE_LAMP":
                return WrappingUtil.cast(net.minecraft.block.Material.REDSTONE_LAMP, Material.class);
            case "SPONGE":
                return WrappingUtil.cast(net.minecraft.block.Material.SPONGE, Material.class);
            case "WOOD":
                return WrappingUtil.cast(net.minecraft.block.Material.WOOD, Material.class);
            case "WOOL":
                return WrappingUtil.cast(net.minecraft.block.Material.WOOL, Material.class);
            case "LEAVES":
                return WrappingUtil.cast(net.minecraft.block.Material.LEAVES, Material.class);
            case "GLASS":
                return WrappingUtil.cast(net.minecraft.block.Material.GLASS, Material.class);
            default:
                throw new RuntimeException("Unknown Material " + s);
        }
    }
}
