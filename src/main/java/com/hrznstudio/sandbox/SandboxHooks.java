package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.block.Material;
import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.client.render.RenderUtil;
import com.hrznstudio.sandbox.api.component.Component;
import com.hrznstudio.sandbox.api.container.ContainerFactory;
import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.server.Server;
import com.hrznstudio.sandbox.api.state.Property;
import com.hrznstudio.sandbox.api.util.Functions;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3i;
import com.hrznstudio.sandbox.api.util.nbt.CompoundTag;
import com.hrznstudio.sandbox.api.util.text.Text;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.impl.BasicRegistry;
import com.hrznstudio.sandbox.security.AddonSecurityPolicy;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.MaterialUtil;
import com.hrznstudio.sandbox.util.PropertyUtil;
import com.hrznstudio.sandbox.util.ReflectionHelper;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.RenderUtilImpl;
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

import java.security.Policy;
import java.util.function.BiFunction;
import java.util.function.Function;
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
        if (FabricLoader.getInstance().getAllMods()
                .stream()
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getId)
                .anyMatch(id -> !id.equals("sandbox") && !id.equals("sandboxapi") && !id.equals("fabricloader"))) {
            Sandbox.unsupportedModsLoaded = true;
        }
        Policy.setPolicy(new AddonSecurityPolicy());
        try {
            ReflectionHelper.setPrivateField(Functions.class, "vec3iFunction", (Function<int[], Vec3i>) (arr) -> (Vec3i) new net.minecraft.util.math.Vec3i(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "positionFunction", (Function<int[], Position>) (arr) -> (Position) new BlockPos(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "mutablePositionFunction", (Function<int[], Position.Mutable>) (arr) -> (Position.Mutable) new BlockPos.Mutable(arr[0], arr[1], arr[2]));
            ReflectionHelper.setPrivateField(Functions.class, "identityFunction", (Function<String, Identity>) s -> (Identity) new Identifier(s));
            ReflectionHelper.setPrivateField(Functions.class, "materialFunction", (Function<String, Material>) MaterialUtil::from);
            ReflectionHelper.setPrivateField(Functions.class, "blockEntityTypeFunction", (BiFunction<Supplier<IBlockEntity>, IBlock[], IBlockEntity.Type>) (s, b) -> {
                return (IBlockEntity.Type) BlockEntityType.Builder.create((Supplier) () -> WrappingUtil.convert(s.get()), WrappingUtil.convert(b)).build(null);
            });
            ReflectionHelper.setPrivateField(Functions.class, "itemStackFromTagFunction", (Function<CompoundTag, ItemStack>) (tag) -> WrappingUtil.cast(net.minecraft.item.ItemStack.fromTag((net.minecraft.nbt.CompoundTag) tag), ItemStack.class));
            ReflectionHelper.setPrivateField(Functions.class, "itemStackFunction", (BiFunction<IItem, Integer, ItemStack>) (item, count) -> {
                if (item == null || count == 0)
                    return WrappingUtil.cast(net.minecraft.item.ItemStack.EMPTY, ItemStack.class);
                return WrappingUtil.cast(new net.minecraft.item.ItemStack(WrappingUtil.convert(item), count), ItemStack.class);
            });
            ReflectionHelper.setPrivateField(Functions.class, "registryFunction", (Function<Class, com.hrznstudio.sandbox.api.registry.Registry>) (cla) -> {
                if (cla == IBlock.class) {
                    return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.BLOCK).get();
                }
                if (cla == IItem.class) {
                    return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.ITEM).get();
                }
                if (cla == IBlockEntity.Type.class) {
                    return ((SandboxInternal.Registry) net.minecraft.util.registry.Registry.BLOCK_ENTITY).get();
                }
                if (cla == IFluid.class) {
                    return ((SandboxInternal.Registry) Registry.FLUID).get();
                }
                if (cla == IEnchantment.class) {
                    return ((SandboxInternal.Registry) Registry.ENCHANTMENT).get();
                }
                if (cla == ContainerFactory.class) {
                    return ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).get();
                }
                throw new RuntimeException("Unknown registry " + cla);
            });
            ReflectionHelper.setPrivateField(Functions.class, "blockFunction", (Function<String, IBlock>) s -> (IBlock) net.minecraft.util.registry.Registry.BLOCK.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "itemFunction", (Function<String, IItem>) s -> (IItem) net.minecraft.util.registry.Registry.ITEM.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "enchantmentFunction", (Function<String, IEnchantment>) s -> (IEnchantment) Registry.ENCHANTMENT.get(new Identifier(s)));
            ReflectionHelper.setPrivateField(Functions.class, "literalTextFunction", (Function<String, Text>) s -> (Text) new LiteralText(s));
            ReflectionHelper.setPrivateField(Functions.class, "translatedTextFunction", (Function<String, Text>) s -> (Text) new TranslatableText(s));
            ReflectionHelper.setPrivateField(Functions.class, "compoundTagCreator", (Supplier<CompoundTag>) () -> (CompoundTag) new net.minecraft.nbt.CompoundTag());
            ReflectionHelper.setPrivateField(Functions.class, "propertyFunction", (Function<String, Property>) PropertyUtil::get);
            ReflectionHelper.setPrivateField(Functions.class, "clientInstance", (Supplier<Client>) () -> SandboxCommon.client);
            ReflectionHelper.setPrivateField(Functions.class, "serverInstance", (Supplier<Server>) () -> SandboxCommon.server);
            ReflectionHelper.setPrivateField(Functions.class, "fluidFunction", (Function<String, IFluid>) s -> WrappingUtil.convert(Registry.FLUID.get(new Identifier(s))));
            ReflectionHelper.setPrivateField(Functions.class, "renderUtil", (Supplier<RenderUtil>) () -> RenderUtilImpl.INSTANCE);
            ReflectionHelper.setPrivateField(Functions.class, "componentFunction", (Function<Class, Component>) SandboxComponents::getComponent);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Registry.REGISTRIES.add(new Identifier("sandbox", "container"), SandboxRegistries.CONTAINER_FACTORIES);

        ((SandboxInternal.Registry) Registry.BLOCK).set(new BasicRegistry<>(Registry.BLOCK, IBlock.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ITEM).set(new BasicRegistry<>(Registry.ITEM, IItem.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENCHANTMENT).set(new BasicRegistry<>((SimpleRegistry<net.minecraft.enchantment.Enchantment>) Registry.ENCHANTMENT, IEnchantment.class, WrappingUtil::convert, b -> (IEnchantment) b));
        ((SandboxInternal.Registry) Registry.FLUID).set(new BasicRegistry<>(Registry.FLUID, IFluid.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.BLOCK_ENTITY).set(new BasicRegistry((SimpleRegistry) Registry.BLOCK_ENTITY, IBlockEntity.Type.class, (Function<IBlockEntity.Type, BlockEntityType>) WrappingUtil::convert, (Function<BlockEntityType, IBlockEntity.Type>) WrappingUtil::convert, true)); // DONT TOUCH THIS FOR HEAVENS SAKE PLEASE GOD NO
        ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).set(new BasicRegistry<>(SandboxRegistries.CONTAINER_FACTORIES, ContainerFactory.class, a -> a, a -> a));

        if (Sandbox.SANDBOX.getSide() == Side.CLIENT)
            SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT)
            SandboxDiscord.shutdown();
    }
}