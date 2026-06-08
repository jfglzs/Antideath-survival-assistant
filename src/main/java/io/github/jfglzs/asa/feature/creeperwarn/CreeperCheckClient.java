package io.github.jfglzs.asa.feature.creeperwarn;

import io.github.jfglzs.asa.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

import static io.github.jfglzs.asa.config.Configs.CREEPER_WARN_RANGE;
import static io.github.jfglzs.asa.utils.MCUtils.getMinecraftClient;

public class CreeperCheckClient {
    public static void creeperWarner(Minecraft mc) {
        if (CreeperCheckClient.isCreeperNearby(mc)) {
            ChatUtils.sendMessWithSound("§c苦力怕来了!!!!!!!", SoundEvents.TNT_PRIMED, 1, 1);
        }
    }

    public static boolean isCreeperNearby(Minecraft mc) {
        LocalPlayer player = mc.player;
        Level world = mc.level;

        if (player == null || world == null) return false;
        if (player.isCreative() || player.isSpectator()) return false;

        double range = CREEPER_WARN_RANGE.getDoubleValue();
        AABB box = player.getBoundingBox().inflate(range);

        return !world.getEntitiesOfClass(Creeper.class, box, (creeper) -> true).isEmpty();
    }
}