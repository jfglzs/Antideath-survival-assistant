package io.github.jfglzs.asa.events;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ClientTickEvent {
    private static int tickCount = 1;
    private static final List<TickTask> tickTasks = new ArrayList<>();

    public static void register(Predicate<Integer> condition, ClientTickCallback callback) {
        tickTasks.add(new TickTask(condition, callback));
    };

    public static void onUpdate(Minecraft client) {
        tickCount++;
        for (TickTask task : tickTasks) {
            if (task.condition.test(tickCount)) {
                task.callback.onTick(client);
            }
        }
    }

    public interface ClientTickCallback {
        void onTick(Minecraft client);
    }

    record TickTask(Predicate<Integer> condition, ClientTickCallback callback) {
    }
}
