package com.hrznstudio.sandbox.loader;

import java.net.URL;
import java.util.function.Consumer;

public interface AddonLocator {
    void find(SandboxLoader loader, Consumer<URL> urlConsumer);
}