package org.sandboxpowered.sandbox.fabric.event;

import org.sandboxpowered.sandbox.api.event.Event;
import org.sandboxpowered.sandbox.api.util.ClassUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventDispatcher {
    private static final Map<Class, List<Consumer>> eventMap = new LinkedHashMap<>();
    private static final ExecutorService eventExecutor = Executors.newFixedThreadPool(3);

    public static <T extends Event> void on(Class<T> eventClass, Consumer<T> consumer) {
        eventMap.computeIfAbsent(eventClass, e -> new LinkedList<>()).add(consumer);
    }

    public static <T extends Event> T publish(T event) {
        Class<? extends Event> eventClass = event.getClass();
        List<Class<?>> eventTypes = ClassUtil.lookupAllSuper(eventClass);
        boolean async = event.isAsync() && !event.isCancellable();
        eventTypes.forEach(s -> (eventMap.getOrDefault(s, Collections.emptyList())).forEach(consumer -> {
            if (async) {
                eventExecutor.execute(() -> consumer.accept(event));
            } else {
                consumer.accept(event);
            }
        }));
        event.complete();
        return event;
    }

    public static void clear() {
        eventMap.clear();
    }
}