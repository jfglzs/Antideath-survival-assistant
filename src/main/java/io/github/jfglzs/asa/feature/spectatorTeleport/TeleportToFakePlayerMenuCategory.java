package io.github.jfglzs.asa.feature.spectatorTeleport;

import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import net.minecraft.network.chat.Component;

public final class TeleportToFakePlayerMenuCategory extends TeleportToPlayerMenuCategory {
    private static final Component NAME = Component.translatable("asa.spectatorMenu.teleportFakePlayer");
    private static final Component PROMPT = Component.translatable("asa.spectatorMenu.teleportFakePlayer.prompt");

    public TeleportToFakePlayerMenuCategory() {
        super(FakePlayerUtils.getFakePlayers());
    }

    @Override
    public Component getName() {
        return NAME;
    }

    @Override
    public Component getPrompt() {
        return PROMPT;
    }
}
