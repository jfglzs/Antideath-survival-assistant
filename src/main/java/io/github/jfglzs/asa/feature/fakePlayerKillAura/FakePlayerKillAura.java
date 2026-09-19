package io.github.jfglzs.asa.feature.fakePlayerKillAura;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class FakePlayerKillAura {
    public static void kill() {
        Minecraft mc = MCUtils.getMinecraft();
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;

        if (level == null && player == null)
            return;

        var box = player.getBoundingBox().inflate(Configs.Functions.FAKE_PLAYER_KILL_AURA_RANGE.getDoubleValue());

        for (Player target : level.getEntitiesOfClass(Player.class, box)) {
            String name = PlayerUtils.getName(target);
            var info = PlayerUtils.getPlayerInfo(name);
            if (info != null && info.getLatency() == 0 && canKill(name))
                MCUtils.executeCommand("player %s kill".formatted(name));
        }
    }

    private static boolean canKill(String name) {
        if (Configs.Functions.ENABLE_FAKE_PLAYER_KILL_AURA_WHITELIST.getBooleanValue()) {
            return Configs.Lists.FAKE_PLAYER_KILL_AURA_WHITELIST.getStrings().contains(name);
        }
        else if (Configs.Functions.ENABLE_FAKE_PLAYER_KILL_AURA_BLACKLIST.getBooleanValue()) {
            return ! Configs.Lists.FAKE_PLAYER_KILL_AURA_BLACKLIST.getStrings().contains(name);
        }
        return true;
    }
}
