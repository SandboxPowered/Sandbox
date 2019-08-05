package com.hrznstudio.sandbox.api;

import java.util.Optional;

public class Gamemode {
    private final String name, richImage, displayName;

    public Gamemode(Properties properties) {
        this.name = properties.name;
        this.richImage = properties.richImage;
        this.displayName = properties.displayName;
    }

    public static Properties properties(String name) {
        return new Properties(name);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getRichImage() {
        return Optional.ofNullable(richImage);
    }

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public static class Properties {
        private final String name;
        private String richImage, displayName;

        private Properties(String name) {
            this.name = name;
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