package io.github.jfglzs.asa.feature.fakePlayerKillAura;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class FakePlayerKillAura {
    public static void kill() {
        var mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            var box = mc.player.getBoundingBox().inflate(Configs.FAKE_PLAYER_KILL_AURA_RANGE.getDoubleValue());
            var players = mc.level.getEntitiesOfClass(Player.class, box);
            for (Player player : players) {
                var name = player.getDisplayName().getString().trim();
                if (name.startsWith(Configs.FAKE_PLAYER_KILL_AURA_PREFIX.getStringValue())) {
                    MCUtils.executeCommand("player %s kill".formatted(name));
                }
            }
        }
    }
}
