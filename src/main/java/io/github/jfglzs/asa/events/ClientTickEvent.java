package io.github.jfglzs.asa.events;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.function.Predicate;

public class ClientTickEvent {
    private static int tickCount = 1;
    private static final Map<Predicate<Integer>, ClientTickCallback> callBacks = new Reference2ObjectOpenHashMap<>();

    public static void register(Predicate<Integer> condition, ClientTickCallback callback) {
        callBacks.put(condition, callback);
    };

    public static void onUpdate(Minecraft client) {
        for (Predicate<Integer> condition : callBacks.keySet()) {
            if (condition.test(tickCount)) callBacks.get(condition).onTick(client);
        }
        tickCount++;
    }

    public interface ClientTickCallback {
        void onTick(Minecraft client);
    }
}
