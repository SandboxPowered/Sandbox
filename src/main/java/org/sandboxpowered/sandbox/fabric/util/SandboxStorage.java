package org.sandboxpowered.sandbox.fabric.util;

import org.sandboxpowered.api.client.Client;
import org.sandboxpowered.api.server.Server;

public class SandboxStorage {
    private static Client client;
    private static Server server;

    private SandboxStorage() {
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        SandboxStorage.client = client;
    }

    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        SandboxStorage.server = server;
    }
}
