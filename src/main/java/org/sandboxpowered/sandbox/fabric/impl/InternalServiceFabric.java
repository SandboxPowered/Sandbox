package org.sandboxpowered.sandbox.fabric.impl;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.client.Client;
import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.fluid.FluidStack;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec2i;
import org.sandboxpowered.api.util.math.Vec3i;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.eventhandler.EventHandler;
import org.sandboxpowered.eventhandler.ResettableEventHandler;
import org.sandboxpowered.internal.InternalService;
import org.sandboxpowered.sandbox.fabric.SandboxComponents;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.MaterialUtil;
import org.sandboxpowered.sandbox.fabric.util.PropertyUtil;
import org.sandboxpowered.sandbox.fabric.util.SandboxStorage;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.math.Vec2iImpl;

import java.lang.invoke.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class InternalServiceFabric implements InternalService {
    public static final InternalService INSTANCE = new InternalServiceFabric();

    @Override
    public Identity.Variant createVariantIdentity(Identity identity, String variant) {
        return null;
    }

    @Override
    public Client clientInstance() {
        return SandboxStorage.getClient();
    }

    @Override
    public Vec2i createVec2i(int x, int y) {
        return new Vec2iImpl(x, y);
    }

    @Override
    public Vec3i createVec3i(int x, int y, int z) {
        return (Vec3i) new net.minecraft.util.math.Vec3i(x, y, z);
    }

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
        return (BlockEntity.Type<T>) BlockEntityType.Builder.create(() -> WrappingUtil.convert(s.get()), WrappingUtil.convert(b)).build(null);
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
    public <T extends Content<T>> Registry<T> registryFunction(Class<T> cla) {
        try {
            @SuppressWarnings("UnnecessaryLocalVariable") Class<?> generic = cla;
            if (generic == Block.class) {
                return getOrCreateRegistry("block", net.minecraft.util.registry.Registry.BLOCK, Block.class, net.minecraft.block.Block.class);
            } else if (generic == Item.class) {
                return getOrCreateRegistry("item", net.minecraft.util.registry.Registry.ITEM, Item.class, net.minecraft.item.Item.class);
            } else if (generic == BlockEntity.Type.class) {
                return getOrCreateRegistry("block_entity_type", net.minecraft.util.registry.Registry.BLOCK_ENTITY_TYPE, BlockEntity.Type.class, BlockEntityType.class);
            } else if (generic == Fluid.class) {
                return getOrCreateRegistry("fluid", net.minecraft.util.registry.Registry.FLUID, Fluid.class, net.minecraft.fluid.Fluid.class);
            } else if (generic == Enchantment.class) {
                return getOrCreateRegistry("enchantment", net.minecraft.util.registry.Registry.ENCHANTMENT, Enchantment.class, net.minecraft.enchantment.Enchantment.class);
            } else if (generic == Entity.Type.class) {
                return getOrCreateRegistry("entity_type", net.minecraft.util.registry.Registry.ENTITY_TYPE, Entity.Type.class, EntityType.class);
            }
        } catch (Throwable throwable) {
            throw new IllegalArgumentException("Unknown registry " + cla, throwable);
        }
        return null;
    }

    private <S extends Content<S>, V, T extends Content<T>> Registry<T> getOrCreateRegistry(String name, net.minecraft.util.registry.Registry<V> vRegistry, Class<S> sClass, Class<?> vClass) throws Throwable {
        SandboxInternal.Registry<S, V> sRegistry = (SandboxInternal.Registry<S, V>) vRegistry;
        Registry<S> registry = sRegistry.sandboxGet();
        if (registry == null) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle handleSV = lookup.findStatic(WrappingUtil.class, "convert", MethodType.methodType(vClass, sClass));
            MethodHandle handleVS = lookup.findStatic(WrappingUtil.class, "convert", MethodType.methodType(sClass, vClass));
            Function<S, V> convertSV = (Function<S, V>) LambdaMetafactory.metafactory(lookup, "apply", MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class), handleSV, handleSV.type()).getTarget().invokeExact();
            Function<V, S> convertVS = (Function<V, S>) LambdaMetafactory.metafactory(lookup, "apply", MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class), handleVS, handleVS.type()).getTarget().invokeExact();
            sRegistry.sandboxSet(new WrappedRegistry<>(Identity.of(name), vRegistry, sClass, convertSV, convertVS));
            registry = sRegistry.sandboxGet();
        }
        //Force cast to T at the end to return the type needed
        return (Registry<T>) registry;
    }

    @Override
    public CompoundTag createCompoundTag() {
        return (CompoundTag) new net.minecraft.nbt.CompoundTag();
    }

    @Override
    public Server serverInstance() {
        return SandboxStorage.getServer();
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
    public <T extends Comparable<T>> Property<T> getProperty(String property) {
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

    @Override
    public Shape shape_cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return WrappingUtil.convert(VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ));
    }

    @Override
    public Shape shape_fullCube() {
        return WrappingUtil.convert(VoxelShapes.fullCube());
    }

    @Override
    public Shape shape_empty() {
        return WrappingUtil.convert(VoxelShapes.empty());
    }

    @Override
    public <X> EventHandler<X> createEventHandler() {
        return new ResettableEventHandler<>();
    }

    @Override
    public Box box_of(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return WrappingUtil.convert(new net.minecraft.util.math.Box(minX, minY, minZ, maxX, maxY, maxZ));
    }

    @Override
    public Box box_of(Position pos1, Position pos2) {
        return WrappingUtil.convert(new net.minecraft.util.math.Box(WrappingUtil.convert(pos1), WrappingUtil.convert(pos2)));
    }
}
