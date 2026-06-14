package io.github.jfglzs.asa.feature.creeperwarn;

import io.github.jfglzs.asa.utils.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

import java.util.List;

import static io.github.jfglzs.asa.config.Configs.CREEPER_WARN_RANGE;
import static io.github.jfglzs.asa.utils.MCUtils.getMinecraftClient;

public class CreeperCheckClient {
    public static void creeperWarner(Minecraft mc) {
        var creepers = CreeperCheckClient.isCreeperNearby(mc);
        if (creepers != null && !creepers.isEmpty()) {
            ChatUtils.sendMessWithSound(
                    ChatUtils.toComponent(
                            "苦力拍来了，距离您 %.2f 米".formatted(creepers.getFirst().distanceTo(mc.player))
                    ).copy().withStyle(ChatFormatting.RED),
                    SoundEvents.TNT_PRIMED,
                    1,
                    1
            );
        }
    }

    public static List<Creeper> isCreeperNearby(Minecraft mc) {
        LocalPlayer player = mc.player;
        Level world = mc.level;

        if (player == null || world == null) return null;
        if (player.isCreative() || player.isSpectator()) return null;

        double range = CREEPER_WARN_RANGE.getDoubleValue();
        AABB box = player.getBoundingBox().inflate(range);


        return world.getEntitiesOfClass(Creeper.class, box, (creeper) -> true);
    }
}