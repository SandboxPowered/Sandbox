package com.hrznstudio.sandbox.config;

import com.electronwill.nightconfig.core.CommentedConfig;

public class ConfigValue<T> {
    private CommentedConfig config;
    private String path;

    public ConfigValue(CommentedConfig config, String path) {
        this.config = config;
        this.path = path;
    }

    public T get() {
        return config.get(path);
    }

    public void set(T val) {
        config.set(path, val);
    }

    public void add(T val) {
        config.add(path, val);
    }

    public void setComment(String comment) {
        config.setComment(path, comment);
    }

    public void removeComment() {
        config.removeComment(path);
    }
}