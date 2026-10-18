package io.github.jfglzs.asa.utils;

import io.github.jfglzs.asa.AsaMod;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThreadUtils {
    public static final Minecraft MC = Minecraft.getInstance();
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "ASA-TASK");
        t.setDaemon(true);
        return t;
    });

    public static void schedule(int delayMs, Runnable onClient) {
        SCHEDULER.schedule(onClient, delayMs, TimeUnit.MILLISECONDS);
    }

    public static void runOnTaskThread(Runnable toRun) {
        schedule(0, toRun);
    }

    public static <T> void parallel(List<T> list, int threads, int batchSize, Consumer<List<T>> processor) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        try {
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < list.size(); i += batchSize) {
                int from = i;
                int to = Math.min(i + batchSize, list.size());

                futures.add(executor.submit(() -> processor.accept(list.subList(from, to))));
            }

            for (Future<?> future : futures) {
                future.get();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        finally {
            executor.shutdown();
        }
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

    public static Future<?> runAsync(Runnable toRun) {
        return THREAD_POOL.submit(toRun);
    }
}
