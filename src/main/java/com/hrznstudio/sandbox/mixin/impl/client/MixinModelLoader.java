package com.hrznstudio.sandbox.mixin.impl.client;

import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.FluidWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ExtendedBlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/SpriteAtlasTexture;stitch(Lnet/minecraft/resource/ResourceManager;Ljava/lang/Iterable;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/texture/SpriteAtlasTexture$Data;"))
    public SpriteAtlasTexture.Data stitch(SpriteAtlasTexture texture, ResourceManager resourceManager_1, Iterable<Identifier> iterable_1, Profiler profiler_1) {
        for (Fluid fluid : Registry.FLUID) {
            if (fluid instanceof FluidWrapper) {
                ((Set<Identifier>) iterable_1).add(WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(false)));
                ((Set<Identifier>) iterable_1).add(WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(true)));
            }
        }
        return texture.stitch(resourceManager_1, iterable_1, profiler_1);
    }
}