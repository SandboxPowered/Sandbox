package org.sandboxpowered.sandbox.fabric;

import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.block.Material;
import org.sandboxpowered.sandbox.api.block.entity.BlockEntity;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.render.RenderUtil;
import org.sandboxpowered.sandbox.api.component.Component;
import org.sandboxpowered.sandbox.api.container.ContainerFactory;
import org.sandboxpowered.sandbox.api.enchant.Enchantment;
import org.sandboxpowered.sandbox.api.fluid.Fluid;
import org.sandboxpowered.sandbox.api.item.Item;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.server.Server;
import org.sandboxpowered.sandbox.api.state.Property;
import org.sandboxpowered.sandbox.api.util.Functions;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Side;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.util.math.Vec3i;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.api.util.text.Text;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
import org.sandboxpowered.sandbox.fabric.util.MaterialUtil;
import org.sandboxpowered.sandbox.fabric.util.PropertyUtil;
import org.sandboxpowered.sandbox.fabric.util.ReflectionHelper;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.RenderUtilImpl;

import java.security.Policy;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SandboxHooks {
    public static void shutdown() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) {
            SandboxClient.INSTANCE.shutdown();
            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
                SandboxServer.INSTANCE.shutdown();
        } else {
            SandboxServer.INSTANCE.shutdown();
        }
    }

    public static void setupGlobal() {
        Set<String> supportedMods = Sets.newHashSet("minecraft", "sandbox", "sandboxapi", "fabricloader");
        Sandbox.unsupportedModsLoaded = FabricLoader.getInstance().getAllMods().stream()
            .map(ModContainer::getMetadata)
            .map(ModMetadata::getId)
            .anyMatch(((Predicate<String>) supportedMods::contains).negate());
        Policy.setPolicy(new AddonSecurityPolicy());
        try {
            ReflectionHelper.setPrivateField(Functions.class, "vec3iFunction", (Function<int[], Vec3i>) (arr) -> (Vec3i) new net.minecraft.util.math.Vec3i(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "positionFunction", (Function<int[], Position>) (arr) -> (Position) new BlockPos(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "mutablePositionFunction", (Function<int[], Position.Mutable>) (arr) -> (Position.Mutable) new BlockPos.Mutable(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "identityFunction", (Function<String, Identity>) s -> (Identity) new Identifier(s));
            ReflectionHelper.setPrivateField(Functions.class, "materialFunction", (Function<String, Material>) MaterialUtil::from);
            ReflectionHelper.setPrivateField(Functions.class, "blockEntityTypeFunction", (BiFunction<Supplier<BlockEntity>, Block[], BlockEntity.Type>) (s, b) -> {
                return (BlockEntity.Type) BlockEntityType.Builder.create((Supplier) () -> WrappingUtil.convert(s.get()), WrappingUtil.convert(b)).build(null);
            });
            ReflectionHelper.setPrivateField(Functions.class, "itemStackFromTagFunction", (Function<CompoundTag, ItemStack>) (tag) -> WrappingUtil.cast(net.minecraft.item.ItemStack.fromTag((net.minecraft.nbt.CompoundTag) tag), ItemStack.class));
            ReflectionHelper.setPrivateField(Functions.class, "itemStackFunction", (BiFunction<Item, Integer, ItemStack>) (item, count) -> {
                if (item == null || count == 0)
                    return WrappingUtil.cast(net.minecraft.item.ItemStack.EMPTY, ItemStack.class);
                return WrappingUtil.cast(new net.minecraft.item.ItemStack(WrappingUtil.convert(item), count), ItemStack.class);
            });
            ReflectionHelper.setPrivateField(Functions.class, "registryFunction", (Function<Class, org.sandboxpowered.sandbox.api.registry.Registry>) (cla) -> {
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
                    return ((SandboxInternal.Registry) Registry.FLUID).get();
                }
                if (cla == Enchantment.class) {
                    return ((SandboxInternal.Registry) Registry.ENCHANTMENT).get();
                }
                if (cla == ContainerFactory.class) {
                    return ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).get();
                }
                throw new RuntimeException("Unknown registry " + cla);
            });
            ReflectionHelper.setPrivateField(Functions.class, "blockFunction", (Function<String, Block>) s -> (Block) net.minecraft.util.registry.Registry.BLOCK.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "itemFunction", (Function<String, Item>) s -> (Item) net.minecraft.util.registry.Registry.ITEM.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "enchantmentFunction", (Function<String, Enchantment>) s -> (Enchantment) Registry.ENCHANTMENT.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "literalTextFunction", (Function<String, Text>) s -> (Text) new LiteralText(s));
            ReflectionHelper.setPrivateField(Functions.class, "translatedTextFunction", (Function<String, Text>) s -> (Text) new TranslatableText(s));
            ReflectionHelper.setPrivateField(Functions.class, "compoundTagCreator", (Supplier<CompoundTag>) () -> (CompoundTag) new net.minecraft.nbt.CompoundTag());
            ReflectionHelper.setPrivateField(Functions.class, "propertyFunction", (Function<String, Property>) PropertyUtil::get);
            ReflectionHelper.setPrivateField(Functions.class, "clientInstance", (Supplier<Client>) () -> SandboxCommon.client);
            ReflectionHelper.setPrivateField(Functions.class, "serverInstance", (Supplier<Server>) () -> SandboxCommon.server);
            ReflectionHelper.setPrivateField(Functions.class, "fluidFunction", (Function<String, Fluid>) s -> WrappingUtil.convert(Registry.FLUID.get(new Identifier(s))));
            ReflectionHelper.setPrivateField(Functions.class, "renderUtil", (Supplier<RenderUtil>) () -> RenderUtilImpl.INSTANCE);
            ReflectionHelper.setPrivateField(Functions.class, "componentFunction", (Function<Class, Component>) SandboxComponents::getComponent);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Registry.REGISTRIES.add(new Identifier("sandbox", "container"), SandboxRegistries.CONTAINER_FACTORIES);

        ((SandboxInternal.Registry) Registry.BLOCK).set(new BasicRegistry<>(Registry.BLOCK, Block.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ITEM).set(new BasicRegistry<>(Registry.ITEM, Item.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENCHANTMENT).set(new BasicRegistry<>((SimpleRegistry<net.minecraft.enchantment.Enchantment>) Registry.ENCHANTMENT, Enchantment.class, WrappingUtil::convert, b -> (Enchantment) b));
        ((SandboxInternal.Registry) Registry.FLUID).set(new BasicRegistry<>(Registry.FLUID, Fluid.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.BLOCK_ENTITY).set(new BasicRegistry((SimpleRegistry) Registry.BLOCK_ENTITY, BlockEntity.Type.class, (Function<BlockEntity.Type, BlockEntityType>) WrappingUtil::convert, (Function<BlockEntityType, BlockEntity.Type>) WrappingUtil::convert, true)); // DONT TOUCH THIS FOR HEAVENS SAKE PLEASE GOD NO
        ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).set(new BasicRegistry<>(SandboxRegistries.CONTAINER_FACTORIES, ContainerFactory.class, a -> a, a -> a));

        if (Sandbox.SANDBOX.getSide() == Side.CLIENT)
            SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT)
            SandboxDiscord.shutdown();
    }
}