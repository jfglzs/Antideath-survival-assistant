package io.github.jfglzs.asa.events;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.Set;
import java.util.function.Consumer;

public class Event<T> {
    private final Set<Consumer<T>> listeners = new ReferenceOpenHashSet<>();

    public void register(Consumer<T> event) {
        this.listeners.add(event);
    }

    public void update(T obj) {
        this.listeners.forEach(l -> {
            try {
                l.accept(obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
