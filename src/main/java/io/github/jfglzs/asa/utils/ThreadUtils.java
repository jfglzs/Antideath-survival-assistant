package io.github.jfglzs.asa.utils;

import io.github.jfglzs.asa.AsaMod;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class ThreadUtils {
    public static final Minecraft MC = Minecraft.getInstance();
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    public static final Queue<Runnable> TASK_QUEUE = new ConcurrentLinkedQueue<>();

    public static void init() {
        makeTaskThread();
    }

    public static void makeTaskThread() {
        Thread thread = new Thread(() -> {
            AsaMod.LOGGER.info("Starting TaskThread");
            while (true) {
                while (! TASK_QUEUE.isEmpty()) {
                    try {
                        Runnable task = TASK_QUEUE.poll();
                        if (task != null) {
                            task.run();
                        }
                    }
                    catch (Exception e) {
                        AsaMod.LOGGER.error("Exception in {}", Thread.currentThread().getName(), e);
                    }
                }
                if (! Thread.interrupted()) {
                    LockSupport.parkNanos(10000);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("ASA-TaskThread");
        thread.start();
    }

    public static <T> T runOnClientThread(Supplier<T> supplier) {
        Minecraft mc = MCUtils.getMinecraft();
        if (mc.isSameThread())
            return supplier.get();

        CompletableFuture<T> future = new CompletableFuture<>();
        mc.execute(() -> {
            try {
                future.complete(supplier.get());
            }
            catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });
        return future.join();
    }

    public static void runAsync(Runnable toRun) {
        THREAD_POOL.submit(toRun);
    }

    public static CompletableFuture<Void> runOnClientThread(Runnable toRun) {
        return MC.submit(toRun);
    }

    public static void runOnTaskThread(Runnable toRun) {
        TASK_QUEUE.offer(toRun);
    }
}
