package org.sandboxpowered.sandbox.fabric.impl;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.github.zafarkhaja.semver.Version;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.addon.LoadingSide;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.annotation.Internal;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Internal
public class AddonSpec implements AddonInfo {
    private static final ConfigSpec CONFIG_SPEC = new ConfigSpec();
    private static final Pattern MODID_PATTERN = Pattern.compile("[a-z0-9-_]{4,15}");
    private static final Predicate<String> MODID_PREDICATE = MODID_PATTERN.asPredicate();

    static {
        CONFIG_SPEC.define("id", "");
        CONFIG_SPEC.define("version", "");
        CONFIG_SPEC.define("title", "");
        CONFIG_SPEC.define("description", "");
        CONFIG_SPEC.define("entrypoint", "");
        CONFIG_SPEC.define("authors", Collections.emptyList());
        CONFIG_SPEC.define("url", "");
        CONFIG_SPEC.define("dependencies", Collections.emptyMap());
        CONFIG_SPEC.define("custom", Config.inMemoryUniversal());
        CONFIG_SPEC.defineOfClass("side", LoadingSide.COMMON, LoadingSide.class);
        CONFIG_SPEC.define("platforms", Collections.emptyMap());
    }

    //metadata
    private final String id;
    private final Version version;
    private final String title;
    private final String description;
    private final List<String> authors;
    private final String url;
    private final Map<String, String> dependencies;
    private final Config customProperties;
    private final LoadingSide side;
    private final Map<String, Boolean> platforms;
    //internal info
    private final String mainClass;
    private final URL path;

    private AddonSpec(String id, Version version, @Nullable String title, String description, List<String> authors, String url, Map<String, String> dependencies, Config customProperties, LoadingSide side, Map<String, Boolean> platforms, String mainClass, URL path) {
        if (!MODID_PREDICATE.test(id))
            throw new IllegalArgumentException(String.format("Addon ID '%s' does not match regex requirement '%s'", id, MODID_PATTERN.pattern()));
        this.id = id;
        this.version = version;
        if (title == null || title.isEmpty())
            title = id;
        this.title = title;
        this.description = description;
        if (authors.isEmpty())
            throw new IllegalArgumentException(String.format("Addon %s does not define any authors!", id));
        this.authors = authors;
        this.url = url;
        this.dependencies = dependencies;
        this.customProperties = customProperties;
        this.side = side;
        this.platforms = platforms;
        this.mainClass = mainClass;
        this.path = path;
    }

    public static AddonSpec from(Config config, URL path) {
        CONFIG_SPEC.correct(config);
        String id = config.get("id");
        if (id.equals(""))
            throw new IllegalArgumentException(String.format("Addon at path %s does not define an ID!", path.toString()));
        String verString = config.get("version");
        if (verString.equals(""))
            throw new IllegalArgumentException(String.format("Addon %s does not define a version!", id));
        Version version = Version.valueOf(verString);
        String title = config.get("title");
        String description = config.get("description");
        String mainClass = config.get("entrypoint");
        if (mainClass.equals(""))
            throw new IllegalArgumentException(String.format("Addon %s does not define an entrypoint!", id));
        List<String> authors = config.get("authors");
        String url = config.get("url");
        Map<String, String> dependencies = config.get("dependencies");
        Config customProperties = config.get("custom");
        LoadingSide side = config.getEnum("side", LoadingSide.class);
        Map<String, Boolean> platforms = config.get("platforms");
        return new AddonSpec(id, version, title, description, authors, url, dependencies, customProperties, side, platforms, mainClass, path);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public LoadingSide getSide() {
        return side;
    }

    @Override
    public Map<String, String> getDependencies() {
        return dependencies;
    }

    @Override
    public Config getCustomProperties() {
        return customProperties;
    }

    @Override
    public Map<String, Boolean> getPlatforms() {
        return platforms;
    }

    @Override
    public AddonSpec.PlatformSupport getPlatformSupport(Identity platform) {
        return platforms.containsKey(platform.toString()) ? platforms.get(platform.toString()) ? AddonSpec.PlatformSupport.YES : AddonSpec.PlatformSupport.NO : AddonSpec.PlatformSupport.MAYBE;
    }

    @Override
    public String getMainClass() {
        return mainClass;
    }

    @Override
    public URL getPath() {
        return path;
    }
}
