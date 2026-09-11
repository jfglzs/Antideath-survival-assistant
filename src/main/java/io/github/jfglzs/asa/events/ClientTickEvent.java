package io.github.jfglzs.asa.events;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class ClientTickEvent {
    private static final List<TickTask> tickTasks = new ObjectArrayList<>();
    private static int tickCount = 1;

    public static void register(IntPredicate condition, ClientTickCallback callback) {
        tickTasks.add(TickTask.of(condition, callback));
    }

    public static void register(IntPredicate condition, Runnable runnable) {
        tickTasks.add(TickTask.of(condition, client ->  runnable.run()));
    }

    public static void register(Runnable runnable) {
        tickTasks.add(TickTask.of(runnable));
    }

    public static void register(ClientTickCallback callback) {
        tickTasks.add(TickTask.of(callback));
    }

    public static void onUpdate(Minecraft client) {
        tickCount++;
        for (TickTask task : tickTasks) {
            if (task.condition.test(tickCount))
                task.callback.onTick(client);
        }
    }

    public interface ClientTickCallback {
        void onTick(Minecraft client);
    }

    record TickTask(IntPredicate condition, ClientTickCallback callback) {
        public static TickTask of(IntPredicate condition, ClientTickCallback callback) {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(callback);
            return new TickTask(condition, callback);
        }

        public static TickTask of(Runnable runnable) {
            return new TickTask((i) -> true, client ->  runnable.run());
        }

        public static TickTask of(ClientTickCallback callback) {
            return new TickTask((i) -> true, callback);
        }
    }
}
