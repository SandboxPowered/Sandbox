package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.class_4730;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.FluidWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z"))
    public boolean addAll(Set<class_4730> set, Collection<class_4730> set2) {
        for (Fluid fluid : Registry.FLUID) {
            if (fluid instanceof FluidWrapper) {
                set.add(new class_4730(SpriteAtlasTexture.BLOCK_ATLAS_TEX, WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(false))));
                set.add(new class_4730(SpriteAtlasTexture.BLOCK_ATLAS_TEX, WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(true))));
            }
        }
        return set.addAll(set2);
    }
}