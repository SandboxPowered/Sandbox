package org.sandboxpowered.sandbox.fabric.security;

import java.security.*;

public class AddonSecurityPolicy extends Policy {

    private static boolean checkingAddon;

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

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION; // Only allow protection domain
    }

    /**
     * Identifies if the domain belongs to an addon
     */
    private boolean isAddon(ProtectionDomain domain) {
        // Identify the classloader of the protection domain
        // The AddonClassLoader is assumed to be the one that loaded the addon
        if (checkingAddon)
            return false;
        checkingAddon = true;
        ClassLoader loader = domain.getClassLoader();
        boolean ret = loader instanceof AddonClassLoader;
        checkingAddon = false;
        return ret;
    }

    private PermissionCollection addonPermissions() {
        return new Permissions();
    }

    private PermissionCollection applicationPermissions() {
        // Grant full access to the application
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}