package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.server.SandboxServer;

public interface IAddon {
    void initServer(SandboxServer server);

    void initClient(SandboxClient client);
}
