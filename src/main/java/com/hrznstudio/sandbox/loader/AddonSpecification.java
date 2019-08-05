package com.hrznstudio.sandbox.loader;

import com.electronwill.nightconfig.core.Config;
import com.github.zafarkhaja.semver.Version;

public class AddonSpecification {
    private final String id, name, description;
    private final Version version;

    public AddonSpecification(String id, String name, String description, Version version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public AddonSpecification(Config config) {
        this(config.get("id"), config.get("name"), config.get("description"), Version.valueOf(config.get("version")));
    }
}
