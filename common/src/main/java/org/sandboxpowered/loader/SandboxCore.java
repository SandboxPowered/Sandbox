package org.sandboxpowered.loader;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.item.tool.ToolType;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.config.Config;
import org.sandboxpowered.loader.loading.AddonSecurityPolicy;
import org.sandboxpowered.loader.loading.SandboxLoader;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.sandboxpowered.loader.wrapper.IVanillaBlock;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Policy;
import java.util.Arrays;
import java.util.Set;

public abstract class SandboxCore implements SandboxPlatform {
    protected Config config;
    @Inject
    protected Logger logger;

    protected SandboxLoader loader = new SandboxLoader();

    public SandboxCore() {
        try {
            config = new Config("data/sandbox/sandbox.toml");
        } catch (IOException e) {
            getLog().error("Error creating configuration file", e);
            return;
        }

        config.save();
    }

    @Override
    public void load(MinecraftServer server, LevelStorageSource.LevelStorageAccess storageSource) {
        if (loader.isLoaded()) {
            throw new UnsupportedOperationException("Cannot load Sandbox if in already loaded state");
        }
        loader.load(server, storageSource);
    }

    @Override
    public void unload() {
        if (!loader.isLoaded()) {
            throw new UnsupportedOperationException("Cannot unload Sandbox if in unloaded state");
        }
        loader.unload();
    }

    @Override
    public void init() {
        Policy.setPolicy(new AddonSecurityPolicy());
        initCachedRegistries();

        initToolTypes();
    }

    private void initToolTypes() {
        Set<Block> blocks = getPrivateField(PickaxeItem.class, null, 0);
        blocks.forEach((block) -> setValue(block, ToolType.PICKAXE, 0));

        blocks = getPrivateField(ShovelItem.class, null, 0);
        blocks.forEach((block) -> setValue(block, ToolType.SHOVEL, 0));

        Set<Material> materials = getPrivateField(AxeItem.class, null, 0);
        Set<Material> pickMats = Set.of(Material.STONE, Material.METAL, Material.HEAVY_METAL);

        for (Block block : Registry.BLOCK) {
            if (materials.contains(block.defaultBlockState().getMaterial())) {
                setValue(block, ToolType.AXE, 0);
            }
            if (pickMats.contains(block.defaultBlockState().getMaterial())) {
                setValue(block, ToolType.PICKAXE, 0);
            }
        }

        blocks = getPrivateField(AxeItem.class, null, 1);
        blocks.forEach((block) -> setValue(block, ToolType.AXE, 0));
        blocks = getPrivateField(HoeItem.class, null, 0);
        blocks.forEach((block) -> setValue(block, ToolType.HOE, 0));

        Arrays.stream(new Block[]{Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE})
                .forEach(block -> setValue(block, ToolType.PICKAXE, 1));
        Arrays.stream(new Block[]{Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE})
                .forEach(block -> setValue(block, ToolType.PICKAXE, 2));
        Arrays.stream(new Block[]{Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.NETHERITE_BLOCK, Blocks.RESPAWN_ANCHOR, Blocks.ANCIENT_DEBRIS})
                .forEach(block -> setValue(block, ToolType.PICKAXE, 3));
    }

    private static void setValue(Block block, ToolType type, int level) {
        ((IVanillaBlock)block).sandbox_setHarvestParams(type, level);
    }

    private static <T, E> T getPrivateField(Class<? super E> classToAccess, @Nullable E instance, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (Exception var4) {
            Throwables.throwIfUnchecked(var4);
            throw new RuntimeException(var4);
        }
    }

    @Override
    public Identity getIdentity() {
        return loader.getPlatform();
    }

    @Override
    public boolean isLoaded() {
        return loader.isLoaded();
    }

    @Override
    public SandboxLoader getLoader() {
        return loader;
    }

    protected abstract void initCachedRegistries();

    public Logger getLog() {
        return logger;
    }
}