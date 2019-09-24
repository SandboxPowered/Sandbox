package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import org.sandboxpowered.sandbox.api.SandboxInternal;
import org.sandboxpowered.sandbox.api.state.StateFactory;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.StateFactoryImpl;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.state.StateFactory.class)
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

    @Mixin(net.minecraft.state.StateFactory.Builder.class)
    public static abstract class MixinStateBuilder implements SandboxInternal.StateFactoryBuilder {
        private StateFactory.Builder sandboxBuilder;

        @Inject(method = "<init>", at = @At("RETURN"))
        public void inject(CallbackInfo info) {
            setSboxBuilder(new StateFactoryImpl.BuilderImpl(WrappingUtil.cast(this, net.minecraft.state.StateFactory.Builder.class)));
        }

        @ModifyVariable(method = "add", at = @At("HEAD"), ordinal = 0)
        public Property<?>[] properties(Property<?>[] in) {
//            if (ArrayUtils.contains(in, Properties.WATERLOGGED)) {
//                in=ArrayUtils.add(ArrayUtils.removeElement(in, Properties.WATERLOGGED), SandboxProperties.PROPERTY_FLUIDLOGGABLE);
//            }
            return in;
        }

        @Override
        public StateFactory.Builder getSboxBuilder() {
            return sandboxBuilder;
        }

        @Override
        public void setSboxBuilder(StateFactory.Builder builder) {
            sandboxBuilder = builder;
        }
    }
}