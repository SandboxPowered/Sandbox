package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import net.minecraft.state.State;
import org.sandboxpowered.api.state.PropertyContainer;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.wrapper.StateFactoryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.state.StateManager.class)
public abstract class MixinStateManager<O, S extends PropertyContainer<S>> implements SandboxInternal.StateFactory<O, S> {
    private StateFactory<O, S> sandboxFactory;

    @Override
    public StateFactory<O, S> getSboxFactory() {
        return sandboxFactory;
    }

    @Override
    public void setSboxFactory(StateFactory<O, S> factory) {
        this.sandboxFactory = factory;
    }

    @Mixin(net.minecraft.state.StateManager.Builder.class)
    public static abstract class MixinStateBuilder<O, S extends State<O, S> & PropertyContainer<S>> implements SandboxInternal.StateFactoryBuilder<O, S> {
        private StateFactory.Builder<O, S> sandboxBuilder;

        @SuppressWarnings("unchecked")
        private static <A, B> B cast(A a) {
            return (B) a;
        }

        @Inject(method = "<init>", at = @At("RETURN"))
        public void inject(CallbackInfo info) {
            setSboxBuilder(new StateFactoryImpl.BuilderImpl<>(cast(this)));
        }

        @Override
        public StateFactory.Builder<O, S> getSboxBuilder() {
            return sandboxBuilder;
        }

        @Override
        public void setSboxBuilder(StateFactory.Builder<O, S> builder) {
            sandboxBuilder = builder;
        }
    }
}
