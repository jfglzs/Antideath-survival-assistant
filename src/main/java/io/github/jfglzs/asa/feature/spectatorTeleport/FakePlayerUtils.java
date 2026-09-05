package io.github.jfglzs.asa.feature.spectatorTeleport;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.Collection;
import java.util.List;

public final class FakePlayerUtils {
    private FakePlayerUtils() {}

    public static List<PlayerInfo> getFakePlayers() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return List.of();

        Collection<PlayerInfo> players = connection.getListedOnlinePlayers();
        if (players == null) return List.of();

        return players.stream()
                .filter(FakePlayerUtils::isFakePlayer)
                .toList();
    }

    public static List<SpectatorMenuItem> withoutFakePlayers(List<SpectatorMenuItem> items) {
        if (items == null) return List.of();
        if (! Configs.Functions.SPECTATOR_TELEPORT_FAKE_PLAYER_LIST.getBooleanValue()) return items;

        return items.stream()
                .filter(item -> ! isFakePlayer(item))
                .toList();
    }

    public static boolean isFakePlayer(PlayerInfo player) {
        return player != null && player.getLatency() == 0;
    }

    private static boolean isFakePlayer(SpectatorMenuItem item) {
        if (item == null || item.getName() == null) return false;

        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return false;

        return isFakePlayer(connection.getPlayerInfo(item.getName().getString()));
    }
}
