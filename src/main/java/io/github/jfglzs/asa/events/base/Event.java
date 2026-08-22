package io.github.jfglzs.asa.events.base;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<T> {
    private final Set<Consumer<T>> listeners = new ReferenceOpenHashSet<>();

    public void register(Consumer<T> event) {
        this.listeners.add(event);
    }

    public void update(T obj) {
        for (Consumer<T> listener : listeners) {
            try {
                listener.accept(obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
