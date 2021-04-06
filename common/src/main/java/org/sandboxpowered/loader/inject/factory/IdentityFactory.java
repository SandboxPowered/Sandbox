package org.sandboxpowered.loader.inject.factory;

import net.minecraft.resources.ResourceLocation;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.Wrappers;

public class IdentityFactory implements Identity.Factory {
    @Override
    public Identity create(String namespace, String path) {
        return Wrappers.IDENTITY.toSandbox(new ResourceLocation(namespace, path));
    }

    @Override
    public Identity create(String id) {
        String[] identity = new String[]{"minecraft", id};
        int idx = id.indexOf(':');
        if (idx >= 0) {
            identity[1] = id.substring(idx + 1);
            if (idx >= 1) {
                identity[0] = id.substring(0, idx);
            }
        }
        return create(identity[0], identity[1]);
    }
}
