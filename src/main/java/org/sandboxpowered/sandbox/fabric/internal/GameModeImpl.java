package org.sandboxpowered.sandbox.fabric.internal;

import org.sandboxpowered.sandbox.api.game.GameMode;
import org.sandboxpowered.sandbox.api.util.Identity;

public class GameModeImpl implements GameMode {
    private final Identity identity;
    private final String displayName;
    private final Mono<String> richImage;

    public GameModeImpl(Properties properties) {
        this.identity = properties.identity;
        this.richImage = Mono.ofNullable(properties.richImage);
        this.displayName = properties.displayName;
    }

    public static Properties properties(Identity identity) {
        return new Properties(identity);
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Mono<String> getRichImage() {
        return richImage;
    }

    public static class Properties {
        private final Identity identity;
        private String richImage, displayName;

        private Properties(Identity identity) {
            this.identity = identity;
        }

        public Properties setRichImage(String richImage) {
            this.richImage = richImage;
            return this;
        }

        public Properties setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
    }
}