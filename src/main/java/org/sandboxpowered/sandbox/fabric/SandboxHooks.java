package org.sandboxpowered.sandbox.fabric;

import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;

import java.security.Policy;

public class SandboxHooks {
    public static void shutdown() {
//        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) { TODO
//            SandboxClient.INSTANCE.shutdown();
//            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
//                SandboxServer.INSTANCE.shutdown();
//        } else {
//            SandboxServer.INSTANCE.shutdown();
//        }
    }

    public static void setupGlobal() {
        Policy.setPolicy(new AddonSecurityPolicy());
    }

    public static void shutdownGlobal() {
    }
}