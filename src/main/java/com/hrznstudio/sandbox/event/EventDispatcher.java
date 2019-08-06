package com.hrznstudio.sandbox.event;

import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.server.SandboxServer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

public class EventDispatcher {
    private final FluxProcessor<Event, Event> processor;

    public EventDispatcher(FluxProcessor<Event, Event> processor) {
        this.processor = processor;
    }

    public static EventDispatcher getServerDispatcher() {
        return SandboxServer.INSTANCE.getDispatcher();
    }

    public static EventDispatcher getClientDispatcher() {
        return SandboxClient.INSTANCE.getDispatcher();
    }

    public <T extends Event> Flux<T> on(Class<T> eventClass) {
        boolean isAsync = AsyncEvent.class.isAssignableFrom(eventClass);
        return processor.publishOn(isAsync ? Schedulers.parallel() : Schedulers.immediate()).ofType(eventClass);
    }

    public <T extends Event> void subscribe(Class<T> eventClass, Consumer<T> consumer) {
        on(eventClass).subscribe(consumer);
    }

    public <T extends Event> T publish(T event) {
        processor.onNext(event);
        event.complete();
        return event;
    }
}