package com.hrznstudio.sandbox.mixin.common.block;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.block.state.StateFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.state.StateFactory.class)
@Unique
public abstract class MixinStateFactory implements SandboxInternal.StateFactory {
    private StateFactory sandboxFactory;

    @Override
    public StateFactory getSboxFactory() {
        return sandboxFactory;
    }

    @Override
    public void setSboxFactory(StateFactory factory) {
        this.sandboxFactory = factory;
    }
}