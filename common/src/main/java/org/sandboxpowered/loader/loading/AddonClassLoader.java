package org.sandboxpowered.loader.loading;

import org.sandboxpowered.internal.AddonSpec;

import java.io.ByteArrayOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;

public class AddonClassLoader extends SecureClassLoader {
    static {
        registerAsParallelCapable();
    }

    private final AddonSpec spec;
    private CodeSource addonSource;
    private final ClassLoader original;
    private final URL url;
    private final DynamicURLClassLoader classLoader = (DynamicURLClassLoader) getParent();

    public AddonClassLoader(ClassLoader original, URL url, AddonSpec spec) {
        super(new DynamicURLClassLoader(new URL[]{url}, original));
        this.original=original;
        this.spec = spec;
        this.url = url;
    }

    public CodeSource getAddonSource() {
        if(addonSource == null)
            addonSource =  new CodeSource(url, (Certificate[]) null);
        return addonSource;
    }

    private byte[] readAddonClass(String name) throws IOException {
        String classFile = name.replace('.', '/') + ".class";
        InputStream inputStream = classLoader.getResourceAsStream(classFile);
        if (inputStream == null) {
            return null;
        }

        int a = inputStream.available();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(a < 32 ? 32768 : a);
        byte[] buffer = new byte[8192];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);

            if (c == null && !name.startsWith("java.")) {
                byte[] input = new byte[0];
                try {
                    input = readAddonClass(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (input != null) {
                    int delimiterIndex = name.lastIndexOf('.');
                    if (delimiterIndex > 0) {
                        String classPackage = name.substring(0, delimiterIndex);
                        if (getPackage(classPackage) == null) {
                            definePackage(classPackage, null, null, null, null, null, null, null);
                        }
                    }

                    c = defineClass(name, input, 0, input.length, getAddonSource());
                }
            }

            if (c == null) {
                c = original.loadClass(name);
            }

            if (resolve) {
                resolveClass(c);
            }

            return c;
        }
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions pc = new Permissions();
        pc.add(new FilePermission("data/-", "read")); // Can read everything from data dir
        pc.add(new FilePermission(String.format("data/%s/-", spec.getId()), "read,write,delete")); // Can write everything inside addon data dir
        return pc;
    }

    private static class DynamicURLClassLoader extends URLClassLoader {
        static {
            registerAsParallelCapable();
        }

        private DynamicURLClassLoader(URL[] urls, ClassLoader original) {
            super(urls, null);
        }

    }
}