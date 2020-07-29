package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.Fluid;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.registry.Registry;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.FluidWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z"))
    public boolean addAll(Set<SpriteIdentifier> set, Collection<SpriteIdentifier> set2) {
        for (Fluid fluid : Registry.FLUID) {
            if (fluid instanceof FluidWrapper) {
                set.add(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(false))));
                set.add(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, WrappingUtil.convert(((FluidWrapper) fluid).fluid.getTexturePath(true))));
            }
        }
        return set.addAll(set2);
    }
}