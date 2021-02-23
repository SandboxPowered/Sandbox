package org.sandboxpowered.loader.util;

public class MojangUtil {
    /**
     * This will return b if it is equal to a, in cases where mojang require a specific instance of an object, such as UUID.
     */
    public static <T> T checkMojangity(T a, T b) {
        if (a.equals(b))
            return b;
        return a;
    }
}
