package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void runAsync(Runnable runnable) {
        threadPool.submit(runnable);
    }

    public static void runOnClientThread(Runnable runnable) {
        mc.submit(runnable);
    }
}
