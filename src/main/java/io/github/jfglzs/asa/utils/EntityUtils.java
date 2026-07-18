package io.github.jfglzs.asa.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class EntityUtils {
    public static boolean isPlayer(Object object) {
        return object instanceof Player;
    }

    public static boolean is(Object object, EntityType<?> type) {
        if (object instanceof Entity entity) {
            return entity.getType() == type;
        }
        return false;
    }

    public static EntityType<?> getType(Entity entity) {
        return entity.getType();
    }
}
