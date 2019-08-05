package com.hrznstudio.sandbox.event;

import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.server.SandboxServer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.scheduler.Scheduler;

public class EventDispatcher {
    private final FluxProcessor<Event, Event> processor;
    private final Scheduler scheduler;

    public EventDispatcher(FluxProcessor<Event, Event> processor, Scheduler scheduler) {
        this.processor = processor;
        this.scheduler = scheduler;
    }

    public static EventDispatcher getServerDispatcher() {
        return SandboxServer.INSTANCE.getDispatcher();
    }

    public static EventDispatcher getClientDispatcher() {
        return SandboxClient.INSTANCE.getDispatcher();
    }

    public <T extends Event> Flux<T> on(Class<T> eventClass) {
        return processor.publishOn(scheduler).ofType(eventClass);
    }

    public void publish(Event event) {
        processor.onNext(event);
    }
}