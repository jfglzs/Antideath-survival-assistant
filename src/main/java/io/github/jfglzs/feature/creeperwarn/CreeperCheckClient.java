package io.github.jfglzs.feature.creeperwarn;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import static io.github.jfglzs.config.Configs.CREEPER_WARN_RANGE;

public class CreeperCheckClient
{
    public static boolean isCreeperNearby()
    {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        World world = client.world;

        if (player == null || world == null) return false;
        if (player.isCreative()) return false;
        if (player.isSpectator()) return false;

        double range = CREEPER_WARN_RANGE.getDoubleValue();
        Box box = player.getBoundingBox().expand(range);

        return !world.getEntitiesByClass(CreeperEntity.class, box, (creeper) -> true).isEmpty();
    }
}