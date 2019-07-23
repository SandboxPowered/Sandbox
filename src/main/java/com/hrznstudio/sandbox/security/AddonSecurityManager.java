package com.hrznstudio.sandbox.security;

import java.security.Permission;

public class AddonSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("Invalid access");
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        throw new SecurityException("Invalid access");
    }

}
