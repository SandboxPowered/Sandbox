package org.sandboxpowered.loader.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private final CommentedFileConfig config;

    public Config(String path) throws IOException {
        Path p = Paths.get(path);
        Files.createDirectories(p.getParent());
        this.config = CommentedFileConfig.builder(p).build();
        load();
    }

    public void setPathComment(String path, String comment) {
        config.setComment(path, comment);
    }

    public <T> GenericConfigValue<T> value(String s) {
        return new GenericConfigValue<>(s);
    }

    public <T extends Enum<T>> EnumConfigValue<T> enumValue(String s, Class<T> enumClass) {
        return new EnumConfigValue<>(s, enumClass);
    }

    public void save() {
        config.save();
    }

    public void load() {
        config.load();
    }

    private interface Value<T> {
        T get();

        void set(T value);

        void setIfAbsent(T value);

        String getComment();

        void setComment(String comment);
    }

    private abstract class BaseConfigValue<T> implements Value<T> {
        protected final String path;

        public BaseConfigValue(String path) {
            this.path = path;
        }

        @Override
        public String getComment() {
            return config.getComment(path);
        }

        @Override
        public void setComment(String comment) {
            config.setComment(path, comment);
        }

        @Override
        public void set(T value) {
            config.set(path, value);
        }

        @Override
        public void setIfAbsent(T value) {
            config.add(path, value);
        }
    }

    private class EnumConfigValue<T extends Enum<T>> extends BaseConfigValue<T> {
        private final Class<T> enumClass;

        public EnumConfigValue(String path, Class<T> enumClass) {
            super(path);
            this.enumClass = enumClass;
        }

        @Override
        public T get() {
            return config.getEnum(path, enumClass);
        }
    }

    public class GenericConfigValue<T> extends BaseConfigValue<T> {
        public GenericConfigValue(String path) {
            super(path);
        }

        @Override
        public T get() {
            return config.get(path);
        }

        public boolean getAsBoolean() {
            return config.get(path);
        }

        public int getAsInt() {
            return config.getInt(path);
        }
    }
}