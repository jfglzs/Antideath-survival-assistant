package io.github.jfglzs.asa.feature.fakePlayerKillAura;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayerKillAura {
    public static void kill() {
        var mc = MinecraftClient.getInstance();
        if (mc.world != null && mc.player != null) {
            var box = mc.player.getBoundingBox().expand(Configs.FAKE_PLAYER_KILL_AURA_RANGE.getDoubleValue());
            var players = mc.world.getNonSpectatingEntities(PlayerEntity.class, box);
            for (PlayerEntity player : players) {
                var name = player.getDisplayName().getString();
                if (name.startsWith(Configs.FAKE_PLAYER_KILL_AURA_PREFIX.getStringValue())) {
                    MCUtils.excuteCommand("player %s kill".formatted(name));
                }
            }
        }
    }
}
