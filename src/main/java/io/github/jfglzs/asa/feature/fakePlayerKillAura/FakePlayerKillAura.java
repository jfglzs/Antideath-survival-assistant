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
                var name = player.getName().getString().trim();
                var prefix = Configs.FAKE_PLAYER_KILL_AURA_PREFIX.getStringValue();
                var canKill = canKill(name);
                if (prefix == null) {
                    if (!canKill) continue;
                }
                else {
                    if (!(name.startsWith(prefix) && canKill)) continue;
                }
                MCUtils.executeCommand("player %s kill".formatted(name));
            }
        }
    }

    private static boolean canKill(String name) {
        if (Configs.ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST.getBooleanValue()) {
            return Configs.FAKE_PLAYER_KILL_AURA_WHITELIST.getStrings().contains(name);
        }
        else if (Configs.ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST.getBooleanValue()) {
            return !Configs.FAKE_PLAYER_KILL_AURA_BLACKLIST.getStrings().contains(name);
        }
        return true;
    }
}
