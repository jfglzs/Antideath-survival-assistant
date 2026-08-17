package io.github.jfglzs.asa.feature.lowHealthSendCommandOrChat;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.options.LowHealthSendMode;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import io.github.jfglzs.asa.config.Configs;

public class LowHealthSendCommandOrChat {
    public static final RateLimiter rateLimiter = RateLimiter.create(0.02);

    public static void tick(Minecraft client) {
        Player player = client.player;
        if (Configs.LOW_HEALTH_EXECUTE_OR_SEND.getBooleanValue() && PlayerUtils.isSurvivalMode(player)) {
            float health = player.getHealth();
            if (health < Configs.LOW_HEALTH_VALUE.getFloatValue()) {
                if (! rateLimiter.tryAcquire()) return;

                String content = Configs.LOW_HEALTH_SEND_CONTENT.getStringValue();
                if (Configs.LOW_HEALTH_SEND_MODE.getOptionListValue() == LowHealthSendMode.SEND_CHAT_MESSAGE) {
                    ChatUtils.serverMess(content);
                    AsaMod.debugMessage(() -> "Send Chat %s".formatted(content));
                }
                else {
                    MCUtils.executeCommand(content);
                }
            }
        }
    }
}
