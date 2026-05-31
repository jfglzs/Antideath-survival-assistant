package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ThreadUtils {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void runAsync(Runnable runnable) {
        threadPool.submit(runnable);
    }

    public static CompletableFuture<Void> runOnClientThread(Runnable runnable) {
        return mc.submit(runnable);
    }

    public static <T> T runOnClientThread(Supplier<T> supplier) {
        return mc.submit(supplier).join();
    }
}
