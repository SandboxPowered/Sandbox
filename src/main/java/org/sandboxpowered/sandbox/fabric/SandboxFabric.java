package org.sandboxpowered.sandbox.fabric;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.Log;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.internal.Sandbox;
import org.sandboxpowered.sandbox.fabric.util.AddonLog;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SandboxFabric implements Sandbox {
    private static final Identity PLATFORM = Identity.of("sandboxpowered", "fabric");

    private final Map<String, AddonInfo> loadedAddons = new LinkedHashMap<>();
    private final Map<AddonInfo, Addon> addonMap = new LinkedHashMap<>();
    private final Map<AddonInfo, SandboxAPI> addonAPIs = new LinkedHashMap<>();
    private final Map<AddonInfo, Registrar> addonRegistrars = new LinkedHashMap<>();

    @Override
    public Identity getPlatform() {
        return PLATFORM;
    }

    @Override
    public Optional<AddonInfo> getAddon(String addonId) {
        return Optional.ofNullable(loadedAddons.get(addonId));
    }

    @Override
    public Map<AddonInfo, Addon> getAllAddons() {
        return ImmutableMap.copyOf(addonMap);
    }

    @Override
    public SandboxAPI getAPIFor(AddonInfo info) {
        return addonAPIs.computeIfAbsent(info, AddonSpecificAPI::new);
    }

    @Override
    public Registrar getRegistrarFor(AddonInfo info) {
        return addonRegistrars.computeIfAbsent(info, AddonSpecificRegistrar::new);
    }

    public class AddonSpecificRegistrar implements Registrar {
        private final AddonInfo info;

        public AddonSpecificRegistrar(AddonInfo info) {
            this.info = info;
        }

        @Override
        public AddonInfo getSourceAddon() {
            return info;
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Class<T> tClass) {
            return getEntry(identity, Registry.getRegistryFromType(tClass));
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Registry<T> registry) {
            return registry.get(identity);
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> register(Identity identity, T content) {
            return Registry.getRegistryFromType(content.getContentType()).register(identity, content);
        }

        @Override
        public <T extends Service> Optional<T> getRegistrarService(Class<T> tClass) {
            return Optional.empty(); //TODO: Implement registrar services
        }
    }

    public class AddonSpecificAPI implements SandboxAPI {
        private final AddonInfo info;
        private final Log log;

        public AddonSpecificAPI(AddonInfo info) {
            this.info = info;
            this.log = new AddonLog(info);
        }

        @Override
        public boolean isAddonLoaded(String addonId) {
            return getAddon(addonId).isPresent();
        }

        @Override
        public boolean isExternalModLoaded(String loader, String modId) {
            if (!"fabric".equals(loader))
                return false;
            if (modId == null || modId.isEmpty())
                return true;
            return FabricLoader.getInstance().isModLoaded(modId);
        }

        @Override
        public AddonInfo getSourceAddon() {
            return info;
        }

        @Override
        public Side getSide() {
            return Side.CLIENT; //TODO
        }

        @Override
        public Log getLog() {
            return log;
        }
    }
}