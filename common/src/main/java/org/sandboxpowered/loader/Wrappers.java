package org.sandboxpowered.loader;

import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.content.Content;

import java.util.function.Function;

public class Wrappers {
    public static Wrapper<Block, net.minecraft.world.level.block.Block> BLOCK = new Wrapper<>(
            block -> null,
            block -> null
    );

    private static class Wrapper<S extends Content<S>,V> {
        private final Function<S, V> sandboxToVanilla;
        private final Function<V, S> vanillaToSandbox;

        public Wrapper(Function<S, V> sandboxToVanilla, Function<V, S> vanillaToSandbox) {
            this.sandboxToVanilla = sandboxToVanilla;
            this.vanillaToSandbox = vanillaToSandbox;
        }

        public V toVanilla(S sandbox) {
            return sandboxToVanilla.apply(sandbox);
        }

        public S toSandbox(V vanilla) {
            return vanillaToSandbox.apply(vanilla);
        }
    }
}
