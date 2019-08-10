package com.hrznstudio.sandbox.event;

import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.server.SandboxServer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventDispatcher {
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap<>();
    private final Map<Class, List<Consumer>> eventMap = new LinkedHashMap<>();
    private final ExecutorService eventExecutor = Executors.newFixedThreadPool(3);

    public static EventDispatcher getServerDispatcher() {
        return SandboxServer.INSTANCE.getDispatcher();
    }

    public static EventDispatcher getClientDispatcher() {
        return SandboxClient.INSTANCE.getDispatcher();
    }

    public <T extends Event> void on(Class<T> eventClass, Consumer<T> consumer) {
        eventMap.computeIfAbsent(eventClass, c -> new LinkedList<>()).add(consumer);
    }

    public <T extends Event> T publish(T event) {
        Class<? extends Event> eventClass = event.getClass();
        List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
        boolean async = eventClass.isAnnotationPresent(Event.Async.class);
        if (async) {
            eventTypes.forEach(s -> eventMap.getOrDefault(s, Collections.emptyList()).forEach(consumer -> eventExecutor.execute(() -> consumer.accept(event))));
        } else {
            eventTypes.forEach(s -> eventMap.getOrDefault(s, Collections.emptyList()).forEach(consumer -> consumer.accept(event)));
        }
        event.complete();
        return event;
    }

    private static List<Class<?>> lookupAllEventTypes(Class<?> eventClass) {
        synchronized (eventTypesCache) {
            List<Class<?>> eventTypes = eventTypesCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList<>();
                Class<?> clazz = eventClass;
                while (clazz != null) {
                    eventTypes.add(clazz);
                    clazz = clazz.getSuperclass();
                    if (clazz == Object.class)
                        clazz = null;
                }
                eventTypesCache.put(eventClass, eventTypes);
            }
            return eventTypes;
        }
    }
}