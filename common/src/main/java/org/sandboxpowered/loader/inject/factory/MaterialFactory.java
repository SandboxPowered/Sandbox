package org.sandboxpowered.loader.inject.factory;


import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.Materials;
import org.sandboxpowered.loader.Wrappers;

public class MaterialFactory implements Materials.Factory {

    @Override
    public Material get(String s) {
        return Wrappers.MATERIAL.toSandbox(from(s));
    }

    public net.minecraft.world.level.material.Material from(String s) {
        switch (s) {
            case "AIR":
                return net.minecraft.world.level.material.Material.AIR;
            case "ANVIL":
            case "REPAIR_STATION":
                return net.minecraft.world.level.material.Material.HEAVY_METAL;
            case "METAL":
                return net.minecraft.world.level.material.Material.METAL;
            case "BAMBOO":
                return net.minecraft.world.level.material.Material.BAMBOO;
            case "BAMBOO_SAPLING":
                return net.minecraft.world.level.material.Material.BAMBOO_SAPLING;
            case "BARRIER":
                return net.minecraft.world.level.material.Material.BARRIER;
            case "BUBBLE_COLUMN":
                return net.minecraft.world.level.material.Material.BUBBLE_COLUMN;
            case "STONE":
                return net.minecraft.world.level.material.Material.STONE;
            case "LAVA":
                return net.minecraft.world.level.material.Material.LAVA;
            case "WATER":
                return net.minecraft.world.level.material.Material.WATER;
            case "UNDERWATER_PLANT":
                return net.minecraft.world.level.material.Material.WATER_PLANT;
            case "CACTUS":
                return net.minecraft.world.level.material.Material.CACTUS;
            case "CAKE":
                return net.minecraft.world.level.material.Material.CAKE;
            case "CARPET":
                return net.minecraft.world.level.material.Material.CLOTH_DECORATION;
            case "CLAY":
            case "ORGANIC_PRODUCT":
                return net.minecraft.world.level.material.Material.CLAY;
            case "PISTON":
                return net.minecraft.world.level.material.Material.PISTON;
            case "UNUSED_PLANT":
                return net.minecraft.world.level.material.Material.PLANT;
            case "TNT":
                return net.minecraft.world.level.material.Material.EXPLOSIVE;
            case "STRUCTURE_VOID":
                return net.minecraft.world.level.material.Material.STRUCTURAL_AIR;
            case "SNOW":
            case "SNOW_LAYER":
                return net.minecraft.world.level.material.Material.TOP_SNOW;
            case "SNOW_BLOCK":
                return net.minecraft.world.level.material.Material.SNOW;
            case "COBWEB":
                return net.minecraft.world.level.material.Material.WEB;
            case "FIRE":
                return net.minecraft.world.level.material.Material.FIRE;
            case "SHULKER_BOX":
                return net.minecraft.world.level.material.Material.SHULKER_SHELL;
            case "AGGREGATE":
            case "SAND":
                return net.minecraft.world.level.material.Material.SAND;
            case "ORGANIC":
                return net.minecraft.world.level.material.Material.VEGETABLE;
            case "PORTAL":
                return net.minecraft.world.level.material.Material.PORTAL;
            case "REPLACEABLE_PLANT":
                return net.minecraft.world.level.material.Material.REPLACEABLE_PLANT;
            case "PLANT":
                return net.minecraft.world.level.material.Material.PLANT;
            case "PUMPKIN":
            case "GOURD":
                return net.minecraft.world.level.material.Material.VEGETABLE;
            case "SEAGRASS":
            case "REPLACEABLE_UNDERWATER_PLANT":
                return net.minecraft.world.level.material.Material.REPLACEABLE_WATER_PLANT;
            case "SUPPORTED":
            case "PART":
                return net.minecraft.world.level.material.Material.PISTON;
            case "DENSE_ICE":
            case "PACKED_ICE":
                return net.minecraft.world.level.material.Material.ICE_SOLID;
            case "EGG":
                return net.minecraft.world.level.material.Material.EGG;
            case "ICE":
                return net.minecraft.world.level.material.Material.ICE;
            case "EARTH":
            case "SOIL":
                return net.minecraft.world.level.material.Material.DIRT;
            case "REDSTONE_LAMP":
                return net.minecraft.world.level.material.Material.BUILDABLE_GLASS;
            case "SPONGE":
                return net.minecraft.world.level.material.Material.SPONGE;
            case "WOOD":
                return net.minecraft.world.level.material.Material.WOOD;
            case "WOOL":
                return net.minecraft.world.level.material.Material.WOOL;
            case "LEAVES":
                return net.minecraft.world.level.material.Material.LEAVES;
            case "GLASS":
                return net.minecraft.world.level.material.Material.GLASS;
            default:
                return net.minecraft.world.level.material.Material.WOOD;
        }
    }
}
