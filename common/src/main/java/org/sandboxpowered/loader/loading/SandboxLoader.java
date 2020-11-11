package org.sandboxpowered.loader.loading;

import com.github.zafarkhaja.semver.Parser;
import com.github.zafarkhaja.semver.expr.Expression;
import com.github.zafarkhaja.semver.expr.ExpressionParser;
import com.google.common.collect.ImmutableMap;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.internal.AddonSpec;

import java.util.*;
import java.util.function.Consumer;

public class SandboxLoader {
    private static final Parser<Expression> PARSER = ExpressionParser.newInstance();
    private final Map<String, AddonSpec> loadedAddons = new HashMap<>();
    private final Map<AddonSpec, Addon> addonMap = new HashMap<>();
    private final Map<AddonSpec, AddonSpecificAPIReference> addonAPIs = new HashMap<>();
    private final Map<AddonSpec, AddonSpecificRegistrarReference> addonRegistrars = new HashMap<>();

    private AddonSpecificRegistrarReference getRegistrarForAddon(AddonSpec spec) {
        return addonRegistrars.computeIfAbsent(spec, s -> new AddonSpecificRegistrarReference(s, this));
    }

    private AddonSpecificAPIReference getAPIForAddon(AddonSpec spec) {
        return addonAPIs.computeIfAbsent(spec, s -> new AddonSpecificAPIReference(s, this));
    }

    public Optional<AddonSpec> getAddon(String addonId) {
        return Optional.ofNullable(loadedAddons.get(addonId));
    }

    public void loadAddon(AddonSpec info, Addon addon) {
        loadedAddons.put(info.getId(), info);
        addonMap.put(info, addon);
    }

    private List<AddonSpec> getLoadOrder() {
        Set<AddonSpec> visited = new HashSet<>();
        List<AddonSpec> loadOrder = new ArrayList<>();
        for (AddonSpec info : getAllAddons().keySet()) {
            handleDependencies(visited, loadOrder, info);
        }
        return loadOrder;
    }

    private void loopInOrder(Consumer<AddonSpec> consumer) {
        getLoadOrder().forEach(spec -> {
            if (!spec.getPlatformSupport(getPlatform()).canRun()) {
                throw new IllegalStateException(String.format("Addon %s cannot run on platform %s!", spec.getId(), getPlatform().toString()));
            }
            try {
                consumer.accept(spec);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Loading for addon %s failed: %s", spec.getId(), e.getMessage()), e);
            }
        });
    }

    public Identity getPlatform() {
        return Identity.of("sandbox", "test");
    }

    private void handleDependencies(Set<AddonSpec> visited, List<AddonSpec> order, AddonSpec spec) {
        if (visited.contains(spec)) return;
        visited.add(spec);
        Map<String, String> dependencies = spec.getDependencies();
        for (String dep : dependencies.keySet()) {
            Optional<AddonSpec> optionalDep = getAddon(dep);
            if (!optionalDep.isPresent())
                throw new IllegalStateException(String.format("Addon %s depends on other addon %s that isn't loaded!", spec.getId(), dep));
            AddonSpec dependency = optionalDep.get();
            String versionString = dependencies.get(dep);
            Expression version = PARSER.parse(versionString);
            if (!version.interpret(dependency.getVersion()))
                throw new IllegalStateException(String.format("Addon %s depends on %s version %s but found version %s instead!", spec.getId(), dep, versionString, dependency.getVersion().toString()));
            handleDependencies(visited, order, dependency);
        }
        order.add(spec);
    }

    public Map<AddonSpec, Addon> getAllAddons() {
        return ImmutableMap.copyOf(addonMap);
    }

    public void load() {
        loopInOrder(spec -> addonMap.get(spec).init(getAPIForAddon(spec)));
        loopInOrder(spec -> addonMap.get(spec).register(getAPIForAddon(spec), getRegistrarForAddon(spec)));
        loopInOrder(spec -> addonMap.get(spec).finishLoad(getAPIForAddon(spec)));
    }

    public boolean isAddonLoaded(String addonId) {
        return getAddon(addonId).isPresent();
    }

    public Side getSide() {
        return Side.SERVER;
    }

    public boolean isExternalModLoaded(String loader, String modId) {
        return false;
    }
}