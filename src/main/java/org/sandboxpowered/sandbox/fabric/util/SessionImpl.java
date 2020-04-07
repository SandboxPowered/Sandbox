package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.client.MinecraftClient;
import org.sandboxpowered.sandbox.api.client.Session;

public class SessionImpl implements Session {
    private final MinecraftClient client;

    public SessionImpl(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public String getUUID() {
        return client.getSession().getUuid();
    }

    @Override
    public String getUsername() {
        return client.getSession().getUsername();
    }
}
