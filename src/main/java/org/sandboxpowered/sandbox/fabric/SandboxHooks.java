package org.sandboxpowered.sandbox.fabric;

import net.minecraft.util.registry.Registry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;

import java.security.Policy;

public class SandboxHooks {
    public static void shutdown() {
        Registry.REGISTRIES.stream().map(registry -> (SandboxInternal.Registry) registry).forEach(SandboxInternal.Registry::sandbox_reset);
    }

    public static void setupGlobal() {
        Policy.setPolicy(new AddonSecurityPolicy());
    }

    public static void shutdownGlobal() {
    }
}