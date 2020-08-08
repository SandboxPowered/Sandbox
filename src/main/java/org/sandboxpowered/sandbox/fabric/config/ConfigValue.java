package org.sandboxpowered.sandbox.fabric.config;

import com.electronwill.nightconfig.core.CommentedConfig;

public class ConfigValue<T> {
    private final CommentedConfig config;
    private final String path;

    public ConfigValue(CommentedConfig config, String path) {
        this.config = config;
        this.path = path;
    }

    public T get() {
        return config.get(path);
    }

    // FIXME find a better way for this
    // we cannot get rid of the ugliness without making the generic type of this class extend enum
    public <V extends Enum<V>> V getEnum(Class<V> enumType) {
        return config.getEnum(path, enumType);
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