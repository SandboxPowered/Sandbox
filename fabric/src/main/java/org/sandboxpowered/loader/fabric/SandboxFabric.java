package org.sandboxpowered.loader.fabric;

import net.minecraft.core.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.SandboxCore;
import org.sandboxpowered.loader.Wrappers;

public class SandboxFabric extends SandboxCore {
    public static SandboxFabric CORE = new SandboxFabric();
    private static final Identity ID = Identity.of("sandbox", "fabric");

    @Override
    public Identity getIdentity() {
        return ID;
    }

    @Override
    protected Logger createLogger() {
        return LogManager.getLogger("Sandbox|Fabric");
    }

    @Override
    protected void initCachedRegistries() {
        setRegistryWrapper(Registry.BLOCK, Wrappers.BLOCK);
        setRegistryWrapper(Registry.ITEM, Wrappers.ITEM);
        setRegistryWrapper(Registry.FLUID, Wrappers.FLUID);
        setRegistryWrapper(Registry.ENCHANTMENT, Wrappers.ENCHANTMENT);
    }

    private <X extends Content<X>, Y> void setRegistryWrapper(Registry<Y> registry, Wrappers.Wrapper<X, Y> wrapper) {
        ((CacheableRegistry<X, Y>) registry).setWrapper(wrapper);
    }
}
