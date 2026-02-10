package io.jfglzs.asa.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static Object lock = new Object();

    public static ExecutorService threadPool = Executors.newCachedThreadPool();
}
