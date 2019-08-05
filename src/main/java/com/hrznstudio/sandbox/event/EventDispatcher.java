package com.hrznstudio.sandbox.event;

import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.server.SandboxServer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.scheduler.Schedulers;

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
        return processor.publishOn(Schedulers.immediate()).ofType(eventClass);
    }

    public <T extends Event> Flux<T> onAsync(Class<T> eventClass) {
        return processor.publishOn(Schedulers.parallel()).ofType(eventClass);
    }

    public void publish(Event event) {
        processor.onNext(event);
    }
}