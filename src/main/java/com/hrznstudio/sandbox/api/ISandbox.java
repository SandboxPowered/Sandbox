package com.hrznstudio.sandbox.api;

public interface ISandbox {

    Side getSide();

    SandboxRegistry getRegistry(String registryString);
}
