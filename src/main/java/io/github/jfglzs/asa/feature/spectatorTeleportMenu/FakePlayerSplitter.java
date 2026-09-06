package io.github.jfglzs.asa.feature.spectatorTeleportMenu;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.PlayerUtils;
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

        return connection.getListedOnlinePlayers().stream().filter(PlayerUtils::isFakePlayer).toList();
    }

    public static List<SpectatorMenuItem> withoutFakePlayers(List<SpectatorMenuItem> items) {
        if (items == null)
            return List.of();
        if (! Configs.Functions.FAKE_PLAYER_TELEPORT_MENU.getBooleanValue())
            return items;

        return items.stream().filter(FakePlayerSplitter::isRealPlayer).toList();
    }

    private static boolean isRealPlayer(SpectatorMenuItem item) {
        if (item == null)
            return true;

        return !PlayerUtils.isFakePlayer(item.getName().getString());
    }
}
