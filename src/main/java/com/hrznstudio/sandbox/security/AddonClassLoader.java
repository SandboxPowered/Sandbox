package com.hrznstudio.sandbox.security;

import java.io.FilePermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;

public class AddonClassLoader extends SecureClassLoader {
    public AddonClassLoader() {
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        PermissionCollection pc = super.getPermissions(codesource);
        pc.add(new FilePermission("-", "read")); // Can read everything from current dir
        pc.add(new FilePermission("data/-", "write")); // Can write everything inside addon data dir, could in future make this block access to data dirs beloning to other addons
        return pc;
    }
}