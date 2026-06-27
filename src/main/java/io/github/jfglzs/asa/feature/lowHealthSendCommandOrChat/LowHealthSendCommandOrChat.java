package io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

import static io.github.jfglzs.asa.config.Configs.*;

public class LowHealthSendCommandOrChat {
    public static final RateLimiter rateLimiter = RateLimiter.create(0.05);

    public static void trigger(Minecraft client) {
        Player player = client.player;
        if (LOW_HEALTH_EXECUTE_OR_SEND.getBooleanValue() && player != null) {
            float health = player.getHealth();
            if (health < LOW_HEALTH_VALUE.getFloatValue()) {
                if (PlayerUtils.isSurvivalMode(player) || !rateLimiter.tryAcquire() || MCUtils.getScreen() instanceof DeathScreen) return;

                if (LOW_HEALTH_SEND_MODE.getOptionListValue().getStringValue().equals("发送聊天消息")) {
                    ChatUtils.sendMessageToServer(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue());
                    AsaMod.debugMessage("Send Chat %s".formatted(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue()));
                } else {
                    MCUtils.executeCommand(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue());
                    AsaMod.debugMessage("Send Command %s".formatted(Configs.LOW_HEALTH_SEND_CONTENT.getStringValue()));
                }
            }
        }
    }
}
