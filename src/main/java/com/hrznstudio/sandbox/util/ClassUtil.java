package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.event.Event;

import java.lang.annotation.Annotation;
import java.util.*;

public class ClassUtil {
    private static final Map<Class<?>, Map<Class<? extends Annotation>, Boolean>> annotationCache = new LinkedHashMap<>();
    private static final Map<Class<?>, List<Class<?>>> superCache = new HashMap<>();

    public static boolean isAnnotationPresent(Class<? extends Event> aClass, Class<? extends Annotation> annotation) {
        return annotationCache.computeIfAbsent(aClass, a -> new LinkedHashMap<>()).computeIfAbsent(annotation, annotationClass -> {
            List<Class<?>> classes = lookupAllSuper(aClass);
            for (Class<?> c : classes)
                if (c.isAnnotationPresent(annotationClass))
                    return true;
            return false;
        });
    }


    public static List<Class<?>> lookupAllSuper(Class<?> eventClass) {
        synchronized (superCache) {
            List<Class<?>> eventTypes = superCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList<>();
                Class<?> clazz = eventClass;
                while (clazz != null) {
                    eventTypes.add(clazz);
                    clazz = clazz.getSuperclass();
                    if (clazz == Object.class)
                        clazz = null;
                }
                superCache.put(eventClass, eventTypes);
            }
            return eventTypes;
        }
    }
}