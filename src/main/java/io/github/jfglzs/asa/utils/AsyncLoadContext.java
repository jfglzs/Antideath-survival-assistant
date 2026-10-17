package io.github.jfglzs.asa.utils;

public final class AsyncLoadContext {
    private static volatile boolean active = false;

    private AsyncLoadContext() {
    }

    public static void begin() {
        active = true;
    }

    public static void end() {
        active = false;
    }

    public static boolean isActive() {
        return active;
    }
}
