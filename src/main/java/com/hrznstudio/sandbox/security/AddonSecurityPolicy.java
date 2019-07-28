package com.hrznstudio.sandbox.security;

import java.security.*;

public class AddonSecurityPolicy extends Policy {
    /**
     * Return a PermissionCollection object containing the set of
     * permissions granted to the specified ProtectionDomain.
     */
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        if (isAddon(domain)) {
            return addonPermissions();
        } else {
            return applicationPermissions();
        }
    }

    /**
     * Identifies if the domain belongs to an addon
     */
    private boolean isAddon(ProtectionDomain domain) {
        // Identify the classloader of the protection domain
        // The AddonClassLoader is assumed to be the one that loaded the addon
        return domain.getClassLoader() instanceof AddonClassLoader;
    }

    private PermissionCollection addonPermissions() {
        // Empty permissions = No permissions
        return new Permissions();
    }

    private PermissionCollection applicationPermissions() {
        // Grant full access to the application
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}