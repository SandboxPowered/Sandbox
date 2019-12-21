package org.sandboxpowered.sandbox.fabric.impl;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.block.Material;
import org.sandboxpowered.sandbox.api.block.entity.BlockEntity;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.render.RenderUtil;
import org.sandboxpowered.sandbox.api.component.Component;
import org.sandboxpowered.sandbox.api.container.ContainerFactory;
import org.sandboxpowered.sandbox.api.content.Content;
import org.sandboxpowered.sandbox.api.enchant.Enchantment;
import org.sandboxpowered.sandbox.api.entity.Entity;
import org.sandboxpowered.sandbox.api.fluid.Fluid;
import org.sandboxpowered.sandbox.api.fluid.FluidStack;
import org.sandboxpowered.sandbox.api.item.Item;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.registry.Registry;
import org.sandboxpowered.sandbox.api.server.Server;
import org.sandboxpowered.sandbox.api.state.Property;
import org.sandboxpowered.sandbox.api.util.Functions;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.util.math.Vec3i;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.sandbox.api.util.text.Text;
import org.sandboxpowered.sandbox.fabric.SandboxCommon;
import org.sandboxpowered.sandbox.fabric.SandboxComponents;
import org.sandboxpowered.sandbox.fabric.SandboxRegistries;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.MaterialUtil;
import org.sandboxpowered.sandbox.fabric.util.PropertyUtil;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.function.Supplier;

public class FunctionsImpl implements Functions {
    public static Functions INSTANCE = new FunctionsImpl();

    @Override
    public Identity createIdentityFromString(String name, String path) {
        return (Identity) new Identifier(name, path);
    }

    @Override
    public Identity createIdentityFromString(String identity) {
        return (Identity) new Identifier(identity);
    }

    @Override
    public Text createLiteralText(String text) {
        return (Text) new LiteralText(text);
    }

    @Override
    public Text createTranslatedText(String translation) {
        return (Text) new TranslatableText(translation);
    }

    @Override
    public Material getMaterial(String material) {
        return MaterialUtil.from(material);
    }

    @Override
    public <T extends BlockEntity> BlockEntity.Type<T> blockEntityTypeFunction(Supplier<T> s, Block[] b) {
        return (BlockEntity.Type) BlockEntityType.Builder.create((Supplier) () -> WrappingUtil.convert(s.get()), WrappingUtil.convert(b)).build(null);
    }

    @Override
    public ItemStack createItemStack(Item item, int amount) {
        if (item == null || amount == 0)
            return WrappingUtil.cast(net.minecraft.item.ItemStack.EMPTY, ItemStack.class);
        return WrappingUtil.cast(new net.minecraft.item.ItemStack(WrappingUtil.convert(item), amount), ItemStack.class);
    }

    @Override
    public ItemStack createItemStackFromTag(ReadableCompoundTag tag) {
        return WrappingUtil.cast(net.minecraft.item.ItemStack.fromTag(WrappingUtil.convert(tag)), ItemStack.class);
    }

    @Override
    public <T extends Content> Registry<T> registryFunction(Class<T> cla) {
        if (cla == Block.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.BLOCK).get();
        }
        if (cla == Item.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.ITEM).get();
        }
        if (cla == BlockEntity.Type.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.BLOCK_ENTITY).get();
        }
        if (cla == Fluid.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.FLUID).get();
        }
        if (cla == Enchantment.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.ENCHANTMENT).get();
        }
        if (cla == ContainerFactory.class) {
            return ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).get();
        }
        if (cla == Entity.Type.class) {
            return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.ENTITY_TYPE).get();
        }
        throw new RuntimeException("Unknown registry " + cla);
    }

    @Override
    public <T extends Content> Registry<T> registryTypeFunction(Class<T> cla) {
        return registryFunction(cla);
    }

    @Override
    public CompoundTag createCompoundTag() {
        return (CompoundTag) new net.minecraft.nbt.CompoundTag();
    }

    @Override
    public Client clientInstance() {
        return SandboxCommon.client;
    }

    @Override
    public Server serverInstance() {
        return SandboxCommon.server;
    }

    @Override
    public Vec3i createVec3i(int x, int y, int z) {
        return (Vec3i) new net.minecraft.util.math.Vec3i(x, y, z);
    }

    @Override
    public Position createPosition(int x, int y, int z) {
        return (Position) new net.minecraft.util.math.BlockPos(x, y, z);
    }

    @Override
    public Position.Mutable createMutablePosition(int x, int y, int z) {
        return (Position.Mutable) new BlockPos.Mutable(x, y, z);
    }

    @Override
    public RenderUtil renderUtilInstance() {
        return RenderUtil.instance();
    }

    @Override
    public Property getProperty(String property) {
        return PropertyUtil.get(property);
    }

    @Override
    public <T> Component<T> componentFunction(Class<T> c) {
        return SandboxComponents.getComponent(c);
    }

    @Override
    public Entity.Type entityTypeEntityFunction(Entity e) {
        return (Entity.Type) EntityType.fromTag(WrappingUtil.convert(e).toTag(new net.minecraft.nbt.CompoundTag())).orElse(null);
    }

    @Override
    public FluidStack fluidStackFunction(Fluid fluid, int amount) {
        return new FluidStackImpl(fluid, amount);
    }

    @Override
    public FluidStack fluidStackFromTagFunction(ReadableCompoundTag tag) {
        return new FluidStackImpl(tag);
    }
}