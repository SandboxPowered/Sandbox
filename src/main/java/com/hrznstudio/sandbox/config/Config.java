package com.hrznstudio.sandbox.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.nio.file.Path;

public class Config {
    private CommentedFileConfig config;

    public Config(Path path) {
        this.config = CommentedFileConfig.builder(path).autosave().autoreload().build();
    }

    public <T> ConfigValue<T> get(String path) {
        return new ConfigValue<>(config, path);
    }
}
