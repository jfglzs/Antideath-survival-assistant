package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ThreadUtils {
    public static final Minecraft MC = Minecraft.getInstance();
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    public static final Queue<Runnable> TASK_QUEUE = new ConcurrentLinkedQueue<>();

    public static void onClientEndTick() {
        while (!TASK_QUEUE.isEmpty()) {
            TASK_QUEUE.poll().run();
        }
    }

    public static void runAsync(Runnable runnable) {
        THREAD_POOL.submit(runnable);
    }

    public static CompletableFuture<Void> runOnClientThread(Runnable runnable) {
        return MC.submit(runnable);
    }

    public static <T> T runOnClientThread(Supplier<T> supplier) {
        return MC.submit(supplier).join();
    }

    public static void runOnClientEndTick(Runnable runnable) {
        TASK_QUEUE.offer(runnable);
    }
}
