package com.hrznstudio.sandbox.util;

import net.minecraft.server.dedicated.command.StopCommand;

import java.lang.reflect.Method;

public class ReflectionHelper {
    public static Method getPrivateMethod(Class c, String method, Class... classes) throws NoSuchMethodException {
        Method m = c.getDeclaredMethod(method, classes);
        m.setAccessible(true);
        return m;
    }
}
