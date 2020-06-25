package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import org.sandboxpowered.sandbox.fabric.resources.SandboxResourceCreator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public class MixinResourcePackManager<T extends ResourcePackProfile> {
    @Shadow
    @Final
    @Mutable
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void construct(ResourcePackProfile.Factory<T> arg, ResourcePackProvider[] resourcePackProviders, CallbackInfo info) {
        providers = new HashSet<>(providers);
        providers.add(new SandboxResourceCreator());
    }
}
