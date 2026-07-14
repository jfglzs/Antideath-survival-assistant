package io.github.jfglzs.asa.events;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.Set;
import java.util.function.Function;

public class Event<T> {
    private final Set<Function<T, Boolean>> listeners = new ReferenceOpenHashSet<>();

    public void register(Function<T, Boolean> event) {
        this.listeners.add(event);
    }

    public boolean update(T obj) {
        for (Function<T, Boolean> listener : listeners) {
            try {
                if (listener.apply(obj)) {
                    return true;
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
