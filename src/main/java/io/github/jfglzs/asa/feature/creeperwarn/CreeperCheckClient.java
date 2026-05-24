package io.github.jfglzs.asa.feature.creeperwarn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

import static io.github.jfglzs.asa.config.Configs.CREEPER_WARN_RANGE;
import static io.github.jfglzs.asa.utils.MCUtils.getMinecraftClient;

public class CreeperCheckClient {
    public static boolean isCreeperNearby() {
        Minecraft client = getMinecraftClient();
        LocalPlayer player = client.player;
        Level world = client.level;

        if (player == null || world == null) return false;
        if (player.isCreative() || player.isSpectator()) return false;

        double range = CREEPER_WARN_RANGE.getDoubleValue();
        AABB box = player.getBoundingBox().inflate(range);

        return !world.getEntitiesOfClass(Creeper.class, box, (creeper) -> true).isEmpty();
    }
}