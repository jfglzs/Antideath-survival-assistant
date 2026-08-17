package io.github.jfglzs.asa.events.base;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ReturnableEvent<T> {
    private final Set<Function<T, T>> handlers  = new HashSet<>();

    public void register(Function<T, T> event) {
        this.handlers.add(event);
    }

    public T update(T obj) {
        T result = obj;
        for (Function<T, T> handler : handlers) {
            try {
                result = handler.apply(result);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
