package dev.rono.permissions.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import dev.rono.permissions.api.event.Event;
import dev.rono.permissions.api.event.group.GroupCreatedEvent;
import dev.rono.permissions.core.event.EventBusImpl;
import dev.rono.permissions.core.event.GroupCreatedEventImpl;
import dev.rono.permissions.core.model.GroupSnapshot;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class EventBusContractTest {
    @Test
    void publishesToExactAndAssignableSubscribers() {
        var exact = new AtomicInteger();
        var all = new AtomicInteger();
        var bus = new EventBusImpl(error -> fail(error));

        bus.subscribe(GroupCreatedEvent.class, ignored -> exact.incrementAndGet());
        bus.subscribe(Event.class, ignored -> all.incrementAndGet());

        bus.publish(new GroupCreatedEventImpl(group()));

        assertEquals(1, exact.get());
        assertEquals(1, all.get());
    }

    @Test
    void unsubscribeIsIdempotentAndStopsDelivery() {
        var count = new AtomicInteger();
        var bus = new EventBusImpl(error -> fail(error));

        var subscription = bus.subscribe(Event.class, ignored -> count.incrementAndGet());

        subscription.unsubscribe();
        subscription.unsubscribe();

        bus.publish(new GroupCreatedEventImpl(group()));

        assertEquals(0, count.get());
    }

    @Test
    void listenerFailureDoesNotPreventLaterListeners() {
        var errors = new AtomicInteger();
        var delivered = new AtomicInteger();
        var bus = new EventBusImpl(ignored -> errors.incrementAndGet());

        bus.subscribe(Event.class, ignored -> {
            throw new IllegalStateException("broken listener");
        });

        bus.subscribe(Event.class, ignored -> delivered.incrementAndGet());

        bus.publish(new GroupCreatedEventImpl(group()));

        assertEquals(1, errors.get());
        assertEquals(1, delivered.get());
    }

    private static GroupSnapshot group() {
        return new GroupSnapshot("staff", OptionalInt.empty(), Set.of(), Set.of(), Set.of());
    }
}
