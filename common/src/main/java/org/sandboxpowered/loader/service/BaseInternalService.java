package org.sandboxpowered.loader.service;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tiers;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.client.Client;
import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.fluid.FluidStack;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.tool.ToolMaterial;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.tags.Tag;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec2i;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.util.math.Vec3i;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.eventhandler.EventHandler;
import org.sandboxpowered.eventhandler.ResettableEventHandler;
import org.sandboxpowered.internal.InternalService;
import org.sandboxpowered.loader.Wrappers;

import java.util.function.Supplier;

public abstract class BaseInternalService implements InternalService {

    @Override
    public Text createLiteralText(String text) {
        return null;
    }

    @Override
    public Text createTranslatedText(String translation) {
        return null;
    }

    @Override
    public Material getMaterial(String material) {
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntity.Type<T> blockEntityTypeFunction(Supplier<T> supplier, Block[] blocks) {
        return null;
    }
    @Override
    public CompoundTag createCompoundTag() {
        return null;
    }

    @Override
    public <T extends Comparable<T>> Property<T> getProperty(String property) {
        return null;
    }

    @Override
    public Server serverInstance() {
        return null;
    }

    @Override
    public Vec3i createVec3i(int x, int y, int z) {
        return null;
    }

    @Override
    public <T> Component<T> componentFunction(Class<T> c) {
        return null;
    }

    @Override
    public FluidStack fluidStackFunction(Fluid fluid, int amount) {
        return null;
    }

    @Override
    public FluidStack fluidStackFromTagFunction(ReadableCompoundTag tag) {
        return null;
    }

    @Override
    public Identity.Variant createVariantIdentity(Identity identity, String variant) {
        return null;
    }

    @Override
    public Client clientInstance() {
        return null;
    }

    @Override
    public Vec2i createVec2i(int x, int y) {
        return null;
    }

    @Override
    public <T extends Content<T>> Registry<T> registryFunction(Class<T> c) {
        return null;
    }

    @Override
    public Tag<Block> getBlockTag(String string) {
        return (Tag<Block>) BlockTags.BEACON_BASE_BLOCKS;
    }

    @Override
    public Vec3d createVec3d(double x, double y, double z) {
        return null;
    }
}