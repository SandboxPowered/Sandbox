package com.hrznstudio.sandbox.api;

import java.util.Optional;

public class Gamemode {
    private final String name, richImage, displayName;

    public Gamemode(Properties properties) {
        this.name = properties.name;
        this.richImage = properties.richImage;
        this.displayName = properties.displayName;
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

    public static Properties properties(String name) {
        return new Properties(name);
    }

    public static class Properties {
        private final String name;

        private Properties(String name) {
            this.name = name;
        }

        private String richImage, displayName;

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