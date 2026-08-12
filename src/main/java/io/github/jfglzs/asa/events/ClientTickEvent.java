package io.github.jfglzs.asa.events;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class ClientTickEvent {
    private static int tickCount = 1;
    private static final List<TickTask> tickTasks = new ObjectArrayList<>();

    public static void register(IntPredicate condition, ClientTickCallback callback) {
        tickTasks.add(TickTask.of(condition, callback));
    }

    ;

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

    record TickTask(IntPredicate condition, ClientTickCallback callback) {
        public static TickTask of(IntPredicate condition, ClientTickCallback callback) {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(callback);
            return new TickTask(condition, callback);
        }
    }
}
