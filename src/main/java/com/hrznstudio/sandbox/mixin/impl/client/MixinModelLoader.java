package com.hrznstudio.sandbox.mixin.impl.client;

import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.FluidWrapper;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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