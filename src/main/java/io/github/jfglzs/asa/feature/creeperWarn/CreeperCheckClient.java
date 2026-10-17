package io.github.jfglzs.asa.feature.creeperWarn;

import io.github.jfglzs.asa.utils.ChatHelper;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static io.github.jfglzs.asa.config.Configs.Functions.CREEPER_WARN_RANGE;

public class CreeperCheckClient {
    public static void tick(Minecraft mc) {
        var creepers = CreeperCheckClient.isCreeperNearby(mc);
        if (creepers != null && ! creepers.isEmpty()) {
            ChatHelper.actionBarWithSound(ChatHelper.c("r [ASA] 苦力怕来了距离 %.1f 米".formatted(creepers.getFirst().distanceTo(mc.player))), SoundEvents.TNT_PRIMED);
        }
    }

    public static List<Creeper> isCreeperNearby(Minecraft mc) {
        LocalPlayer player = mc.player;
        Level world = mc.level;

        if (world == null)
            return null;
        if (! PlayerUtils.isSurvivalMode(player))
            return null;

        double range = CREEPER_WARN_RANGE.getDoubleValue();
        AABB box = player.getBoundingBox().inflate(range);

        return world.getEntitiesOfClass(Creeper.class, box, (creeper) -> true);
    }
}