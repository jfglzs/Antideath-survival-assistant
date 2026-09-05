package io.github.jfglzs.asa.feature.spectatorTeleport;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;

public final class FakePlayerSplitter {
    private FakePlayerSplitter() {
    }

    public static List<PlayerInfo> getFakePlayers() {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null)
            return List.of();

        return connection.getListedOnlinePlayers().stream().filter(FakePlayerSplitter::isFakePlayer).toList();
    }

    public static List<SpectatorMenuItem> withoutFakePlayers(List<SpectatorMenuItem> items) {
        if (items == null)
            return List.of();
        if (! Configs.Functions.FAKE_PLAYER_TELEPORT_MENU.getBooleanValue())
            return items;

        return items.stream().filter(item -> ! isFakePlayer(item)).toList();
    }

    public static boolean isFakePlayer(PlayerInfo player) {
        return player != null && player.getLatency() == 0;
    }

    private static boolean isFakePlayer(SpectatorMenuItem item) {
        if (item == null)
            return false;
        else
            item.getName();

        var connection = Minecraft.getInstance().getConnection();

        if (connection == null)
            return false;

        return isFakePlayer(connection.getPlayerInfo(item.getName().getString()));
    }
}
