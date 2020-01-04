package org.sandboxpowered.sandbox.fabric.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    private static Field mods;

    public static <A> void setPrivateField(Class<A> c, A obj, String field, Object val) throws NoSuchFieldException, IllegalAccessException {
        Field m = c.getDeclaredField(field);
        m.setAccessible(true);
        if (Modifier.isFinal(m.getModifiers()))
            getMods().setInt(m, m.getModifiers() & ~Modifier.FINAL);
        m.set(obj, val);
    }

    private static Field getMods() throws NoSuchFieldException {
        if (mods == null) {
            mods = Field.class.getDeclaredField("modifiers");
            mods.setAccessible(true);
        }
        return mods;
    }
}