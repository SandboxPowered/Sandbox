package org.sandboxpowered.sandbox.fabric.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.nio.file.Path;

public class Config {
    private final CommentedFileConfig fileConfig;

    public Config(Path path) {
        this.fileConfig = CommentedFileConfig.builder(path).autosave().build();
        fileConfig.load();
    }

    public <T> ConfigValue<T> get(String path) {
        return new ConfigValue<>(fileConfig, path);
    }

    public void save() {
        fileConfig.save();
    }
}
