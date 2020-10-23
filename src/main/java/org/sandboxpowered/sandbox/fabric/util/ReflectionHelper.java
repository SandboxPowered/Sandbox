package org.sandboxpowered.sandbox.fabric.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionHelper {
    private static Field mods;

    private ReflectionHelper() {
    }

    public static <A> void setPrivateField(Class<A> c, A obj, String[] fields, Object val) throws NoSuchFieldException, IllegalAccessException {
        boolean done = false;
        for (String field : fields) {
            try {
                setPrivateField(c, obj, field, val);
                done = true;
                break;
            } catch (NoSuchFieldException ignored) {
            }
        }
        if (!done)
            throw new NoSuchFieldException(Arrays.toString(fields));
    }

    @SuppressWarnings("java:S3011")
    public static <A> void setPrivateField(Class<A> c, A obj, String field, Object val) throws NoSuchFieldException, IllegalAccessException {
        Field m = c.getDeclaredField(field);
        m.setAccessible(true);
        if (Modifier.isFinal(m.getModifiers()))
            getMods().setInt(m, m.getModifiers() & ~Modifier.FINAL);
        m.set(obj, val);
    }

    @SuppressWarnings("java:S3011")
    private static Field getMods() throws NoSuchFieldException {
        if (mods == null) {
            mods = Field.class.getDeclaredField("modifiers");
            mods.setAccessible(true);
        }
        return mods;
    }
}