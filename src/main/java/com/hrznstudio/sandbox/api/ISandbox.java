package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.api.util.Side;

public interface ISandbox {

    Side getSide();

    SandboxRegistry getRegistry(SandboxRegistry.RegistryType registryType);
}
